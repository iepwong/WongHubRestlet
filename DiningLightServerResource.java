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
public class DiningLightServerResource extends ServerResource {

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
		HueClient hueClient = new HueClient();
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		float LIGHTTHRESH = 15;
		Jedis jedis = new Jedis("localhost");
		
	
		String DA = NinjaElt.getString("DA");
		if (isNumeric(DA))
		{

			String PrevLightStr = jedis.get("PrevDiningLight");
			jedis.set("PrevDiningLight", DA);

			if (PrevLightStr == null) PrevLightStr = "0";
			float PrevLight =  Float.parseFloat(PrevLightStr);
			float fltDA = Float.parseFloat(DA);

			if (fltDA >= LIGHTTHRESH && PrevLight < LIGHTTHRESH){
				try {
					hueClient.HueClientPut("3", "false", "100", "0", "none");
					hueClient.HueClientPut("1", "false", "100", "0", "none");
				} catch (Exception e) {e.printStackTrace();}
			}
		}
	}
}
