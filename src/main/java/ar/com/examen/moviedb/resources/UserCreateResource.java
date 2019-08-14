package ar.com.examen.moviedb.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

import ar.com.examen.moviedb.db.UserDAO;
import ar.com.examen.moviedb.model.User;
import ar.com.examen.moviedb.util.StringRandomizer;

@Path("/user/create/new/{email}/{password}")
@Produces(MediaType.APPLICATION_JSON)
public class UserCreateResource {
	private static Logger log = LoggerFactory.getLogger(UserCreateResource.class);

	
	private UserDAO dao;
	private StringRandomizer randomizer;


	public UserCreateResource(UserDAO dao) {
		this.dao = dao;
		randomizer = new StringRandomizer(30);
	}


	@POST
	@Timed
	public Optional<User> createUser(@PathParam("email") String email,
									 @PathParam("password") String password) {

		if (password.length() < 6) throw new IllegalArgumentException("Password too short.");

		String confirmationKey = randomizer.nextString();

		Optional<User> createdUser = dao.createUser(email, password, confirmationKey);
		
		System.out.println( email + " - " + confirmationKey);
		log.info(email + " - " + confirmationKey);	
		
		return createdUser;
	}
}
