package tsp;

public class VisitationException extends Exception {
    VisitationException() {
        super("City already visited");
    }
}
