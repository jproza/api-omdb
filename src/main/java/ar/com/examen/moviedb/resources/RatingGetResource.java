package ar.com.examen.moviedb.resources;

import com.codahale.metrics.annotation.Timed;

import ar.com.examen.moviedb.db.RatingDAO;
import ar.com.examen.moviedb.model.Rating;
import ar.com.examen.moviedb.model.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/ratings/")
@Produces(MediaType.APPLICATION_JSON)
public class RatingGetResource {
	RatingDAO dao;

	public RatingGetResource(RatingDAO dao) {
		this.dao = dao;
	}

	@GET
	@Timed
	public List<Rating> getRatings(@Auth User user) {
		return dao.getRatings(user);
	}
}
