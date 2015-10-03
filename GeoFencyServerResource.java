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
public class GeoFencyServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation rep) throws JSONException {
		int postVal = 0;
		int secondsBetween = 0;
		int lateHour = 17;
		LifxClient lifxClient = new LifxClient();
		HueClient hueClient = new HueClient();
		SparkClient sparkClient = new SparkClient();
		PushoverClient pushoverClient = new PushoverClient();
		GetTwitterStatus getTwitterStatus = new GetTwitterStatus();
		Jedis jedis = new Jedis("localhost");
		String WongHubAnnounce = jedis.get("WongHubAnnounce");
		org.joda.time.DateTime currentdatetime = new org.joda.time.DateTime();
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		String lastDateStr;
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int nightHour = 20;
		
		JSONObject Elt = rep.getJsonObject();
		String Event = Elt.getString("entry");
		String Location = Elt.getString("name");
	
		if (Event.equals("0")){
			Event= "exited";
		}
		if (Event.equals("1")){
			Event= "entered";
		}
	
		System.out.println("Ian has "+Event+" "+Location+".");
		try {
			// pushoverClient.PushoverClientPost("Ian has "+Event+" "+Location+"! -> " + postVal, "WongHome");
			getTwitterStatus.postWhereisiepwongMsg("Ian has "+Event+" "+Location+"! -> "+ postVal);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		float LIGHTTHRESH = (float)90;
		float Light =(float) sparkClient.SparkClientGetLight("Front");
		
		if (Location.equals("78 Berry Street")) {

			lastDateStr = jedis.get("IanHomeTime");
			jedis.set("IanHomeTime", parser1.print(currentdatetime));

			if (lastDateStr != null) {
				org.joda.time.DateTime alexdatetime = parser1
						.parseDateTime(lastDateStr);
				secondsBetween = org.joda.time.Seconds.secondsBetween(alexdatetime,
						currentdatetime).getSeconds();
			}

			if (secondsBetween > 300) {
				if (hour > lateHour && Light < LIGHTTHRESH) {
					try {
						lifxClient.LifxClientPut("label:FrontDoor", "on", "1","white");
						lifxClient.LifxClientPut("label:FrontDoor", "on", "300","white");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			} /* 1800 */
		} /* Home */
	}
}
