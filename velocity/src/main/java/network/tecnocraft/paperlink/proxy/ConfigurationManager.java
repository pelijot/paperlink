package network.tecnocraft.paperlink.proxy;

import org.simpleyaml.configuration.file.YamlFile;
import java.nio.file.Path;
import java.util.Map;

public class ConfigurationManager {

    private final YamlFile config;

    public ConfigurationManager(Path dataDirectory) {
        try {
            config = new YamlFile(dataDirectory.resolve("crafty.yml").toFile());
            if (!config.exists()) {
                config.createNewFile(true);
            }
            config.load();
        } catch (Exception e) {
            throw new RuntimeException("Could not load crafty.yml", e);
        }
    }

    public String getCraftyHost() {
        return config.getString("crafty-api.host");
    }

    public int getCraftyPort() {
        return config.getInt("crafty-api.port");
    }

    public String getCraftyUsername() {
        return config.getString("crafty-api.username");
    }

    public String getCraftyPassword() {
        return config.getString("crafty-api.password");
    }

    public String getCraftyId(String serverName) {
        return config.getString("servers." + serverName);
    }

    public Map<String, Object> getAllServerMappings() {
        return config.getConfigurationSection("servers").getValues(false);
    }
}
