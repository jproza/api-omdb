Javier Nicolas Prozapas
=======================

MovieDb, proyecto que puede realizar las siguientes operaciones en conjunto con el API OMDB:
*Exportacion masiva  de peliculas con datos extraidos desde omdb movies.
*Consulta tanto online como offline de peliculas por nombre y diferentes caracteristicas.
*Persistencia de peliculas en orientdb.

MovieDb api backend esta construido con java 1.8 y los siguientes frameworks:

* [Dropwizard](http://www.dropwizard.io/)
* [OrientDB](http://orientdb.com/docs/last/)


##Construyendo el ambiente


1. Clone el repositorio de github : 
2. Editar config.yml y completar los campos con clave valor.
  * omdb-api-key: la api key del proyecto OMDB API.
  * omd-download-url: La url de donde bajar el listado de peliculas.
 
3. Asegurar que el puerto  8081 este habilitado en el firewall. 
4. Ejecutar la clase MovieDbApplication con los parametros: MovieDbApplication server y config.json.

##Como usar MovieDB api:

###Ejecutar tareas(task) endpoints - modo administrador

Para todos estos endspoints funcionan en el puerto 8081

* POST  /tasks/download-omdb : hace un bulk inserts de peliculas en la base de datos orientdb.

* POST  /tasks/update-db : actualiza la base de peliculas.

###Modo Cliente HTTP endpoints

Todos los siguientes end-points funcionan en el puerto 8080. 

####Como crear una cuenta nueva (usuario/clave)

Sin autenticacion requerida.

1. POST  /user/create/new/{email}/{password}
2. GET  /user/create/confirm/{email}/{confirmationKey}
  * confirmationKey: recibido en el console.log.
  
####Los demas endponints

Autenticacion requerida:

GET /search/online/movies/{i}  -> http://localhost:8080/search/online/movies/terminator
http://localhost:8080/search/online/movies/Bella

GET  /search/movies/{query}/{numResults} -> localhost:8080/search/movies/Back to the future/1

GET  /ratings/

POST  /ratings/add/{imdbID}/{rating}  -->localhost:8080/ratings/add/tt5817333/9

GET  /recommendations/{numReturned}

POST  /user/password/forgot/{email}/

POST  /user/password/change/{email}/{newPassword}/{confirmKey}

GET  /user/exists/{username}

POST  /user/delete/
