package me.wjwzpeajhc.filedeletion.common;

import java.io.File;
import java.time.Instant;

public class FileDeleter {

    private final Config config;

    public FileDeleter(Config config) {
        this.config = config;
    }

    public void deleteFiles() {
        for (String path : config.filesToDelete) {
            File file = new File(path);
            file.delete();
        }
    }

    public void cleanDirectories() {
        for (String directory : config.directoriesToClean) {
            if (!new File(directory).exists()) return;

            File[] files = new File(directory).listFiles();
            for (File file : files) {
                if (isOlderThanAllowed(file.lastModified())) {
                    file.delete();
                }
            }
        }
    }

    private boolean isOlderThanAllowed(long lastModified) {
        var now = Instant.now().toEpochMilli();
        long difference = now - lastModified;
        long DAY_IN_MILLISECONDS = 86_400_000;

        return difference > DAY_IN_MILLISECONDS * config.dayAgeToDelete;
    }
}
