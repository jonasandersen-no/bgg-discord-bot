package no.jonasandersen.bgg.discord;

import java.io.IOException;
import nl.vv32.rcon.Rcon;
import no.jonasandersen.bgg.discord.BggProperties.RconProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RconService {

  private static final Logger log = LoggerFactory.getLogger(RconService.class);
  private final RconProperties rconProperties;

  public RconService(RconProperties rconProperties) {
    this.rconProperties = rconProperties;
  }

  public String runCommand(String payload) {
    try (Rcon rcon = Rcon.open(rconProperties.host(), rconProperties.port())) {
      rcon.authenticate(rconProperties.password());
      log.info("Sending payload: {}", payload);
      String response = rcon.sendCommand(payload);
      log.info("Got Response: {}", response);
      return response;
    } catch (IOException e) {
      log.warn("Something went wrong", e);
    }
    return null;
  }
}
