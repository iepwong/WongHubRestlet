package WongHubRestlet;

import java.util.Calendar;
import java.util.Date;
import java.lang.Math;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import redis.clients.jedis.Jedis;

/**
 * Resource corresponding to a WongHub received or sent with the parent WongHub
 * account. Leverages JSON.org extension.
 */
public class NinjaServerResource extends ServerResource {

	@Get
	public Representation toJson() throws JSONException {
		// Create a JSON object structure similar to a map
		JSONObject NinjaElt = new JSONObject();
		NinjaElt.put("status", "received");
		NinjaElt.put("subject", "We only take POSTs");
		NinjaElt.put("content", "Doh!");
		NinjaElt.put("accountRef", new Reference(getReference(), "..")
				.getTargetRef().toString());
		return new JsonRepresentation(NinjaElt);
	}

	private String getLEDColor(int postVal) {
		String LEDColor = "050505";
		if (postVal > 10) {
			LEDColor = "7F00FF";
		}
		if (postVal > 20) {
			LEDColor = "003FFF";
		}
		if (postVal > 30) {
			LEDColor = "00FF9D";
		}
		if (postVal > 40) {
			LEDColor = "E2FF00";
		}
		if (postVal > 50) {
			LEDColor = "FFBF00";
		}
		if (postVal > 60) {
			LEDColor = "FF0000";
		}
		if (postVal > 70) {
			LEDColor = "FFFFFF";
		}
		return LEDColor;
	}

