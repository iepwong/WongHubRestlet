package WongHubRestlet;

import org.json.JSONException;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import redis.clients.jedis.Jedis;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class UbiServerResource extends ServerResource {

	@Post
	public void poststore(Representation rep) throws JSONException {
		String Command = getQuery().getValues("command");
		HueClient hueClient = new HueClient();
		LifxClient lifxClient = new LifxClient();
		Jedis jedis = new Jedis("localhost");
		
		System.out.println("UbiServer: " + Command);

		if (Command.equals("AnnounceOn")) {
			jedis.set("AlexButton", "on");
			RunShell.Run("/Users/Ian/blink1-tool --green -d 4");
		}
		if (Command.equals("AnnounceOff")) {
			jedis.set("AlexButton", "off");
			RunShell.Run("/Users/Ian/blink1-tool --red -d 4");
		}
		if (Command.equals("FrontLightOn")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 3");
				lifxClient.LifxClientPut("label:FrontDoor", "on","1","white");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Command.equals("FrontLightOff")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 3");
				lifxClient.LifxClientPut("label:FrontDoor", "off","1","white");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Command.equals("KidsLightOn")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 3");
				RunShell
						.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/AmeliaElephant.pl");
				hueClient.HueClientPut("2", "true", "550", "10", "none");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Command.equals("KidsLightOff")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 3");
				RunShell
						.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/AmeliaElephantOff.pl");
				hueClient.HueClientPut("2", "false", "25500", "50", "none");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Command.equals("LivingLightOn")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 3");
				lifxClient.LifxClientPut("label:LivingRoom", "on","1","orange");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Command.equals("LivingLightOff")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 3");
				lifxClient.LifxClientPut("label:LivingRoom", "off","1","orange");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Command.equals("DiningLightOn")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 3");
				hueClient.HueClientPut("3", "true", "550", "10", "none");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Command.equals("DiningLightOff")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 3");
				hueClient.HueClientPut("3", "false", "100", "0", "none");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Command.equals("AlexLightOn")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 2 -d 3");
				hueClient.HueClientPut("2", "true", "550", "10", "none");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Command.equals("AlexLightOff")) {
			try {
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 2 -d 3");
				hueClient.HueClientPut("2", "false", "100", "0", "none");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
