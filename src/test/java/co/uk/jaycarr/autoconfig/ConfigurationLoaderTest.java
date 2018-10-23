package co.uk.jaycarr.autoconfig;

import org.bukkit.plugin.java.JavaPlugin;

import co.uk.jaycarr.autoconfig.annotation.Value;

public final class ConfigurationLoaderTest extends JavaPlugin {

    private ConfigOptions configOptions;
    private ConfigurationLoader configurationLoader;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.configurationLoader = new ConfigurationLoader(this.getConfig());
        this.configOptions = new ConfigOptions();

        try {
            this.configurationLoader.load(this.configOptions);
        } catch (ConfigurationException e) {
            this.getLogger().warning("Failed to load configuration, disabling plugin...");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        this.getLogger().info("hasDefaultValue (expected: 1) - " + this.configOptions.hasDefaultValue);
        this.getLogger().info("noDefaultValue (expected: 2 from file): " + this.configOptions.noDefaultValue);
    }

    @Override
    public void onDisable() {
        try {
            this.configurationLoader.save(this.configOptions);
            this.saveConfig();
        } catch (ConfigurationException e) {
            this.getLogger().warning("Could not save config.");
        }
    }

    private static class ConfigOptions {

        @Value(path = "has-default-value")
        private final int hasDefaultValue = 1;

        @Value(path = "no-default-value")
        private int noDefaultValue;
    }
}