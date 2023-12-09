# Portfolio project IDATA1003 - 2023

STUDENT NAME = "Jonathan Skomsøy Hübertz"  
STUDENT ID = "111753"

## Project description
This project is a train dispatch system for traindepartures at a trainstation. The user of the
system can add, remove and edit traindepartures, etc... from a persistent storage of
traindepartures.

## Project structure
The project is set up as a maven project. The project is divided into two main folders:

- The main folder, which contains the main code of the project
- The test folder, which contains the tests of the project

Each of these folders are divided into 2 subfolders:

- The java folder, which contains the java code.  
- The resources folder, which contains the resources of the project

The project uses the package edu.ntnu.stud as the main package. This package contains all the code.
All the sourcefiles are located in the java folder of the main folder. And all the JUnit-test
classes are located in the java folder of the test folder.

## Link to repository
GitHub repository: https://github.com/jhub04/IDATT1003-TrainDispatchSystem.git

## How to run the project
The main class is TrainDispatchApp.java and the main method is located in this class.
To run this project through an IDE simply run the TrainDispatchApp.java class.

To run the project from the terminal, first make sure that you're in the root directory where the
pom.xml file is located.  
Then run the following commands:

```bash
javac -d target/classes src/main/java/edu/ntnu/stud/* 
java -cp target/classes edu.ntnu.stud.TrainDispatchApp
```

## How to run the tests
The tests are located in the test folder of the project.
To run the tests from the IDE, simply run the test class you want.

To run all the tests through the terminal run the following command:

```bash
mvn clean test
```

