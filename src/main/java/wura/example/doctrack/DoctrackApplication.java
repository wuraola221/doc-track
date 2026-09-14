package wura.example.doctrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DoctrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctrackApplication.class, args);
	}

}
