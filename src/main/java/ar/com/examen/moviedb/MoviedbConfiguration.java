package ar.com.examen.moviedb;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilderSpec;
import com.sendgrid.SendGrid;

import io.dropwizard.Configuration;

public class MoviedbConfiguration extends Configuration {


	@NotNull
	private static String accountCreationConfirmURL;
	@NotNull
	private static String passwordChangeConfirmURL;
	@NotNull
	private static String sendgridUsername;
	@NotNull
	private static String sendgridPassword;
	@NotNull
	private static String omdbAPIKey;
	@NotNull
	private static String omdbDownloadURL;
	@NotNull
	private static String orientConnectionString;
	@NotNull
	private static String orientUsername;
	@NotNull
	private static String orientPassword;
	@NotNull
	private static int dbPoolMin;
	@NotNull
	private static int dbPoolMax;
	@NotNull
	private static int maxRetries;
	@NotNull
	private static String omdbPosterURL;
	@NotNull
	private static String omdbExportPath;
	@NotNull
	private static String omdbMoviesFilename;
	@NotNull
	private static String omdbTomatoesFilename;
	@NotNull
	private CacheBuilderSpec authenticationCachePolicy;

	
	@JsonProperty("omdb-export-path")
	public static String getOmdbExportPath() {
		return omdbExportPath;
	}

	@JsonProperty("omdb-export-path")
	public void setOmdbExportPath(String omdbExportPath) {
		MoviedbConfiguration.omdbExportPath = omdbExportPath;
	}

	@JsonProperty("omdb-movies-filename")
	public static String getOmdbMoviesFilename() {
		return omdbMoviesFilename;
	}

	@JsonProperty("omdb-movies-filename")
	public void setOmdbMoviesFilename(String omdbMoviesFilename) {
		MoviedbConfiguration.omdbMoviesFilename = omdbMoviesFilename;
	}

	@JsonProperty("omdb-tomatoes-filename")
	public static String getOmdbTomatoesFilename() {
		return omdbTomatoesFilename;
	}

	@JsonProperty("omdb-tomatoes-filename")
	public void setOmdbTomatoesFilename(String omdbTomatoesFilename) {
		MoviedbConfiguration.omdbTomatoesFilename = omdbTomatoesFilename;
	}

	@JsonProperty("orient-username")
	public static String getOrientUsername() {
		return orientUsername;
	}

	@JsonProperty("orient-username")
	public void setOrientUsername(String orientUsername) {
		MoviedbConfiguration.orientUsername = orientUsername;
	}

	@JsonProperty("orient-password")
	public static String getOrientPassword() {
		return orientPassword;
	}

	@JsonProperty("orient-password")
	public void setOrientPassword(String orientPassword) {
		MoviedbConfiguration.orientPassword = orientPassword;
	}

	@JsonProperty("omdb-poster-url")
	public static String getOmdbPosterURL() {
		return omdbPosterURL.replace("%k", omdbAPIKey);
	}

	@JsonProperty("omdb-poster-url")
	public void setOmdbPosterURL(String omdbPosterURL) {
		MoviedbConfiguration.omdbPosterURL = omdbPosterURL;
	}

	@JsonProperty("max-db-retries")
	public static int getMaxRetries() {
		return maxRetries;
	}

	@JsonProperty("max-db-retries")
	public void setMaxRetries(int maxRetries) {
		MoviedbConfiguration.maxRetries = maxRetries;
	}

	@JsonProperty("db-pool-min")
	public static int getDbPoolMax() {
		return dbPoolMax;
	}

	@JsonProperty("db-pool-max")
	public void setDbPoolMax(int dbPoolMax) {
		MoviedbConfiguration.dbPoolMax = dbPoolMax;
	}

	@JsonProperty("db-pool-min")
	public static int getDbPoolMin() {
		return dbPoolMin;
	}

	@JsonProperty("db-pool-min")
	public void setDbPoolMin(int dbPoolMin) {
		MoviedbConfiguration.dbPoolMin = dbPoolMin;
	}

	@JsonProperty("orient-conn-string")
	public static String getOrientConnectionString() {
		return orientConnectionString;
	}

	@JsonProperty("orient-conn-string")
	public void setOrientConnectionString(String orientConnectionString) {
		MoviedbConfiguration.orientConnectionString = orientConnectionString;
	}

	@JsonProperty("password-change-confirm-url")
	public static String getPasswordChangeConfirmURL() {
		return passwordChangeConfirmURL;
	}

	@JsonProperty("password-change-confirm-url")
	public void setPasswordChangeConfirmURL(String passwordChangeConfirmURL) {
		MoviedbConfiguration.passwordChangeConfirmURL = passwordChangeConfirmURL;
	}

	@JsonProperty("sendgrid-username")
	public static String getSendgridUsername() {
		return sendgridUsername;
	}

	@JsonProperty("sendgrid-username")
	public void setSendgridUsername(String sendgridUsername) {
		MoviedbConfiguration.sendgridUsername = sendgridUsername;
	}

	@JsonProperty("sendgrid-password")
	public static String getSendgridPassword() {
		return sendgridPassword;
	}

	@JsonProperty("sendgrid-password")
	public void setSendgridPassword(String sendgridPassword) {
		MoviedbConfiguration.sendgridPassword = sendgridPassword;
	}

	@JsonProperty("account-creation-confirm-url")
	public static String getAccountCreationConfirmURL() {
		return accountCreationConfirmURL;
	}

	@JsonProperty("account-creation-confirm-url")
	public void setAccountCreationConfirmURL(String accountCreationConfirmURL) {
		MoviedbConfiguration.accountCreationConfirmURL = accountCreationConfirmURL;
	}

	@JsonProperty("omdb-download-url")
	public static String getOMDBDownloadURL() {
		return omdbDownloadURL;
	}

	@JsonProperty("omdb-download-url")
	public void setOMDBDownloadURL(String omdbDownloadURL) {
		MoviedbConfiguration.omdbDownloadURL = omdbDownloadURL;
	}

	public static SendGrid buildSendGrid() {
		return new SendGrid(sendgridUsername, sendgridPassword);
	}

	@JsonProperty("authenticationCachePolicy")
	public CacheBuilderSpec getAuthenticationCachePolicy() {
		return authenticationCachePolicy;
	}

	@JsonProperty("authenticationCachePolicy")
	public void setAuthenticationCachePolicy(CacheBuilderSpec authenticationCachePolicy) {
		this.authenticationCachePolicy = authenticationCachePolicy;
	}

	@JsonProperty("omdb-api-key")
	public void setOmdbAPIKey(String newKey) {
		omdbAPIKey = newKey;
	}
	
	
}
