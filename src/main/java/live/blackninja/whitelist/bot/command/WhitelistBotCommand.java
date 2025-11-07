package live.blackninja.whitelist.bot.command;

import live.blackninja.whitelist.WhitelistPlugin;
import live.blackninja.whitelist.bot.Bot;
import live.blackninja.whitelist.manager.WhitelistManager;
import live.blackninja.whitelist.translation.BotTranslation;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class WhitelistBotCommand implements SlashCommand {

    protected final SlashCommandInteraction event;
    protected final Bot bot;

    public WhitelistBotCommand(SlashCommandInteraction event, Bot bot) {
        this.event = event;
        this.bot = bot;

        execute();
    }

    @Override
    public void execute() {
        WhitelistManager whitelistManager = bot.getPlugin().getWhitelistManager();
        BotTranslation translation = bot.getTranslation();


        User user = event.getUser();

        if (event.getGuild() == null) {
            event.replyEmbeds(translation.getEmbed("default.unknown_error", null)
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .build()).queue();
            return;
        }

        Member member = event.getMember();

        if (member == null) {
            event.replyEmbeds(translation.getEmbed("default.unknown_error", null)
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .build()).setEphemeral(true).queue();
            return;
        }

        Guild guild = member.getGuild();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            event.replyEmbeds(translation.getEmbed("command.no_permission", event.getUserLocale().toLocale())
                    .setAuthor(member.getEffectiveName(), null, member.getEffectiveAvatarUrl())
                    .build()).setEphemeral(true).queue();
            return;
        }

        String subCommand = event.getSubcommandName();

        if (subCommand == null) {
            event.replyEmbeds(translation.getEmbed("default.unknown_error", event.getUserLocale().toLocale())
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .build()).setEphemeral(true).queue();
            return;
        }

        event.deferReply(true).queue(hook -> {
            switch (subCommand.toLowerCase()) {
                case "toggle" -> {
                    boolean state = event.getOption("state").getAsBoolean();

                    if (state) {
                        WhitelistPlugin plugin = bot.getPlugin();
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            boolean newState = true;
                            plugin.getWhitelistManager().toggleWhitelist(newState);
                        });
                        hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.enable.description", event.getUserLocale().toLocale())
                                .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                                .build()).queue();
                        return;
                    }
                    WhitelistPlugin plugin = bot.getPlugin();
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        boolean newState = false;
                        plugin.getWhitelistManager().toggleWhitelist(newState);
                    });
                    hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.disable.description", event.getUserLocale().toLocale())
                            .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                            .build()).queue();

                }
                default -> hook.editOriginalEmbeds(translation.getEmbed("default.unknown_error", event.getUserLocale().toLocale())
                        .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                        .build()).queue();
            }
        });


    }
}
