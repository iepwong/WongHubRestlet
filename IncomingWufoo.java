package WongHubRestlet;

import org.json.JSONException;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.data.Form;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class IncomingWufoo extends ServerResource {

	@Post
	public void poststore(Representation rep) throws JSONException {
		Form form = new Form(rep);
		String Field1 = form.getFirstValue("Field1");
		
		System.out.println("Incoming Wufoo: " + Field1);
		System.out.println("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/SayAllOverHouse.scpt \"     "+Field1+"\"");
		
		try {
		Runtime.getRuntime().exec(new String[]{ "/usr/bin/osascript","/Users/Ian/Documents/scripts/AppleScripts/SayAllOverHouse.scpt",Field1});
		Runtime.getRuntime().exec(new String[]{ "/usr/bin/osascript","/Users/Ian/Documents/scripts/AppleScripts/SayAllOverHouse.scpt",Field1});
		Runtime.getRuntime().exec(new String[]{ "/usr/bin/osascript","/Users/Ian/Documents/scripts/AppleScripts/SayAllOverHouse.scpt",Field1});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}		
}

