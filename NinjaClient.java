package WongHubRestlet;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class NinjaClient {

	public void NinjaClientGet(String[] args) throws Exception {

		ClientResource NinjaClient = new ClientResource(
				"http://api.Ninja.com/v2/feeds/88818/datastreams/3.json?timezone=Melbourne&key=L5OWO0Ow-cmUfuXoAbS4p1cSTWz1o6KWsBQBgXf3oh0");

		// NinjaClient.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "iepwong",
		// "arat1209");

		Representation reply = NinjaClient.get(MediaType.APPLICATION_JSON);
		String replyText = reply.getText();
		JSONObject JsonReply = new JSONObject(replyText);
		System.out.println(replyText);
		System.out.println(JsonReply.getString("at"));
	}

	public void NinjaClientPut(String deviceid, String value) throws Exception {

		String JSONString = "{\"DA\" : \"" + value + "\"}";
		// System.out.println(JSONString);
		JSONObject NinjaElt = new JSONObject(JSONString.trim());
		String httpstring = "https://api.ninja.is/rest/v0/device/" + deviceid
				+ "?user_access_token=13e2f93b-dc6e-4460-b8e2-006456bcb85f";
		// System.out.println(httpstring);

		ClientResource NinjaClient = new ClientResource(httpstring);
		NinjaClient.setRequestEntityBuffering(true);

		Representation rep = new JsonRepresentation(NinjaElt);
		rep.setMediaType(MediaType.APPLICATION_JSON);
		Representation reply = NinjaClient.put(rep);
		System.out.println(reply);
	}

}
