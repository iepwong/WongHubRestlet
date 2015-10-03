package WongHubRestlet;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import redis.clients.jedis.Jedis;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class VitaminDFrontDoorServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation VitaminDHallwayRep)
			throws JSONException {
		int postVal = 0;
		int minutesElapsed = 1;
		PushoverClient pushoverClient = new PushoverClient();
		Jedis jedis = new Jedis("localhost");
		String WongHubAnnounce = jedis.get("WongHubAnnounce");
		String Announce = getQuery().getValues("announce");
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();
		int secondsBetween = 0;

		RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 0");
		RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 1");
		RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 2");
		RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 3");
		
		String lastDateStr = jedis.get("VitaminDFrontDoor");
		if (lastDateStr != null) {
			org.joda.time.DateTime frontdoordatetime = parser1.parseDateTime(lastDateStr);
			secondsBetween = org.joda.time.Seconds.secondsBetween(frontdoordatetime, currentdatetime).getSeconds();
		}
		jedis.set("VitaminDFrontDoor", parser1.print(currentdatetime));

		if (secondsBetween > minutesElapsed * 60) {
			System.out.println("Announcing Someone is at the Front Door.");
			
			if (Announce.equals("on") && (WongHubAnnounce.equals("on"))) {
				/* RunShell.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/FrontDoorSomeone.scpt"); */
				/* RunShell.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/LivingRoomAlex.pl"); */
			}

			try {
				pushoverClient.PushoverClientPost("Someone is at the front door! -> "+ postVal, "WongHome");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}