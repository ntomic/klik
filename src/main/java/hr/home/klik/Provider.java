package hr.home.klik;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Slf4j
@Component
public class Provider {
	
	static final String KONZUM_URL = "https://www.konzum.hr/web/raspolozivi-termini";
	static final String SLACK_API = "https://slack.com/api/chat.postMessage";
	
	@Value("${slack.authorization}")
	String slackAuthorization;
	@Value("${slack.user}")
	String slackUser;
	@Value("${slack.channel}")
	String slackChannel;
	
	@Value("${cookie.konzum}")
	String cookieKonzum;
	
	
	String parseKonzumSite() throws IOException {
		
		String parsingMessage;
		
		var connection = Jsoup.connect(KONZUM_URL).cookie("cookie", cookieKonzum);
		var doc = connection.get();
		var deliveryTitle = doc.select("[data-tab-type=delivery] h2").first();
		
		if (deliveryTitle != null) {
			boolean isDeliveryAvailable = !deliveryTitle.text().equals("Trenutno nema dostupnih termina");
			parsingMessage      = isDeliveryAvailable == Boolean.TRUE ? "\n konzum-klik delivery available: " + KONZUM_URL : null;
		}
		else if (connection.response().url().getPath().equals("/web/sign_in")) {
			parsingMessage = "Authorization error: unable to parse Konzum site!";
		}
		else {
			parsingMessage = "Unknown error with the Konzum site!";
		}
		
		log.warn(parsingMessage);
		return parsingMessage;
	}
	
	void sendNotificationToSlack(String parseMessage) {
		
		var headers = new HttpHeaders() {{
			setContentType(MediaType.APPLICATION_JSON);
			setBearerAuth(slackAuthorization);
		}};
		
		var body = new HashMap<>() {{
			put("username", slackUser);
			put("channel", slackChannel);
			put("text", parseMessage);
		}};
		
		new RestTemplate().postForObject(URI.create(SLACK_API), new HttpEntity<>(body, headers), String.class);
		
	}
	
}
