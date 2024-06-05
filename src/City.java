/**
 * The {@code City} class represents a city with its name and coordinates in a two-dimensional space.
 */
public class City {
    /** The name of the city. */
    public String cityName;

    /** The x-coordinate of the city. */
    public int x;

    /** The y-coordinate of the city. */
    public int y;

    /**
     * Constructs a new {@code City} object with the given name and coordinates.
     *
     * @param name The name of the city.
     * @param x    The x-coordinate of the city.
     * @param y    The y-coordinate of the city.
     */
    public City(String name, int x, int y) {
        this.cityName = name;
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the name of the city.
     *
     * @param cityName The name of the city to be set.
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Sets the x-coordinate of the city.
     *
     * @param x The x-coordinate of the city to be set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the city.
     *
     * @param y The y-coordinate of the city to be set.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the name of the city.
     *
     * @return The name of the city.
     */
    public String cityName() {
        return cityName;
    }

    /**
     * Returns the x-coordinate of the city.
     *
     * @return The x-coordinate of the city.
     */
    public int x() {
        return x;
    }

    /**
     * Returns the y-coordinate of the city.
     *
     * @return The y-coordinate of the city.
     */
    public int y() {
        return y;
    }

    /**
     * Returns a string representation of the city.
     *
     * @return A string representation of the city.
     */
    @Override
    public String toString() {
        return "City{" +
                "name='" + cityName + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
