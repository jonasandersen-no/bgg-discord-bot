package no.jonasandersen.bgg.discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import no.jonasandersen.bgg.discord.BaseCommand;
import no.jonasandersen.bgg.discord.BggProperties;
import no.jonasandersen.bgg.discord.RconService;
import no.jonasandersen.bgg.discord.RemoteDockerManager;
import no.jonasandersen.bgg.discord.RemoteDockerManager.SSHException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServerCommand extends BaseCommand {

  private static final Logger log = LoggerFactory.getLogger(ServerCommand.class);
  private final RconService rconService;
  private final BggProperties bggProperties;

  public ServerCommand(RconService rconService, BggProperties bggProperties) {
    super("Used to start or stop the server.");

    OptionData option =
        new OptionData(
            OptionType.STRING,
            "command",
            "Choose START, STATUS or STOP depending on what you want to do",
            true);

    option.addChoice("START", "START");
    option.addChoice("STATUS", "STATUS");
    option.addChoice("STOP", "STOP");

    addOptions(option);
    this.rconService = rconService;
    this.bggProperties = bggProperties;
  }

  @Override
  public void onSlashCommand(SlashCommandInteractionEvent event) {
    String command = event.getOption("command").getAsString();

    event.deferReply().queue();

    log.info("Received command {}", command);

    switch (command) {
      case "START" -> {
        try {
          String response =
              RemoteDockerManager.executeSsh(
                  bggProperties.host().host(),
                  bggProperties.host().user(),
                  bggProperties.host().password(),
                  "cd testing-atm && docker compose up -d");
          log.info(response);
        } catch (SSHException e) {
          log.warn("Something went wrong", e);
          event
              .getHook()
              .sendMessage("Something went wrong. Message: " + e.getCause().getMessage())
              .queue();
          return;
        }
        event.getHook().sendMessage("Server started").queue();
      }
      case "STOP" -> {
        try {
          RemoteDockerManager.executeSsh(
              bggProperties.host().host(),
              bggProperties.host().user(),
              bggProperties.host().password(),
              "cd testing-atm && docker compose down");
        } catch (SSHException e) {
          log.warn("Something went wrong", e);
          event
              .getHook()
              .sendMessage("Something went wrong. Message: " + e.getCause().getMessage())
              .queue();
          return;
        }
        event.getHook().sendMessage("Server stopped").queue();
      }
      case "STATUS" -> {
        try {
          String response =
              RemoteDockerManager.executeSsh(
                  bggProperties.host().host(),
                  bggProperties.host().user(),
                  bggProperties.host().password(),
                  "docker ps");
          log.info(response);

          if (response.contains("testing-atm-mc-1")) {
            event.getHook().sendMessage("Server is up").queue();
          } else {
            event.getHook().sendMessage("Server is not up").queue();
          }

        } catch (SSHException e) {
          log.warn("Something went wrong", e);
          event
              .getHook()
              .sendMessage("Something went wrong. Message: " + e.getCause().getMessage())
              .queue();
          return;
        }
      }
      default -> {
        log.warn("Invalid option");
        event.getHook().sendMessage("Invalid option!").queue();
      }
    }
  }
}
