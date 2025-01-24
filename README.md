# DragonsOfMugloar

## Overview

DragonsOfMugloar is a Java-based implementation of the "Dragons of Mugloar" challenge. Developed using Spring Boot.

## Getting Started

### Using

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

### H2 Database
The project uses an H2 database for data storage. The database is configured in the application and is automatically initialized during application startup.

### Training
The project also includes functionality for training/data gathering purposes, designed to optimize certain performance aspects based on the challenge requirements. This training part allows the application to run various simulations and assess the results based on the provided task history.
- training.games property in application.properties file

### Solution steps overview
For overview, refer to the SolvingApproach.md.

### Analysis
For detailed analysis, refer to the TaskHistoryAnalysis.md.

### Challenge Link
You can explore the "Dragons of Mugloar" challenge at https://dragonsofmugloar.com.