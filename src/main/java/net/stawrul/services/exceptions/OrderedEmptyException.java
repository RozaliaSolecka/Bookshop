package net.stawrul.services.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderedEmptyException extends RuntimeException {
    public OrderedEmptyException() {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.log(Level.WARNING, this.getClass().getName());
    }
}
