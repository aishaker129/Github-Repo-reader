# GitHub Repo Reader

## Description
This is a Spring Boot application that reads data from a specified GitHub repository, including contributors and topics. It exposes RESTful APIs to retrieve this information and automatically updates the data from GitHub on a scheduled basis. Topics are extracted from the `README.md` file of the target GitHub repository.

## Features
*   **GitHub Data Ingestion:** Automatically fetches contributors and parses topics from a designated GitHub repository's `README.md` file.
*   **Scheduled Updates:** Data is updated daily at midnight from the GitHub API.
*   **RESTful API:** Provides endpoints to access contributor and topic information.
*   **Database Persistence:** Stores contributor and topic data in a MySQL database using Spring Data JPA.
*   **Centralized Error Handling:** Consistent API error responses for resource not found and generic exceptions.

## Technologies Used
*   **Spring Boot**: 4.0.2
*   **Java**: 17
*   **Spring Data JPA**
*   **MySQL Database**
*   **Lombok**
*   **RestTemplate** (for GitHub API interaction)
*   **Jackson** (for JSON processing)

## Setup and Installation

### Prerequisites
*   Java Development Kit (JDK) 17 or higher
*   Maven or Gradle (this project uses Gradle)
*   MySQL database instance
*   A GitHub Personal Access Token (PAT) with `repo` scope to access private repositories or higher rate limits for public ones.

### Configuration

Create an `application.yaml` (or `application.properties`) file in `src/main/resources` or set environment variables with the following properties:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
github:
  repo-owner: "abusaeed2433" # Replace with your GitHub username or organization
  repo-name: "JavaInREADME" # Replace with your target repository name
  access-token: ${GITHUB_PERSONAL_ACCESS_TOKEN} # Your GitHub Personal Access Token
```

**Environment Variables:**
*   `DB_URL`: JDBC URL for your MySQL database (e.g., `jdbc:mysql://localhost:3306/github_reader_db`)
*   `DB_USERNAME`: Username for your MySQL database
*   `DB_PASSWORD`: Password for your MySQL database
*   `GITHUB_PERSONAL_ACCESS_TOKEN`: Your GitHub Personal Access Token

### Build and Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/Github-Repo-reader.git
    cd Github-Repo-reader/github-repo-reader
    ```
2.  **Build the project using Gradle:**
    ```bash
    ./gradlew clean build
    ```
3.  **Run the application:**
    ```bash
    java -jar build/libs/github-repo-reader-0.0.1-SNAPSHOT.jar
    ```
    Or, if using an IDE like IntelliJ IDEA, you can run the `GithubRepoReaderApplication.java` file directly.

## API Endpoints

The API is available under the `/api/v1` base path.

### 1. Get All Contributors
Retrieves a list of contributors to the configured GitHub repository.

*   **URL:** `/api/v1/read_contributions`
*   **Method:** `GET`
*   **Response:**
    ```json
    {
      "status": "OK",
      "statusCode": 200,
      "message": "Contributors info",
      "error": false,
      "data": [
        {
          "login": "contributor1",
          "contributions": 10
        },
        {
          "login": "contributor2",
          "contributions": 5
        }
      ]
    }
    ```

### 2. Get All Topics
Retrieves a list of topics parsed from the `README.md` file of the configured GitHub repository.

*   **URL:** `/api/v1/read_indices`
*   **Method:** `GET`
*   **Response:**
    ```json
    {
      "status": "OK",
      "statusCode": 200,
      "message": "Topic info",
      "error": false,
      "data": [
        {
          "id": 1,
          "name": "TopicName1",
          "subTopic": "SubTopic1",
          "blogUrl": "https://example.com/blog1"
        },
        {
          "id": 2,
          "name": "TopicName2",
          "subTopic": "SubTopic2",
          "blogUrl": "https://example.com/blog2"
        }
      ]
    }
    ```

### 3. Get Specific Blog Topic
Retrieves details for a specific topic and sub-topic.

*   **URL:** `/api/v1/read_blog`
*   **Method:** `GET`
*   **Parameters:**
    *   `topic_name` (String, Required): The name of the main topic.
    *   `sub_topic_name` (String, Required): The name of the sub-topic.
*   **Example Request:** `/api/v1/read_blog?topic_name=Java&sub_topic_name=Spring`
*   **Response (Success):**
    ```json
    {
      "status": "OK",
      "statusCode": 200,
      "message": "Topic and sub topic info",
      "error": false,
      "data": {
        "id": 1,
        "name": "Java",
        "subTopic": "Spring",
        "blogUrl": "https://example.com/java-spring-blog"
      }
    }
    ```
*   **Response (Resource Not Found):**
    ```json
    {
      "status": "fail",
      "statusCode": 404,
      "message": "Topic 'Java' with subtopic 'Spring' not found",
      "error": true,
      "data": null
    }
    ```

## `README.md` Topic Format
The application expects topics to be defined in the target GitHub repository's `README.md` file using the following format:

```
## TopicName - SubTopicName - BlogURL
```

For example:
```markdown
# My Awesome GitHub Repository

This is a description of my repository.

## Java - Spring Boot - https://spring.io/blog/
## Python - FastAPI - https://fastapi.tiangolo.com/
## JavaScript - React - https://react.dev/blog
```

## Error Handling
The API provides consistent error responses:
*   `404 Not Found`: When a requested resource (e.g., a specific topic) is not found.
*   `500 Internal Server Error`: For unhandled exceptions within the application.

---
Feel free to contribute to this project by opening issues or pull requests.
