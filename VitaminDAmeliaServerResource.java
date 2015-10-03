package WongHubRestlet;

import java.util.Calendar;
import java.util.Date;
import org.json.JSONException;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import redis.clients.jedis.Jedis;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class VitaminDAmeliaServerResource extends ServerResource {

	@Post
	public void poststore(Representation rep) throws JSONException {
		SparkClient sparkClient = new SparkClient();
		PushoverClient pushoverClient = new PushoverClient();
		Jedis jedis = new Jedis("localhost");
		float lightLevel =(float) sparkClient.SparkClientGetLight("Amelia");
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();
		int secondsBetween = 0;
		String lastDateStr;
		
		lastDateStr = jedis.get("AmeliaTime");
		if (lastDateStr != null) {
			org.joda.time.DateTime alexdatetime = parser1.parseDateTime(lastDateStr);
			secondsBetween = org.joda.time.Seconds.secondsBetween(alexdatetime, currentdatetime).getSeconds();
		}

		if (secondsBetween > 3) {
			jedis.set("AmeliaTime", parser1.print(currentdatetime));

			RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 0");
			RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 1");
			RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 2");
			RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 3");
			// Get Last Open.Sen.Se Lightlevel from Amelia's Room

			if (lightLevel < 90) {
				if (secondsBetween > 180)
				{
					System.out.println("Announcing Amelia is stirring.");
					try {
						pushoverClient.PushoverClientPost("Amelia is stirring!", "AmeliaSleep");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}