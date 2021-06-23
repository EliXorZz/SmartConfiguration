package fr.elixorzz.smartconfiguration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.logging.Level;

/**
 * @author EliXorZz
 * @version 1.0
 */

public abstract class SmartConfiguration<T> {
    private final transient static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private transient static Plugin plugin;

    private transient String name;
    private transient Class<T> configClass;
    private transient Path configPath;
    private transient Path filePath;

    public SmartConfiguration(String name, Class<T> configClass) {
        this.name = name;
        this.configClass = configClass;

        if (plugin != null) {
            this.configPath = plugin.getDataFolder().toPath();
            this.filePath = Paths.get(plugin.getDataFolder() + "/" + name);
        }
    }

    public T load() {
        if (plugin == null) {
            Bukkit.getLogger().log(Level.SEVERE, "[SmartConfiguration] Please register SmartConfiguration");
            return null;
        }

        plugin.getLogger().log(Level.INFO, "[SmartConfiguration] " + name + " is loading ...");

        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath);
            }

            if (!Files.exists(filePath)) {
                T config = configClass.newInstance();

                Files.createFile(filePath);
                Files.write(filePath, gson.toJson(config).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                plugin.getLogger().log(Level.INFO, "[SmartConfiguration] " + name + " was created.");
            }

            if (filePath.toFile().length() == 0) {
                T config = configClass.newInstance();
                Files.write(filePath, gson.toJson(config).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            try(FileReader reader = new FileReader(filePath.toFile())) {
                T json = gson.fromJson(reader, configClass);
                plugin.getLogger().log(Level.INFO, "[SmartConfiguration] " + name + " was loaded correctly.");
                return json;
            }
        } catch (IllegalAccessException | InstantiationException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void update(T config) {
        try {
            Files.delete(filePath);
            Files.write(filePath, gson.toJson(config).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void register(Plugin p) {
        plugin = p;
    }
}
