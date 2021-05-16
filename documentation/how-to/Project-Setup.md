# Setup

Prerequisite (Using docker, this requirements are not needed): 
* NodeJS >=v11.0.0
* v11.0.0 <= Java <= v15.0.0

Follow the steps below:
 * Clone the git repository
 * `cd` into the directory

## Setup using docker
 * Create a production build using `mvn -DskipTests package` (We skip the tests as the database is setup using docker)
 * Build the docker image using `docker build -t timeguess-webapp .`
 * Start the container using `docker compose up --build webapp` (in this case, the app will be mounted at port `8080` and the database at `8081`)

## Manual setup
 * Install the dependencies using `mvn clean install`
 * Edit the database configuration in `/backend/src/main/resources/application.properties`. A valid configuration could be:
```
spring.datasource.url=jdbc:mysql://localhost:3306/database?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

 Hint: Don't forget to adjust the Test application properties as well
 * Run `mvn spring-boot:run`
---

All steps are done. After launching the application, it should be up and running at port `8080` ([http://localhost:8080/](http://localhost:8080/)).

### Hint:
If your main focus is to edit the frontend, consider starting a second npm server using `npm run serve -- --port=8081`- this enables [hot reloading](https://stackoverflow.com/questions/41428954/what-is-the-difference-between-hot-reloading-and-live-reloading-in-react-native). The page (with hot reloading enabled) is available at [http://localhost:8081/](http://localhost:8081/).