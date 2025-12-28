package live.blackninja.whitelist.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserAPI {

    private static final OkHttpClient client = new OkHttpClient();

    public static CompletableFuture<UUID> getUUIDByName(String name) {

        CompletableFuture<UUID> future = new CompletableFuture<>();

        Request request = new Request.Builder()
                .url(MessageFormat.format("https://api.mojang.com/users/profiles/minecraft/{0}", name))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(new Exception());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful() || responseBody == null) {
                        future.completeExceptionally(new Exception());
                        return;
                    }

                    String body = responseBody.string();

                    //System.out.println(body);

                    JsonElement parse = JsonParser.parseString(body);

                    String id = parse.getAsJsonObject().getAsJsonPrimitive("id").getAsString();

                    if (id == null) {
                        future.completeExceptionally(new Exception());
                        return;
                    }

                    future.complete(fromTrimmed(id));
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    public static CompletableFuture<String> getNameByUUID(UUID uuid) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // UUID ohne Bindestriche für die API
        String uuidString = uuid.toString().replace("-", "");

        Request request = new Request.Builder()
                .url(MessageFormat.format("https://sessionserver.mojang.com/session/minecraft/profile/{0}", uuidString))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(new Exception());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful() || responseBody == null) {
                        future.completeExceptionally(new Exception());
                        return;
                    }

                    String body = responseBody.string();
                    JsonElement parse = JsonParser.parseString(body);

                    String name = parse.getAsJsonObject().getAsJsonPrimitive("name").getAsString();

                    if (name == null) {
                        future.completeExceptionally(new Exception());
                        return;
                    }

                    future.complete(name);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    private static UUID fromTrimmed(String trimmedUUID) {
        if (trimmedUUID == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(trimmedUUID.trim());

        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }

        return UUID.fromString(builder.toString());
    }
}
