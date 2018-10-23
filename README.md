# AutoConfig
An annotation-based configuration utility for Bukkit/Spigot used to automatically retrieve and set configuration values.

### Usage
These steps make the assumption that you are using Maven to build your plugin, however, steps will be similar for Gradle and other build tools. Please note that there is no Maven repository for this resource yet.

 1) Download a copy of the latest JAR file from the [releases](https://github.com/JayCarrUK/AutoConfig/releases).
 2) Install the JAR into your local Maven repository. A guide can be found [here](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html).
 3) Add the repository to your `pom.xml`, ensuring that you use the Maven Shade Plugin to shade the resource.
 4) Enjoy making use of AutoConfig in your plugin. An example setup can be seen below.
 
 ### Example
 ```java
 public final class ExamplePlugin extends JavaPlugin {
    
    private ConfigurationLoader configurationLoader;
    private ConfigOptions configOptions;
    
    @Override
    public void onEnable() {
        // Be sure to load the configuration. 
        // This method will do that for us, ensuring the file exists also.
        this.saveDefaultConfig();
        
        // Create a ConfigurationLoader for the FileConfiguration. You can use configs other than the config.yml too.
        this.configurationLoader = new ConfigurationLoader(this.getConfig());
        
        // A class seen below which holds fields to our configurable values.
        this.configOptions = new ConfigOptions();
        
        // Try to load the configuration files with the loader.
        try {
            this.configurationLoader.load(this.configOptions);
        } catch (ConfigurationException e) {
            this.getLogger().log(Level.WARNING, "Failed to load configuration, disabling plugin...", e);
            // You might want to disable the plugin due to a missing configuration value.
            this.getServer().getPluginManager().disablePlugin(this);
        }
        
        // We can now use our configurable fields. We can then branch out and create a getter for our options to use it elsewhere.
        this.getLogger().info("Here is a configured value: " + this.configOptions.noDefaultValue);
    }
    
    @Override
    public void onDisable() {
        // Try to save the configuration fields. Remember to save the file once your done.
        // You can also supply a file via ConfigurationLoader#save(Object, File) to save there and then, most useful for custom files.
        try {
            this.configurationLoader.save(this.configOptions);
            this.saveConfig();
        } catch (ConfigurationException e) {
            this.getLogger().log(Level.WARNING, "Could not save config.", e);
        }
    }
    
    private static class ConfigOptions {

        // The default value will be overridden if a value is found in the config.
        @Value(path = "has-default-value")
        private final int hasDefaultValue = 1;

        // No default value is found. The default int value of 0 will be used.
        @Value(path = "no-default-value")
        private int noDefaultValue;

        // No default value is found. A ConfigurationException is thrown as this value will be null.
        @Value(path = "null-default-value")
        private String nullDefaultValue;
    }
}
 ```
 ### License
 This project is currently under the MIT License.
