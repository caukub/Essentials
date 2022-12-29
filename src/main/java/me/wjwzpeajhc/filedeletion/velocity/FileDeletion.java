package me.wjwzpeajhc.filedeletion.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import me.wjwzpeajhc.filedeletion.common.Config;
import me.wjwzpeajhc.filedeletion.common.FileDeleter;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = "filedeletion", name = "File Deletion", version = "0.0.1-SNAPSHOT")
public class FileDeletion {
    private final Config config;

    @Inject
    public FileDeletion(@DataDirectory Path dataDirectory) {
        this.config = new Config(dataDirectory + File.separator + "config.yml");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        var deleter = new FileDeleter(config);

        delete(deleter);
    }

    private void delete(FileDeleter deleter) {
        if (config.isDeletionEnabled) {
            deleter.deleteFiles();
        }

        if (config.isCleanerEnabled) {
            deleter.cleanDirectories();
        }
    }
}

