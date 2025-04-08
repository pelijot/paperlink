package network.tecnocraft.paperlink.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class ProxyServerCommand implements SimpleCommand {

    private final ProxyServer proxy;
    private final ConfigurationManager config;
    private final CraftyManager crafty;

    public ProxyServerCommand(ProxyServer proxy, ConfigurationManager config, CraftyManager crafty) {
        this.proxy = proxy;
        this.config = config;
        this.crafty = crafty;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        var source = invocation.source();

        if (args.length != 2) {
            source.sendMessage(Component.text("Usage: /proxyserver <start|stop|status> <server-name>"));
            return;
        }

        String action = args[0].toLowerCase();
        String serverName = args[1];

        String craftyId = config.getCraftyId(serverName);
        if (craftyId == null) {
            source.sendMessage(Component.text("Server not found in crafty.yml: " + serverName));
            return;
        }

        switch (action) {
            case "start" -> {
                source.sendMessage(Component.text("Starting server " + serverName + "..."));
                boolean success = crafty.startServer(craftyId);
                source.sendMessage(success
                        ? Component.text("Server " + serverName + " started successfully!")
                        : Component.text("Failed to start server " + serverName + ". Check Crafty."));
            }
            case "stop" -> {
                source.sendMessage(Component.text("Stopping server " + serverName + "..."));
                boolean success = crafty.stopServer(craftyId);
                source.sendMessage(success
                        ? Component.text("Server " + serverName + " stopped successfully!")
                        : Component.text("Failed to stop server " + serverName + ". Check Crafty."));
            }
            case "status" -> {
                source.sendMessage(Component.text("Checking status of server " + serverName + "..."));
                String status = crafty.getServerStatus(craftyId);
                source.sendMessage(Component.text("Server " + serverName + " status: " + status));
            }
            default -> source.sendMessage(Component.text("Unknown action: " + action));
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("paperlink.proxyserver"); // Optional permission clearly defined
    }
}
