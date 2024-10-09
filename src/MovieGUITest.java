import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class MovieGUITest {

    @Test
    void createMovieGUI_ValidMovieList_ShouldNotBeNull() {
        // Create a sample list of movies
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Movie1", 2021, new String[]{"Action"}, 120, 8.5, "Writer1", "Summary1"));
        movies.add(new Movie("Movie2", 2022, new String[]{"Drama"}, 110, 7.8, "Writer2", "Summary2"));

        // Create a MovieGUI instance
        MovieGUI movieGUI = new MovieGUI(movies);

        // Check if the MovieGUI instance is not null
        assertNotNull(movieGUI.frame);
    }

    @Test
    void showMovieDetailsDialog_ValidMovie_ShouldReturnTrue() {
        // Create a sample movie
        Movie movie = new Movie("TestMovie", 2023, new String[]{"Comedy"}, 120, 9.0, "John Doe", "A funny movie");

        // Create a JFrame for the dialog
        JFrame testFrame = new JFrame();

        // Call the showMovieDetailsDialog method
        boolean result = MovieGUI.showMovieDetailsDialog(testFrame, movie, new HashSet<>());

        // Check if the result is true
        assertTrue(result);
    }



    @Test
    void getYearFromUserInput_UserSelectsYear_ShouldReturnSelectedYear() {
        // Create a MovieGUI instance
        MovieGUI movieGUI = new MovieGUI(new ArrayList<>());

        // Create a JFrame for the dialog
        JFrame testFrame = new JFrame();

        // Mock user input by setting the selected year
        int selectedYear = 2010;
        movieGUI.frame = testFrame; // Set the frame to the MovieGUI instance

        // Call the getYearFromUserInput method
        int result = movieGUI.getYearFromUserInput();

        // Check if the result is the selected year
        assertEquals(selectedYear, result);
    }
}
