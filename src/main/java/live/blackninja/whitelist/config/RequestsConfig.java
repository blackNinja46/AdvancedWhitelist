package live.blackninja.whitelist.config;

import live.blackninja.whitelist.WhitelistPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RequestsConfig {

    private WhitelistPlugin plugin;

    private final File file;
    private final YamlConfiguration config;

    public RequestsConfig(WhitelistPlugin plugin) {
        this.plugin = plugin;
        File dir = new File("./plugins/AdvancedWhitelist/");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        this.file = new File(dir, "requests.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        try {
            this.config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
