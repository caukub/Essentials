package me.wjwzpeajhc.filedeletion.bukkit;

import me.wjwzpeajhc.filedeletion.common.FileDeleter;
import me.wjwzpeajhc.filedeletion.common.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class FileDeletion extends JavaPlugin {

    private Config config;

    @Override
    public void onEnable() {
        this.config = new Config(getDataFolder() + File.separator + "config.yml");
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
