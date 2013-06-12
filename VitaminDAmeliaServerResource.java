package WongHubRestlet;

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
		int postVal = 0;
		int secondsElapsed = 0;
		int secondsBetween = 5;
		float lightLevel = 0;
		float minuteselapsed = 2;
		COSMClient cOSMClient = new COSMClient();
		SenseClient senseClient = new SenseClient();
		NinjaClient ninjaClient = new NinjaClient();
		PushoverClient pushoverClient = new PushoverClient();
		Jedis jedis = new Jedis("localhost");
		String lastDateStr;
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();
		String Announce = getQuery().getValues("announce");

		lastDateStr = jedis.get("AmeliaTime");
		if (lastDateStr != null) {
			org.joda.time.DateTime alexdatetime = parser1.parseDateTime(lastDateStr);
			secondsBetween = org.joda.time.Seconds.secondsBetween(alexdatetime, currentdatetime).getSeconds();
		}

		if (secondsBetween > 3) {
			jedis.set("AmeliaTime", parser1.print(currentdatetime));

			RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 0");
			RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 1");

			// Get Last Open.Sen.Se Lightlevel from Amelia's Room
			try {
				lightLevel = senseClient.SenseClientGetLastEvent(12358);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			// Get Last COSM Update value from Amelia's Room
			try {
				postVal = cOSMClient.COSMClientGetLastUpdate(88818, 0, minuteselapsed);
				secondsElapsed = cOSMClient.COSMClientGetSecondsSinceLastUpdate(88818, 0);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			// Post COSM and Open.Sen.Se (Amelia's Camera) update for current movement
			// in Amelia's Room
			try {
				Date date1 = new Date();
				try {
					cOSMClient.COSMClientPostImpulse(88818, 0, date1,
							Integer.toString(postVal));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				senseClient.SenseClientPost(7019, "0");
				senseClient.SenseClientPost(7019, Integer.toString(postVal));
				senseClient.SenseClientPost(7019, "0");
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			if (lightLevel < 0.3) {
				// Update Amelia Open.Sen.Se (Amelia is Stirring) with newest value
				try {
					senseClient.SenseClientPost(12796, "0");
					senseClient.SenseClientPost(12796, Integer.toString(postVal));
					senseClient.SenseClientPost(12796, "0");
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				if ((secondsElapsed > minuteselapsed * 60)
						|| (((postVal % 5) == 0) && (postVal != 0))) {
					System.out.println("Announcing Amelia is stirring.");
					try {
						pushoverClient.PushoverClientPost("Amelia is stirring! -> "
								+ postVal, "AmeliaSleep");
					}
					catch (Exception e) {
						e.printStackTrace();
					}

					if (Announce.equals("on")) {
						RunShell
								.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/AmeliaIsStirring.scpt");
					}
				}
			}
		}
	}
}