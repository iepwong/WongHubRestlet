package WongHubRestlet;

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
public class AlexanderLightServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		// Parse the JSON representation to get the Ninja properties
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		Jedis jedis = new Jedis("localhost");
		
		String DA = NinjaElt.getString("DA");
		String PrevLightStr = jedis.get("PrevAlexLight");
		jedis.set("PrevAlexLight", DA);
		
		if (PrevLightStr == null) PrevLightStr = "0";
		float PrevLight =  Float.parseFloat(PrevLightStr);
		float fltDA = Float.parseFloat(DA);

//		float PrevLight =  Float.parseFloat(PrevLightStr);

		
//		if (Float.parseFloat(DA) < LIGHTTHRESH && PrevLight >= LIGHTTHRESH){
//			RunShell.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/DiningLamp.pl");
//		}

//		if (Float.parseFloat(DA) >= LIGHTTHRESH && PrevLight < LIGHTTHRESH){
//			RunShell.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/DiningLampOff.pl");
//		}
		
	}
}
