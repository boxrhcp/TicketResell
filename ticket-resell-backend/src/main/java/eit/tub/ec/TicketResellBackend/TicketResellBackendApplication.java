package eit.tub.ec.TicketResellBackend;

import eit.tub.ec.TicketResellBackend.Home.Home;
import eit.tub.ec.TicketResellBackend.Home.HomeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class TicketResellBackendApplication {

	private static final Logger log = LoggerFactory.getLogger(TicketResellBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TicketResellBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(HomeRepository repository) {
		return (args) -> {
			// save a few customers
			repository.save(new Home("Jack", "Bauer"));
			repository.save(new Home("Chloe", "O'Brian"));
			repository.save(new Home("Kim", "Bauer"));
			repository.save(new Home("David", "Palmer"));
			repository.save(new Home("Michelle", "Dessler"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Home home : repository.findAll()) {
				log.info(home.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			Optional<Home> home = repository.findById(1L);
			log.info("Customer found with findById(1L):");
			log.info("--------------------------------");
			log.info(home.get().toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			repository.findByLastName("Bauer").forEach(bauer -> {
				log.info(bauer.toString());
			});
			// for (Customer bauer : repository.findByLastName("Bauer")) {
			//  log.info(bauer.toString());
			// }
			log.info("");
		};
	}
}
