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
public class VitaminDAlexanderServerResource extends ServerResource {

	@Post
	public void poststore(Representation rep) throws JSONException {
		SparkClient sparkClient = new SparkClient();
		PushoverClient pushoverClient = new PushoverClient();
		String Announce = getQuery().getValues("announce");
		Jedis jedis = new Jedis("localhost");
		String WongHubAnnounce = jedis.get("WongHubAnnounce");
		float PrevLight =(float) sparkClient.SparkClientGetLight("Alex");
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();
		int secondsBetween = 0;
		String lastDateStr;
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int postVal = 0;
		String postValStr = null;

		lastDateStr = jedis.get("AlexanderTime");
		postValStr = jedis.get("AlexPostVal");

		if (postValStr != null) {
			postVal = Integer.parseInt(postValStr);
			jedis.set("AlexPostVal", Integer.toString(postVal+1));
		}else{
			jedis.set("AlexPostVal", "0");
		}
		
		if (lastDateStr != null) {
			org.joda.time.DateTime alexdatetime = parser1
					.parseDateTime(lastDateStr);
			secondsBetween = org.joda.time.Seconds.secondsBetween(alexdatetime,
					currentdatetime).getSeconds();
		}
		else
		{
			jedis.set("AlexanderTime", parser1.print(currentdatetime));
		}
		
		System.out.println("Alexander seconds between ->"+secondsBetween);
		
		if ((secondsBetween > 60) && (postVal % 10) ==0) {
			jedis.set("AlexanderTime", parser1.print(currentdatetime));

			if (PrevLight < 90){
//				if ((hour > 7) && (hour < 23) && Announce.equals("on") && (WongHubAnnounce.equals("on"))) {
//					RunShell.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/AlexanderIsStirring.scpt");			
					RunShell.Run("/Users/Ian/blink1-tool --blue --blink 5 -d 0");
					RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 1");
					RunShell.Run("/Users/Ian/blink1-tool --blue --blink 5 -d 2");
					RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 3");				}
				try {
					pushoverClient.PushoverClientPost("Alexander is stirring!", "AlexanderStir");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
//			}
		}
	}
}
