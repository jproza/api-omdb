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

@Path("/ratings/ignore/{imdbID}")
@Produces(MediaType.APPLICATION_JSON)
public class RatingIgnoreResource {
	RatingDAO dao;

	public RatingIgnoreResource(RatingDAO dao) {
		this.dao = dao;
	}

	@POST
	@Timed
	public Rating ignoreMovie(@Auth User user, @PathParam("imdbID") String imdbID) {
		return dao.addRating(user, imdbID, 0);
	}
}
