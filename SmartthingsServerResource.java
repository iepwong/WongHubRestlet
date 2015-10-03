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
public class SmartthingsServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation rep) throws JSONException {
		int secondsBetween = 0;
		int lateHour = 17;
		LifxClient lifxClient = new LifxClient();
		SparkClient sparkClient = new SparkClient();
		PushoverClient pushoverClient = new PushoverClient();
		Jedis jedis = new Jedis("localhost");
		org.joda.time.DateTime currentdatetime = new org.joda.time.DateTime();
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		String lastDateStr;
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		
		try {
			float LIGHTTHRESH = (float)90;
			float Light =(float) sparkClient.SparkClientGetLight("Front");
			String PresenceStr = rep.getText();
			String[] presenceStrArray = PresenceStr.split("\\."); // String array, each element is text between dots
			String car = presenceStrArray[0];
			String action = presenceStrArray[1];
		
			System.out.println(car+" "+action+"!");
			
			if (car.equals("Subaru") || car.equals("Peugeot")) {
				jedis.set(PresenceStr, parser1.print(currentdatetime));

				if (action.equals("Arrive")) {
					lastDateStr = jedis.get(car+".Leave");
					if (lastDateStr != null) {
						org.joda.time.DateTime alexdatetime = parser1.parseDateTime(lastDateStr);
						secondsBetween = org.joda.time.Seconds.secondsBetween(alexdatetime,currentdatetime).getSeconds();
					}

					if (secondsBetween > 300) {
						try {
							pushoverClient.PushoverClientPost(car+" "+action+"! -> ", "WongHome");
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						if (hour > lateHour && Light < LIGHTTHRESH) {
							try {
								lifxClient.LifxClientPut("label:FrontDoor", "on", "1","white");
								lifxClient.LifxClientPut("label:FrontDoor", "on", "300","white");
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				if (action.equals("Leave")) {
					lastDateStr = jedis.get(car+".Arrive");
					if (lastDateStr != null) {
						org.joda.time.DateTime alexdatetime = parser1.parseDateTime(lastDateStr);
						secondsBetween = org.joda.time.Seconds.secondsBetween(alexdatetime,currentdatetime).getSeconds();
					}

					if (secondsBetween > 300) {
						try {
							pushoverClient.PushoverClientPost(car+" "+action+"! -> ", "WongHome");
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						if (hour > lateHour && Light < LIGHTTHRESH) {
							try {
								lifxClient.LifxClientPut("label:FrontDoor", "on", "1","white");
								lifxClient.LifxClientPut("label:FrontDoor", "on", "300","white");
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
			} 
	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
