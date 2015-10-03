package WongHubRestlet;

import java.util.Date;
import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import redis.clients.jedis.Jedis;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class VitaminDBackyardServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation VitaminDHallwayRep)
			throws JSONException {
		int secondsBetween = 5;
		PushoverClient pushoverClient = new PushoverClient();
		Jedis jedis = new Jedis("localhost");
		String lastDateStr;
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();
				
		// Blink purple
		RunShell.Run("/Users/Ian/blink1-tool --rgb 255,0,255 --blink 5 -d 0");
		RunShell.Run("/Users/Ian/blink1-tool --rgb 255,0,255 --blink 5 -d 1");

		// Get Last Update value from Hallway
		
		lastDateStr = jedis.get("BackyardTime");
		if (lastDateStr != null) {
			org.joda.time.DateTime backyarddatetime = parser1
					.parseDateTime(lastDateStr);
			secondsBetween = org.joda.time.Seconds.secondsBetween(backyarddatetime,
					currentdatetime).getSeconds();
		}
		else
		{
			jedis.set("BackyardTime", parser1.print(currentdatetime));
		}
		
		if (secondsBetween > 180) {
			try {
				pushoverClient.PushoverClientPost("Someone is in the backyard!", "WongHome");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}