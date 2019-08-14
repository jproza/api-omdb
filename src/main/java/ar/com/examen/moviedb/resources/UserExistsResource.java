package ar.com.examen.moviedb.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ar.com.examen.moviedb.api.responses.UserExists;
import ar.com.examen.moviedb.db.UserDAO;

@Path("/user/exists/{username}")
@Produces(MediaType.APPLICATION_JSON)

public class UserExistsResource {
	UserDAO dao;

	public UserExistsResource(UserDAO dao) {
		this.dao = dao;
	}

	@GET
	public UserExists doesUserExist(@PathParam("username") String username) {
		return dao.doesUserExist(username);
	}
}
