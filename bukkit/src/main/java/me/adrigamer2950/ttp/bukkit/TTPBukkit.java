package me.adrigamer2950.ttp.bukkit;

import com.gmail.olexorus.themis.api.NotificationEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TTPBukkit extends JavaPlugin implements Listener {

    private static final String BUNGEE_CHANNEL = "themistoproxy:main";

    private bStats bStats;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("Themis") == null) {
            getLogger().severe("You need to have Themis Anti Cheat installed in order to use this plugin");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, BUNGEE_CHANNEL);

        bStats = new bStats(this, 24034);

        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        bStats.shutdown();

        getLogger().info("Disabled!");
    }

    @EventHandler
    public void onNotification(NotificationEvent e) {
        //noinspection UnstableApiUsage
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(e.getMessage());

        Bukkit.getOnlinePlayers().stream().findFirst().ifPresent(p -> p.sendPluginMessage(this, BUNGEE_CHANNEL, out.toByteArray()));
    }
}
