import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieReader {
    public static List<Movie> readMoviesFromCSV(String filePath) throws IOException {
        List<Movie> movies = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String headerRow = br.readLine();

            String line;

            while ((line = br.readLine()) != null) {

                // Split the CSV line by comma, but ignore commas within double quotes
                // its called " CSV split regex "
                String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");



                // Check if the line has enough elements (Title, Year, Genres, Runtime, Rating, Writers, Summary)
                if (data.length >= 7) {
                    String title = data[0].trim();
                    int year = Integer.parseInt(data[1].trim());
                    String[] genres = data[2].split("\\|");
                    int runtime = Integer.parseInt(data[3].trim());
                    double rating = Double.parseDouble(data[4].trim());
                    String writers = data[5].trim();
                    String summary = data[6].replaceAll("\"", ""); // Remove quotes from the summary

                    // Create a Movie object and add it to the movies list
                    Movie movie = new Movie(title, year, genres, runtime, rating, writers, summary);
                    movies.add(movie);

                } else {
                    // Handle incomplete or incorrect data format
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            // Handle file not found or other IO exceptions
            throw new IOException("Error reading file: " + e.getMessage());
        }

        return movies;
    }
}