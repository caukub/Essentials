package me.wjwzpeajhc.essentials.bukkit;

import me.wjwzpeajhc.essentials.common.FileDeleter;
import me.wjwzpeajhc.essentials.common.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Essentials extends JavaPlugin implements Listener {

    private Config config;

    @Override
    public void onEnable() {
        this.config = new Config(getDataFolder() + File.separator + "config.yml");
    }

    @EventHandler
    public void onLoad(ServerLoadEvent event) {
        FileDeleter deleter = new FileDeleter(config);
        delete(deleter);
    }

    private void delete(FileDeleter deleter) {
        getServer().getScheduler().runTask(this, () -> {
            if (config.isDeletionEnabled) {
                deleter.deleteFiles();
            }

            if (config.isCleanerEnabled) {
                deleter.cleanDirectories();
            }
        });
    }
}
