package ar.com.examen.moviedb.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class Rating {
	@NotNull
	private Movie movie;

	@NotNull
	private int rating;

	public Rating(Movie movie, int rating) {
		this.movie = movie;
		this.rating = rating;
	}

	@JsonProperty
	public Movie getMovie() {
		return movie;
	}

	@JsonProperty
	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@JsonProperty
	public int getRating() {
		return rating;
	}

	@JsonProperty
	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Rating) {
			return movie.equals(((Rating) other).getMovie());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return movie.hashCode();
	}
}
