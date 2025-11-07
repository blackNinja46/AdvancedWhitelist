package live.blackninja.whitelist;

import live.blackninja.whitelist.bot.Bot;
import live.blackninja.whitelist.listener.LoginListener;
import live.blackninja.whitelist.manager.RequestManager;
import live.blackninja.whitelist.manager.WhitelistManager;
import live.blackninja.whitelist.translation.PluginTranslation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import live.blackninja.whitelist.command.*;

public final class WhitelistPlugin extends JavaPlugin {

    public WhitelistManager whitelistManager;
    public RequestManager requestManager;
    private PluginTranslation translation;
    private Bot bot;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        this.translation = new PluginTranslation();
        this.whitelistManager = new WhitelistManager(this);
        this.requestManager = new RequestManager(this);

        if (getConfig().getBoolean("Discord-Bot.Enabled")) {
            if (getConfig().getString("Discord-Bot.Token") == null) {
                getLogger().warning("Discord Bot Token not found in config.yml");
                return;
            }
            this.bot = new Bot(this, getConfig().getString("Discord-Bot.Token"));
        }

        this.requestManager.loadRequests();

        registerCommands();
        registerEvents(Bukkit.getPluginManager());
    }

    public void registerCommands() {
        this.getCommand("whitelist").setExecutor(new WhitelistCommand(this));
    }

    public void registerEvents(PluginManager pluginManager) {
        pluginManager.registerEvents(new LoginListener(this), this);
    }

    @Override
    public void onDisable() {
        if (bot != null) {
            try {
                bot.shutdown();
            } catch (Exception e) {
                getLogger().warning("Error shutting down the Discord bot: " + e.getMessage());
            }
        }

        // Dann die Requests speichern
        if (requestManager != null) {
            this.requestManager.saveRequests();
        }
    }

    public PluginTranslation getTranslation() {
        return translation;
    }

    public WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }
}
