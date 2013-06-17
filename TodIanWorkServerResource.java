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
import redis.clients.jedis.Jedis;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class TodIanWorkServerResource extends ServerResource {

	@Get
	public Representation toJson() throws JSONException {
		System.out.println("Tod -> Ian Work");
		JSONObject WongHubElt = new JSONObject();
		WongHubElt.put("status", "received");
		WongHubElt.put("subject", "Message to self");
		WongHubElt.put("content", "Doh!");
		WongHubElt.put("accountRef", new Reference(getReference(), "..")
				.getTargetRef().toString());
		return new JsonRepresentation(WongHubElt);
	}
}
