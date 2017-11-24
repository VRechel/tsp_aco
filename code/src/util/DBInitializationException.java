package util;

/**
 * @author Viktor
 */
public class DBInitializationException extends Exception{
    public DBInitializationException() {
        super("DB already initialized!");
    }
}