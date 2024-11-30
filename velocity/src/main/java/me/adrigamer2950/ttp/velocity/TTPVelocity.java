package me.adrigamer2950.ttp.velocity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(
        id = "themistoproxy",
        name = "ThemisToProxy",
        version = BuildConstants.VERSION,
        description = "Get Themis' notifications from anywhere in your network",
        url = "https://github.com/Adrigamer2950/ThemisToProxy",
        authors = {"devadri"}
)
public class TTPVelocity {

    private static final ChannelIdentifier CHANNEL_IDENTIFIER = MinecraftChannelIdentifier.from("themistoproxy:main");

    @Inject
    private Logger logger;
    @Inject
    private ProxyServer proxy;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    private YamlDocument config;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            this.config = YamlDocument.create(
                    new File(dataDirectory.toFile(), "config.yml"),
                    Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("config.yml"))
            );

            this.config.reload();
            this.config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new bStats.Factory(proxy, logger, dataDirectory).make(this, 24036);

        proxy.getChannelRegistrar().register(CHANNEL_IDENTIFIER);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent e) {
        if (!CHANNEL_IDENTIFIER.equals(e.getIdentifier())) {
            return;
        }

        //noinspection UnstableApiUsage
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        String message = in.readUTF();

        proxy.getAllPlayers()
                .stream()
                .filter(p -> p.hasPermission("themis.notifications"))
                .filter(p ->
                        p.getCurrentServer().isPresent()
                                && !p.getCurrentServer().get().getServerInfo().getName().equals(((ServerConnection) e.getSource()).getServerInfo().getName())
                )
                .forEach(p -> p.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
                        this.config.getString("message-format")
                                .replaceAll("%server%", ((ServerConnection) e.getSource()).getServerInfo().getName())
                                .replaceAll("%message%", message)
                )));
    }
}
