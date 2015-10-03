package WongHubRestlet;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class HueClient {


	public void HueClientPut(String deviceid, String onValue, String hueValue, String satValue, String effectValue) throws Exception {

		String JSONString = "{\"on\" : "+onValue+", \"hue\": " + hueValue + ", \"bri\" : 255, \"sat\": " + satValue + ", \"alert\" : "+effectValue+"}";
		// System.out.println(JSONString);
		JSONObject NinjaElt = new JSONObject(JSONString.trim());
		String httpstring = "http://10.1.1.114/api/newdeveloper/lights/"+deviceid+"/state";

		ClientResource NinjaClient = new ClientResource(httpstring);
		NinjaClient.setRequestEntityBuffering(true);

		Representation rep = new JsonRepresentation(NinjaElt);
		rep.setMediaType(MediaType.APPLICATION_JSON);
		Representation reply = NinjaClient.put(rep);
		String replyText = reply.getText();
		System.out.println(replyText);
	}

}
