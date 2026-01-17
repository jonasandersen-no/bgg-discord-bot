package no.jonasandersen.bgg.discord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BggDiscordBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(BggDiscordBotApplication.class, args);
  }
}
