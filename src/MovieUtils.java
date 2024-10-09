import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.HashSet;
import java.util.Set;


import java.util.function.Function;

public class MovieUtils {

    //private static final String SEEN_MOVIES_FILE = "seenMovies.ser";
    private static Set<Movie> seenMovies = new HashSet<>();



    public static void markAsSeen(Movie movie, Set<Movie> seenMovies) {
        seenMovies.add(movie);
        saveSeenMovies(seenMovies);
    }


    public static void saveSeenMovies(Set<Movie> seenMovies) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("seenMovies.ser"))) {
            oos.writeObject(seenMovies);
        } catch (IOException e) {
            // Handle exceptions (e.g., file not found, permission issues, etc.)
            e.printStackTrace();
        }
    }


    public static Set<Movie> loadSeenMovies() {
        Set<Movie> seenMovies = new HashSet<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("seenMovies.ser"))) {
            seenMovies = (Set<Movie>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Handle the case where the file is not found, create the file if needed
            saveSeenMovies(seenMovies);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return seenMovies;
    }

    public static void generateRandomMovie(List<Movie> movies, Set<Movie> seenMovies, Function<Movie, Boolean> displayCallback) {
        Random random = new Random();
        Movie randomMovie = null;

        while (randomMovie == null) {
            int randomIndex = random.nextInt(movies.size());
            Movie selectedMovie = movies.get(randomIndex);

            boolean markAsSeen = displayCallback.apply(selectedMovie);

            if (markAsSeen) {
                seenMovies.add(selectedMovie);

            }

            randomMovie = selectedMovie;
        }

        saveSeenMovies(seenMovies); // Save seen movies after adding a new one
    }

    // Function to convert a string to lowercase
    private static String toLowerCase(String str) {
        return str.toLowerCase();
    }

    // Function to search for movies by title
    public static List<Movie> searchByTitle(List<Movie> movieList, String title) {
        String lowerUserInput = toLowerCase(title);
        List<Movie> searchResults = new ArrayList<>();

        for (Movie movie : movieList) {
            String lowerMovieTitle = toLowerCase(movie.getTitle());
            if (lowerMovieTitle.contains(lowerUserInput)) {
                searchResults.add(movie);
            }
        }

        if (searchResults.isEmpty()) {
            System.out.println("Movie Not Found!");
        }

        return searchResults;
    }

    // Function to search for movies by year
    public static List<Movie> searchByYear(List<Movie> movieList, int year) {
        List<Movie> searchResults = new ArrayList<>();

        for (Movie movie : movieList) {
            if (movie.getYear() == year) {
                searchResults.add(movie);
            }
        }

        return searchResults;
    }



    public static List<Movie> getTopRatedMoviesByYear(List<Movie> movies, int year) {
        // List to store the top-rated movies
        List<Movie> topRatedMovies = new ArrayList<>();

        // Iterate through the list of movies
        for (Movie movie : movies) {
            if (movie.getYear() == year) {
                // Check if the movie has a higher rating than the lowest rated movie in the list
                if (topRatedMovies.size() < 10 || movie.getRating() > topRatedMovies.get(topRatedMovies.size() - 1).getRating()) {
                    topRatedMovies.add(movie);

                    // Sort the movies based on rating in descending order
                    topRatedMovies.sort(Comparator.comparingDouble(Movie::getRating).reversed());

                    // Ensure the list contains only the top 10 movies
                    if (topRatedMovies.size() > 10) {
                        topRatedMovies.remove(topRatedMovies.size() - 1);
                    }
                }
            }
        }

        return topRatedMovies;
    }




    public static void generateRandomMovieByGenre(List<Movie> movies, String[] selectedGenres, Set<Movie> seenMovies, Function<Movie, Boolean> displayCallback) {
        // Filter movies by year and genre
        List<Movie> matchingMovies = new ArrayList<>();

        for (Movie movie : movies) {
            if (movieContainsGenres(movie, selectedGenres)) {
                matchingMovies.add(movie);
            }
        }

        // Check if there are matching movies
        if (!matchingMovies.isEmpty()) {
            // Return a random movie from the matching list
            Random random = new Random();
            int randomIndex = random.nextInt(matchingMovies.size());
            Movie selectedMovie = matchingMovies.get(randomIndex);

            boolean markAsSeen = displayCallback.apply(selectedMovie);

            if (markAsSeen) {
                seenMovies.add(selectedMovie);
            }
        }

        saveSeenMovies(seenMovies); // Save seen movies after adding a new one

    }

    private static boolean movieContainsGenres(Movie movie, String[] genres) {
        // Check if the movie contains any of the specified genres
        for (String genre : genres) {
            for (String movieGenre : movie.getGenres()) {
                if (movieGenre.equals(genre)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void addMovieToFile(Movie movie, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Append the movie details to the file
            writer.write(movie.toCSVString());
            writer.newLine(); // Move to the next line for the next movie
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

