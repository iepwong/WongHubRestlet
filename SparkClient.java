package WongHubRestlet;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class SparkClient {

	public int SparkClientGetLight(String room) {

		String deviceID = "";
		
		if (room.equals("Front")) {
			deviceID = "48ff75065067555046362287";
		}
		if (room.equals("Dining")) {
			deviceID = "55ff70066678505506321367";
		}
		if (room.equals("Living")) {
			deviceID = "55ff6f066678505552371367";
		}
		if (room.equals("Alex")) {
			deviceID = "54ff6d066678574944460267";
		}
		if (room.equals("Amelia")) {
			deviceID = "48ff71065067555034481587";
		}
		
		ClientResource SparkClient = new ClientResource(
				"https://api.spark.io/v1/devices/"+deviceID+"/light?access_token=a0ef33fbe9211def0a46fbb941a625fb4a5a6d4e");
		int Light = 0;
		
		
		
		// System.out.println("Getting room light from Spark: "+room);
		
		Representation reply = SparkClient.get(MediaType.APPLICATION_JSON);
		try {
			String replyText = reply.getText();
			JSONObject JsonReply = new JSONObject(replyText);
			// System.out.println(replyText);
			// System.out.println(room+" light: "+JsonReply.getString("result"));
			Light = Integer.parseInt(JsonReply.getString("result"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Light;
	}
}
