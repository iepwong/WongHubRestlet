package WongHubRestlet;

import java.util.Date;
import org.json.JSONException;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class VitaminDAlexanderServerResource extends ServerResource {

	@Post
	public void poststore(Representation rep) throws JSONException {
		int postVal = 0;
		int secondsElapsed = 0;
		float minuteselapsed = 2;
		COSMClient cOSMClient = new COSMClient();
		NinjaClient ninjaClient = new NinjaClient();
		PushoverClient pushoverClient = new PushoverClient();

		String Announce = getQuery().getValues("announce");

		RunShell.Run("/Users/Ian/blink1-tool --rgb 128,128,255--blink 5 -d 0");
		RunShell.Run("/Users/Ian/blink1-tool --rgb 128,128,255--blink 5 -d 1");

		// Get Last COSM Update value from Alexander's Cot
		try {
			postVal = cOSMClient.COSMClientGetLastUpdate(88818, 5, minuteselapsed);
			secondsElapsed = cOSMClient.COSMClientGetSecondsSinceLastUpdate(88818, 5);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Date date1 = new Date();
		try {
			cOSMClient.COSMClientPostImpulse(88818, 5, date1, Integer.toString(postVal));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if ((postVal % 10) == 0) {
			System.out.println("Announcing Alexander is stirring.");
			try {
				pushoverClient.PushoverClientPost("Alexander is stirring:VD! -> "+ postVal, "AlexanderStir");
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			if (Announce.equals("on")) {
				RunShell.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/LivingRoomAlex.pl");
				RunShell.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/AlexanderIsStirring.scpt");				
			}
		}
	}
}
