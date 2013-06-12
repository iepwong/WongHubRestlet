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
public class VitaminDBackyardServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation VitaminDHallwayRep)
			throws JSONException {
		int postVal = 0;
		COSMClient cOSMClient = new COSMClient();
		SenseClient senseClient = new SenseClient();
		PushoverClient pushoverClient = new PushoverClient();
		
		// Blink purple
		RunShell.Run("/Users/Ian/blink1-tool --rgb 255,0,255 --blink 5 -d 0");
		RunShell.Run("/Users/Ian/blink1-tool --rgb 255,0,255 --blink 5 -d 1");

		// Get Last Update value from Hallway
		try {
			postVal = cOSMClient.COSMClientGetLastUpdate(88818, 4, 2);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("PostVal->"+postVal);
		// Update Hallway with newest value
		Date date1 = new Date();
		try {
			cOSMClient.COSMClientPostImpulse(88818, 4, date1,
					Integer.toString(postVal));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			senseClient.SenseClientPost(7020, "0");
			senseClient.SenseClientPost(7020, Integer.toString(postVal));
			senseClient.SenseClientPost(7020, "0");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (((postVal % 30) == 0) && (postVal != 0)) {
			try {
				pushoverClient.PushoverClientPost("Someone is in the backyard! -> "
						+ postVal, "WongHome");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}