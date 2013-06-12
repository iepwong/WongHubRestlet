package WongHubRestlet;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import redis.clients.jedis.Jedis;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class ResetOfficeLightServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		NinjaClient ninjaClient = new NinjaClient();
		
		try {
			ninjaClient.NinjaClientPut("4412BB000368_0_0_11","101010100001100101010101");
			Thread.sleep(3000);
			ninjaClient.NinjaClientPut("4412BB000368_0_0_11","101010100001100101011101");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
