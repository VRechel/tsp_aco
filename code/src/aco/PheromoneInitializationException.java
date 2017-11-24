package aco;

public class PheromoneInitializationException extends Exception {
    PheromoneInitializationException() {
        super("Colony already initialized!");
    }
}
