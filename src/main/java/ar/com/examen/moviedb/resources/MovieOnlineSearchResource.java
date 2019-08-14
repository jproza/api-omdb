package ar.com.examen.moviedb.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;

import ar.com.examen.moviedb.MoviedbConfiguration;
import ar.com.examen.moviedb.model.Movie;
import ar.com.examen.moviedb.model.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/search/online/movies/{i}")
@Produces(MediaType.APPLICATION_JSON)
public class MovieOnlineSearchResource {


		private Client client;

		public MovieOnlineSearchResource(Client client) {
			this.client = client;
		}

		@GET
		@Timed
		@CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
		public String searchOnlineMovies(@PathParam("i") String i, @Auth User user) {

			String url = MoviedbConfiguration.getOmdbPosterURL();
			String apikey = "863202ee";

			//url.replace("&apikey=%k", "&apikey="+apikey);
			
			WebTarget webTarget = client.target(url.replace("%i", i));
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
	        Response response = invocationBuilder.get();
	        @SuppressWarnings("rawtypes")
	        String employees = response.readEntity(String.class);
	        return employees.toString();
			
		}

	}