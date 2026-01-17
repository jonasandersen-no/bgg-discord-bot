package no.jonasandersen.bgg.discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import no.jonasandersen.bgg.discord.BaseCommand;
import no.jonasandersen.bgg.discord.RconService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MinecraftCommand extends BaseCommand {

  public static final String COMMAND = "command";
  private final RconService rconService;

  public MinecraftCommand(RconService rconService) {
    super("Run commands on BGG Minecraft server");
    addOptions(
        new OptionData(
            OptionType.STRING,
            COMMAND,
            "Command to run on the server. Supports most of ingame commands",
            true));
    this.rconService = rconService;
  }

  @Override
  public void onSlashCommand(SlashCommandInteractionEvent event) {
    String command = event.getOption(COMMAND).getAsString();

    String response = rconService.runCommand(command);

    if (!StringUtils.hasText(response)) {
      response = "No Response on this command";
    }
    event.reply(response).queue();
  }
}
