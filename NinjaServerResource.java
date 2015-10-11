package WongHubRestlet;

import java.util.Calendar;
import java.util.Date;
import java.lang.Math;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
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
		NinjaClient ninjaClient = new NinjaClient();
		SparkClient sparkClient = new SparkClient();
		HueClient hueClient = new HueClient();
		LifxClient lifxClient = new LifxClient();
		CubeSensor cubeSensor = new CubeSensor();
		GetTwitterStatus getTwitterStatus = new GetTwitterStatus();
		int postVal = 0;
		int secondsBetween = 50;
		int secondsBetweenCurrent = 0;
		int secondsBetweenCorrelated = 5;
		int CORRTHRESH = 60;
		int wakeup = 6;
		int lateHour = 17;
		int nightHour = 20;
		int SPARKLIGHTTHRESH = 240;
		PushoverClient pushoverClient = new PushoverClient();
		Date date1 = new Date();
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		Jedis jedis = new Jedis("localhost");
		String WongHubAnnounce = jedis.get("WongHubAnnounce");
		String WongHubAnnounceTime;
		String lastDateStr;
		String lastDateStrFrontDoor1;
		String lastDateStrFrontDoor2;
		String lastDateStrFrontDoorCorrelated;
		String AlexButton = jedis.get("AlexButton");
		org.joda.time.DateTime currentdatetime;
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		currentdatetime = new org.joda.time.DateTime();

		// cubeSensor.getDevices();
		
		if (DA.equals("110101010101011100110000")) {
		// Alert Button Pushed	
			WongHubAnnounce = jedis.get("WongHubAnnounce");
			WongHubAnnounceTime = jedis.get("WongHubAnnounceTime");
			
			if (WongHubAnnounceTime != null) {
				org.joda.time.DateTime wonghubannouncedatetime = parser1.parseDateTime(WongHubAnnounceTime);
				secondsBetween = org.joda.time.Seconds.secondsBetween(wonghubannouncedatetime, currentdatetime).getSeconds();
			}
			if (WongHubAnnounce == null) {
				jedis.set("WongHubAnnounce", "off");
			}
			jedis.set("WongHubAnnounceTime", parser1.print(currentdatetime));
			if (secondsBetween > 1) {
				if (WongHubAnnounce.equals("on")){
					jedis.set("WongHubAnnounce", "off");
					RunShell.Run("/Users/Ian/blink1-tool --red -d 4");
				} else
				{
					jedis.set("WongHubAnnounce", "on");				
					RunShell.Run("/Users/Ian/blink1-tool --green -d 4");
				}
				WongHubAnnounce = jedis.get("WongHubAnnounce");				
			}
		}
		
		if (DA.equals("000101110100010101010000")) {
			// Leroy Door Motion
			lastDateStr = jedis.get("LeroyDoor");
			RunShell.Run("/Users/Ian/blink1-tool --rgb 128,0,128 --blink 2 -d 3");
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
		
		if (DA.equals("010111011100000100110000")) {
			// Alexander Button Pushed	
			AlexButton = jedis.get("AlexButton");


			String AlexButtonTime = jedis.get("AlexButtonTime");
			if (AlexButtonTime != null) {
				org.joda.time.DateTime alexbuttondatetime = parser1.parseDateTime(AlexButtonTime);
				secondsBetween = org.joda.time.Seconds.secondsBetween(alexbuttondatetime, currentdatetime).getSeconds();
			}

			jedis.set("AlexButtonTime", parser1.print(currentdatetime));
			if (secondsBetween > 1) {
				System.out.println("Button is currently -> "+AlexButton);
				if (AlexButton == null) {
					jedis.set("AlexButton", "off");
				}
				if (AlexButton.equals("off")) {
					jedis.set("AlexButton", "on");
					try {
						hueClient.HueClientPut("2", "true", "100", "255", "none");
					} catch (Exception e) {e.printStackTrace();}					
				}
				if (AlexButton.equals("on")) {
					jedis.set("AlexButton", "off");
					try {
						hueClient.HueClientPut("2", "true", "100", "180", "none");
					} catch (Exception e) {e.printStackTrace();}	
				}
				AlexButton = jedis.get("AlexButton");
				System.out.println("Button turned to -> "+AlexButton);
			}
		}
		
		if (DA.equals("010100010101111101010101")) {
			// Dining Room Motion
			float LIGHTTHRESH = (float)89;
			float LivingLight = 0;
			float DiningLight = 0;

			String lastDateStrDiningMotion = jedis.get("DiningMotionTime");

			if (lastDateStrDiningMotion != null) {
				org.joda.time.DateTime diningmotiondatetime = parser1.parseDateTime(lastDateStrDiningMotion);
				secondsBetween = Math.abs(org.joda.time.Seconds.secondsBetween(currentdatetime, diningmotiondatetime).getSeconds());
			}
			if (secondsBetween > 5) {
				jedis.set("DiningMotionTime", parser1.print(currentdatetime));
				try {
					LivingLight =(float) sparkClient.SparkClientGetLight("Living");
					DiningLight =(float) sparkClient.SparkClientGetLight("Dining");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				RunShell.Run("/Users/Ian/blink1-tool --rgb 128,0,128 --blink 2 -d 2");			

				if ((hour > nightHour) || (hour < 7)) {
					if (LivingLight < LIGHTTHRESH) {
						RunShell
						.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/BathroomNight.pl");
						
						try {
							pushoverClient.PushoverClientPost("Dining room motion: "+DiningLight, "WongHome");
						}
						catch (Exception e) {
							e.printStackTrace();
						}

						try {
							try {
								lifxClient.LifxClientPut("label:LivingRoom", "on", "1", "orange brightness:1.0");
								lifxClient.LifxClientPut("label:LivingRoom", "off", "180", "orange brightness:0.5");
							} catch (Exception e) {e.printStackTrace();} 
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					} 
					if (DiningLight < LIGHTTHRESH) {
						try {
							lifxClient.LifxClientPut("label:DiningRoom", "on", "1","orange brightness:1.0");
							lifxClient.LifxClientPut("label:DiningRoom", "off", "180","orange brightness:0.5");
						} catch (Exception e) {e.printStackTrace();}
					}
				}
			}
		}

		if (DA.equals("010101010111111101010101")) {
			// Living Room Motion
			float LIGHTTHRESH = (float)89;
			float LivingLight = 0;
			float DiningLight = 0;

			String lastDateStrLivingMotion = jedis.get("LivingMotionTime");

			if (lastDateStrLivingMotion != null) {
				org.joda.time.DateTime livingmotiondatetime = parser1.parseDateTime(lastDateStrLivingMotion);
				secondsBetween = Math.abs(org.joda.time.Seconds.secondsBetween(currentdatetime, livingmotiondatetime).getSeconds());
			}
			if (secondsBetween > 5) {
				jedis.set("LivingMotionTime", parser1.print(currentdatetime));

				try {
					LivingLight =(float) sparkClient.SparkClientGetLight("Living");
					DiningLight =(float) sparkClient.SparkClientGetLight("Dining");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				RunShell.Run("/Users/Ian/blink1-tool --rgb 0,128,128 --blink 2 -d 2");			

				if ((hour > nightHour) || (hour < 7)) {
					if (LivingLight < LIGHTTHRESH) {
						RunShell
						.Run("perl /Users/Ian/Perl-Belkin-WeMo-API-master/BathroomNight.pl");
						
						try {
							pushoverClient.PushoverClientPost("Living room motion: "+LivingLight, "WongHome");
						}
						catch (Exception e) {
							e.printStackTrace();
						}

						try {
							lifxClient.LifxClientPut("label:LivingRoom", "on", "1","orange brightness:1.0");
							lifxClient.LifxClientPut("label:LivingRoom", "off", "180","orange brightness:0.5");
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (DiningLight < LIGHTTHRESH) {
						try {
							lifxClient.LifxClientPut("label:DiningRoom", "on", "1","orange brightness:1.0");
							lifxClient.LifxClientPut("label:DiningRoom", "off", "180","orange brightness:0.5");
						} catch (Exception e) {e.printStackTrace();}
					}
				}
			}
		}
		
		if (DA.equals("011111110111010101010101")){
			// Front Door 2 Motion
			lastDateStrFrontDoor2 = jedis.get("FrontDoor2MotionTime");
			lastDateStrFrontDoor1 = jedis.get("FrontDoor1MotionTime");
			lastDateStrFrontDoorCorrelated = jedis.get("FrontDoorCorrelatedMotionTime");

			if (lastDateStrFrontDoor2 != null && lastDateStrFrontDoor1 != null) {
				org.joda.time.DateTime frontdoor2motiondatetime = parser1.parseDateTime(lastDateStrFrontDoor2);
				org.joda.time.DateTime frontdoor1motiondatetime = parser1.parseDateTime(lastDateStrFrontDoor1);
				secondsBetween = Math.abs(org.joda.time.Seconds.secondsBetween(currentdatetime, frontdoor1motiondatetime).getSeconds());
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

					RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 0");
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 1");
					RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 5 -d 2");
					RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 5 -d 3");

					System.out.println("Front Door Correlated Motion -> " + " @ "+ inDate);
					
					if ((hour > wakeup) && (hour < nightHour)) {
						if (WongHubAnnounce.equals("on")) {
							RunShell.Run("curl https://portal.theubi.com/webapi/behaviour?access_token=c2c64031-fb6b-417a-9d43-aa381a0d9220");
						}
					}
				
					try {
						pushoverClient.PushoverClientPost("Front Door Correlated Motion ->" + secondsBetweenCorrelated, "WongHome");
					}
					catch (Exception e) {
						e.printStackTrace();}
				}

				if (sparkClient.SparkClientGetLight("Front") < SPARKLIGHTTHRESH) {
					try {
						try {
							lifxClient.LifxClientPut("label:FrontDoor", "on", "1","white brightness:1.0");
							lifxClient.LifxClientPut("label:FrontDoor", "off", "300","white brightness:0.75");
						} catch (Exception e) {e.printStackTrace();} 
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		if (DA.equals("010101010101010101010101")) {
			// Front Door Motion
			lastDateStrFrontDoor2 = jedis.get("FrontDoor2MotionTime");
			lastDateStrFrontDoor1 = jedis.get("FrontDoor1MotionTime");
			lastDateStrFrontDoorCorrelated = jedis.get("FrontDoorCorrelatedMotionTime");

			if (lastDateStrFrontDoor2 != null && lastDateStrFrontDoor1 != null) {
				org.joda.time.DateTime frontdoor2motiondatetime = parser1.parseDateTime(lastDateStrFrontDoor2);
				org.joda.time.DateTime frontdoor1motiondatetime = parser1.parseDateTime(lastDateStrFrontDoor1);
				secondsBetween = Math.abs(org.joda.time.Seconds.secondsBetween(frontdoor2motiondatetime, currentdatetime).getSeconds());
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

					RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 0");
					RunShell.Run("/Users/Ian/blink1-tool --red --blink 5 -d 1");
					RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 5 -d 2");
					RunShell.Run("/Users/Ian/blink1-tool --rgb 255,255,0 --blink 5 -d 3");

					System.out.println("Front Door Correlated Motion -> " +" @ "+ inDate);
					
					if ((hour > wakeup) && (hour < nightHour)) {
						if (WongHubAnnounce.equals("on"))
						{
							RunShell.Run("curl https://portal.theubi.com/webapi/behaviour?access_token=c2c64031-fb6b-417a-9d43-aa381a0d9220");

						}
					}

					try {
						pushoverClient.PushoverClientPost("Front Door Correlated Motion ->"+ secondsBetweenCorrelated, "WongHome");
					}
					catch (Exception e) {
						e.printStackTrace();}
				}
				
				if (sparkClient.SparkClientGetLight("Front") < SPARKLIGHTTHRESH) {
					try {
						try {
							lifxClient.LifxClientPut("label:FrontDoor", "on", "1","white brightness:1.0");
							lifxClient.LifxClientPut("label:FrontDoor", "off", "300","white brightness:0.75");
						} catch (Exception e) {e.printStackTrace();} 
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		if (DA.equals("001100110101010101010000")) {
			// Front Door Sensor
			String lastDateStrFrontDoorSensor = jedis.get("FrontDoorSensorTime");

			if (lastDateStrFrontDoorSensor != null) {
				org.joda.time.DateTime frontdoorsensordatetime = parser1.parseDateTime(lastDateStrFrontDoorSensor);
				secondsBetween = Math.abs(org.joda.time.Seconds.secondsBetween(currentdatetime, frontdoorsensordatetime).getSeconds());
			}
			if (secondsBetween > 1) {
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 10 -d 0");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 10 -d 1");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 10 -d 2");
				RunShell.Run("/Users/Ian/blink1-tool --red --blink 10 -d 3");

				try {
					pushoverClient
					.PushoverClientPost("Front Door has opened", "WongHome");
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				if ((hour > wakeup) && (hour < nightHour)) {
					if (WongHubAnnounce.equals("on"))
					{
						RunShell.Run("curl https://portal.theubi.com/webapi/behaviour?access_token=baca2002-f0b4-49f1-afda-356f4e34d3ee");
						RunShell.Run("/usr/bin/osascript /Users/Ian/Documents/scripts/AppleScripts/FrontDoorOpen.scpt");
					}
				}

				if (sparkClient.SparkClientGetLight("Front") < SPARKLIGHTTHRESH) {
					// if ((hour < wakeup) || (hour > lateHour))
					try {
						lifxClient.LifxClientPut("label:FrontDoor", "on", "1","white brightness:1.0");
						lifxClient.LifxClientPut("label:FrontDoor", "off", "300","white brightness:0.75");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
