package ar.com.examen.moviedb.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.codahale.metrics.annotation.Timed;

import ar.com.examen.moviedb.db.UserDAO;
import ar.com.examen.moviedb.util.StringRandomizer;

@Path("/user/password/forgot/{email}/")
public class UserForgotPasswordResource {
	private UserDAO dao;
	private StringRandomizer randomizer;
	public UserForgotPasswordResource(UserDAO dao) {
		randomizer = new StringRandomizer(30);
		this.dao = dao;
	}

	@POST
	@Timed
	public void forgotPassword(@PathParam("email") String email) {
		String confirmationKey = randomizer.nextString();
		dao.forgotPassword(email, confirmationKey);

		System.out.println(email + " - " + confirmationKey);
	}
}