	@Post
	public void poststore(JsonRepresentation NinjaRep) throws JSONException {
		// Parse the JSON representation to get the Ninja properties
		JSONObject NinjaElt = NinjaRep.getJsonObject();
		Date inDate = new Date(Long.parseLong(NinjaElt.getString("timestamp")));
		String DA = NinjaElt.getString("DA");
		COSMClient cOSMClient = new COSMClient();
		NinjaClient ninjaClient = new NinjaClient();
		int postVal = 0;
		float minuteselapsed = 5;
		int secondsElapsed = 0;
		int secondsBetween = 50;
		int secondsBetweenCurrent = 5;
		int secondsBetweenCorrelated = 5;
		int CORRTHRESH = 5;
		PushoverClient pushoverClient = new PushoverClient();
		Date date1 = new Date();
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		Jedis jedis = new Jedis("localhost");
		String lastDateStr;
		String lastDateStrFrontDoor1;
		String lastDateStrFrontDoor2;
		String lastDateStrFrontDoorCorrelated;
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();

		if (DA.equals("000101110100010101010000")) {
			// Leroy Door Motion
			lastDateStr = jedis.get("LeroyDoor");
			if (lastDateStr != null) {
				org.joda.time.DateTime leroydatetime = parser1.parseDateTime(lastDateStr);
				secondsBetween = org.joda.time.Seconds.secondsBetween(leroydatetime, currentdatetime).getSeconds();
			}

			if (secondsBetween > 30) {
				jedis.set("LeroyDoor", parser1.print(currentdatetime));

				System.out.println("Leroy Door: " + inDate);
				try {
					pushoverClient.PushoverClientPost("Leroy Door Sensor", "WongHome");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (DA.equals("010100010101111101010101")) {
			// Dining Room Motion
			lastDateStr = jedis.get("DiningRoomMotion");
			float LIGHTTHRESH = (float)0.5;
			
			String OfficeLightStr = jedis.get("PrevOfficeLight");
			
			if (OfficeLightStr == null) OfficeLightStr = "0";
			
			float OfficeLight =  Float.parseFloat(OfficeLightStr);
			
			if (OfficeLight < LIGHTTHRESH) {
				if ((hour > 21) || (hour < 8)) {
					RunShell
							.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/LivingRoomNight.pl");
				}
			}
		}
		
		if (DA.equals("010101010111111101010101")) {
			// Alexander Motion
			lastDateStr = jedis.get("AlexanderTime");
			if (lastDateStr != null) {
				org.joda.time.DateTime alexdatetime = parser1
						.parseDateTime(lastDateStr);
				secondsBetween = org.joda.time.Seconds.secondsBetween(alexdatetime,
						currentdatetime).getSeconds();
			}
			// System.out.println("Alexander Seconds Between: "+secondsBetween);

			if (secondsBetween > 3) {
				jedis.set("AlexanderTime", parser1.print(currentdatetime));
				try {
					postVal = cOSMClient.COSMClientGetLastUpdate(88818, 5, 3);
					secondsElapsed = cOSMClient.COSMClientGetSecondsSinceLastUpdate(88818, 5);
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				try {
					cOSMClient.COSMClientPostImpulse(88818, 5, date1,
							Integer.toString(postVal));
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				// System.out.println("Alexander Seconds: "+secondsElapsed);

				if (((postVal % 10) == 0) && (postVal !=0)){
					if ((hour > 7) && (hour < 23)) {
						RunShell.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/AlexanderIsStirring.scpt");				
					}
					try {
						pushoverClient.PushoverClientPost("Alexander is stirring! -> "
								+ postVal, "AlexanderStir");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}

				RunShell.Run("/Users/Ian/blink1-tool --blue --blink 5 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --green --blink 5 -d 1");
				System.out.println("Alexander Motion: " + postVal + " -> " + inDate);
			}
		}

		if (DA.equals("011111110111010101010101")){
			// Front Door 2 Motion
			lastDateStrFrontDoor2 = jedis.get("FrontDoor2MotionTime");
			lastDateStrFrontDoor1 = jedis.get("FrontDoor1MotionTime");
			lastDateStrFrontDoorCorrelated = jedis.get("FrontDoorCorrelatedMotionTime");
			// System.out.println(lastDateStrFrontDoor2+"->FD2 & "+lastDateStrFrontDoor1);
			if (lastDateStrFrontDoor2 != null && lastDateStrFrontDoor1 != null) {
				org.joda.time.DateTime frontdoor2motiondatetime = parser1.parseDateTime(lastDateStrFrontDoor2);
				org.joda.time.DateTime frontdoor1motiondatetime = parser1.parseDateTime(lastDateStrFrontDoor1);
				secondsBetween = Math.abs(org.joda.time.Seconds.secondsBetween(frontdoor2motiondatetime, frontdoor1motiondatetime).getSeconds());
				secondsBetweenCurrent = Math.abs(org.joda.time.Seconds.secondsBetween(frontdoor2motiondatetime, currentdatetime).getSeconds());
			}
			if (lastDateStrFrontDoorCorrelated != null) {
				org.joda.time.DateTime frontdoorcorrelatedmotiondatetime = parser1.parseDateTime(lastDateStrFrontDoorCorrelated);
				secondsBetweenCorrelated = Math.abs(org.joda.time.Seconds.secondsBetween(frontdoorcorrelatedmotiondatetime, currentdatetime).getSeconds());
			}
			if (secondsBetweenCurrent > 1) {
				jedis.set("FrontDoor2MotionTime", parser1.print(currentdatetime));
				System.out.println("Front Door 2 Motion -> " + inDate);
				if (secondsBetween < 20 && secondsBetweenCorrelated > CORRTHRESH) {
					jedis.set("FrontDoorCorrelatedMotionTime", parser1.print(currentdatetime));
					try {
						postVal = cOSMClient.COSMClientGetLastUpdate(122999, 0, 1);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 0");
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 1");
					try {
						cOSMClient.COSMClientPostImpulse(122999, 0, date1,
								Integer.toString(postVal));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("Front Door Correlated Motion -> " + inDate);
					try {
						pushoverClient.PushoverClientPost("Front Door Correlated Motion ", "WongHome");
					}
					catch (Exception e) {
						e.printStackTrace();}
				}
			}
		}
		
		if (DA.equals("010101010101010101010101")) {
			// Front Door Motion
			lastDateStrFrontDoor2 = jedis.get("FrontDoor2MotionTime");
			lastDateStrFrontDoor1 = jedis.get("FrontDoor1MotionTime");
			lastDateStrFrontDoorCorrelated = jedis.get("FrontDoorCorrelatedMotionTime");
			// System.out.println(lastDateStrFrontDoor1+"->FD1 & "+lastDateStrFrontDoor2);
			if (lastDateStrFrontDoor2 != null && lastDateStrFrontDoor1 != null) {
				org.joda.time.DateTime frontdoor2motiondatetime = parser1.parseDateTime(lastDateStrFrontDoor2);
				org.joda.time.DateTime frontdoor1motiondatetime = parser1.parseDateTime(lastDateStrFrontDoor1);
				secondsBetween = Math.abs(org.joda.time.Seconds.secondsBetween(frontdoor2motiondatetime, frontdoor1motiondatetime).getSeconds());
				secondsBetweenCurrent = Math.abs(org.joda.time.Seconds.secondsBetween(frontdoor1motiondatetime, currentdatetime).getSeconds());
			}
			if (lastDateStrFrontDoorCorrelated != null) {
				org.joda.time.DateTime frontdoorcorrelatedmotiondatetime = parser1.parseDateTime(lastDateStrFrontDoorCorrelated);
				secondsBetweenCorrelated = Math.abs(org.joda.time.Seconds.secondsBetween(frontdoorcorrelatedmotiondatetime, currentdatetime).getSeconds());
			}
			if (secondsBetweenCurrent > 1) {
				jedis.set("FrontDoor1MotionTime", parser1.print(currentdatetime));
				System.out.println("Front Door 1 Motion -> " + inDate);
				if (secondsBetween < 20 && secondsBetweenCorrelated > CORRTHRESH) {
					jedis.set("FrontDoorCorrelatedMotionTime", parser1.print(currentdatetime));
					try {
						postVal = cOSMClient.COSMClientGetLastUpdate(122999, 0, 1);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 0");
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 1");
					try {
						cOSMClient.COSMClientPostImpulse(122999, 0, date1,
								Integer.toString(postVal));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("Front Door Correlated Motion -> " + inDate);
					try {
						pushoverClient.PushoverClientPost("Front Door Correlated Motion ", "WongHome");
					}
					catch (Exception e) {
						e.printStackTrace();}
				}
			}
		}

		if (DA.equals("001100110101010101010000")) {
			// Front Door Sensor
			try {
				postVal = cOSMClient.COSMClientGetLastUpdate(122999, 1, 1);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			RunShell.Run("/Users/Ian/blink1-tool --rgb 128,0,128 --blink 2 -d 0");
			RunShell.Run("/Users/Ian/blink1-tool --rgb 0,128,128 --blink 2 -d 1");
			try {
				cOSMClient.COSMClientPostImpulse(122999, 1, date1,
						Integer.toString(postVal));
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			try {
				pushoverClient.PushoverClientPost("Front door has opened!", "WongHome");
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("Front Door Open: " + postVal + " -> " + inDate);
			if ((hour > 7) && (hour < 23)) {
				RunShell.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/FrontDoorOpen.scpt");				
			}
		}

		if (DA.equals("010101010001010101010101")) {
			// Office Motion
			lastDateStr = jedis.get("OfficeMotionTime");
			if (lastDateStr != null) {
				org.joda.time.DateTime officemotiondatetime = parser1
						.parseDateTime(lastDateStr);
				secondsBetween = org.joda.time.Seconds.secondsBetween(
						officemotiondatetime, currentdatetime).getSeconds();
			}
			if (secondsBetween > 1) {
				jedis.set("OfficeMotionTime", parser1.print(currentdatetime));
				try {
					postVal = cOSMClient.COSMClientGetLastUpdate(102211, 1, 5);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				String LEDColor = getLEDColor(postVal);
				String RGBDec = "0x" + LEDColor.substring(0, 2) + ",0x"
						+ LEDColor.substring(2, 4) + ",0x" + LEDColor.substring(4, 6);
				RunShell.Run("/Users/Ian/blink1-tool --rgb " + RGBDec
						+ " --blink 1 -d 1");
				try {
					ninjaClient.NinjaClientPut("4412BB000368_0_0_1007", LEDColor);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				try {
					cOSMClient.COSMClientPostImpulse(102211, 1, date1,
							Integer.toString(postVal));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Office Motion: " + postVal + " -> " + inDate);
			}
		}
	}
}
