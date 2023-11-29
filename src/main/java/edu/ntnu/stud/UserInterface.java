package edu.ntnu.stud;

import java.time.LocalTime;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This is the user interface class for the train dispatch application.
 *
 * @author Jonathan Hübertz
 * @version 0.1
 * @since 21. november 2023
 */
public class UserInterface {

  private final TrainDepartureRegister register;
  private final Scanner input;

  /**
   * Constructor for UserInterface.
   */
  public UserInterface() {
    this.register = new TrainDepartureRegister();
    this.input = new Scanner(System.in);
  }


  /**
   * This method displays all the train departures in the register.
   */
  public void displayDepartures() {
    System.out.println(register);
  }

  /**
   * Adds a train departure to the register.
   */
  public void addDeparture() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    LocalTime departureTime = collectTime("Enter departure time (hh:mm): ", formatter);

    // Validate departure time
    if (departureTime.isBefore(register.getSystemTime())) {
      System.out.println("Error: Cannot assign a train departure time before the current time.");
      return;
    }

    String line = collectString("Enter line: ");
    int trainNumber = collectPositiveInt("Enter train number: ");

    // Validate train number
    if (register.searchByTrainNumber(trainNumber) != null) {
      System.out.println("Error: A train with number " + trainNumber + " already exists.");
      return;
    }

    String destination = collectString("Enter destination: ");
    int track = collectOptionalInt("Enter track [optional]: ");
    LocalTime delay = collectOptionalTime("Enter delay (hh:mm) [optional]: ", formatter);

