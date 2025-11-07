package live.blackninja.whitelist.bot.listener;

import live.blackninja.whitelist.bot.Bot;
import live.blackninja.whitelist.bot.command.WhitelistBotCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {

    protected final Bot bot;

    public SlashCommandListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "whitelist":
                new WhitelistBotCommand(event, this.bot);
                break;
        }
    }
}
