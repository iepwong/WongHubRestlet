package WongHubRestlet;

import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class ResetOfficeLightServerResource extends ServerResource {

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		SparkClient sparkClient = new SparkClient();
		CubeSensor cubeSensor = new CubeSensor();
		LifxClient lifxClient = new LifxClient();
		float PrevLight =0;
		
		try {
		PrevLight = (float) sparkClient.SparkClientGetLight("Alex");
		// cubeSensor.getDevices();
		} catch (Exception e) {e.printStackTrace();}
		System.out.println("Alex Light -> "+PrevLight);

		// Alexander's Temperature
		if (PrevLight < 90){
			try {
				ClientResource NinjaClient = new ClientResource(
						"https://api.ninja.is/rest/v0/device/4412BB000368_0103_0_31/heartbeat?user_access_token=13e2f93b-dc6e-4460-b8e2-006456bcb85f");
				Representation reply = NinjaClient.get(MediaType.APPLICATION_JSON);
				String replyText = reply.getText();
				// System.out.println(replyText);
				JSONObject JsonReply = new JSONObject(replyText);
				String DataStr = JsonReply.getString("data");
				// System.out.println(DataStr);
				JSONObject JsonData = new JSONObject(DataStr);
				String AlexTemp = JsonData.getString("DA");
				System.out.println("Alex Temp -> "+AlexTemp);

				if (Float.parseFloat(AlexTemp) > 20) {
					RunShell
					.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/AlexanderHeaterOff.pl");
				}
				if (Float.parseFloat(AlexTemp) < 18) {
					RunShell
					.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/AlexanderHeater.pl");
				}			
			} catch (Exception e) {e.printStackTrace();}
		}
		
		try {
			lifxClient.LifxClientPut("label:FrontDoor", "off", "1", "white");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			lifxClient.LifxClientPut("label:LivingRoom", "off", "1", "orange");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {	lifxClient.LifxClientPut("label:DiningRoom", "off", "1", "orange");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			lifxClient.LifxClientPut("label:Backyard", "off", "1", "white");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		RunShell
		.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/BathroomOff.pl");
	} 
}