    // Add the train departure
    try {
      register.addTrainDeparture(departureTime, line, trainNumber, destination, track, delay);
      System.out.println("Train departure successfully added.");
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * This method collects a time from the user.
   *
   * @param prompt    the prompt to display to the user
   * @param formatter the formatter to use for parsing the time
   * @return the time collected from the user
   */
  private LocalTime collectTime(String prompt, DateTimeFormatter formatter) {
    while (true) {
      try {
        System.out.println(prompt);
        return LocalTime.parse(input.nextLine(), formatter);
      } catch (DateTimeParseException e) {
        System.out.println("Invalid time format. Please try again.");
      }
    }
  }

  /**
   * This method collects an optional time from the user.
   * @param prompt the prompt to display to the user
   * @param formatter the formatter to use for parsing the time
   * @return the optional time collected from the user
   */
  private LocalTime collectOptionalTime(String prompt, DateTimeFormatter formatter) {
    System.out.println(prompt);
    String inputString = input.nextLine();
    if (inputString.trim().isEmpty()) {
      return LocalTime.of(0, 0);
    }
    try {
      return LocalTime.parse(inputString, formatter);
    } catch (DateTimeParseException e) {
      System.out.println("Invalid time format. Using default value.");
      return LocalTime.of(0, 0);
    }
  }

  /**
   * This method collects a string from the user.
   *
   * @param prompt the prompt to display to the user
   * @return the string collected from the user
   */
  private String collectString(String prompt) {
    while (true) {
      System.out.println(prompt);
      String inputString = input.nextLine();
      if (!inputString.trim().isEmpty()) {
        return inputString;
      }
      System.out.println("Input cannot be empty. Please try again.");
    }
  }

  /**
   * This method collects a positive integer from the user.
   *
   * @param prompt the prompt to display to the user
   * @return the positive integer collected from the user
   */
  private int collectPositiveInt(String prompt) {
    while (true) {
      try {
        System.out.println(prompt);
        int number = Integer.parseInt(input.nextLine());
        if (number > 0) {
          return number;
        }
        System.out.println("Number must be positive. Please try again.");
      } catch (NumberFormatException e) {
        System.out.println("Invalid number format. Please try again.");
      }
    }
  }

  /**
   * This method collects an optional integer from the user.
   *
   * @param prompt the prompt to display to the user
   * @return the optional integer collected from the user
   */
  private int collectOptionalInt(String prompt) {
    System.out.println(prompt);
    String inputString = input.nextLine();
    if (inputString.trim().isEmpty()) {
      return -1;
    }
    try {
      return Integer.parseInt(inputString);
    } catch (NumberFormatException e) {
      System.out.println("Invalid number format. Using default value.");
      return -1;
    }
  }

  /**
   * Sets the track of a train departure.
   */

  public void setTrack() {
    int trainNumber = collectPositiveInt("Enter train number: ");
    // Validate train number
    if (register.searchByTrainNumber(trainNumber) == null) {
      System.out.println("Error: A train with train number " + trainNumber + " doesn't exists.");
      return;
    }

    int track = collectOptionalInt("Enter track: ");

    try {
      register.setTrack(trainNumber, track);
      System.out.println("Track successfully set for train number " + trainNumber);
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
    // You can add more catch blocks for other specific exceptions if necessary.
  }

  /**
   * Sets the delay of a train departure.
   */

  public void setDelay() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    int trainNumber = collectPositiveInt("Enter train number: ");
    // Validate train number
    if (register.searchByTrainNumber(trainNumber) == null) {
      System.out.println("Error: A train with train number " + trainNumber + " doesn't exists.");
      return;
    }
    LocalTime delay = collectOptionalTime("Enter delay (hh:mm): ", formatter);

    try {
      register.setDelay(trainNumber, delay);
      System.out.println("Delay successfully set for train " + trainNumber);
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
    // Additional catch blocks can be added here for other exceptions.
  }


  /**
   * Searches for a train departure by train number.
   */
  public void searchByTrainNumber() {
    int trainNumber = collectPositiveInt("Enter train number: ");
    // Validate train number
    if (register.searchByTrainNumber(trainNumber) == null) {
      System.out.println("Error: A train with train number " + trainNumber + " doesn't exists.");
      return;
    }
    System.out.println(register.searchByTrainNumberString(trainNumber));

  }

  /**
   * Searches for train departures by destination.
   */

  public void searchByDestination() {
    String destination = collectString("Enter destination: ");
    System.out.println(register.searchByDestinationString(destination));

  }

  /**
   * Sets the system time.
   */

  public void setSystemTime() {
    LocalTime systemTime = collectTime("Enter system time (hh:mm): ",
        DateTimeFormatter.ofPattern("HH:mm"));
    if (!register.setSystemTime(systemTime)) {
      System.out.println("Can not set system time before current time.");
      setSystemTime();
    }
    register.setSystemTime(systemTime);
  }

  /**
   * Displays the menu.
   */

  public void displayMenu() {
    System.out.println("\nTime: " + register.getSystemTime());
    System.out.println("[1] Display all departures");
    System.out.println("[2] Add departure");
    System.out.println("[3] Set track");
    System.out.println("[4] Set delay");
    System.out.println("[5] Search by train number");
    System.out.println("[6] Search by destination");
    System.out.println("[7] Change system time");
    System.out.println("[8] Exit");
  }

  /**
   * Gets the user input.
   */
  public void getUserInput() {
    System.out.print("> ");
    int choice = input.nextInt();
    input.nextLine();
    switch (choice) {
      case 1:
        displayDepartures();
        break;
      case 2:
        addDeparture();
        break;
      case 3:
        setTrack();
        break;
      case 4:
        setDelay();
        break;
      case 5:
        searchByTrainNumber();
        break;
      case 6:
        searchByDestination();
        break;
      case 7:
        setSystemTime();
        break;
      case 8:
        System.exit(0);
        break;
      default:
        System.out.println("Invalid choice");
    }
  }

  /**
   * This method initializes the user interface.
   */
  public void init() {
    register.getInitialData();
  }

  /**
   * This method starts the user interface and checks that it's working properly.
   */
  public void start() {
    while (true) {
      displayMenu();
      getUserInput();
    }
  }

}