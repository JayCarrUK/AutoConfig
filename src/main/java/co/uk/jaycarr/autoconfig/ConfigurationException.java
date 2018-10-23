package co.uk.jaycarr.autoconfig;

/**
 * An exception throws when an error occurs during the loading
 * and saving of {@link ConfigurationLoader} instances.
 *
 * @author Jay Carr
 */
public final class ConfigurationException extends RuntimeException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}