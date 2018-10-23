package co.uk.jaycarr.autoconfig;

import org.bukkit.configuration.file.FileConfiguration;

import co.uk.jaycarr.autoconfig.annotation.Value;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * An automated loader for Bukkit {@link FileConfiguration}
 * instances whereby field values can be retrieved from and
 * set to the configuration based on paths given using the
 * {@link Value} annotation.
 *
 * @author Jay Carr
 * @since 1.0-SNAPSHOT
 */
public class ConfigurationLoader {
    
    /**
     * The configuration used to retrieve and set values.
     */
    private final FileConfiguration configuration;

    /**
     * Creates a {@link ConfigurationLoader} based off of the
     * given {@link FileConfiguration}.
     *
     * @param configuration the configuration used when setting
     *                      and retrieving values
     */
    public ConfigurationLoader(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Loads the fields annotated with the {@link Value} annotation
     * within the given objects class.
     *
     * @param object the object to load values from
     * @throws ConfigurationException if no value(s) could be loaded
     * due to access reasons or no default values
     */
    public void load(Object object) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(Value.class)) {
                    continue;
                }

                String path = field.getDeclaredAnnotation(Value.class).path();

                Object configValue = this.configuration.get(path);
                Object fieldValue = field.get(object);

                if (configValue == null) {
                    if (fieldValue == null) {
                        throw new ConfigurationException("No value for '" + field.getName() + "' with no default value.");
                    }
                    return;
                }

                field.setAccessible(true);
                field.set(object, configValue);
            }
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("Could not load object: " + object, e);
        }
    }

    /**
     * Loads the fields annotated with the {@link Value} annotation
     * within the given objects classes.
     *
     * @param objects the objects to load values from
     * @throws ConfigurationException if no value(s) could be loaded
     * due to access reasons or no default values
     */
    public void loadAll(Object... objects) {
        for (Object object : objects) {
            this.load(object);
        }
    }

    /**
     * Saves the fields annotated with the {@link Value} annotation
     * within the given objects class and saves them to the file.
     *
     * @param object the object to save values from
     * @param file the file to save the configuration to
     * @throws ConfigurationException if a file I/O or field access issue occurs
     */
    public void save(Object object, File file) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(Value.class)) {
                    continue;
                }

                String path = field.getDeclaredAnnotation(Value.class).path();
                Object value = field.get(object);

                this.configuration.set(path, value);

                if (file != null) {
                    this.configuration.save(file);
                }
            }
        } catch (IllegalAccessException | IOException e) {
            throw new ConfigurationException("Could not save object to configuration.", e);
        }
    }

    /**
     * Saves the fields annotated with the {@link Value} annotation
     * within the given objects class without handling file writing.
     *
     * @param object the object to save values from
     * @throws ConfigurationException if a field access issue occurs
     */
    public void save(Object object) {
        this.save(object, null);
    }

    /**
     * Saves the fields annotated with the {@link Value} annotation
     * within the given objects classes without handling file writing.
     *
     * @param objects the objects to save values from
     * @throws ConfigurationException if a field access issue occurs
     */
    public void saveAll(Object... objects) {
        for (Object object : objects) {
            this.save(object);
        }
    }
}