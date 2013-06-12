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
public class AmeliaLightServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		// Parse the JSON representation to get the Ninja properties
		double LIGHTTHRESH = 0.1;
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		Date inDate = new Date(Long.parseLong(NinjaElt.getString("timestamp")));
		String DA = NinjaElt.getString("DA");
		COSMClient cOSMClient = new COSMClient();

		Jedis jedis = new Jedis("localhost");
		
		String PrevLightStr = jedis.get("PrevAmeliaLight");
		
		if (PrevLightStr == null) PrevLightStr = "0";
		
		float PrevLight =  Float.parseFloat(PrevLightStr);
		jedis.set("PrevAmeliaLight", DA);
		

		if (Float.parseFloat(DA) >= LIGHTTHRESH && PrevLight < LIGHTTHRESH){
			RunShell.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/AmeliaElephantOff.pl");
		}
		
		// Output the JSON element values
		// System.out.println("D: " + NinjaElt.toString());
		// Output the JSON element values
		// System.out.println("GUID: " + NinjaElt.getString("GUID"));
		// System.out.println("DA: " + DA);
		// System.out.println("TIMESTAMP: " + inDate);
		try {
			cOSMClient.COSMClientPost(40517, 0, inDate, DA);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
