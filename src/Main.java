import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String fileName = "moviesData.csv"; // File is in the same directory

        try {
            List<Movie> movies = MovieReader.readMoviesFromCSV(fileName);

            // Create and show the GUI after reading movies
            MovieGUI movieGUI = new MovieGUI(movies);
            movieGUI.show();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    } }
