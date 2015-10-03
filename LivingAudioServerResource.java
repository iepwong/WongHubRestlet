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
public class LivingAudioServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		// Parse the JSON representation to get the Ninja properties
		float AUDIOTHRESH = (float) 1800;
		PushoverClient pushoverClient = new PushoverClient();
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		Date inDate = new Date(Long.parseLong(NinjaElt.getString("timestamp")));
		Jedis jedis = new Jedis("localhost");
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();

		String DA = NinjaElt.getString("DA");
		String PrevAudioStr = jedis.get("PrevLivingAudio");
		
		if (PrevAudioStr == null) PrevAudioStr = "0";
		jedis.set("PrevLivingAudio", DA);
		jedis.set("LivingAudioTime", parser1.print(currentdatetime));
		
		if (Float.parseFloat(DA)  >= AUDIOTHRESH){
		try
		{
			System.out.println("Living Audio Tripped: " +DA+ " -> "+ inDate);
			// pushoverClient.PushoverClientPost("Living Audio Tripped -> "+DA, "WongLiving");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		}
		
	}
}
