import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieReaderTest {

    private static final String VALID_CSV_PATH = "moviesData.csv";
    private static final String INVALID_CSV_PATH = "invalid_movies.csv"; // Contains incomplete data


    @Test
    void readMoviesFromCSV_validFile_shouldReturnListOfMovies() {
        try {
            List<Movie> movies = MovieReader.readMoviesFromCSV(VALID_CSV_PATH);

            assertNotNull(movies);
            assertFalse(movies.isEmpty());

            // Check if the movie "Hacksaw Ridge" is present in the list
            Movie hacksawRidge = new Movie("Hacksaw Ridge", 2016,
                    new String[]{"Action", "Biography", "Drama", "History", "War"},
                    139, 8.2, "Robert Schenkkan",
                    "The true story of Desmond T. Doss, the conscientious objector who, at the Battle of Okinawa, won the Medal of Honor for his incredible bravery and regard for his fellow soldiers. We see his upbringing and how this shaped his views, especially his religious view and anti-killing stance. We see Doss's trials and tribulations after enlisting in the US Army and trying to become a medic. Finally, we see the hell on Earth that was Hacksaw Ridge.");

            assertTrue(movies.stream().anyMatch(m -> m.getTitle().equals(hacksawRidge.getTitle())));
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }


    @Test
    void readMoviesFromCSV_invalidFile_shouldThrowIOException() {
        // Verify that reading from an invalid file throws IOException
        assertThrows(IOException.class, () -> MovieReader.readMoviesFromCSV(INVALID_CSV_PATH));
    }

    @Test
    void readMoviesFromCSV_nonexistentFile_shouldThrowIOException() {
        // Verify that reading from a nonexistent file throws IOException
        assertThrows(IOException.class, () -> MovieReader.readMoviesFromCSV("nonexistent_file.csv"));
    }

    // Add more test cases to cover various scenarios:
    // - CSV file with empty content
    // - CSV file with incorrect data format
    // - ...

}
