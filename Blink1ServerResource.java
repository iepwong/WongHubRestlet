package WongHubRestlet;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class Blink1ServerResource extends ServerResource {

	@Get
	public Representation toJson() throws JSONException {
		// Create a JSON object structure similar to a map
		JSONObject WongHubElt = new JSONObject();
		WongHubElt.put("status", "received");
		WongHubElt.put("subject", "Message to self");
		WongHubElt.put("content", "Doh!");
		WongHubElt.put("accountRef", new Reference(getReference(), "..")
				.getTargetRef().toString());
		return new JsonRepresentation(WongHubElt);
	}

	@Put
	public void store(JsonRepresentation WongHubRep) throws JSONException {
		// Parse the JSON representation to get the WongHub properties
		JSONObject WongHubElt = WongHubRep.getJsonObject();

		// Output the JSON element values
		System.out.println("Status: " + WongHubElt.getString("status"));
		System.out.println("Subject: " + WongHubElt.getString("subject"));
		System.out.println("Content: " + WongHubElt.getString("content"));
		System.out.println("Account URI: " + WongHubElt.getString("accountRef"));
	}

	@Post
	public void poststore(JsonRepresentation WongHubRep) throws JSONException {
		// Parse the JSON representation to get the WongHub properties
		JSONObject WongHubElt = WongHubRep.getJsonObject();
		Date inDate = new Date(Long.parseLong(WongHubElt.getString("TIMESTAMP")));
		String DA = WongHubElt.getString("DA");

		if (DA.equals("000101111011100011111111")) {
			RunShell.Run("/Users/Ian/blink1-tool --red --blink 3 -d 0");
			RunShell.Run("/Users/Ian/blink1-tool --red --blink 3 -d 1");
			System.out.println("P1On");
		}
		else if (DA.equals("000101111011100011111011")) {
			RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,255 --blink 5 -d 0");
			RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,255 --blink 5 -d 1");
			System.out.println("P2On");
		}

		// Output the JSON element values
		// System.out.println("D: " + WongHubElt.toString());
		// Output the JSON element values
		System.out.println("GUID: " + WongHubElt.getString("GUID"));
		System.out.println("DA: " + WongHubElt.getString("DA"));
		System.out.println("TIMESTAMP: " + inDate);

	}
}
