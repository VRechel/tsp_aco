package tsp;

import java.util.Objects;

/**
 * @author Viktor
 */
public class City {
    private final int id;
    private boolean visited = false;

    public City(int id) {
        this.id = --id;
    }

    public void visit() throws VisitationException {
        if(visited)
            throw new VisitationException();
        else
            visited = true;
    }

    public boolean getVisited() {
        return visited;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "City " + id;
    }

    public int getId() {
        return id;
    }
}
