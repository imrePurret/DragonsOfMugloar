# Overview of Implementation Steps

## Main Solving Steps
- Set up the database.
- Create the data collection component of the application.
- Collect a sufficient amount of data.
- Analyze the collected data.
- Implement gameplay logic based on the analysis.

## Database Setup
The first step in the project involved gathering data. To accomplish this, an H2 database was created and initialized using a schema.sql file. This file defines the TASK_HISTORY table, which stores key data points critical for achieving success in the system.

## Training Mode
To simulate real gameplay and gather additional data, a Training Mode was implemented. This service plays games using random choices, aiming to cover a wide range of game scenarios. The results of each game are saved in the TASK_HISTORY table to refine and enhance the system's decision-making process.

The application.properties file includes the property training.games. If its value is set to 0, no training mode games will be played. If it is set to a higher number, the training mode will launch, play the specified number of games, and store task-solving results in the database.

## Data Collection
To gather sufficient data, approximately 1,000 training mode games were played.

### Data Analysis
Various correlation data points were queried from the database, and simple analyses were conducted. Additional details can be found in the TaskHistoryAnalysis.md file.

### Gameplay Logic Implementation
Key insights derived from the data analysis were incorporated into the GameLogicService. The currently implemented logic includes:

- Filtering out encrypted and outlier messages (e.g., "diamond stealing" tasks).
- Identifying tasks with the highest success rate based on probabilities using the TASK_HISTORY table.
- Purchasing health potions until reaching 7 lives, then buying "Claw Sharpening," a random product that increases the dragon's level and costs 100 gold.
- Starting the application to play a single game, logging each step in the application log.

### Possible Improvements/Changes
Several enhancements could be made to the system, including:

- Adding a user interface or enabling usage via an API.
- Enhancing the training process by incorporating additional fields and conducting deeper analyses.
- Introducing more advanced logic based on the current analysis to further improve results.
- Replacing database success rate query with hardcoded mapper, as these seem to be pretty concrete.