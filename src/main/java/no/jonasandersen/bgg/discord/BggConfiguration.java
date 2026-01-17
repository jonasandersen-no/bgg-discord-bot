package no.jonasandersen.bgg.discord;

import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
public class BggConfiguration {

  @Bean(destroyMethod = "shutdown")
  @Profile("!junit")
  JDA jda(BggProperties properties, List<EventListener> listeners) throws InterruptedException {
    JDA jda =
        JDABuilder.createDefault(
                properties.discord().token(),
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MESSAGES)
            .setActivity(Activity.playing("Yuuuuuuuuup"))
            .addEventListeners(listeners.toArray())
            .build();
    jda.awaitReady();
    return jda;
  }

  @Bean
  RconService rconService(BggProperties properties) {
    return new RconService(properties.rcon());
  }
}
