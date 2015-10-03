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
public class LivingLightServerResource extends ServerResource {

	private static boolean isNumeric(String str)  
	{  
		try  
		{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		// Parse the JSON representation to get the Ninja properties
		LifxClient lifxClient = new LifxClient();
		float LIGHTTHRESH = 85;
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		Jedis jedis = new Jedis("localhost");

		String DA = NinjaElt.getString("DA");
		if (isNumeric(DA))
		{
			String PrevLightStr = jedis.get("PrevLivingLight");
			jedis.set("PrevLivingLight", DA);

			if (PrevLightStr == null) PrevLightStr = "0";

			float PrevLight =  Float.parseFloat(PrevLightStr);
			float fltDA = Float.parseFloat(DA);


			if (fltDA >= LIGHTTHRESH && PrevLight < LIGHTTHRESH){
				try {
					lifxClient.LifxClientPut("label:LivingRoom", "off", "1", "orange");
				} catch (Exception e) {e.printStackTrace();}
			}
		}
	}
}
