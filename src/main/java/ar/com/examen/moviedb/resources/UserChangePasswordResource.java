package ar.com.examen.moviedb.resources;

import com.codahale.metrics.annotation.Timed;

import ar.com.examen.moviedb.db.UserDAO;
import ar.com.examen.moviedb.model.User;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/user/password/change/{email}/{newPassword}/{confirmKey}")
@Produces("application/json")
public class UserChangePasswordResource {
	private UserDAO dao;

	public UserChangePasswordResource(UserDAO dao) {
		this.dao = dao;
	}

	@POST
	@Timed
	public User changePassword(@PathParam("email") String email,
							   @PathParam("newPassword") String newPassword,
							   @PathParam("confirmKey") String confirmKey) {

		return dao.confirmPasswordChange(email, newPassword, confirmKey);
	}
}
