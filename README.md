# GitHub API Application

**This application retrieves public repositories for any GitHub user. It automatically filters out forked repositories
so you only see the original projects. For each repository, you'll get its name, the owner's login, and for every
branch, the branch name along with the SHA of the latest commit.**

## Technologies Used

- **Java 21**
- **Spring Boot 3.4.4**
- **Lombok**

## Running the Application

To start the app in development mode, simply open a terminal in your project folder and run:

```shell
./mvnw spring-boot:run
```

- By default, the app will be available at http://localhost:8080

## API Endpoint

#### GET

```text
/api/{username}
````

- Just replace {username} with the GitHub username you're interested in. For example, to check out Linus Torvalds'
  repositories, visit:

```text
http://localhost:8080/api/torvalds
```

## Integration Tests

The project comes with thorough integration tests to ensure everything works as expected. You can run the tests with:

```shell
./mvnw test
```