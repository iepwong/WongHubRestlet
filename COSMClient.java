package WongHubRestlet;

import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

/**
 * Mail client retrieving a mail then storing it again on the same resource.
 */
public class COSMClient {

	public int COSMClientGetSecondsSinceLastUpdate(int feed, int datastream)
			throws Exception {
		ClientResource COSMClient = new ClientResource(
				"http://api.cosm.com/v2/feeds/"
						+ feed
						+ "/datastreams/"
						+ datastream
						+ ".json?timezone=Melbourne&key=L5OWO0Ow-cmUfuXoAbS4p1cSTWz1o6KWsBQBgXf3oh0");

		Representation reply = COSMClient.get(MediaType.APPLICATION_JSON);
		String replyText = reply.getText();
		JSONObject JsonReply = new JSONObject(replyText);
		String at = (String) JsonReply.get("at");
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat
				.dateTime();
		org.joda.time.DateTime datetime = parser1.parseDateTime(at);
		org.joda.time.DateTime newat = new org.joda.time.DateTime(
				datetime.getYear(), datetime.getMonthOfYear(),
				datetime.getDayOfMonth(), datetime.getHourOfDay(),
				datetime.getMinuteOfHour());
		newat = convertJodaTimezone(newat, "Australia/Melbourne","UTC");
		String formattedDate = parser1.print(newat);

		COSMClient = new ClientResource(
				"http://api.cosm.com/v2/feeds/"
						+ feed
						+ "/datastreams/"
						+ datastream
						+ ".json?key=L5OWO0Ow-cmUfuXoAbS4p1cSTWz1o6KWsBQBgXf3oh0&start="
						+ formattedDate + "&interval=0&duration=1minute");
		reply = COSMClient.get(MediaType.APPLICATION_JSON);
		replyText = reply.getText();
		// System.out.println(replyText);
		JsonReply = new JSONObject(replyText);
		at = (String) JsonReply.get("at");

		parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		datetime = new org.joda.time.DateTime();
		int secondselapsed = org.joda.time.Seconds.secondsBetween(
				parser1.parseDateTime(at), datetime).getSeconds();

		return secondselapsed;
	}

	public static org.joda.time.DateTime convertJodaTimezone(org.joda.time.DateTime date, String srcTz, String destTz) {
	  org.joda.time.DateTime srcDateTime = date.toDateTime(org.joda.time.DateTimeZone.forID(srcTz));
	  org.joda.time.DateTime dstDateTime = srcDateTime.withZone(org.joda.time.DateTimeZone.forID(destTz));
	  return dstDateTime.toLocalDateTime().toDateTime();
	}
	
	public int COSMClientGetLastUpdate(int feed, int datastream, float timereset)
			throws Exception {

		ClientResource COSMClient = new ClientResource(
				"http://api.cosm.com/v2/feeds/"
						+ feed
						+ "/datastreams/"
						+ datastream
						+ ".json?timezone=Melbourne&key=L5OWO0Ow-cmUfuXoAbS4p1cSTWz1o6KWsBQBgXf3oh0");
		int maxVal = 0;
		Representation reply = COSMClient.get(MediaType.APPLICATION_JSON);
		String replyText = reply.getText();
		JSONObject JsonReply = new JSONObject(replyText);
		String at = (String) JsonReply.get("at");
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		org.joda.time.DateTime datetime = parser1.parseDateTime(at);
		org.joda.time.DateTime newat = new org.joda.time.DateTime(
				datetime.getYear(), datetime.getMonthOfYear(),
				datetime.getDayOfMonth(), datetime.getHourOfDay(),
				datetime.getMinuteOfHour());
		newat = convertJodaTimezone(newat, "Australia/Melbourne","UTC");
		String formattedDate = parser1.print(newat);

		COSMClient = new ClientResource(
				"http://api.cosm.com/v2/feeds/"
						+ feed
						+ "/datastreams/"
						+ datastream
						+ ".json?key=L5OWO0Ow-cmUfuXoAbS4p1cSTWz1o6KWsBQBgXf3oh0&start="
						+ formattedDate + "&interval=0&duration=1minute");

		
		reply = COSMClient.get(MediaType.APPLICATION_JSON);
		replyText = reply.getText();
		// System.out.println(replyText);
		JsonReply = new JSONObject(replyText);
		JSONArray jsonArray = (JSONArray) JsonReply.get("datapoints");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject childJSONObject = jsonArray.getJSONObject(i);
			String current_value_str = childJSONObject.getString("value");
			Float current_value_flt = Float.valueOf(current_value_str);
			int current_value = current_value_flt.intValue();
			if (current_value > maxVal)
				maxVal = current_value;
		}

		at = (String) JsonReply.get("at");

		parser1 = org.joda.time.format.ISODateTimeFormat.dateTime();
		datetime = new org.joda.time.DateTime();
		int minuteselapsed = org.joda.time.Minutes.minutesBetween(
				parser1.parseDateTime(at), datetime).getMinutes();
		if (minuteselapsed < timereset) {
			maxVal = maxVal + 1;
		}
		else {
			maxVal = 1;
		}
		return maxVal;
	}

	public void COSMClientPost(int feed, int datastream, Date inDate, String value)
			throws Exception {
		org.joda.time.format.DateTimeFormatter parser1 = org.joda.time.format.ISODateTimeFormat
				.dateTime();
		org.joda.time.DateTime datetime1 = new org.joda.time.DateTime(inDate);
		String DateTime1 = parser1.print(datetime1);

		String JSONString = "{\"datapoints\" :[{\"at\" : \"" + DateTime1
				+ "\",\"value\" : \"" + value + "\"}]}";
		// System.out.println(JSONString);
		JSONObject COSMElt = new JSONObject(JSONString.trim());

		// System.out.println("To COSM ->"+feed+" +"+datastream+" "+value);

		ClientResource COSMClient = new ClientResource(
				"http://api.cosm.com/v2/feeds/" + feed + "/datastreams/" + datastream
						+ "/datapoints?key=L5OWO0Ow-cmUfuXoAbS4p1cSTWz1o6KWsBQBgXf3oh0");
		COSMClient.setRequestEntityBuffering(true);

		Representation rep = new JsonRepresentation(COSMElt);
		rep.setMediaType(MediaType.APPLICATION_JSON);
		COSMClient.post(rep);
	}

	public void COSMClientPostImpulse(int feed, int datastream, Date inDate,
			String value) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(inDate);
		cal.add(Calendar.SECOND, -2);
		Date date1 = cal.getTime();
		cal.setTime(inDate);
		cal.add(Calendar.SECOND, -1);
		Date date2 = cal.getTime();
		cal.setTime(inDate);
		cal.add(Calendar.SECOND, 1);
		Date date3 = cal.getTime();
		cal.setTime(inDate);
		cal.add(Calendar.SECOND, 2);
		Date date4 = cal.getTime();
		try {
			COSMClientPost(feed, datastream, date1, "0");
			COSMClientPost(feed, datastream, date2, value);
			COSMClientPost(feed, datastream, inDate, value);
			COSMClientPost(feed, datastream, date3, value);
			COSMClientPost(feed, datastream, date4, "0");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
