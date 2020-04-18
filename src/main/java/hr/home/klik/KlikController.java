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
	@Scheduled(cron = "${cron.minutes.ten}")
	void konzumKlik() throws IOException {
		
		provider.pingKonzum();
		provider.sendToSlack();
		
	}
}