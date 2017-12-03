package tsp;

import java.util.Objects;

/**
 * @author Viktor
 */
public class City {
    private final int id;

    /*
        The city will be named by its id. As arrays start at zero and to not waste space all city ids will be
        decremented before usage.
     */
    public City(int id) {
        this.id = --id;
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
        return String.valueOf(id);
    }

    public int getId() {
        return id;
    }
}
