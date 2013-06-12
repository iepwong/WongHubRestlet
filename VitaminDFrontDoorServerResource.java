package WongHubRestlet;

import java.util.Date;
import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class VitaminDFrontDoorServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation VitaminDHallwayRep)
			throws JSONException {
		int postVal = 0;
		int secondsElapsed = 0;
		int minutesElapsed = 1;
		COSMClient cOSMClient = new COSMClient();
		SenseClient senseClient = new SenseClient();
		NinjaClient ninjaClient = new NinjaClient();
		PushoverClient pushoverClient = new PushoverClient();

		String Announce = getQuery().getValues("announce");

		RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 0");
		RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 1");

		// Get Last Update value from Front Door
		try {
			postVal = cOSMClient.COSMClientGetLastUpdate(88818, 1, minutesElapsed);
			secondsElapsed = cOSMClient.COSMClientGetSecondsSinceLastUpdate(88818, 1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Date date1 = new Date();
		try {
			cOSMClient.COSMClientPostImpulse(88818, 1, date1, Integer.toString(postVal));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			senseClient.SenseClientPost(8528, "0");
			senseClient.SenseClientPost(8528, Integer.toString(postVal));
			senseClient.SenseClientPost(8528, "0");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if ((secondsElapsed > minutesElapsed * 60) || ((postVal % 5) == 0)) {
			System.out.println("Announcing Someone is at the Front Door.");

			try {
				pushoverClient.PushoverClientPost("Someone is at the front door! -> "+ postVal, "WongHome");
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			if (Announce.equals("on")) {
				RunShell.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/LivingRoomAlex.pl");
				RunShell.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/FrontDoorSomeone.scpt");
			}
		}
	}
}