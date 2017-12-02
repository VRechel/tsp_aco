package aco;

class VisitationException extends Exception {
    VisitationException() {
        super("City was already visited!");
    }
}
