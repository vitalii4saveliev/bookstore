# Project Name

A brief description of your project.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Build](#build)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)


## Prerequisites

Before setting up the project, ensure that you have the following installed on your system:

- Java Development Kit 11 (JDK)
- Redis server (running on the default port)
- Your preferred IDE or text editor

## Installation

1. Clone the repository: `git clone https://github.com/username/repo.git`
2. Open the project in your IDE.

## Configuration

The application uses an in-memory H2 database by default, which requires no additional configuration. However, make sure that the Redis server is running on the default port (6379) before starting the application.

If you need to customize the database or Redis configuration, modify the `application.yml` file located in the `src/main/resources` directory.

## Build

To build the project, navigate to the project root directory and run the following command:

```shell
./gradlew build
```
This will compile the source code, run tests, and generate the executable JAR file.

## Running the Application
To run the application, use the following command:

```shell
./gradlew bootRun
```
The application will start on the default port (8080). You can access it in your web browser at http://localhost:8080.

## API Documentation
The project utilizes Springdoc for generating API documentation. Once the application is running, you can access the Swagger UI documentation at http://localhost:8080/api/docs/swagger-ui-custom.html.
