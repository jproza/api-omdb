package ar.com.examen.moviedb.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import ar.com.examen.moviedb.db.MovieDAO;
import ar.com.examen.moviedb.model.Movie;
import ar.com.examen.moviedb.model.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;


@Path("/search/movies/{query}/{numResults}")
@Produces(MediaType.APPLICATION_JSON)

public class MovieSearchResource {
	MovieDAO dao;

	public MovieSearchResource(MovieDAO dao) {
		this.dao = dao;
	}

	@GET
	@Timed
	@CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
	public List<Movie> searchMovies(@PathParam("query") String query,
									@PathParam("numResults") int numResults,
									@Auth User user) {
		return dao.searchMovies(query, numResults);
	}
	
	
}
