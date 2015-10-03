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
public class AlexanderAudioServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		// Parse the JSON representation to get the Ninja properties
		float AUDIOTHRESH = (float) 1800;
		float LIGHTTHRESH = (float) 90.0;
		PushoverClient pushoverClient = new PushoverClient();
		SparkClient sparkClient = new SparkClient();
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		Jedis jedis = new Jedis("localhost");
		String WebHubAnnounce = jedis.get("WongHubAnnounce");
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();
		int secondsBetween = 0;
		int moveSecondsBetween = 600;
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		String lastDateStr;
		
		jedis.set("AlexAudioTime", parser1.print(currentdatetime));
		String lastAlexMoveDateStr = jedis.get("AlexanderTime");
		if (lastAlexMoveDateStr != null) {
			org.joda.time.DateTime alexmovedatetime = parser1.parseDateTime(lastAlexMoveDateStr);
			moveSecondsBetween = org.joda.time.Seconds.secondsBetween(alexmovedatetime, currentdatetime).getSeconds();
		}
			
		float PrevLight =(float) sparkClient.SparkClientGetLight("Alex");

		String DA = NinjaElt.getString("DA");
		String PrevAudioStr = jedis.get("PrevAlexAudio");
		if (PrevAudioStr == null) PrevAudioStr = "0";
		jedis.set("PrevAlexAudio", DA);
			
		if (Float.parseFloat(DA)  >= AUDIOTHRESH && PrevLight < LIGHTTHRESH){
			lastDateStr = jedis.get("AlexAudioThresholdTime");
			if (lastDateStr != null) {
				org.joda.time.DateTime alexdatetime = parser1.parseDateTime(lastDateStr);
				secondsBetween = org.joda.time.Seconds.secondsBetween(alexdatetime, currentdatetime).getSeconds();
			}

			jedis.set("AlexAudioThresholdTime", parser1.print(currentdatetime));
			if (secondsBetween > 15 && secondsBetween < 240 && moveSecondsBetween < 30) {


				try
				{
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 1 -d 0");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --blue --blink 1 -d 1");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --green --blink 1 -d 2");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 1 -d 3");
					Thread.sleep(500);
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 1 -d 3");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --blue --blink 1 -d 2");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --green --blink 1 -d 1");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 1 -d 0");		
					Thread.sleep(500);
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 1 -d 0");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --blue --blink 1 -d 1");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --green --blink 1 -d 2");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 1 -d 3");
					Thread.sleep(500);
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 1 -d 3");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --blue --blink 1 -d 2");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --green --blink 1 -d 1");
					Thread.sleep(200);
					RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 1 -d 0");
					// System.out.println("Alexander Audio Tripped: " +DA+ " -> "+ inDate+" "+ moveSecondsBetween+" = "+ lastAlexMoveDateStr + " - " + currentdatetime);
					pushoverClient.PushoverClientPost("Alexander Audio Tripped -> "+DA, "AlexanderStir");
					
					if ((hour < 21) && (hour > 6)) {
					if (WebHubAnnounce.equals("on")) {
						RunShell
						.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/LivingRoomAlex.pl");
						RunShell
						.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/AlexanderIsCrying.scpt");
						RunShell.Run("curl https://portal.theubi.com/webapi/behaviour?access_token=0299d8f0-7934-475b-8c8c-40f11bb1a701");
					}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
