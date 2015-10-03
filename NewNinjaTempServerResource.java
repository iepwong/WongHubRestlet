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
public class NewNinjaTempServerResource extends ServerResource {

	@Get
	public Representation toJson() throws JSONException {
		// Create a JSON object structure similar to a map
		JSONObject NinjaElt = new JSONObject();
		NinjaElt.put("status", "received");
		NinjaElt.put("subject", "We only take POSTs");
		NinjaElt.put("content", "Doh!");
		NinjaElt.put("accountRef", new Reference(getReference(), "..")
				.getTargetRef().toString());
		return new JsonRepresentation(NinjaElt);
	}

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		// Parse the JSON representation to get the Ninja properties
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		// System.out.println(NinjaElt.toString());
		Date inDate = new Date(Long.parseLong(NinjaElt.getString("timestamp")));
		String DA = NinjaElt.getString("DA");

		// Output the JSON element values
		// System.out.println("D: " + NinjaElt.toString());
		// Output the JSON element values
		// System.out.println("GUID: " + NinjaElt.getString("GUID"));
		// System.out.println("DA: " + DA);
		// System.out.println("TIMESTAMP: " + inDate);
		try {
//			cOSMClient.COSMClientPost(102055, 1, inDate, DA);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
