package ar.com.examen.moviedb.resources;

import com.codahale.metrics.annotation.Timed;

import ar.com.examen.moviedb.db.UserDAO;
import ar.com.examen.moviedb.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/user/create/confirm/{email}/{confirmationKey}")
@Produces("application/json")
public class UserCreateConfirmResource {
	private UserDAO dao;

	public UserCreateConfirmResource(UserDAO dao) {
		this.dao = dao;
	}

	@GET
	@Timed
	public User confirmUserCreation(@PathParam("email") String email,
									@PathParam("confirmationKey") String confirmKey) {
		return dao.confirmUserCreation(email, confirmKey);
	}
}
