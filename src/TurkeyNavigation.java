import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.awt.Font;
import java.util.ArrayList;

/**
 * The program finds the shortest path from the given source city to the destination city using Dijkstra's algorithm.
 * The roads and cities are given as txt files, map is given as a png file. My code read the files and creates a City object to hold
 * the information. It keeps the information about if any connection exists inter-cities in a matrix called matrix,
 * also hold the lengths of roads in another matrix called adjacencyMatrix.
 * It prints out the path and the distance if exists and draws the map the route in blue and other cities in gray.
 * If the city is not inside the map or written wrongly than it asks until getting a proper input.
 * If a city is unreachable than it prints out no path could be found and the map does not get drew.
 * @author Omer Taha Ornek, Studen ID: 2022400117
 * @since Date: 23.03.2024
 */
public class TurkeyNavigation {
    public static void main(String[] args) {
        int numberCities = 0; // number of cities
        int numberAdjacent = 0; // number of roads
        int[][] matrix = null; // matrix for holding the roads between cities
        double[][] adjacencyMatrix = new double[numberCities][numberCities]; // to hold the distance between;
        City[] cities = null;
        String[][] cityNamesWithIndex = null; // created to arrays one for holding the index in the cities array after alphabetically sorting
        String[] cityNames = null; // created a second without indexes to use binary search.
        try (Scanner input = new Scanner(new File("city_coordinates.txt"))) {
            while (input.hasNextLine()) { // finding the number of cities // finding the number of cities
                input.nextLine();
                numberCities++;
            }
            input.close(); // closing it to reopen from the first line
            cities = new City[numberCities]; // initializing arrays
            cityNamesWithIndex = new String[numberCities][2];
            cityNames = new String[numberCities];
            Scanner readLine = new Scanner(new File("city_coordinates.txt"));
            for (int i = 0; i < numberCities; i++) { // adding the information to the cities array
                String line = readLine.nextLine(); // read the line
                String[] lineSplit = line.split(", "); // split it
                cityNames[i] = lineSplit[0]; // add the names of the city to cityNames
                cityNamesWithIndex[i][0] = lineSplit[0]; // add the name and the index to cityNamesWithIndex
                cityNamesWithIndex[i][1] = String.valueOf(i);
                cities[i] = new City(lineSplit[0], Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]));
                // create a city object to add cities array
            }
            Arrays.sort(cityNamesWithIndex, Comparator.comparing(row -> row[0]));// Sorting city names alphabetically to keep in matrix
            Arrays.sort(cityNames);
            matrix = new int[numberCities][numberCities];
            adjacencyMatrix = new double[numberCities][numberCities];
            Scanner connection = new Scanner(new File("city_connections.txt"));
            while (connection.hasNextLine()) {
                connection.nextLine();
                numberAdjacent++;
            }
            connection.close();
            Scanner reconnection = new Scanner(new File("city_connections.txt"));
            for (int i = 0; i < numberAdjacent; i++) {
                String line = reconnection.nextLine();
                String[] lineSplit = line.split(",");
                int sourceIndex = Arrays.binarySearch(cityNames, lineSplit[0]); // finding the index in matrix
                int destinationIndex = Arrays.binarySearch(cityNames, lineSplit[1]);
                matrix[sourceIndex][destinationIndex] = 1; // giving value 1 if they are connected
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //Creating the adjacencyMatrix
        for (int i = 0; i < numberCities; i++) {
            int indexInClass; // the index in the cities array, i is the index in cityName array
            indexInClass = Integer.parseInt(cityNamesWithIndex[i][1]);
            int xCoordinate = cities[indexInClass].x();
            int yCoordinate = cities[indexInClass].y();
            for (int k = 0; k < numberCities; k++) { // drawing the roads between cities
                if (matrix[i][k] == 1) { //  checking the cities to find the connections with cityName[i]
                    int destinationIndexInClass = Integer.parseInt(cityNamesWithIndex[k][1]); // finding the index in the class
                    int destinationXCoordinate = cities[destinationIndexInClass].x();
                    // using index to get coordinates of the destination cities
                    int destinationYCoordinate = cities[destinationIndexInClass].y();
                    double temp = Math.pow(destinationXCoordinate - xCoordinate, 2)
                            + Math.pow(destinationYCoordinate - yCoordinate, 2); // finding the length of the road
                    adjacencyMatrix[i][k] = Math.pow(temp, 0.5); //
                    adjacencyMatrix[k][i] = Math.pow(temp, 0.5);
                }
            }
        }
        Scanner input = new Scanner(System.in);
        int indexSource;
        while (true) {
            System.out.print("Enter starting city: ");
            String sourceCity = input.next(); // getting the start point
            indexSource = Arrays.binarySearch(cityNames, sourceCity);
            if (indexSource < 0){
                System.out.println("City named " + "'" + sourceCity + "'" + " not found. Please enter a valid city name");
            }
            else { break; }
        }
        int indexDestination;
        while (true) {
            System.out.print("Enter destination city: ");
            String destinationCity = input.next(); // getting the end point
            indexDestination = Arrays.binarySearch(cityNames, destinationCity);
            if (indexDestination < 0) {
                System.out.println("City named " + "'" + destinationCity + "'" + " not found. Please enter a valid city name");
            } else {
                break;
            }
        }
        Result values = dijkstra(adjacencyMatrix, indexSource, indexDestination); // getting the values from dijkstra
        double distance = values.getDistance(); // distance
        if (Double.isFinite(distance)) { // Checking if reachable
            int[] path = values.getPath(); // the path from source to destination
            int numberRoads = path.length; // number of roads + 1 since there will be more
            int sourceCitiesIndex = Integer.parseInt(cityNamesWithIndex[indexSource][1]);
            int sourceCityXCoordinate = cities[sourceCitiesIndex].x(); // coordinates of the source city
            int sourceCityYCoordinate = cities[sourceCitiesIndex].y();
            String sourceCityName = cities[sourceCitiesIndex].cityName(); // getting the name of the source city
            // printing the map
            StdDraw.setCanvasSize(2377 / 2, 1055 / 2);
            StdDraw.setXscale(0, 2377);
            StdDraw.setYscale(0, 1055);
            StdDraw.picture(2377 / 2.0, 1055 / 2.0, "map.png", 2377, 1055);
            StdDraw.enableDoubleBuffering();
            StdDraw.setFont(new Font("Serif", Font.PLAIN, 14));
            StdDraw.setPenColor(Color.GRAY);
            for (int i = 0; i < numberCities; i++) {
                int indexInCities = Integer.parseInt(cityNamesWithIndex[i][1]);
                int x1 = cities[indexInCities].x(); /* I hold the information of the roads between
                cities in an alphabetic matrix, so I am iterating in alphabetic order and finding
                city's information from the cities array. To avoid iterating to find which index a city
                is in the cities array I hold that information in String form in cityNamesWithIndex. */
                int y1 = cities[indexInCities].y();
                String name = cities[indexInCities].cityName();
                StdDraw.filledCircle(x1, y1, 5); // printing the city center circles
                StdDraw.text(x1,y1+15,name); // printing the city names
                for (int j = 0; j < numberCities; j++){
                    if (matrix[i][j] == 1) { // means there is a road between
                        int citiesIndexJ = Integer.parseInt(cityNamesWithIndex[j][1]);
                        int x2 = cities[citiesIndexJ].x();
                        int y2 = cities[citiesIndexJ].y();
                        StdDraw.line(x1,y1,x2,y2);
                    }
                }
            }
            // Printing the route which algorithm uses
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.text(sourceCityXCoordinate,sourceCityYCoordinate+15,sourceCityName);
            StdDraw.filledCircle(sourceCityXCoordinate,sourceCityYCoordinate,4);
            StdDraw.setPenRadius(0.006);
            System.out.print("Total Distance: " + String.format("%.2f",distance) +". Path: " + sourceCityName); // printing the first part of the prompt
            for (int k = 0; k < numberRoads; k++) { // Blue marking the cities and the road in the path
                int tempIndex = Integer.parseInt(cityNamesWithIndex[path[k]][1]);
                int tempXCoordinate = cities[tempIndex].x();
                int tempYCoordinate = cities[tempIndex].y();
                String cityName = cities[tempIndex].cityName();
                System.out.print(" -> " + cityName); // printing the path
                StdDraw.line(sourceCityXCoordinate,sourceCityYCoordinate,tempXCoordinate,tempYCoordinate);
                StdDraw.text(tempXCoordinate,tempYCoordinate+15,cityName);
                StdDraw.filledCircle(tempXCoordinate,tempYCoordinate,4);
                sourceCityXCoordinate =tempXCoordinate;
                sourceCityYCoordinate = tempYCoordinate;
            }
            StdDraw.show();
        }
        else { System.out.println("No path could be found");} // If unreachable
    }
    /**
     * Runs Dijkstra's algorithm to find the shortest path from a source node to a destination node in a weighted graph represented by the given matrix.
     *
     * @param matrix      The weighted adjacency matrix representing the graph.
     * @param source      The index of the source node.
     * @param destination The index of the destination node.
     * @return A {@code Result} object containing the distance of the shortest path and the path itself.
     */
    public static Result dijkstra(double[][] matrix, int source, int destination) {
        int n = matrix.length; // finding the number of cities
        boolean[] visited = new boolean[n];
        double[] distances = new double[n];
        int[] predecessors = new int[n];
        ArrayList<Integer> path = new ArrayList<>();
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[source] = 0.0;
        while (true) {
            int minVertex = -1;
            double minDistance = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && distances[i] < minDistance) {
                    minVertex = i;
                    minDistance = distances[i];
                }
            }
            if (minVertex == -1 || minVertex == destination){
                /* to make it more efficient we stop finding the distances after finding the destination.
                As base case we put finding all distances since input can be like that */
                String holder = String.format("%.2f", distances[destination]);
                distances[destination] = Double.parseDouble(holder);
                break;
            }
            visited[minVertex] = true; // we mark the cities we have already checked to increase the efficiency
            for (int v = 0; v < n; v++) {
                if (matrix[minVertex][v] != 0.0 && !visited[v]) {
                    double newDist = minDistance + matrix[minVertex][v];
                    if (newDist < distances[v]) { // checking if our current distance is less than the one we found before for that city.
                        distances[v] = newDist;  // if so we are
                        predecessors[v] = minVertex; // Which city I came from to the current city;
                    }
                }
            }
        }
        int currentLocation = destination;// start from the destination and keep the track backwards.
        if (Double.isFinite(distances[destination])) {
            while (currentLocation != source){ // go until reaching the source city
                path.add(0,currentLocation); // add always to beginning to get the correct direction
                currentLocation = predecessors[currentLocation];
            }
        }
        int[] pathArray = path.stream().mapToInt(Integer::intValue).toArray();
        Result result = new Result(distances[destination], pathArray);
        return result;
    }

    /**
     * returns the result of the Dijkstra's algorithm
     */
    static class Result{
        private double distance;
        private int[] path;
        /**
         * Constructs a new {@code Result} object with the given distance and path.
         *
         * @param distance The distance of the computed path.
         * @param path     The array representing the computed path.
         */
        public Result(double distance,int[] path){
            this.distance = distance;
            this.path = path;
        }
        /**
         * Returns the computed path.
         *
         * @return The array representing the computed path.
         */
        public int[] getPath() {
            return path;
        }
        /**
         * Returns the distance of the computed path.
         *
         * @return The distance of the computed path.
         */
        public double getDistance(){
            return distance;
        }
    }

}
