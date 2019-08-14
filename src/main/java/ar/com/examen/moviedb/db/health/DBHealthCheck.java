package ar.com.examen.moviedb.db.health;

import com.codahale.metrics.health.HealthCheck;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import ar.com.examen.moviedb.db.GraphConnection;


public class DBHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		OrientGraph graph = GraphConnection.getGraph();

		if (!GraphConnection.started()) return Result.unhealthy("Graph connection not initiated.");

		if (graph == null) return Result.unhealthy("Graph connection not initiated.");

		return Result.healthy();
	}
}
