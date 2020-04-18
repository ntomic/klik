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
import java.util.HashMap;

@Slf4j
@Component
public class Provider {
	
	static final String KONZUM_URL = "https://www.konzum.hr/web/raspolozivi-termini";
	static final String SLACK_API = "https://slack.com/api/chat.postMessage";
	
	@Value("${slack.authorization}")
	String slackAuthorization;
	
	@Value("${cookie.konzum}")
	String cookieKonzum;
	
	Boolean isDeliveryAvailable = Boolean.FALSE;
	Boolean isParsingError = Boolean.FALSE;
	
	void pingKonzum() throws IOException {
		
		var doc = Jsoup.connect(KONZUM_URL).cookie("cookie", cookieKonzum).get();
		var deliveryTitle = doc.select("[data-tab-type=delivery] h2").first();
		
		if (deliveryTitle != null) {
			isDeliveryAvailable = !deliveryTitle.text().equals("Trenutno nema dostupnih termina");
			log.info("Delivery availability: {}", isDeliveryAvailable);
		} else {
			isParsingError = Boolean.TRUE;
			log.warn("Authorization error: unable to parse Konzum site!");
		}
		
	}
	
	void sendToSlack() {
		
		var headers = new HttpHeaders() {{
			setContentType(MediaType.APPLICATION_JSON);
			setBearerAuth(slackAuthorization);
		}};
		
		var body = new HashMap<>() {{
			put("username", "konzum-bot");
			put("channel", "C010WQ59WNL");
			if (isDeliveryAvailable) {
				put("text", "\n konzum-klik delivery available: " + KONZUM_URL);
			}
			else if (isParsingError) {
				put("text", "\n konzum-klik delivery: authorization error!");
			}
		}};
		
		new RestTemplate().postForObject(URI.create(SLACK_API), new HttpEntity<>(body, headers), String.class);
		
	}
	
}