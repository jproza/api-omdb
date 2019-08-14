package ar.com.examen.moviedb.resources;

import com.codahale.metrics.annotation.Timed;

import ar.com.examen.moviedb.db.RatingDAO;
import ar.com.examen.moviedb.model.Rating;
import ar.com.examen.moviedb.model.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ratings/add/{imdbID}/{rating}")
@Produces(MediaType.APPLICATION_JSON)
public class RatingAddResource {
	RatingDAO dao;

	public RatingAddResource(RatingDAO dao) {
		this.dao = dao;
	}

	@POST
	@Timed
	public Rating addRating(@Auth User user, @PathParam("imdbID") String imdbID, @PathParam("rating") int rating) {
		if (rating < 0 || rating > 10)
			throw new IllegalArgumentException("Invalid number for rating.");

		return dao.addRating(user, imdbID, rating);
	}
}
