import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private int year;
    private String[] genres;
    private int runtime;
    private double rating;
    private String writers;
    private String summary;

    // Constructors
    public Movie() {
        // Default constructor (no-argument constructor)
    }

    public Movie(String title, int year, String[] genres, int runtime, double rating, String writers, String summary) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.runtime = runtime;
        this.rating = rating;
        this.writers = writers;
        this.summary = summary;
    }


    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }


    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    // Other methods can be added as needed


    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Year: " + year + "\n" +
                "Genres: " + String.join(", ", genres) + "\n" +
                "Runtime: " + runtime + " minutes\n" +
                "Rating: " + rating + "\n" +
                "Writers: " + writers + "\n" +
                "Summary: " + summary + "\n";
    }


    public String toCSVString() {
        // Format movie details as a CSV-formatted string
        String genresString = String.join("|", genres);
        return title + "," + year + "," + genresString + "," + runtime + "," + rating + "," + writers + "," + "\"" + summary + "\"";
    }

}

