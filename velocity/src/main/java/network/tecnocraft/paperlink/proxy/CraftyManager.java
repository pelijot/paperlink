package network.tecnocraft.paperlink.proxy;

import okhttp3.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Base64;

public class CraftyManager {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ConfigurationManager config;

    public CraftyManager(ConfigurationManager config) {
        this.config = config;
    }

    private String getAuthHeader() {
        String credentials = config.getCraftyUsername() + ":" + config.getCraftyPassword();
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    private String buildUrl(String craftyId, String action) {
        return String.format("http://%s:%d/api/v2/servers/%s/action/%s",
                config.getCraftyHost(), config.getCraftyPort(), craftyId, action);
    }

    public boolean startServer(String craftyId) {
        return sendAction(craftyId, "start");
    }

    public boolean stopServer(String craftyId) {
        return sendAction(craftyId, "stop");
    }

    public String getServerStatus(String craftyId) {
        String url = String.format("http://%s:%d/api/v2/servers/%s/stats",
                config.getCraftyHost(), config.getCraftyPort(), craftyId);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", getAuthHeader())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                return json.get("data").getAsJsonObject().get("state").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private boolean sendAction(String craftyId, String action) {
        String url = buildUrl(craftyId, action);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .header("Authorization", getAuthHeader())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
