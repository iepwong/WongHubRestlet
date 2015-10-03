package WongHubRestlet;

import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.ChallengeScheme;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class LifxClient {

	public void LifxClientPut(String label, String onoff, String duration, String color) throws Exception {
		
		String httpstring = "https://api.lifx.com/v1beta1/lights/"+label+"/power";
		
		Form form = new Form();
		form.add("state", onoff);
		form.add("duration", duration);

		ClientResource NinjaClient = new ClientResource(httpstring);
		NinjaClient.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "cae6cfa59093a00835ac006f13d74724fc14b6f1332a7083b05bde5e285deed1", "");
		NinjaClient.setRequestEntityBuffering(true);

		Representation reply = NinjaClient.put(form, MediaType.APPLICATION_JSON);
		String replyText = reply.getText();
		System.out.println(replyText);

		String colorstring = "https://api.lifx.com/v1beta1/lights/"+label+"/color";
		
		Form colorform = new Form();
		colorform.add("color", color);
		colorform.add("power_on", "false");

		NinjaClient = new ClientResource(colorstring);
		NinjaClient.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "cae6cfa59093a00835ac006f13d74724fc14b6f1332a7083b05bde5e285deed1", "");
		NinjaClient.setRequestEntityBuffering(true);

		reply = NinjaClient.put(colorform, MediaType.APPLICATION_JSON);
		replyText = reply.getText();
		System.out.println(replyText);
		}
}
