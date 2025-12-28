package live.blackninja.whitelist.translation;

import live.blackninja.whitelist.WhitelistPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class BotTranslation extends Translation {

    public BotTranslation(WhitelistPlugin plugin) {
        super("bot", BotTranslation.class.getClassLoader(), plugin);
    }

    @NotNull
    public EmbedBuilder getEmbed(@NotNull String key, @Nullable Locale locale, @Nullable String authorUrl, @Nullable String authorIconUrl,
                                 @Nullable String titleUrl, @Nullable String footerIcon, int fields, Object... format) {

        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        String author = get(key + ".author", locale, format);
        String title = get(key + ".title", locale, format);
        String description = get(key + ".description", locale, format);
        String footer = get(key + ".footer", locale, format);

        List<MessageEmbed.Field> list = new ArrayList<>();

        for (int i = 0; i < fields; i++) {
            String fieldName = get(key + ".field" + i + ".name", locale, format);
            String fieldDescription = get(key + ".field" + i + ".description", locale, format);

            if (fieldName == null || fieldDescription == null) {
                continue;
            }

            list.add(new MessageEmbed.Field("", "", false));
        }

        EmbedBuilder embed = new EmbedBuilder();

        if (title != null && !title.equals(key + ".title")) {
            embed.setTitle(title, titleUrl);
        }
        if (description != null && !description.equals(key + ".description")) {
            embed.setDescription(description);
        }
        if (author != null && !author.equals(key + ".author")) {
            embed.setAuthor(author, authorUrl, authorIconUrl);
        }
        if (footer != null && !footer.equals(key + ".footer")) {
            embed.setFooter(footer, footerIcon);
        }

        list.forEach(embed::addField);

        return embed;
    }

    @NotNull
    public EmbedBuilder getEmbed(@NotNull String key, @Nullable Locale locale, Object... format) {
        return this.getEmbed(key, locale, null, null, null, null, 0, format);
    }

    @NotNull
    public String get(String key, Guild guild, Object... format) {
        return Objects.requireNonNullElse(getNull(key, guild.getLocale(), format), key);
    }
}
