package WongHubRestlet;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class SenseClient {

	public float SenseClientGetLastEvent(int feed) throws Exception {

		ClientResource SenseClient = new ClientResource("http://api.sen.se/feeds/"
				+ feed + "/last_event/?sense_key=dMsuklvoGphzLUA2LBBgTQ&");

		Representation reply = SenseClient.get(MediaType.APPLICATION_JSON);
		String replyText = reply.getText();
		JSONObject JsonReply = new JSONObject(replyText);
		// System.out.println(replyText);
		return Float.parseFloat(JsonReply.getString("value"));
	}

	public void SenseClientPost(int feed, String value) throws Exception {
		String JSONString = "{\n\"feed_id\" : \"" + feed + "\",\n\"value\" : \""
				+ value + "\"\n}";
		// System.out.println(JSONString);
		JSONObject SenseElt = new JSONObject(JSONString.trim());

		ClientResource SenseClient = new ClientResource(
				"http://api.sen.se/events/?sense_key=dMsuklvoGphzLUA2LBBgTQ");
		SenseClient.setRequestEntityBuffering(true);
		

		Representation rep = new JsonRepresentation(SenseElt);
		rep.setMediaType(MediaType.APPLICATION_WWW_FORM);
		Representation reply = SenseClient.post(rep);
		// System.out.println(reply+"SenseClient "+value);
	}

}
