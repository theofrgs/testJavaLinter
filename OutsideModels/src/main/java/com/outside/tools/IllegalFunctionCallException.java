package com.outside.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This exception exists to indicate the developers to not use a function when its deletion is impossible,
 * in the case of inheritance for example.
 * If this exception is thrown, please do not use the function you're trying to, this exception must not be caught.
 */
public class IllegalFunctionCallException extends Exception {

    public static final Logger LOGGER = Logger.getLogger("Architecture");

    public IllegalFunctionCallException() {
        LOGGER.log(Level.SEVERE, "This function must not be used. It can indicate a bad architecture.");
    }

    public IllegalFunctionCallException(String desc) {
        LOGGER.log(Level.SEVERE, "This function must not be used. It can indicate a bad architecture:");
        LOGGER.log(Level.SEVERE, () -> "-> " + desc);
    }
}
