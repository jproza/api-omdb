package ar.com.examen.moviedb.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Timestamper {
	public static String getTimestamp() {
		return new SimpleDateFormat("yyyyMMdd.HHmmss").format(new Date());
	}
}
