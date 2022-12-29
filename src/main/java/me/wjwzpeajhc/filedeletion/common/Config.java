package me.wjwzpeajhc.filedeletion.common;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Config {

    private ConfigurationNode rootNode;
    private ConfigurationNode cleanerNode;

    public boolean isDeletionEnabled;
    public List<String> filesToDelete;
    public boolean isCleanerEnabled;
    public float dayAgeToDelete;
    public List<String> directoriesToClean;

    public Config(String configDirectory) {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of(configDirectory))
                .build();

        CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("An error occurred while loading configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            return;
        }

        rootNode = root.node("file-deletion");

        loadValues(loader);

        try {
            if (cleanerNode.node("days-old").getFloat() == 0.0) {
                cleanerNode.node("days-old").set("1.0");
            }
            loader.save(root);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadValues(YamlConfigurationLoader loader) {
        isDeletionEnabled = rootNode.node("enabled").getBoolean();

        try {
            filesToDelete = rootNode.node("files").getList(String.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }

        cleanerNode = rootNode.node("cleaner");
        isCleanerEnabled = cleanerNode.node("enabled").getBoolean();
        dayAgeToDelete = cleanerNode.node("days-old").getFloat();

        try {
            directoriesToClean = cleanerNode.node("directories").getList(String.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }
}
