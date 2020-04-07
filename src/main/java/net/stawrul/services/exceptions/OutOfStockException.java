package net.stawrul.services.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wyjątek sygnalizujący niedostępność towaru.
 * <p>
 * Wystąpienie wyjątku z hierarchii RuntimeException w warstwie biznesowej
 * powoduje wycofanie transakcji (rollback).
 */
public class OutOfStockException extends RuntimeException {
    public OutOfStockException() {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.log(Level.WARNING, this.getClass().getName());
    }
}
