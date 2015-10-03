package WongHubRestlet;

import org.json.JSONException;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import redis.clients.jedis.Jedis;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class WongHubAnnounceServerResource extends ServerResource {

	@Post
	public void poststore(Representation rep) throws JSONException {
		String Announce = getQuery().getValues("announce");
		Jedis jedis = new Jedis("localhost");

		jedis.set("WongHubAnnounce", Announce);
	}
}
