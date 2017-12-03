package tsp;

/**
 * @author Viktor
 */
public class City {
    private final String name;
    private boolean visited = false;

    public City(String a) {
        this.name = a;
    }

    public City(int id) {
        this.name= String.valueOf(id);
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

        return name != null ? name.equals(city.name) : city.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
