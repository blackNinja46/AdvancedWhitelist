package live.blackninja.whitelist.bot;

import live.blackninja.whitelist.WhitelistPlugin;
import live.blackninja.whitelist.bot.listener.SlashCommandListener;
import live.blackninja.whitelist.translation.BotTranslation;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.requests.GatewayIntent.ALL_INTENTS;

public class Bot {

    protected final WhitelistPlugin plugin;

    protected final String token;
    protected final JDA bot;

    protected final BotTranslation translation;

    public Bot(WhitelistPlugin plugin, String token) {
        this.plugin = plugin;
        this.token = token;

        translation = new BotTranslation();

        bot = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setStatus(OnlineStatus.ONLINE)
                .setAutoReconnect(false)
                .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                .addEventListeners(new SlashCommandListener(this))
                .build();

        registerCommands();

        this.plugin.getServer().sendMessage(this.plugin.getWhitelistManager().getPrefix().append(this.plugin.getTranslation().getComponent("bot.start")));
    }

    public void registerCommands() {
        CommandListUpdateAction commands = bot.updateCommands();

        commands.addCommands(
                Commands.slash("whitelist", translation.get("common.command.whitelist.description"))
                        .addSubcommands(new SubcommandData("toggle", translation.get("common.command.whitelist.toggle.description"))
                                .addOption(OptionType.BOOLEAN, "state", translation.get("common.command.whitelist.toggle.state.description"), true))
                        .addSubcommands(new SubcommandData("add", translation.get("common.command.whitelist.add.description"))
                                .addOption(OptionType.STRING, "player", translation.get("common.command.whitelist.add.player.description"), true))
                        .addSubcommands(new SubcommandData("remove", translation.get("common.command.whitelist.remove.description"))
                                .addOption(OptionType.STRING, "player", translation.get("common.command.whitelist.remove.player.description"), true))
                        .addSubcommands(new SubcommandData("list", translation.get("common.command.whitelist.list.description")))
                        .addSubcommands(new SubcommandData("requests", translation.get("common.command.whitelist.requests.description")))

        );

        commands.queue();
    }

    public void shutdown() {
        if (bot != null) {
            bot.shutdown();

            try {
                if (!bot.awaitShutdown(10, TimeUnit.SECONDS)) {
                    bot.shutdownNow();
                    bot.awaitShutdown();
                }
            } catch (InterruptedException e) {
                bot.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        this.plugin.getServer().sendMessage(this.plugin.getWhitelistManager().getPrefix().append(this.plugin.getTranslation().getComponent("bot.shutdown")));
    }

    public JDA getBot() {
        return bot;
    }

    public WhitelistPlugin getPlugin() {
        return plugin;
    }

    public BotTranslation getTranslation() {
        return translation;
    }
}
