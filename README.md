# DragonsOfMugloar

## Overview

DragonsOfMugloar is a Java-based implementation of the "Dragons of Mugloar" challenge. Developed using Spring Boot.

## Getting Started

### Prerequisites

- Java 21
- Maven

### Installation

Clone the repository:

```bash
git clone https://github.com/imrePurret/DragonsOfMugloar.git
```

Navigate to the project folder and run:

```bash
mvn install
```

### Running the Application
To start the application, run:

```bash
mvn spring-boot:run
```

### Tests
To run the tests:

```bash
mvn test
```

### Dependencies
This project uses the following key dependencies:

- Spring Boot: For building the application.
- Lombok: To simplify the code and avoid boilerplate.
- JUnit: For testing the components.

Other dependencies include:

- Spring Data JPA: For managing data persistence.
- Maven: For project build and dependency management.

You can see the full list in the pom.xml file.

### H2 Database
The project uses an H2 database for data storage during local development and testing. This allows quick and easy setup without external dependencies. The database is configured in the application and is automatically initialized during application startup.

### Training
The project also includes functionality for training/data gathering purposes, designed to optimize certain performance aspects based on the challenge requirements. This training part allows the application to run various simulations and assess the results based on the provided task history.
- training.games property in application.properties file

### Solution steps overview
For overview, refer to the SolvingApproach.md.

### Analysis
For detailed analysis, refer to the TaskHistoryAnalysis.md.

### Challenge Link
You can explore the "Dragons of Mugloar" challenge at https://dragonsofmugloar.com.