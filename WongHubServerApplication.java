package WongHubRestlet;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

/**
 * Simple test application serving WongHub resources using JSON.org extension.
 */
public class WongHubServerApplication extends Application {

	/**
	 * Launches the application with an HTTP server.
	 * 
	 * @param args
	 *          The arguments.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Component WongHubServer = new Component();
		
		GetTwitterStatus getTwitterStatus = new GetTwitterStatus();
		getTwitterStatus.loginTwitter();

		WongHubServer.getClients().add(Protocol.HTTP);
		WongHubServer.getClients().add(Protocol.HTTPS);
		WongHubServer.getServers().add(Protocol.HTTP, 58080);
		WongHubServer.getDefaultHost().attach(new WongHubServerApplication());
		WongHubServer.start();
	}

	/**
	 * Creates a root Router to dispatch call to server resources.
	 */
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/accounts/{accountId}/WongHubs/{WongHubId}", Blink1ServerResource.class);
		router.attach("/blink1", Blink1ServerResource.class);
		router.attach("/ninja", NinjaServerResource.class);
		router.attach("/ninja/blocktemp", NinjaBlockTempServerResource.class);
		router.attach("/ninja/temp", NinjaTempServerResource.class);
		router.attach("/ninja/humid", NinjaHumidServerResource.class);
		router.attach("/newninja/temp", NewNinjaTempServerResource.class);
		router.attach("/Amelia/temp", AmeliaTempServerResource.class);
		router.attach("/Amelia/light", AmeliaLightServerResource.class);
		router.attach("/Amelia/humid", AmeliaHumidServerResource.class);
//		router.attach("/newninja/humid", NewNinjaHumidServerResource.class);
		router.attach("/Dining/light", DiningLightServerResource.class);
		router.attach("/Dining/audio", DiningAudioServerResource.class);
		router.attach("/Office/light", OfficeLightServerResource.class);
		router.attach("/Living/light", LivingLightServerResource.class);
		router.attach("/Alex/light", AlexanderLightServerResource.class);
		router.attach("/Amelia/audio", AmeliaAudioServerResource.class);
		router.attach("/Living/audio", LivingAudioServerResource.class);
		router.attach("/Alex/audio", AlexanderAudioServerResource.class);
		router.attach("/vitamind/hallway", VitaminDHallwayServerResource.class);
		router.attach("/vitamind/sidegate", VitaminDSidegateServerResource.class);
		router.attach("/vitamind/frontdoor", VitaminDFrontDoorServerResource.class);
		router.attach("/vitamind/amelia", VitaminDAmeliaServerResource.class);
		router.attach("/vitamind/backyard", VitaminDBackyardServerResource.class);
		router.attach("/vitamind/alexander", VitaminDAlexanderServerResource.class);
		router.attach("/wonghub/announce", WongHubAnnounceServerResource.class);
		router.attach("/Office/resetlight", ResetOfficeLightServerResource.class);
		router.attach("/Office/checkAudio", OfficeCheckAudioResource.class);
		router.attach("/geohopper/ianhome", GeoHopperIanHomeServerResource.class);
		router.attach("/geofency", GeoFencyServerResource.class);
		router.attach("/Smartthings", SmartthingsServerResource.class);
		router.attach("/UbiServer", UbiServerResource.class);
		router.attach("/incoming_wufoo", IncomingWufoo.class);
		return router;
	}
}
