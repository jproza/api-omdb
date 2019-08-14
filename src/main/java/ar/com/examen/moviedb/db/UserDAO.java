package ar.com.examen.moviedb.db;

import com.google.common.base.Optional;
import com.orientechnologies.orient.core.exception.OTransactionException;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import ar.com.examen.moviedb.MoviedbConfiguration;
import ar.com.examen.moviedb.api.responses.UserExists;
import ar.com.examen.moviedb.auth.SaltedHasher;
import ar.com.examen.moviedb.db.enums.Indexes;
import ar.com.examen.moviedb.db.enums.UserProps;
import ar.com.examen.moviedb.db.enums.Vertices;
import ar.com.examen.moviedb.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class UserDAO {
	private static final Logger log = LoggerFactory.getLogger(UserDAO.class);
	private SecureRandom random;


	public UserDAO() {
		random = new SecureRandom();
	}

	public Optional<User> createUser(String email, String password, String confirmationKey) {
		OrientGraph graph = GraphConnection.getGraph();
		Optional<User> returned = Optional.absent();

		for (int i = 0; i < MoviedbConfiguration.getMaxRetries(); i++) {
			try {
				Vertex userVertex = graph.getVertexByKey(Indexes.USER_USERNAME, email);
				if (userVertex != null) break;

				byte[] salt = new byte[16];
				random.nextBytes(salt);

				SaltedHasher hasher = new SaltedHasher(password, salt);

				userVertex = graph.addVertex("class:" + Vertices.USER);

				userVertex.setProperty(UserProps.USERNAME, email);
				userVertex.setProperty(UserProps.EMAIL, email);
				userVertex.setProperty(UserProps.SALT, hasher.getSalt());
				userVertex.setProperty(UserProps.HASH, hasher.getHash());

				userVertex.setProperty(UserProps.EMAIL_CONFIRMED, false);
				userVertex.setProperty(UserProps.EMAIL_CONF_KEY, confirmationKey);

				long timestamp = System.currentTimeMillis();
				userVertex.setProperty(UserProps.RAT_UPDATED, timestamp);
				userVertex.setProperty(UserProps.REC_UPDATED, timestamp);

				User user = buildUser(userVertex);
				returned = Optional.of(user);
				graph.commit();

				log.info("Created unconfirmed account  " + email);
				break;
			} catch (OTransactionException ote) {
			}
		}
		graph.shutdown();

		return returned;
	}

	public boolean deleteUser(User user) {
		OrientGraph graph = GraphConnection.getGraph();
		boolean deleted = false;

		for (int i = 0; i < MoviedbConfiguration.getMaxRetries(); i++) {
			try {
				Vertex userVertex = graph.getVertexByKey(Indexes.USER_USERNAME, user.getUsername());
				if (userVertex != null) {
					graph.removeVertex(userVertex);
					deleted = true;
				}

				graph.commit();
				log.info("Successfully deleted account " + user.getUsername());
				break;
			} catch (OTransactionException ote) {
			}
		}

		graph.shutdown();
		return deleted;
	}

	public User getUser(String username) {
		OrientGraph graph = GraphConnection.getGraph();
		User returnedUser = null;
		try {
			Vertex userVertex = graph.getVertexByKey(Indexes.USER_USERNAME, username);
			if (userVertex != null) {
				log.debug("Found account for " + username);
				boolean confirmed = userVertex.getProperty(UserProps.EMAIL_CONFIRMED);

				if (confirmed) {
					log.debug("Account is confirmed by email for " + username);
					returnedUser = buildUser(userVertex);
				}
			}

		} finally {
			graph.shutdown();
		}
		return returnedUser;
	}

	public User confirmUserCreation(String email, String confirmKey) {
		OrientGraph graph = GraphConnection.getGraph();
		User confirmedUser = null;

		for (int i = 0; i < MoviedbConfiguration.getMaxRetries(); i++) {
			try {
				Vertex userVertex = graph.getVertexByKey(Indexes.USER_USERNAME, email);
				String storedConfirmKey = userVertex.getProperty(UserProps.EMAIL_CONF_KEY);

				
				if (storedConfirmKey.equals(confirmKey)) {
					userVertex.setProperty(UserProps.EMAIL_CONFIRMED, true);
				}

				graph.commit();

				confirmedUser = buildUser(userVertex);

				log.info("Successfully confirmed user account for " + email);
				break;
			} catch (OTransactionException ote) {
			}
		}

		graph.shutdown();
		return confirmedUser;
	}

	public User forgotPassword(String email, String confirmationKey) {
		OrientGraph graph = GraphConnection.getGraph();
		User user = null;
		try {
			Vertex userVertex = graph.getVertexByKey(Indexes.USER_USERNAME, email);

			userVertex.setProperty(UserProps.PASS_CHANGE_KEY, confirmationKey);

			graph.commit();
			user = buildUser(userVertex);
		} finally {
			graph.shutdown();
		}

		return user;
	}

	public User confirmPasswordChange(String email, String newPassword, String confirmKey) {
		OrientGraph graph = GraphConnection.getGraph();
		User user = null;

		for (int i = 0; i < MoviedbConfiguration.getMaxRetries(); i++) {
			try {

				Vertex userVertex = graph.getVertexByKey(Indexes.USER_USERNAME, email);
				if (userVertex != null) {
					String storedConfirmKey = userVertex.getProperty(UserProps.PASS_CHANGE_KEY);

					if (storedConfirmKey.equals(confirmKey)) {

						byte[] salt = userVertex.getProperty(UserProps.SALT);
						SaltedHasher newHasher = new SaltedHasher(newPassword, salt);
						userVertex.setProperty(UserProps.HASH, newHasher.getHash());

						user = buildUser(userVertex);
					}
				}
				graph.commit();
				break;

			} catch (OTransactionException ote) {
			}
		}
		graph.shutdown();
		return user;
	}

	public User buildUser(Vertex userVertex) {
		OrientGraph graph = GraphConnection.getGraph();
		User built = null;
		try {
			Vertex newVertex = graph.getVertex(userVertex.getId());

			String unameFromDB = newVertex.getProperty(UserProps.USERNAME);
			byte[] saltFromDB = newVertex.getProperty(UserProps.SALT);
			byte[] hashFromDB = newVertex.getProperty(UserProps.HASH);

			built = new User(unameFromDB, saltFromDB, hashFromDB);
			built.setEmailConfirmed(userVertex.getProperty(UserProps.EMAIL_CONFIRMED));

		} finally {
			graph.shutdown();
		}
		return built;
	}

	public UserExists doesUserExist(String username) {
		OrientGraph graph = GraphConnection.getGraph();
		UserExists exists = new UserExists();
		exists.setUsername(username);
		try {
			Vertex userVertex = graph.getVertexByKey(Indexes.USER_USERNAME, username);

			if (userVertex != null) exists.setExists(true);

		} finally {
			graph.shutdown();
		}
		return exists;
	}
}
