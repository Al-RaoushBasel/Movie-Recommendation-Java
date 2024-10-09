import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieUtilsTest {

    // Define a constant for the temporary file path
    private static final String TEMP_FILE_PATH = "tempTestFile.csv";

    // Method to create a list of sample movies for testing
    private static List<Movie> createMovieList() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Movie1", 2021, new String[]{"Action"}, 120, 8.5, "Writer1", "Summary1"));
        movies.add(new Movie("Movie2", 2022, new String[]{"Drama"}, 110, 7.8, "Writer2", "Summary2"));
        return movies;
    }

    // Method to create an empty set for seen movies
    private static Set<Movie> createSeenMoviesSet() {
        return new HashSet<>();
    }

    // Method to create a display callback function for testing purposes
    private static Function<Movie, Boolean> createDisplayCallback() {
        return movie -> true; // For testing purposes, always mark as seen
    }

    // Test case for generating a random movie
    @Test
    void generateRandomMovie() {
        List<Movie> movies = createMovieList();
        Set<Movie> seenMovies = createSeenMoviesSet();
        Function<Movie, Boolean> displayCallback = createDisplayCallback();

        // Call the utility method to generate a random movie
        MovieUtils.generateRandomMovie(movies, seenMovies, displayCallback);

        // Assert that the set of seen movies is not empty
        assertFalse(seenMovies.isEmpty());
    }

    // Test case for generating a random movie by genre
    @Test
    void generateRandomMovieByGenre() {
        List<Movie> movies = createMovieList();
        Set<Movie> seenMovies = createSeenMoviesSet();
        Function<Movie, Boolean> displayCallback = createDisplayCallback();

        // Call the utility method to generate a random movie by genre
        MovieUtils.generateRandomMovieByGenre(movies, new String[]{"Action"}, seenMovies, displayCallback);

        // Assert that the set of seen movies is not empty
        assertFalse(seenMovies.isEmpty());
    }

    // Test case for adding a movie to a file
    @Test
    void addMovieToFile() {
        // Create a sample movie
        Movie movie = new Movie("TestMovie", 2023, new String[]{"Comedy"}, 120, 9.0, "John Doe", "A funny movie");

        // Call the utility method to add the movie to the file
        MovieUtils.addMovieToFile(movie, TEMP_FILE_PATH);

        // Read the content of the temporary file
        List<String> fileContent = readFileContent();

        // Assert that the file content contains the movie details
        assertTrue(fileContent.contains(movie.toCSVString()));

        // Clean up: delete the temporary file
        deleteFile();
    }

    // Method to read the content of a file
    private List<String> readFileContent() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MovieUtilsTest.TEMP_FILE_PATH))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Method to delete a file
    private void deleteFile() {
        try {
            Files.deleteIfExists(Paths.get(MovieUtilsTest.TEMP_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test case for marking a movie as seen
    @Test
    void markAsSeen() {
        // Create a sample movie
        Movie movie = new Movie("Dummy Movie", 2022, new String[]{"Action"}, 120, 8.0, "Writer", "Summary");
        Set<Movie> seenMovies = createSeenMoviesSet();

        // Call the utility method to mark the movie as seen
        MovieUtils.markAsSeen(movie, seenMovies);

        // Assert that the movie is in the set of seen movies
        assertTrue(seenMovies.contains(movie));
    }
}
