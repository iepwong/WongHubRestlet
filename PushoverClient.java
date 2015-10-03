package WongHubRestlet;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class PushoverClient {

	public void PushoverClientPost(String value, String source) throws Exception {
		String AmeliaSleepToken = "XsfOaQFTSn96Gk5H7tM5PIhJbECeHY";
		String AlexanderStirToken = "6U2vcULF2MZ04PUip30Br7DnMEklnO";
		String WongHomeToken = "uYSOD2wlANdIHFh7vbcGbvvZP812pr";
		String WongOfficeToken = "amsHz5DNTPRDVHcNgJSNVH8JCCeEsZ";
		String WongLivingToken = "aWhSt4vJw1dwzXx7HrHbfSFP9f7BMc";
		String WongDiningToken = "a6fDi3iU9VSWTYNkZXX28K4CDwCm2K";
		String PushoverToken = "";
		String sound = "gamelan";
		String PushoverUserKey = "N0fmCA6zF37oS00EaIzcqNAS6TBf8T";
		// System.out.println(JSONString);

		if (source == "AmeliaSleep") {
			PushoverToken = AmeliaSleepToken;
			sound = "magic";
		}
		if (source == "AlexanderStir") {
			PushoverToken = AlexanderStirToken;
			sound = "alien";
		}
		if (source == "WongHome") {
			PushoverToken = WongHomeToken;
			sound = "intermission";
		}
		if (source == "WongOffice") {
			PushoverToken = WongOfficeToken;
			sound = "intermission";
		}
		if (source == "WongLiving") {
			PushoverToken = WongLivingToken;
			sound = "intermission";
		}
		if (source == "WongDining") {
			PushoverToken = WongDiningToken;
			sound = "intermission";
		}
		
		org.joda.time.DateTime datetime = new org.joda.time.DateTime();
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat
				.dateTimeNoMillis();
		String formattedDate = parser1.print(datetime);

		String Message = value + " at " + formattedDate;

		String httpstring = "https://api.pushover.net/1/messages.json?token="
				+ PushoverToken + "&user=" + PushoverUserKey + "&message=" + Message
				+ "&sound=" + sound;
		// System.out.println(httpstring);

		Form form = new Form();
		form.add("Category", "");

		ClientResource PushoverClient = new ClientResource(httpstring);
		PushoverClient.setRequestEntityBuffering(true);

		Representation reply = PushoverClient
				.post(form, MediaType.APPLICATION_JSON);
		System.out.println(reply);
	}

}
