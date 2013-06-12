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
public class VitaminDSidegateServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation VitaminDHallwayRep)
			throws JSONException {
		int postVal = 0;
		COSMClient cOSMClient = new COSMClient();
		SenseClient senseClient = new SenseClient();
		NinjaClient ninjaClient = new NinjaClient();
		PushoverClient pushoverClient = new PushoverClient();

		// Magenta blink
		RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 5 -d 0");
		RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 5 -d 1");

		// Get Last Update value from Hallway
		try {
			postVal = cOSMClient.COSMClientGetLastUpdate(88818, 2, 2);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("PostVal->"+postVal);
		// Update Hallway with newest value
		Date date1 = new Date();
		try {
			cOSMClient.COSMClientPostImpulse(88818, 2, date1,
					Integer.toString(postVal));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			senseClient.SenseClientPost(8767, "0");
			senseClient.SenseClientPost(8767, Integer.toString(postVal));
			senseClient.SenseClientPost(8767, "0");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}