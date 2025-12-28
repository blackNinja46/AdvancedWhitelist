package live.blackninja.whitelist.translation;

import live.blackninja.whitelist.WhitelistPlugin;

public class PluginTranslation extends Translation {

    public PluginTranslation(WhitelistPlugin plugin) {
        super("plugin", Translation.class.getClassLoader(), plugin);
    }
}
