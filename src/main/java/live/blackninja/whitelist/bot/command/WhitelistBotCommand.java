package live.blackninja.whitelist.bot.command;

import live.blackninja.whitelist.WhitelistPlugin;
import live.blackninja.whitelist.bot.Bot;
import live.blackninja.whitelist.manager.Request;
import live.blackninja.whitelist.manager.RequestManager;
import live.blackninja.whitelist.manager.WhitelistManager;
import live.blackninja.whitelist.translation.BotTranslation;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.bukkit.OfflinePlayer;

import java.util.List;
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
        RequestManager requestManager = bot.getPlugin().getRequestManager();
        BotTranslation translation = bot.getTranslation();

        if (event.getGuild() == null) {
            event.replyEmbeds(translation.getEmbed("default.unknown_error", null)
                    .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                    .build()).queue();
            return;
        }

        Member member = event.getMember();

        if (member == null) {
            event.replyEmbeds(translation.getEmbed("default.unknown_error", null)
                    .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                    .build()).setEphemeral(true).queue();
            return;
        }

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(translation.getEmbed("command.no_permission", event.getUserLocale().toLocale())
                    .setAuthor(member.getEffectiveName(), null, member.getEffectiveAvatarUrl())
                    .build()).setEphemeral(true).queue();
            return;
        }

        String subCommand = event.getSubcommandName();

        if (subCommand == null) {
            event.replyEmbeds(translation.getEmbed("default.unknown_error", event.getUserLocale().toLocale())
                    .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                    .build()).setEphemeral(true).queue();
            return;
        }

        event.deferReply(true).queue(hook -> {
            switch (subCommand.toLowerCase()) {
                case "toggle" -> {
                    boolean state = Objects.requireNonNull(event.getOption("state")).getAsBoolean();

                    if (state) {
                        WhitelistPlugin plugin = bot.getPlugin();
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            boolean newState = true;
                            plugin.getWhitelistManager().toggleWhitelist(newState);
                        });
                        hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.enable", event.getUserLocale().toLocale())
                                .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                                .build()).queue();
                        return;
                    }
                    WhitelistPlugin plugin = bot.getPlugin();
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        boolean newState = false;
                        plugin.getWhitelistManager().toggleWhitelist(newState);
                    });
                    hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.disable", event.getUserLocale().toLocale())
                            .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                            .build()).queue();

                }
                case "add" -> {
                    String playerName = Objects.requireNonNull(event.getOption("player")).getAsString();

                    if (whitelistManager.existsPlayer(playerName)) {
                        hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.not-found", event.getUserLocale().toLocale(), playerName)
                                .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                                .build()).queue();
                        return;
                    }

                    WhitelistPlugin plugin = bot.getPlugin();
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        if (whitelistManager.whitelistPlayer(playerName)) {
                            if (requestManager.hasOpenRequest(playerName)) {
                                requestManager.removeRequest(playerName);
                            }
                            hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.add", event.getUserLocale().toLocale(), playerName)
                                    .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                                    .build()).queue();
                            return;
                        }
                        hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.already-whitelisted", event.getUserLocale().toLocale(), playerName)
                                .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                                .build()).queue();
                    });
                }
                case "remove" -> {
                    String playerName = Objects.requireNonNull(event.getOption("player")).getAsString();

                    WhitelistPlugin plugin = bot.getPlugin();
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        if (whitelistManager.unWhitelistPlayer(playerName)) {
                            hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.remove", event.getUserLocale().toLocale(), playerName)
                                    .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                                    .build()).queue();
                            return;
                        }
                        hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.not-whitelisted", event.getUserLocale().toLocale(), playerName)
                                .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                                .build()).queue();
                    });
                }
                case "list" -> {
                    List<OfflinePlayer> whitelist = whitelistManager.getWhitelist();

                    if (whitelist.isEmpty()) {
                        hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.list-empty", event.getUserLocale().toLocale())
                                .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                                .build()).queue();
                        return;
                    }

                    String playerList = String.join(", ", whitelist.stream().map(OfflinePlayer::getName).toList());

                    hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.list", event.getUserLocale().toLocale(), whitelist.size(), playerList)
                            .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                            .build()).queue();
                }
                case "requests" -> {
                    List<Request> requests = requestManager.getRequests();

                    if (requests.isEmpty()) {
                        hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.requests.empty", event.getUserLocale().toLocale())
                                .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                                .build()).queue();
                        return;
                    }

                    String requestsList = String.join(", ", requests.stream().map(Request::getPlayer).toList());

                    hook.editOriginalEmbeds(translation.getEmbed("command.whitelist.requests", event.getUserLocale().toLocale(), requests.size(), requestsList)
                            .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                            .build()).queue();
                }
                default -> hook.editOriginalEmbeds(translation.getEmbed("default.unknown_error", event.getUserLocale().toLocale())
                                .setTitle(translation.get("command.whitelist.title", event.getUserLocale().toLocale()))
                        .build()).queue();
            }
        });


    }
}
