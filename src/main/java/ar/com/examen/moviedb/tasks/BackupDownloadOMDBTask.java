package ar.com.examen.moviedb.tasks;

import com.google.common.collect.ImmutableMultimap;

import ar.com.examen.moviedb.MoviedbConfiguration;
import ar.com.examen.moviedb.util.Timestamper;
import io.dropwizard.servlets.tasks.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class BackupDownloadOMDBTask extends Task {
	private static Logger LOGGER = LoggerFactory.getLogger(BackupDownloadOMDBTask.class);

	public BackupDownloadOMDBTask() {
		super("download-omdb");
	}

	@Override
	public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
		File currentZip = new File(MoviedbConfiguration.getOmdbExportPath());

		//make the path to the specified directory if it doesn't exist
		currentZip.getParentFile().mkdirs();

		if (currentZip.exists()) {
			if (!currentZip.renameTo(new File(currentZip.getAbsolutePath() + "." + Timestamper.getTimestamp()))) {
				LOGGER.error("Unable to move existing OMDB export.");
			}
		}

		OutputStream outputStream = new FileOutputStream(currentZip);

		URL omdbDownloadURL = new URL(MoviedbConfiguration.getOMDBDownloadURL());
		URLConnection omdbConnection = omdbDownloadURL.openConnection();
		omdbConnection.setRequestProperty("Accept", "application/zip");
		InputStream input = omdbConnection.getInputStream();

		byte[] buffer = new byte[4096];
		int n = -1;

		while ((n = input.read(buffer)) != -1) {
			outputStream.write(buffer, 0, n);
		}
		outputStream.flush();
		outputStream.close();
	}
}
