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

	static String OAuthConsumerKey = "Wb1v47mK7EDNcYqVpLKwjYzvS";
	static String OAuthConsumerSecret = "wh07wOrZHS9yXp4GFImsMmMW7IRXC9kNp3JsXBcfcQi3xaZWob";
	static String AccessToken = "2701982725-qyR7Bu7SSgLKF7ARyoSYyVAFDvrrrS6igEiaYlD";
	static String AccessTokenSecret = "pm8GfnrpaqpO80kwuxQnXpR8qEA5KcaUzsNIDrbn26KIo";

	static Twitter twitter = new TwitterFactory().getSingleton();

	public void postWhereisiepwongMsg(String statusMsg) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Date()
		Date date = new Date();
		postMsg(statusMsg + ": " + dateFormat.format(date));

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
			System.out.println("Twitter Status String -> "+status_str);
		}
		catch (TwitterException e) {
		}
	}

}
