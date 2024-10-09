import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MovieGUI {

    // JFrame for the GUI
    public JFrame frame;

    // List of movies
    private final List<Movie> movies;

    // Set to store seen movies
    private  Set<Movie> seenMovies = new HashSet<>();

    // Constructor
    public MovieGUI(List<Movie> movies) {
        this.movies = movies;
        this.seenMovies = MovieUtils.loadSeenMovies(); // Load previously seen movies
        initialize();
    }

    // Method to initialize the GUI
    private void initialize() {
        // Create the main JFrame
        frame = new JFrame();
        frame.setTitle("Movie Recommendation");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(4, 1, 10, 10));

        // Create buttons with styles
        JButton randomMovieButton = createStyledButton("Generate Random Movie", "icons/random.png", Color.decode("#3498db"));
        JButton topMoviesButton = createStyledButton("Top Rated Movies by Year", "icons/toprated.png", Color.decode("#3498db"));
        JButton randomMovieByGenreButton = createStyledButton("Random Movie by Genre", "icons/random.png", Color.decode("#3498db"));
        JButton searchButton = createStyledButton("Search Movies", "icons/search.png", Color.decode("#3498db"));
        JButton addMovieButton = createStyledButton("Add Movie", "icons/add.png", Color.decode("#2ecc71")); // Green color
        JButton viewSeenMoviesButton = createStyledButton("View Seen Movies", "icons/eye.png", Color.decode("#2ecc71")); // Green color
        JButton exitButton = createStyledButton("Exit", "icons/exit.png", Color.decode("#e74c3c")); // Red color

        // Add buttons to the content pane
        frame.getContentPane().add(randomMovieButton);
        frame.getContentPane().add(topMoviesButton);
        frame.getContentPane().add(randomMovieByGenreButton);
        frame.getContentPane().add(searchButton);
        frame.getContentPane().add(addMovieButton);
        frame.getContentPane().add(viewSeenMoviesButton);
        frame.getContentPane().add(exitButton);

        // Add action listeners to buttons

        // Add Movie button
        addMovieButton.addActionListener(e -> {
            // Call the method to add a movie
            addMovie();
        });

        // View Seen Movies button
        viewSeenMoviesButton.addActionListener(e -> {
            // Load seen movies from the saved file
            Set<Movie> seenMovies = MovieUtils.loadSeenMovies();

            if (!seenMovies.isEmpty()) {
                displayResults(new ArrayList<>(seenMovies), "Seen Movies");
            } else {
                JOptionPane.showMessageDialog(frame, "No movies marked as seen.", "Seen Movies", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Generate Random Movie button
        randomMovieButton.addActionListener(e -> {
            // Inside the ActionListener for randomMovieButton
            MovieUtils.generateRandomMovie(movies, seenMovies, movie -> {
                boolean markAsSeen = MovieGUI.showMovieDetailsDialog(frame, movie, seenMovies);

                if (markAsSeen) {
                    seenMovies.add(movie);
                    MovieUtils.saveSeenMovies(seenMovies);
                }

                return markAsSeen;
            });
        });

        // Random Movie by Genre button
        randomMovieByGenreButton.addActionListener(e -> {
            // Ask the user to select genres
            String[] allGenres = getAllGenresFromMovies(movies);
            String[] selectedGenres = askUserToSelectGenres(allGenres);

            // Generate a random movie by year and genre
            MovieUtils.generateRandomMovieByGenre(movies, selectedGenres, seenMovies, movie -> MovieGUI.showMovieDetailsDialog(frame, movie, seenMovies));
        });

        // Top Rated Movies by Year button
        topMoviesButton.addActionListener(e -> {
            // Implement logic for displaying top 10 movies by year
            int year = getYearFromUserInput();
            List<Movie> topMovies = MovieUtils.getTopRatedMoviesByYear(movies, year);
            displayResults(topMovies, "Top Rated Movies by Year");
        });

        // Search Movies button
        searchButton.addActionListener(e -> {
            // Display a dialog for the user to choose the search option
            String[] options = {"Search by Title", "Search by Year"};
            int choice = JOptionPane.showOptionDialog(
                    frame,
                    "Choose a search option:",
                    "Search Movies",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            // Initialize the searchResults list
            List<Movie> searchResults;

            switch (choice) {
                case 0:
                    // Search by Title
                    String titleInput = JOptionPane.showInputDialog(frame, "Enter movie title:");
                    searchResults = MovieUtils.searchByTitle(movies, titleInput);
                    displayResults(searchResults, "Search Results");
                    break;
                case 1:
                    // Search by Year
                    String yearInput = JOptionPane.showInputDialog(frame, "Enter movie year:");

                    // Check if the user pressed the 'X' button or canceled the input
                    if (yearInput == null) {
                        // User closed the dialog or clicked Cancel
                        return;
                    }

                    try {
                        int year = Integer.parseInt(yearInput);
                        searchResults = MovieUtils.searchByYear(movies, year);
                        displayResults(searchResults, "Search Results");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid year input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                default:
                    // User closed the dialog or clicked outside the options
                    break;
            }
        });

        // Exit button
        exitButton.addActionListener(e -> System.exit(0));

        // Make the frame visible
        frame.setVisible(true);
    }

    // Method to create a styled button
    private JButton createStyledButton(String text, String iconPath, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));

        // Set icon
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            button.setIcon(icon);
        } catch (Exception e) {
            // Print the error message to the standard error stream
            System.err.println("Error setting button icon: " + e.getMessage());
        }

        return button;
    }

    // Method to show movie details dialog
    public static boolean showMovieDetailsDialog(JFrame frame, Movie movie, Set<Movie> seenMovies) {
        JPanel panel = new JPanel(new BorderLayout());

        // Movie details text area with limited size
        JTextArea textArea = new JTextArea(movie.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);  // Enable line wrapping
        textArea.setWrapStyleWord(true);  // Wrap at word boundaries
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Set a fixed size

        // Checkbox for marking the movie as seen
        JCheckBox checkBox = new JCheckBox("Mark this movie as seen");

        // Add components to the panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(checkBox, BorderLayout.SOUTH);

        // Show the dialog
        int choice = JOptionPane.showConfirmDialog(
                frame,
                panel,
                "Movie Details",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (choice == JOptionPane.OK_OPTION && checkBox.isSelected()) {
            MovieUtils.markAsSeen(movie, seenMovies);
        }

        // Return whether the checkbox was selected
        return choice == JOptionPane.OK_OPTION && checkBox.isSelected();
    }


    // Method to display search results
    public void displayResults(List<Movie> results, String title) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);  // Enable line wrapping
        textArea.setWrapStyleWord(true);  // Wrap at word boundaries

        if (results.isEmpty()) {
            textArea.append("No matching movies found.");
        } else {
            for (Movie movie : results) {
                textArea.append(movie.toString() + "\n\n");
            }
        }

        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(frame, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to get the year from user input
    public int getYearFromUserInput() {
        // Create a list of years from 2000 to 2018
        Integer[] yearsArray = new Integer[19]; // 2018 - 2000 + 1
        for (int i = 0; i < 19; i++) {
            yearsArray[i] = 2000 + i;
        }

        // Display a dialog with a dropdown menu for selecting the year
        JComboBox<Integer> yearComboBox = new JComboBox<>(yearsArray);
        JPanel yearPanel = new JPanel();
        yearPanel.add(new JLabel("Select movie year:"));
        yearPanel.add(yearComboBox);

        int result = JOptionPane.showConfirmDialog(frame, yearPanel, "Select Year", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // User clicked OK, return the selected year
            return (Integer) yearComboBox.getSelectedItem();
        } else {
            // User clicked Cancel or closed the dialog
            return -1; // You can choose a default value or handle this case accordingly
        }
    }

    // Method to show the GUI
    public void show() {
        frame.setVisible(true);
    }

    // Method to get all genres from movies
    private String[] getAllGenresFromMovies(List<Movie> movies) {
        // Extract all unique genres from the list of movies
        Set<String> allGenres = new HashSet<>();

        for (Movie movie : movies) {
            allGenres.addAll(Arrays.asList(movie.getGenres()));
        }

        return allGenres.toArray(new String[0]);
    }

    // Method to ask the user to select genres
    private String[] askUserToSelectGenres(String[] allGenres) {
        // Display a dialog for the user to choose genres
        JComboBox<String> genreComboBox = new JComboBox<>(allGenres);
        JPanel genrePanel = new JPanel();
        genrePanel.add(new JLabel("Select genre(s):"));
        genrePanel.add(genreComboBox);

        int result = JOptionPane.showConfirmDialog(frame, genrePanel, "Select Genre", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // User clicked OK, return selected genres
            Object[] selectedObjects = genreComboBox.getSelectedObjects();

            if (selectedObjects != null) {
                String[] selectedGenres = new String[selectedObjects.length];
                for (int i = 0; i < selectedObjects.length; i++) {
                    selectedGenres[i] = (String) selectedObjects[i];
                }
                return selectedGenres;
            } else {
                return new String[0];
            }
        } else {
            // User clicked Cancel or closed the dialog
            return new String[0];
        }
    }

    // Method to add a movie
    private void addMovie() {
        // Ask the user to enter movie details
        String title = JOptionPane.showInputDialog(frame, "Enter movie title:");
        int year = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter the Year:"));
        String[] genres = askUserToSelectGenres(getAllGenresFromMovies(movies)); // Use the updated function
        int runtime = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter movie runtime:"));
        double rating = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter movie rating:"));
        String writers = JOptionPane.showInputDialog(frame, "Enter movie writers:");
        String summary = JOptionPane.showInputDialog(frame, "Enter movie summary:");

        // Create a new Movie object
        Movie newMovie = new Movie(title, year, genres, runtime, rating, writers, summary);

        // Add the movie to the CSV file
        MovieUtils.addMovieToFile(newMovie, "moviesData.csv");

        JOptionPane.showMessageDialog(frame, "Movie added successfully!", "Add Movie", JOptionPane.INFORMATION_MESSAGE);
    }
}
