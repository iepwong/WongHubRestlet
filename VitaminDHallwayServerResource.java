package WongHubRestlet;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class VitaminDHallwayServerResource extends ServerResource {

	@Get
	public Representation toJson() throws JSONException {
		// Create a JSON object structure similar to a map
		JSONObject VitaminDHallwayElt = new JSONObject();
		VitaminDHallwayElt.put("status", "received");
		VitaminDHallwayElt.put("subject", "We only take POSTs");
		VitaminDHallwayElt.put("content", "Doh!");
		VitaminDHallwayElt.put("accountRef", new Reference(getReference(), "..")
				.getTargetRef().toString());
		return new JsonRepresentation(VitaminDHallwayElt);
	}

	@Post
	public void poststore(JsonRepresentation VitaminDHallwayRep)
			throws JSONException {
		int postVal = 0;
		COSMClient cOSMClient = new COSMClient();
		SenseClient senseClient = new SenseClient();
		PushoverClient pushoverClient = new PushoverClient();

		RunShell.Run("/Users/Ian/blink1-tool --blue --blink 5 -d 0");
		RunShell.Run("/Users/Ian/blink1-tool --blue --blink 5 -d 1");

		// Get Last Update value from Hallway
		try {
			postVal = cOSMClient.COSMClientGetLastUpdate(88818, 3, 2);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("PostVal->"+postVal);
		// Update Hallway with newest value
		Date date1 = new Date();
		try {
			cOSMClient.COSMClientPostImpulse(88818, 3, date1,
					Integer.toString(postVal));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			senseClient.SenseClientPost(7018, "0");
			senseClient.SenseClientPost(7018, Integer.toString(postVal));
			senseClient.SenseClientPost(7018, "0");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (((postVal % 30) == 0) && (postVal != 0)) {
			try {
				pushoverClient.PushoverClientPost("Someone is walking the hallway! -> "
						+ postVal, "WongHome");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}