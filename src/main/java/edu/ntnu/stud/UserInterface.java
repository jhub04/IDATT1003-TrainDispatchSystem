package edu.ntnu.stud;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the user interface class for the TrainDispatchApp class.
 *
 * @author Jonathan Hübertz
 * @version 0.1
 * @since 21. november 2023
 */
public class UserInterface {

  private final TrainDepartureRegister register;

  private final Scanner input;
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

  private static final String INVALID_TIME_FORMAT = "Invalid time format. Please try again.";
  private static final String INVALID_NUMBER_FORMAT = "Invalid number format. Please try again.";
  private static final String ERROR = "Error: ";

  // Constructor

  /**
   * Constructor for UserInterface.
   */
  public UserInterface(TrainDeparturePersistence persistence) {
    this.register = new TrainDepartureRegister(persistence);
    this.input = new Scanner(System.in);
  }

  // Methods for adding and removing train departures

  /**
   * Adds a train departure to the permanent register.
   */
  public void addDeparture() {
    LocalTime departureTime = collectDepartureTime();
    String line = collectLine();
    int trainNumber = collectTrainNumberDoesntExist();
    String destination = collectDestination();
    int track = collectTrack();
    LocalTime delay = collectDelay();

    // Add the train departure
    try {
      if (track == -1) {
        register.addTrainDeparture(departureTime, line, trainNumber, destination, delay);
      } else {
        register.addTrainDeparture(departureTime, line, trainNumber, destination, track, delay);
      }
      System.out.println("Train departure successfully added.");
      displayDepartures();
    } catch (IllegalArgumentException | IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  /**
   * Removes a train departure from the permanent register.
   */
  public void removeDeparture() {
    int trainNumber = collectTrainNumberDoesExist();

    try {
      register.removeDeparture(trainNumber);
      displayDepartures();
    } catch (IllegalArgumentException | IOException e) {
      System.out.println(ERROR + e.getMessage());
    } finally {
      System.out.println("Train departure successfully removed.");
    }

  }

  // Setters

  /**
   * Sets the track of a train departure.
   */
  public void setTrack() {
    int trainNumber = collectTrainNumberDoesExist();
    int track = collectTrack();

    try {
      register.setTrack(trainNumber, track);
      if (track == -1) {
        System.out.println("Track successfully removed for train number " + trainNumber);
      } else {
        System.out.println("Track successfully set for train number " + trainNumber);
      }

      displayDepartures();
    } catch (IllegalArgumentException | IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  /**
   * Sets the delay of a train departure.
   */
  public void setDelay() {
    int trainNumber = collectTrainNumberDoesExist();
    LocalTime delay = collectDelay();

    try {
      register.setDelay(trainNumber, delay);
      if (delay.equals(LocalTime.of(0, 0)) && !register.searchByTrainNumber(trainNumber)
          .getDelay().equals(LocalTime.of(0, 0))) {
        System.out.println("Delay successfully removed for train " + trainNumber);
      } else if (!delay.equals(LocalTime.of(0, 0)) && register.searchByTrainNumber(trainNumber)
          .getDelay().equals(LocalTime.of(0, 0))) {
        System.out.println("No delay was set for train " + trainNumber);
      } else {
        System.out.println("Delay successfully set for train " + trainNumber);
      }
      displayDepartures();
    } catch (IllegalArgumentException | IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  /**
   * Sets the system time.
   */
  public void setSystemTime() {
    LocalTime systemTime = collectSystemTime();
    register.setSystemTime(systemTime);
    System.out.println("System time successfully set to " + systemTime);
    displayDepartures();
  }

  // Methods for searching for train departures

  /**
   * Searches for a train departure by train number in the temporary register.
   */
  public void searchByTrainNumber() {
    int trainNumber = collectTrainNumberDoesExist();
    try {
      System.out.println(register.searchByTrainNumberString(trainNumber));
    } catch (IOException e) {
      System.out.println(ERROR + e.getMessage());
    }

  }

  /**
   * Searches for train departures by destination in the temporary register.
   */
  public void searchByDestination() {
    String destination = collectDestination();
    try {
      System.out.println(register.searchByDestinationString(destination));
    } catch (IOException e) {
      System.out.println(ERROR + e.getMessage());
    }

  }

  // Methods for displaying data

  /**
   * Displays all the train departures in the temporary register.
   */
  public void displayDepartures() {
    try {
      System.out.println(register.toSortedTable(register.getSystemTime()));
    } catch (IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  /**
   * Displays the menu of options that a user can choose from.
   */
  public void displayMenu() {
    System.out.println("[1] Display all departures");
    System.out.println("[2] Add departure");
    System.out.println("[3] Remove departure");
    System.out.println("[4] Set track");
    System.out.println("[5] Set delay");
    System.out.println("[6] Search by train number");
    System.out.println("[7] Search by destination");
    System.out.println("[8] Change system time");
    System.out.println("[9] Exit");
  }

  // Methods for initializing and starting the user interface

  /**
   * Initializes the user interface.
   */
  public void init() {
    System.out.println("\nWelcome to the train dispatch application!\n");
    LocalTime time = collectSystemTime();
    register.setSystemTime(time);
    displayDepartures();
  }

  /**
   * Starts the user interface and handles the user input.
   */
  public void start() {
    boolean running = true;
    while (running) {
      displayMenu();
      System.out.print("> ");
      try {
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
            removeDeparture();
            break;
          case 4:
            setTrack();
            break;
          case 5:
            setDelay();
            break;
          case 6:
            searchByTrainNumber();
            break;
          case 7:
            searchByDestination();
            break;
          case 8:
            setSystemTime();
            break;
          case 9:
            System.out.println("Exiting...");
            running = false;
            break;
          default:
            System.out.println(ERROR + "Invalid input. Please try again.");
            break;
        }
      } catch (InputMismatchException e) {
        System.out.println(ERROR + "Invalid input. Please try again.");
        input.nextLine();
      }
    }
  }

  // Helper methods for collecting user input with proper validation

  /**
   * Collects a valid departure time from the user.
   *
   * @return the departure time.
   */
  private LocalTime collectDepartureTime() {
    while (true) {
      try {
        System.out.println("Enter departure time (hh:mm): ");
        LocalTime departureTime = LocalTime.parse(input.nextLine(), formatter);
        if (departureTime.isBefore(register.getSystemTime())) {
          System.out.println(
              ERROR + "Cannot assign a train departure time before the current time.");
        } else {
          return departureTime;
        }
      } catch (DateTimeParseException e) {
        System.out.println(INVALID_TIME_FORMAT);
      }
    }
  }

  /**
   * Collects a valid line from the user.
   *
   * @return the line.
   */
  private String collectLine() {
    Pattern pattern = Pattern.compile("^[A-Z]+\\d+$"); // Generated by ChatGPT

    while (true) {
      System.out.println(
          "Enter line (Uppercase letter(s) followed by number(s). e.g., L1, RE11): ");
      String line = input.nextLine();
      if (!line.trim().isEmpty()) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          return line;
        } else {
          System.out.println(
              ERROR
                  + "Invalid format. Please enter the line in the correct format "
                  + "(e.g., L1, RE11).");
        }
      } else {
        System.out.println(ERROR + "Input cannot be empty. Please try again.");
      }
    }
  }

  /**
   * Collects a valid train number that doesn't exist in the permanent register.
   *
   * @return the train number.
   */
  private int collectTrainNumberDoesntExist() {
    while (true) {
      System.out.println("Enter train number: ");
      try {
        int trainNumber = Integer.parseInt(input.nextLine());
        if ((trainNumber >= 0 && trainNumber <= 999) && register
            .searchByTrainNumber(trainNumber) == null) {
          return trainNumber;
        } else {
          if (trainNumber <= 0 || trainNumber > 999) {
            System.out.println(ERROR + "Train number must be between 1 and 999.");
          } else {
            System.out.println(ERROR + "A train departure with number " + trainNumber
                + " already exists or has existed.");
          }
        }
      } catch (NumberFormatException e) {
        System.out.println(INVALID_NUMBER_FORMAT);
      } catch (IOException e) {
        System.out.println(ERROR + e.getMessage());
      }
    }
  }

  /**
   * Collects a valid train number that exists in the temporary register.
   *
   * @return the train number.
   */
  private int collectTrainNumberDoesExist() {
    while (true) {
      try {
        System.out.println("Enter train number: ");
        int trainNumber = Integer.parseInt(input.nextLine());
        if (register.searchByTrainNumber(trainNumber) == null) {
          System.out.println(ERROR + "A train departure with number " + trainNumber
              + " doesn't exist or has existed.");
          continue;
        }
        return trainNumber;

      } catch (NumberFormatException e) {
        System.out.println(INVALID_NUMBER_FORMAT);
      } catch (IOException e) {
        System.out.println(ERROR + e.getMessage());
      }
    }
  }

  /**
   * Collects a valid destination from the user.
   *
   * @return the destination.
   */
  private String collectDestination() {
    Pattern pattern = Pattern.compile("^[a-zæøå]+$");

    while (true) {
      System.out.println("Enter destination: ");
      String destination = input.nextLine().toLowerCase();
      if (!destination.trim().isEmpty()) {
        Matcher matcher = pattern.matcher(destination);
        if (matcher.find()) {
          return destination;
        } else {
          System.out.println(ERROR
              + "Invalid format. Please enter the destination in the correct format"
              + " (e.g., Oslo S, Lillestrøm).");
        }
      } else {
        System.out.println(ERROR + "Input cannot be empty. Please try again.");
      }
    }
  }

  /**
   * Collects a valid track from the user.
   *
   * @return the track.
   */
  private int collectTrack() {
    while (true) {
      try {
        System.out.println("Enter track [optional]: ");
        String trackString = input.nextLine();
        if (trackString.trim().isEmpty()) {
          return -1;
        }
        int track = Integer.parseInt(trackString);
        if (track <= 0 || track > 99) {
          System.out.println(ERROR + "Track must be between 0 and 99.");
        } else {
          return track;
        }
      } catch (NumberFormatException e) {
        System.out.println(INVALID_NUMBER_FORMAT);
      }
    }
  }

  /**
   * Collects a valid delay from the user.
   *
   * @return the delay.
   */
  private LocalTime collectDelay() {
    while (true) {
      try {
        System.out.println("Enter delay [optional] (hh:mm): ");
        String delayString = input.nextLine();
        if (delayString.trim().isEmpty()) {
          return LocalTime.of(0, 0);
        }
        LocalTime delay = LocalTime.parse(delayString, formatter);
        if (delay.isBefore(LocalTime.of(0, 0))) {
          System.out.println(ERROR + "Delay cannot be negative.");
        } else {
          return delay;
        }
      } catch (DateTimeParseException e) {
        System.out.println(INVALID_TIME_FORMAT);
      }
    }
  }

  /**
   * Collects a valid system time from the user.
   *
   * @return the system time.
   */
  private LocalTime collectSystemTime() {
    while (true) {
      try {
        System.out.println("Enter system time (hh:mm): ");
        LocalTime systemTime = LocalTime.parse(input.nextLine(), formatter);
        if (systemTime.isBefore(register.getSystemTime())) {
          System.out.println(ERROR + "Cannot set system time before current time.");
        } else {
          return systemTime;
        }
      } catch (DateTimeParseException e) {
        System.out.println(INVALID_TIME_FORMAT);
      }
    }
  }
}