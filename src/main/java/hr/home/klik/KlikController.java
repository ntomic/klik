package hr.home.klik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class KlikController {
	
	@Autowired
	Provider provider;

	@GetMapping(value="/klik")
	@Scheduled(cron = "${cron.job}")
	void konzumKlik() throws IOException {
		
		String parseMessage = provider.parseKonzumSite();
		if(parseMessage != null) {
			provider.sendNotificationToSlack(parseMessage);
		}
		
	}
}
