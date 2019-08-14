package ar.com.examen.moviedb.resources;

import com.codahale.metrics.annotation.Timed;

import ar.com.examen.moviedb.db.UserDAO;
import ar.com.examen.moviedb.model.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/user/delete/")
@Produces(MediaType.APPLICATION_JSON)
public class UserDeleteResource {
	UserDAO dao;

	public UserDeleteResource(UserDAO dao) {
		this.dao = dao;
	}

	@POST
	@Timed
	public boolean deleteUser(@Auth User user) {
		return dao.deleteUser(user);
	}
}
