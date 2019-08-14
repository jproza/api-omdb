package ar.com.examen.moviedb.db;

import com.orientechnologies.orient.core.exception.OTransactionException;
import com.orientechnologies.orient.core.intent.OIntentMassiveRead;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import ar.com.examen.moviedb.MoviedbConfiguration;
import ar.com.examen.moviedb.db.enums.Edges;
import ar.com.examen.moviedb.db.enums.Indexes;
import ar.com.examen.moviedb.db.enums.Vertices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GraphCleaner {
	private Logger LOGGER = LoggerFactory.getLogger(GraphCleaner.class);
	private String currentUpdateTimestamp;
	private int maxRetries;

	public GraphCleaner(String currentUpdateTimestamp) {
		this.currentUpdateTimestamp = currentUpdateTimestamp;
		this.maxRetries = MoviedbConfiguration.getMaxRetries();
	}

	public void cleanGraph() {
		OrientGraph graph = GraphConnection.getGraph();
		graph.declareIntent(new OIntentMassiveRead());

        Vertex naVertex = graph.getVertexByKey(Indexes.PERSON_NAME, "null");
        if (naVertex != null) graph.removeVertex(naVertex);

		List<Vertex> outOfDate = new ArrayList<>();

		StreamSupport.stream(graph.getVerticesOfClass(Vertices.MOVIE).spliterator(), false)
				.filter(v -> !v.getProperty(Vertices.UPDATED).equals(currentUpdateTimestamp))
				.forEach(outOfDate::add);

		StreamSupport.stream(graph.getVerticesOfClass(Vertices.PERSON).spliterator(), false)
				.filter(v -> !v.getProperty(Vertices.UPDATED).equals(currentUpdateTimestamp))
				.forEach(outOfDate::add);

		LOGGER.info("Deleting " + outOfDate.size() + " out of date vertices.");
		outOfDate.stream().forEach(graph::removeVertex);

		int numOrphanedMovies = 0;
		StreamSupport.stream(graph.getVerticesOfClass(Vertices.MOVIE).spliterator(), false)
				.filter(this::isOrphanedMovie)
				.forEach(graph::removeVertex);

		graph.commit();
		LOGGER.info("Deleted " + numOrphanedMovies + " orphan movies.");

		try {
			for (int i = 0; i < maxRetries; i++) {
				int numUnconnected = 0;

				Stream.concat(
						StreamSupport.stream(graph.getVerticesOfClass(Vertices.MOVIE).spliterator(), false),
						StreamSupport.stream(graph.getVerticesOfClass(Vertices.PERSON).spliterator(), false))
						.filter(v ->
								!v.getEdges(Direction.BOTH, Edges.DIRECTED, Edges.WROTE, Edges.ACTED).iterator().hasNext())
						.forEach(graph::removeVertex);

				graph.commit();
				LOGGER.info("Deleted " + numUnconnected + " movies.");
				break;
			}
		} catch (OTransactionException ote) {
			
		}

		
		String rebuildIndexes = "rebuild index *";
		graph.command(new OCommandSQL(rebuildIndexes)).execute();

		graph.shutdown();
	}

	public boolean isOrphanedMovie(Vertex vertex) {
		for (Vertex v : vertex.getVertices(Direction.IN)) {
			int others = 0;
			for (Vertex other : v.getVertices(Direction.OUT)) {
				others++;
				if (others > 1) return false;
			}
		}
		return true;
	}
}
