package live.blackninja.whitelist.manager;

import live.blackninja.whitelist.WhitelistPlugin;
import live.blackninja.whitelist.api.UserAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WhitelistManager {

    public WhitelistPlugin plugin;

    public WhitelistManager(WhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    public void toggleWhitelist(boolean state) {
        this.plugin.getServer().setWhitelist(state);
    }

    public List<OfflinePlayer> getWhitelist() {
        return this.plugin.getServer().getWhitelistedPlayers().stream().toList();
    }

    public boolean whitelistPlayer(String player) {
        OfflinePlayer offlinePlayer = this.getPlayerByName(player);
        if (offlinePlayer.isWhitelisted()) {
            return false;
        }
        offlinePlayer.setWhitelisted(true);
        return true;
    }

    public boolean unWhitelistPlayer(String player) {
        OfflinePlayer offlinePlayer = this.getPlayerByName(player);
        if (!offlinePlayer.isWhitelisted()) {
            return false;
        }
        offlinePlayer.setWhitelisted(false);
        return true;
    }

    public boolean isWhitelisted(String player) {
        OfflinePlayer offlinePlayer = this.getPlayerByName(player);
        return this.getWhitelist().contains(offlinePlayer);
    }

    public boolean isWhitelist() {
        return this.plugin.getServer().hasWhitelist();
    }

    public boolean existsPlayer(String player) {
        try {
            getPlayerByName(player);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public final OfflinePlayer getPlayerByName(String name) {
        CompletableFuture<UUID> uuid = UserAPI.getUUIDByName(name);
        OfflinePlayer offlinePlayer = null;
        try {
            offlinePlayer = this.plugin.getServer().getOfflinePlayer(uuid.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return offlinePlayer;
    }

    public final Component getPrefix() {
        return Component.text("Whitelist", NamedTextColor.GOLD)
                .append(Component.text(" | ", NamedTextColor.DARK_GRAY));
    }
}
