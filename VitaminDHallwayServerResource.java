package WongHubRestlet;

import java.util.Calendar;
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
public class VitaminDHallwayServerResource extends ServerResource {


	@Post
	public void postStore (JsonRepresentation VitaminDHallwayRep)
			throws JSONException {
		String lastDateStr;
		Jedis jedis = new Jedis("localhost");
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();
		int secondsBetween = 0;
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		PushoverClient pushoverClient = new PushoverClient();
		int postVal = 0;
		String postValStr = null;

		RunShell.Run("/Users/Ian/blink1-tool --blue --blink 5 -d 0");
		RunShell.Run("/Users/Ian/blink1-tool --blue --blink 5 -d 1");
		
		lastDateStr = jedis.get("HallwayTime");
		postValStr = jedis.get("HallwayPostVal");
		if (postValStr != null) {
			postVal = Integer.parseInt(postValStr);
			jedis.set("HallwayPostVal", Integer.toString(postVal+1));
		}else{
			jedis.set("HallwayPostVal", "0");
		}

		if (lastDateStr != null) {
			org.joda.time.DateTime hallwaydatetime = parser1
					.parseDateTime(lastDateStr);
			secondsBetween = org.joda.time.Seconds.secondsBetween(hallwaydatetime,
					currentdatetime).getSeconds();
		}
		else
		{
			jedis.set("HallwayTime", parser1.print(currentdatetime));
		}
		
		// Update Hallway with newest value
		if ((secondsBetween > 30)  && ((postVal % 5) ==0)) {
			jedis.set("HallwayTime", parser1.print(currentdatetime));
			try {
				pushoverClient.PushoverClientPost("Someone is walking the hallway!", "WongHome");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}