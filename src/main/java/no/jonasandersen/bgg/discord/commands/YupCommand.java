package no.jonasandersen.bgg.discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import no.jonasandersen.bgg.discord.BaseCommand;
import org.springframework.stereotype.Component;

@Component
public class YupCommand extends BaseCommand {

  public YupCommand() {
    super("Yup Command");
  }

  @Override
  public void onSlashCommand(SlashCommandInteractionEvent event) {
    event.reply("Yuuuuuuup").queue();
  }
}
