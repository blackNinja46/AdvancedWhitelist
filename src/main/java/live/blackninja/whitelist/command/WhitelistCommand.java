package live.blackninja.whitelist.command;

import live.blackninja.whitelist.WhitelistPlugin;
import live.blackninja.whitelist.manager.Request;
import live.blackninja.whitelist.manager.RequestManager;
import live.blackninja.whitelist.manager.WhitelistManager;
import live.blackninja.whitelist.translation.PluginTranslation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record WhitelistCommand(WhitelistPlugin plugin) implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        WhitelistManager whitelistManager = plugin.getWhitelistManager();
        RequestManager requestManager = plugin.getRequestManager();
        PluginTranslation translation = plugin.getTranslation();

        if (!this.hasPermission(sender)) {
            sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("global.no-permission")));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.usage")));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on" -> {
                if (!sender.hasPermission("advancedwhitelist.toggle")) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("global.no-permission")));
                    return true;
                }
                whitelistManager.toggleWhitelist(true);
                sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.on")));
            }
            case "off" -> {
                if (!sender.hasPermission("advancedwhitelist.toggle")) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("global.no-permission")));
                    return true;
                }
                whitelistManager.toggleWhitelist(false);
                sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.off")));
            }
            case "toggle" -> {
                if (!sender.hasPermission("advancedwhitelist.toggle")) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("global.no-permission")));
                    return true;
                }
                if (whitelistManager.isWhitelist()) {
                    whitelistManager.toggleWhitelist(false);
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.off")));
                } else {
                    whitelistManager.toggleWhitelist(true);
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.on")));
                }
            }
            case "add" -> {
                if (!sender.hasPermission("advancedwhitelist.player")) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("global.no-permission")));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.usage")));
                    return true;
                }
                String playerName = args[1];

                if (whitelistManager.existsPlayer(playerName)) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.player-not-found", playerName)));
                    return true;
                }

                if (whitelistManager.whitelistPlayer(playerName)) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.player-added", playerName)));
                    if (requestManager.hasOpenRequest(playerName)) {
                        requestManager.removeRequest(playerName);
                    }
                } else {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.already-whitelisted", playerName)));
                }
            }
            case "remove" -> {
                if (!sender.hasPermission("advancedwhitelist.player")) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("global.no-permission")));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.usage")));
                    return true;
                }
                String playerName = args[1];
                if (whitelistManager.unWhitelistPlayer(playerName)) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.player-removed", playerName)));
                } else {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.not-whitelisted", playerName)));
                }
            }
            case "set" -> {
                if (!sender.hasPermission("advancedwhitelist.player")) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("global.no-permission")));
                    return true;
                }
                this.plugin.getServer().getWhitelistedPlayers().clear();

                Bukkit.getOnlinePlayers().forEach(player -> whitelistManager.whitelistPlayer(player.getName()));

                sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.set")));
            }
            case "list" -> {
                if (!sender.hasPermission("advancedwhitelist.list")) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("global.no-permission")));
                    return true;
                }
                if (whitelistManager.getWhitelist().isEmpty()) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.list.empty")));
                    return true;
                }
                sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.list.header")));
                for (OfflinePlayer player : whitelistManager.getWhitelist()) {
                    if (player == null) {
                        continue;
                    }
                    String playerName = whitelistManager.getPlayerName(player);
                    sender.sendMessage(translation.getComponent("command.whitelist.list.player", playerName));
                }
            }
            case "requests" -> {
                if (!sender.hasPermission("advancedwhitelist.requests")) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("global.no-permission")));
                    return true;
                }
                if (requestManager.getRequests().isEmpty()) {
                    sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.request-list.empty")));
                    return true;
                }
                sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.request-list.header")));
                for (Request request : requestManager.getRequests()) {
                    String formattedTime = requestManager.getFormattedTime(requestManager.getExpirationDuration(request));
                    sender.sendMessage(translation.getComponent("command.whitelist.request-list.entry", request.getPlayer(), formattedTime, request.getPlayer()));
                }
            }
            default -> {
                sender.sendMessage(whitelistManager.getPrefix().append(translation.getComponent("command.whitelist.usage")));
            }
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NonNull [] args) {
        List<String> arg0 = Stream.of("add", "remove", "list", "off", "on", "toggle", "set", "requests").sorted().toList();

        switch (args.length) {
            case 0 -> {
                return arg0;
            }
            case 1 -> {
                return arg0.stream().filter(s -> s.startsWith(args[0].toLowerCase())).toList();
            }
            case 2 -> {
                if (!args[0].equalsIgnoreCase("remove")) {
                    return Collections.emptyList();
                }

                List<String> playerList = new ArrayList<>();

                for (OfflinePlayer player : this.plugin.getWhitelistManager().getWhitelist()) {
                    if (player == null) {
                        continue;
                    }
                    String playerName = this.plugin.getWhitelistManager().getPlayerName(player);
                    if (playerName != null) {
                        playerList.add(playerName);
                    }
                }

                return playerList;
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    private boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("advancedwhitelist.admin") ||
                sender.hasPermission("advancedwhitelist.player") ||
                sender.hasPermission("advancedwhitelist.list") ||
                sender.hasPermission("advancedwhitelist.requests") ||
                sender.hasPermission("advancedwhitelist.toogle");
    }
}
