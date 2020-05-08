package fr.elixorzz.smartconfiguration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

/**
 * @author EliXorZz
 * @version 1.0
 */

public abstract class SmartConfiguration<T> {
    private final transient static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private transient static Plugin plugin;

    private transient String name;
    private transient Class<T> configClass;

    public SmartConfiguration(String name, Class<T> configClass) {
        if (plugin == null)
            Bukkit.getLogger().log(Level.SEVERE, "[SmartConfiguration] Please register SmartConfiguration");

        this.name = name;
        this.configClass = configClass;
    }

    public T load() {
        File file = new File(plugin.getDataFolder(), name);
        plugin.getLogger().log(Level.INFO, "[SmartConfiguration] " + name + " is loading ...");

        if (!file.exists()) {
            try {
                T config = configClass.newInstance();
                try {
                    plugin.getDataFolder().mkdir();
                    Files.write(file.toPath(), gson.toJson(config).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                    plugin.getLogger().log(Level.INFO, "[SmartConfiguration] " + name + " was created.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            T json = gson.fromJson(new FileReader(file), configClass);
            plugin.getLogger().log(Level.INFO, "[SmartConfiguration] " + name + " was loaded correctly.");
            return json;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void update(T config) {
        try {
            File file = new File(plugin.getDataFolder(), name);
            plugin.getDataFolder().mkdir();
            Files.write(file.toPath(), gson.toJson(config).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void register(Plugin p) {
        plugin = p;
    }
}
