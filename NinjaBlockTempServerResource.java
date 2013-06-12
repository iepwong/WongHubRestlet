package WongHubRestlet;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class NinjaBlockTempServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		// Parse the JSON representation to get the Ninja properties
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		Date inDate = new Date(Long.parseLong(NinjaElt.getString("timestamp")));
		String DA = NinjaElt.getString("DA");
		COSMClient cOSMClient = new COSMClient();

		// Output the JSON element values
		// System.out.println("D: " + NinjaElt.toString());
		// Output the JSON element values
		// System.out.println("GUID: " + NinjaElt.getString("GUID"));
		// System.out.println("DA: " + DA);
		// System.out.println("TIMESTAMP: " + inDate);
		try {
			cOSMClient.COSMClientPost(96124, 0, inDate, DA);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
