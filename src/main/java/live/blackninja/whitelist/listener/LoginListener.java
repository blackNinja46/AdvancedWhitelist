package live.blackninja.whitelist.listener;

import live.blackninja.whitelist.WhitelistPlugin;
import live.blackninja.whitelist.manager.Request;
import live.blackninja.whitelist.manager.RequestManager;
import live.blackninja.whitelist.manager.WhitelistManager;
import live.blackninja.whitelist.translation.PluginTranslation;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.List;

public record LoginListener(WhitelistPlugin plugin) implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String username = event.getName();
        WhitelistManager whitelistManager = plugin.getWhitelistManager();
        RequestManager requestManager = plugin.getRequestManager();
        PluginTranslation translation = plugin.getTranslation();

        if (!plugin.getWhitelistManager().isWhitelisted(username)) {
            requestManager.sendRequest(username);

            Request request = requestManager.getRequest(username);
            if (request == null) {
                Component kickMessage = whitelistManager.getPrefix()
                        .append(translation.getComponent("whitelist.kick-message"))
                        .append(this.getRequestMessage(null));
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMessage);
                return;
            }

            Component kickMessage = whitelistManager.getPrefix()
                    .append(translation.getComponent("whitelist.kick-message"))
                    .append(this.getRequestMessage(request));

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMessage);
        }

    }

    public Component getRequestMessage(Request request) {
        PluginTranslation translation = plugin.getTranslation();
        RequestManager requestManager = plugin.getRequestManager();

        List<? extends Player> admins = plugin.getServer().getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("whitelist.admin.notify"))
                .toList();
        if (admins.isEmpty()) {
            long expirationTime = requestManager.getExpirationDuration(request);
            return translation.getComponent("whitelist.request-no-admins-found", requestManager.getFormattedTime(expirationTime));
        }
        return translation.getComponent("whitelist.request-message");
    }


}
