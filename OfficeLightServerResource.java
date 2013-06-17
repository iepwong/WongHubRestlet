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
public class OfficeLightServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		// Parse the JSON representation to get the Ninja properties
		float LIGHTTHRESH = 1;
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		Date inDate = new Date(Long.parseLong(NinjaElt.getString("timestamp")));
		Jedis jedis = new Jedis("localhost");
		
		String DA = NinjaElt.getString("DA");
		String PrevLightStr = jedis.get("PrevOfficeLight");
		
		if (PrevLightStr == null) PrevLightStr = "0";
		
		float PrevLight =  Float.parseFloat(PrevLightStr);
		jedis.set("PrevOfficeLight", DA);
		
//		if (Float.parseFloat(DA) < LIGHTTHRESH && PrevLight >= LIGHTTHRESH){
//			RunShell.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/DiningLamp.pl");
//		}

//		if (Float.parseFloat(DA) >= LIGHTTHRESH && PrevLight < LIGHTTHRESH){
//			RunShell.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/DiningLampOff.pl");
//		}
		
	}
}
