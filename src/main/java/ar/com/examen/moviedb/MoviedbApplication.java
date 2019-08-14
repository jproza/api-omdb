package ar.com.examen.moviedb;

import java.security.NoSuchAlgorithmException;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.google.common.cache.CacheBuilder;

import ar.com.examen.moviedb.auth.HTTPAuthenticator;
import ar.com.examen.moviedb.auth.SaltedHasher;
import ar.com.examen.moviedb.db.GraphConnection;
import ar.com.examen.moviedb.db.MovieDAO;
import ar.com.examen.moviedb.db.RatingDAO;
import ar.com.examen.moviedb.db.UserDAO;
import ar.com.examen.moviedb.db.health.DBHealthCheck;
import ar.com.examen.moviedb.model.User;
import ar.com.examen.moviedb.resources.MovieOnlineSearchResource;
import ar.com.examen.moviedb.resources.MovieSearchResource;
import ar.com.examen.moviedb.resources.RatingAddResource;
import ar.com.examen.moviedb.resources.RatingGetResource;
import ar.com.examen.moviedb.resources.RatingIgnoreResource;
import ar.com.examen.moviedb.resources.UserChangePasswordResource;
import ar.com.examen.moviedb.resources.UserCreateConfirmResource;
import ar.com.examen.moviedb.resources.UserCreateResource;
import ar.com.examen.moviedb.resources.UserDeleteResource;
import ar.com.examen.moviedb.resources.UserExistsResource;
import ar.com.examen.moviedb.resources.UserForgotPasswordResource;
import ar.com.examen.moviedb.tasks.BackupDownloadOMDBTask;
import ar.com.examen.moviedb.tasks.DBUpdateTask;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MoviedbApplication extends Application<MoviedbConfiguration> {
	private static Logger log = LoggerFactory.getLogger(MoviedbApplication.class);

	public static void main(String[] args) throws Exception {
		new MoviedbApplication().run(args);
	}

	@Override
	public String getName() {
		return "omdb";
	}

	@Override
	public void initialize(final Bootstrap<MoviedbConfiguration> bootstrap) {

	}

	@Override
	public void run(MoviedbConfiguration config, Environment environment) {
		try {
			SaltedHasher.setSecretKeyFactory();
		} catch (NoSuchAlgorithmException nsae) {
			log.error("Default algorithms not found. Exiting.");
			throw new RuntimeException("Exiting -- algorithm unavailable.");
		}

		environment.lifecycle().manage(new GraphConnection());
		UserDAO userDAO = new UserDAO();

		environment.jersey().register(new UserCreateResource(userDAO));

		environment.jersey().register(new UserCreateConfirmResource(userDAO));

		environment.jersey().register(new UserDeleteResource(userDAO));

		environment.jersey().register(new UserForgotPasswordResource(userDAO));

		environment.jersey().register(new UserChangePasswordResource(userDAO));

		environment.jersey().register(new UserExistsResource(userDAO));

		
		MovieDAO movieDAO = new MovieDAO();
		environment.jersey().register(new MovieSearchResource(movieDAO));

		
		RatingDAO ratingDAO = new RatingDAO();

		environment.jersey().register(new RatingAddResource(ratingDAO));

		environment.jersey().register(new RatingGetResource(ratingDAO));

		environment.jersey().register(new RatingIgnoreResource(ratingDAO));

		
		
		MetricRegistry metricRegistry = new MetricRegistry();
		HTTPAuthenticator httpAuthenticator = new HTTPAuthenticator(new UserDAO());
		CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.from(config.getAuthenticationCachePolicy());

		BasicAuthFactory<User> authFactory = new BasicAuthFactory<>(
				new CachingAuthenticator<>(metricRegistry, httpAuthenticator, cacheBuilder),
				"omdb", User.class);

		environment.jersey().register(AuthFactory.binder(authFactory));

		
		environment.healthChecks().register("database", new DBHealthCheck());

		
		environment.admin().addTask(new DBUpdateTask());

		environment.admin().addTask(new BackupDownloadOMDBTask());
		
		
		final Client client = new JerseyClientBuilder().build();
	    environment.jersey().register(new MovieOnlineSearchResource(client));

	    
	    
	}
}
