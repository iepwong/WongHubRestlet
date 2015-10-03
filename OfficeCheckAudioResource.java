package WongHubRestlet;


import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import redis.clients.jedis.Jedis;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class OfficeCheckAudioResource extends ServerResource {

	private void checkAudioTime(String AudioTimeStr, String PushoverStr) {
		String lastDateStr;
		Jedis jedis = new Jedis("localhost");
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat
				.dateTime();
		currentdatetime = new org.joda.time.DateTime();
		int secondsBetween = 0;
		PushoverClient pushoverClient = new PushoverClient();

		lastDateStr = jedis.get(AudioTimeStr);
		if (lastDateStr != null) {
			org.joda.time.DateTime AudioDateTime = parser1.parseDateTime(lastDateStr);
			secondsBetween = org.joda.time.Seconds.secondsBetween(AudioDateTime,
					currentdatetime).getSeconds();
		}
		
		if (secondsBetween > 600 && secondsBetween < 86400) {
			System.out.println(AudioTimeStr + " is down:" + secondsBetween + " @ "
					+ currentdatetime);
			try {
				pushoverClient.PushoverClientPost(AudioTimeStr
						+ " is down.  Last pulse = " + secondsBetween + " seconds back ",
						PushoverStr);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (secondsBetween > 86400  && (secondsBetween % 3600) == 0) {
			System.out.println(AudioTimeStr + " is down:" + secondsBetween + " @ "
					+ currentdatetime);
			try {
				pushoverClient.PushoverClientPost(AudioTimeStr
						+ " is down.  Last pulse = " + secondsBetween + " seconds back ",
						PushoverStr);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		/* checkAudioTime("AlexAudioTime", "AlexanderStir");
		checkAudioTime("DiningAudioTime", "WongDining");
		checkAudioTime("LivingAudioTime", "WongLiving");
    checkAudioTime("AmeliaAudioTime", "AmeliaSleep"); */
	} 
}
