package me.wjwzpeajhc.essentials.common;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

@ConfigSerializable
public class Config {
    private CommentedConfigurationNode root;
    private ConfigurationNode rootNode;
    private ConfigurationNode cleanerNode;

    public boolean isDeletionEnabled;
    public List<String> filesToDelete;
    public boolean isCleanerEnabled;
    public float dayAgeToDelete;
    public List<String> directoriesToClean;
    public int deletionDelay;

    private final String configDirectory;

    public Config(String configDirectory) {
        this.configDirectory = configDirectory;
        createConfig();

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of(configDirectory))
                .nodeStyle(NodeStyle.BLOCK)
                .build();

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
    }

    private void createConfig() {
        File file = new File(configDirectory);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(configDirectory);
                writer.write(getDefaultConfig());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadValues(YamlConfigurationLoader loader) {
        isDeletionEnabled = rootNode.node("enabled").getBoolean();
        deletionDelay = rootNode.node("delay").getInt();

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

    private String getDefaultConfig() {
        return "#     ___ _ _           ___     _      _   _\n" +
                "#    / __(_) | ___     /   \\___| | ___| |_(_) ___  _ __\n" +
                "#   / _\\ | | |/ _ \\   / /\\ / _ \\ |/ _ \\ __| |/ _ \\| '_ \\\n" +
                "#  / /   | | |  __/  / /_//  __/ |  __/ |_| | (_) | | | |\n" +
                "#  \\/    |_|_|\\___| /___,' \\___|_|\\___|\\__|_|\\___/|_| |_|\n" +
                "\n" +
                "# Function that allows you to delete certain files/folders after server startup\n" +
                "# and to delete file inside certain folder if they have a certain duration.\n" +
                "file-deletion:\n" +
                "  # Enable or disable the function\n" +
                "  enabled: false\n" +
                "  # Files that will be deleted\n" +
                "  files: []\n" +
                "  # The amount of seconds after deletion task run \n" +
                "  delay: 0" +
                "  # The files inside the logs folder, if they have a certain duration, will be deleted\n" +
                "  cleaner:\n" +
                "    # Enable or disable the function\n" +
                "    enabled: false\n" +
                "    # Number of days the file must have to get deleted\n" +
                "    days-old: 1\n" +
                "    # Directories of the files that will be deleted\n" +
                "    directories: []";
    }
}
