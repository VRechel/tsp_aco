package tsp;

class VisitationException extends Exception {
    VisitationException() {
        super("City already visited");
    }
}
