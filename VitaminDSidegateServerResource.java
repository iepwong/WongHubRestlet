package WongHubRestlet;

import java.util.Calendar;
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
public class VitaminDSidegateServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation VitaminDHallwayRep)
			throws JSONException {
		SparkClient sparkClient = new SparkClient();
		LifxClient lifxClient = new LifxClient();
		int SPARKLIGHTTHRESH = 240;
		PushoverClient pushoverClient = new PushoverClient();
		Jedis jedis = new Jedis("localhost");
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();
		int secondsBetween = 0;
		String lastDateStr;
		
		// Magenta blink
		RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 5 -d 0");
		RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 5 -d 1");
		
		// Get Last Update value from Sidegate
		lastDateStr = jedis.get("SidegateTime");
		if (lastDateStr != null) {
			org.joda.time.DateTime sidegatedatetime = parser1
					.parseDateTime(lastDateStr);
			secondsBetween = org.joda.time.Seconds.secondsBetween(sidegatedatetime,
					currentdatetime).getSeconds();
		}
		else
		{
			jedis.set("SidegateTime", parser1.print(currentdatetime));
		}
		
		System.out.println("Sidegate seconds between ->"+secondsBetween);
		
		if (secondsBetween > 60) {
			jedis.set("SidegateTime", parser1.print(currentdatetime));

			if (sparkClient.SparkClientGetLight("Front") < SPARKLIGHTTHRESH) {
				try {
					lifxClient.LifxClientPut("label:Backyard", "on", "1", "white");
					lifxClient.LifxClientPut("label:Backyard", "off", "180", "white");
				} catch (Exception e) {e.printStackTrace();}
			}
			try {
				pushoverClient.PushoverClientPost("Sidegate tripped!", "WongHome");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}