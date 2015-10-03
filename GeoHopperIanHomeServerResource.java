package WongHubRestlet;

import java.util.Calendar;
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
public class GeoHopperIanHomeServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation rep) throws JSONException {
		int postVal = 0;
		int secondsBetween = 0;
		int lateHour = 17;
		LifxClient lifxClient = new LifxClient();
		PushoverClient pushoverClient = new PushoverClient();
		Jedis jedis = new Jedis("localhost");
		String WongHubAnnounce = jedis.get("WongHubAnnounce");
		org.joda.time.DateTime currentdatetime = new org.joda.time.DateTime();
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		String lastDateStr;
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		
		JSONObject Elt = rep.getJsonObject();
		String Event = Elt.getString("event");
		String Location = Elt.getString("location");
		
		System.out.println("Ian has "+Event+" "+Location+".");
		try {
			pushoverClient.PushoverClientPost("Ian has "+Event+" "+Location+"! -> "
					+ postVal, "WongHome");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (Location.equals("Home")) {

			lastDateStr = jedis.get("IanHomeTime");
			jedis.set("IanHomeTime", parser1.print(currentdatetime));

			if (lastDateStr != null) {
				org.joda.time.DateTime alexdatetime = parser1
						.parseDateTime(lastDateStr);
				secondsBetween = org.joda.time.Seconds.secondsBetween(alexdatetime,
						currentdatetime).getSeconds();
			}

			RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 0");
			RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 1");
			RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 2");
			RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 3");

			if (secondsBetween > 1800) {

				if (WongHubAnnounce.equals("on")) {
					if (Event.equals("LocationEnter") && (Location.equals("Home"))) {
						RunShell
								.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/IanIsNearHome.scpt");
					}
					if (hour > lateHour) {
						try {
							lifxClient.LifxClientPut("label:FrontDoor", "on", "1", "white");
							lifxClient.LifxClientPut("label:FrontDoor", "on", "300", "white");
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} /* 1800 */
		} /* Home */
	}
}
