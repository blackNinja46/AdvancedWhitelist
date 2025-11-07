package live.blackninja.whitelist.manager;

import live.blackninja.whitelist.WhitelistPlugin;
import live.blackninja.whitelist.config.RequestsConfig;
import live.blackninja.whitelist.translation.PluginTranslation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RequestManager {

    protected final WhitelistPlugin plugin;
    protected final List<Request> requests;
    protected final long expirationTime;

    private RequestsConfig config;

    public RequestManager(WhitelistPlugin plugin) {
        this.plugin = plugin;

        this.config = new RequestsConfig(plugin);

        this.requests = new ArrayList<>();
        this.expirationTime = (long) this.plugin.getConfig().getInt("RequestsExpiration") * 1000 * 60;

        runExpiration();
    }

    public void saveRequests() {
        this.config.getConfig().set("Requests", null);
        for (Request request : this.requests) {
            this.config.getConfig().set("Requests." + request.getPlayer() + ".Expiration", request.getRequestTime());
        }
        this.config.save();
        System.out.println("Saved " + requests.size() + " requests");
    }

    public void loadRequests() {
        ConfigurationSection section = config.getConfig().getConfigurationSection("Requests");

        if (section == null|| !config.getConfig().contains("Requests")) {
            config.getConfig().createSection("Requests");
            return;
        }

        for (String key : section.getKeys(false)) {
            long requestTime = this.config.getConfig().getLong("Requests." + key + ".Expiration");
            this.requests.add(new Request(key, requestTime));
        }

        System.out.println("Loaded " + requests.size() + " requests");
    }

    public void sendRequest(String player) {
        PluginTranslation translation = plugin.getTranslation();

        if (!hasOpenRequest(player) && this.getOnlineAdmins().isEmpty()) {
            requests.add(new Request(player, System.currentTimeMillis()));
            System.out.println("Added " + player + " to the request list");
            return;
        }

        if (!this.getOnlineAdmins().isEmpty()){
            requests.remove(getRequest(player));
            for (Player admin : getOnlineAdmins()) {
                admin.sendMessage(plugin.getWhitelistManager().getPrefix().append(translation.getComponent("broadcast.whitelist.request", player, player)));
            }
        }
    }

    public Request getRequest(String player) {
        return requests.stream()
                .filter(request -> request.getPlayer().equalsIgnoreCase(player))
                .findFirst()
                .orElse(null);
    }

    public void removeExpiringRequests() {
        List<Request> expiredRequests = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        for (Request request : requests) {
            if (currentTime - request.getRequestTime() >= expirationTime) {
                expiredRequests.add(request);
            }
        }
        requests.removeAll(expiredRequests);
    }

    public void runExpiration() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::removeExpiringRequests, 20L, 20L);
    }

    public void removeRequest(String player) {
        Request request = getRequest(player);
        if (request != null) {
            requests.remove(request);
        }
    }

    public boolean hasOpenRequest(String player) {
        return requests.contains(getRequest(player));
    }

    public List<Player> getOnlineAdmins() {
        List<Player> admins = new ArrayList<>();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.hasPermission("advancedwhitelist.request.notify")) {
                admins.add(player);
            }
        }
        return admins;
    }

    public long getExpirationDuration(Request request) {
        long elapsedTime = System.currentTimeMillis() - request.getRequestTime();
        return expirationTime - elapsedTime;
    }

    public String getFormattedTime(long totalSeconds) {
        long seconds = totalSeconds / 1000;

        long weeks = seconds / (7L * 24 * 60 * 60);
        seconds %= (7L * 24 * 60 * 60);

        long days = seconds / (24 * 60 * 60);
        seconds %= (24 * 60 * 60);

        long hours = seconds / (60 * 60);
        seconds %= (60 * 60);

        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder sb = new StringBuilder();
        if (weeks > 0)   sb.append(weeks).append("w ");
        if (days > 0)    sb.append(days).append("d ");
        if (hours > 0)   sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        sb.append(seconds).append("s");

        return sb.toString().trim();
    }

    public List<Request> getRequests() {
        return requests;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
