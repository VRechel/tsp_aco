package util;

import tsp.City;

/**
 * Created by Viktor on 01.12.2017.
 */
public class CityPair {
    private final City a;
    private final City b;

    public CityPair(City a, City b){
        this.a = a;
        this.b = b;
    }

    public City getCityB() {
        return b;
    }

    public City getCityA() {
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityPair cityPair = (CityPair) o;

        return a.equals(cityPair.a) && b.equals(cityPair.b);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }
}
