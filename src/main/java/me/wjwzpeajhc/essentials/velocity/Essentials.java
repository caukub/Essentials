package me.wjwzpeajhc.essentials.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import me.wjwzpeajhc.essentials.common.Config;
import me.wjwzpeajhc.essentials.common.FileDeleter;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(id = "essentials", name = "Essentials", version = "0.0.1-SNAPSHOT")
public class Essentials {
    private final Config config;
    private ProxyServer server;

    @Inject
    public Essentials(@DataDirectory Path dataDirectory, ProxyServer server) {
        this.server = server;
        this.config = new Config(dataDirectory + File.separator + "config.yml");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        var deleter = new FileDeleter(config);
        server.getScheduler().buildTask(this, () -> {
            delete(deleter);
        })
                .delay(config.deletionDelay, TimeUnit.SECONDS)
                .schedule();
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

