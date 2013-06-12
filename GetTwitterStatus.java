package WongHubRestlet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

public class GetTwitterStatus {

	static String OAuthConsumerKey = "FIYlQbXyFDQA3A5af7cTw";
	static String OAuthConsumerSecret = "GMarxGlm9Ty8qQ6pi5A6Js6yZnqwlWj4ZDyz7uY68";
	static String AccessToken = "453669960-uXhcshsjlgK4ge7RGViz3R5qdx9IrFxxl4gHidIw";
	static String AccessTokenSecret = "2zWIWhfU6TKkiAHRqmJgKcA3iX9VXnFNsOTrum8";

	static Twitter twitter = new TwitterFactory().getInstance();

	public static void postAmeliaSleepMsg(String statusMsg) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Date()
		Date date = new Date();

		loginTwitter();
		postMsg(statusMsg + ": " + dateFormat.format(date));

	}

	public static void getAmeliaSleepTimeline() {
		loginTwitter();
		try {
			User user = twitter.verifyCredentials();
			List<Status> statuses = twitter.getUserTimeline();
			System.out.println("Showing @" + user.getScreenName()
					+ "'s home timeline.");
			for (Status status : statuses) {
				System.out.println("@" + status.getUser().getScreenName() + " - "
						+ status.getText());
			}
		}
		catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}

	static void loginTwitter() {
		twitter.setOAuthConsumer(OAuthConsumerKey, OAuthConsumerSecret);
		AccessToken accessToken = loadAccessToken();
		twitter.setOAuthAccessToken(accessToken);
	}

	private static AccessToken loadAccessToken() {
		return new AccessToken(AccessToken, AccessTokenSecret);
	}

	static void postMsg(String s) {
		try {
			Status status = twitter.updateStatus(s);
			String status_str = status.getText();
		}
		catch (TwitterException e) {
		}
	}

}
