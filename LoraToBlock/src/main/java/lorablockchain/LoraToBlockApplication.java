package lorablockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class LoraToBlockApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoraToBlockApplication.class, args);
	}

}
