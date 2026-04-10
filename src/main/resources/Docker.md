# DOSW-Library - Sistema de Gestión de Biblioteca



## Uso de PostgreSQL con Docker

1. **Levantar la base de datos con Docker:**

    En terminal
	 ```sh
	 docker run --name dosw-postgres -e POSTGRES_DB=dosw_library -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
	 ```

	 - Usuario: `postgres`
	 - Contraseña: `postgres`
	 - Base de datos: `dosw_library`
	 - Puerto: `5432`

2. **Verifica que el contenedor esté corriendo:**

	 ```sh
	 docker ps
	 ```

3. **Conexión desde Spring Boot:**

	 La configuración en yaml

	 ```yaml
	 spring:
		 datasource:
			 url: jdbc:postgresql://localhost:5432/dosw_library
			 username: postgres
			 password: postgres
			 driver-class-name: org.postgresql.Driver
		 jpa:
			 hibernate:
				 ddl-auto: update
			 show-sql: true
			 properties:
				 hibernate:
					 format_sql: true
			 database-platform: org.hibernate.dialect.PostgreSQLDialect
	 ```

4. **Inicia tu aplicación Spring Boot** y verifica que se conecte correctamente a la base de datos.

docker start dosw-postgres

5. Como acceder

docker exec -it dosw-postgres psql -U postgres -d dosw_library

\dt

\d books

SELECT * FROM books LIMIT 10;


6. Reiniciar docker

docker stop dosw-postgres
docker rm dosw-postgres





docker:
 
docker run -d --name sonarName -p 9000:9000 sonarqube  
 
mvn clean verify sonar:sonar -Dsonar.projectKey=bitacora -Dsonar.host.url=http://localhost:9000 -Dsonar.login=TOKEN
 
