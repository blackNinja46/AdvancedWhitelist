package live.blackninja.whitelist.bot.command;

import live.blackninja.whitelist.WhitelistPlugin;
import live.blackninja.whitelist.bot.Bot;
import live.blackninja.whitelist.bot.listener.SlashCommandListener;
import live.blackninja.whitelist.manager.WhitelistManager;
import live.blackninja.whitelist.translation.BotTranslation;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Objects;

public class ToggleWhitelistCommand implements SlashCommand {

    protected final SlashCommandInteraction event;
    protected final Bot bot;

    public ToggleWhitelistCommand(SlashCommandInteraction event, Bot bot) {
        this.event = event;
        this.bot = bot;

        execute();
    }

    @Override
    public void execute() {
        WhitelistManager whitelistManager = bot.getPlugin().getWhitelistManager();
        BotTranslation translation = bot.getTranslation();

        boolean state = Objects.requireNonNull(event.getOption("state")).getAsBoolean();

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.replyEmbeds(translation.getEmbed("command.no_permission", event.getUserLocale()))
            return;
        }


        whitelistManager.toggleWhitelist(state);


    }
}
