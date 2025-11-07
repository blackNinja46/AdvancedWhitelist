package live.blackninja.whitelist.translation;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import javax.annotation.Nullable;

public class Translation {

    private final String resourceBundlePrefix;
    private final ClassLoader loader;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public Translation(String resourceBundlePrefix) {
        this.resourceBundlePrefix = resourceBundlePrefix;
        this.loader = getClass().getClassLoader();
    }

    public Translation(String resourceBundlePrefix, ClassLoader loader) {
        this.resourceBundlePrefix = resourceBundlePrefix;
        this.loader = loader;
    }

    /**
     * Gibt den Text als String zurück (ohne MiniMessage-Parsing)
     */
    public String get(String key, Object... format) {
        return get(key, Locale.GERMAN, format);
    }

    public String get(String key, Locale locale, Object... format) {
        try {
            ResourceBundle bundle = getResourceBundle(locale);

            if (!bundle.containsKey(key)) {
                bundle = getDefaultResourceBundle();
                if (!bundle.containsKey(key)) {
                    return key;
                }
            }

            String text = bundle.getString(key);
            if (format.length > 0) {
                text = MessageFormat.format(text, format);
            }

            return text;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    @Nullable
    public String getNull(String key, Object... format) {
        return getNull(key, Locale.ENGLISH, format);
    }

    @Nullable
    public String getNull(String key, Locale locale, Object... format) {
        try {
            ResourceBundle bundle = getResourceBundle(locale);

            if (!bundle.containsKey(key)) {
                bundle = getDefaultResourceBundle();

                if (!bundle.containsKey(key)) {
                    return null;
                }
            }

            if (format.length == 0) {
                return bundle.getString(key);
            }

            return MessageFormat.format(bundle.getString(key), format);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public Component getComponent(String key, Object... format) {
        String text = get(key, format);
        return mm.deserialize(text);
    }

    private ResourceBundle getResourceBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle(resourceBundlePrefix, locale, loader);
        } catch (MissingResourceException e) {
            return getDefaultResourceBundle();
        }
    }

    private ResourceBundle getDefaultResourceBundle() {
        return ResourceBundle.getBundle(resourceBundlePrefix, Locale.GERMAN, loader);
    }
}
