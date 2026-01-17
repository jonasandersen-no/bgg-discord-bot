package no.jonasandersen.bgg.discord;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "no.jonasandersen.bgg")
public record BggProperties(Discord discord, RconProperties rcon) {

  record Discord(String token) {}

  public record RconProperties(String host, @DefaultValue("25575") Integer port, String password) {}
}
