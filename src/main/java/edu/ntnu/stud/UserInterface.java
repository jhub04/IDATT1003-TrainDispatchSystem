package edu.ntnu.stud;

import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

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
    LocalTime departureTime = collectDepartureTime();
    String line = collectLine();
    int trainNumber = collectTrainNumberDoesntExist();
    String destination = collectDestination();
    int track = collectTrack();
    LocalTime delay = collectDelay();

    // Add the train departure
    try {
      register.addTrainDeparture(departureTime, line, trainNumber, destination, track, delay);
      System.out.println("Train departure successfully added.");
      displayDepartures();
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }


  private LocalTime collectDepartureTime() {
    while (true) {
      try {
        System.out.println("Enter departure time (hh:mm): ");
        LocalTime departureTime = LocalTime.parse(input.nextLine(), formatter);
        if (departureTime.isBefore(register.getSystemTime())) {
          System.out.println("Cannot assign a train departure time before the current time.");
        } else {
          return departureTime;
        }
      } catch (DateTimeParseException e) {
        System.out.println("Invalid time format. Please try again.");
      }
    }
  }

  private String collectLine() {
    Pattern pattern = Pattern.compile("^[A-Z]+[0-9]+$");

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
              "Invalid format. Please enter the line in the correct format (e.g., L1, RE11).");
        }
      } else {
        System.out.println("Input cannot be empty. Please try again.");
      }
    }
  }

  private int collectTrainNumberDoesntExist() {
    while (true) {
      try {
        System.out.println("Enter train number: ");
        int trainNumber = Integer.parseInt(input.nextLine());
        if (register.trainNumberExistsInCsv(trainNumber)) {
          System.out.println("Error: A train departure with number " + trainNumber
              + " already exists or has existed.");
          continue;
        }
        if (trainNumber < 0 || trainNumber > 999) {
          System.out.println("Error: Train number must be between 0 and 999.");
          continue;
        }
        return trainNumber;
      } catch (NumberFormatException e) {
        System.out.println("Invalid number format. Please try again.");
      }
    }
  }

  private int collectTrainNumberDoesExist() {
    while (true) {
      try {
        System.out.println("Enter train number: ");
        int trainNumber = Integer.parseInt(input.nextLine());
        if (register.searchByTrainNumber(trainNumber) == null) {
          System.out.println("Error: A train departure with number " + trainNumber
              + " doesn't exist or has existed.");
          continue;
        }
        return trainNumber;

      } catch (NumberFormatException e) {
        System.out.println("Invalid number format. Please try again.");
      }
    }
  }

  private String collectDestination() {
    while (true) {
      try {
        System.out.println("Enter destination: ");
        String destination = input.nextLine().toLowerCase();
        if (!destination.trim().isEmpty()) {
          return destination;
        }
        System.out.println("Input cannot be empty. Please try again.");
      } catch (NumberFormatException e) {
        System.out.println("Invalid number format. Please try again.");
      }

    }
  }

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
          System.out.println("Error: Track must be between 0 and 99.");
        } else {
          return track;
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid number format. Please try again.");
      }
    }
  }

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
          System.out.println("Error: Delay cannot be negative.");
        } else {
          return delay;
        }
      } catch (DateTimeParseException e) {
        System.out.println("Invalid time format. Please try again.");
      }
    }
  }

  private LocalTime collectSystemTime() {
    while (true) {
      try {
        System.out.println("Enter system time (hh:mm): ");
        LocalTime systemTime = LocalTime.parse(input.nextLine(), formatter);
        if (systemTime.isBefore(register.getSystemTime())) {
          System.out.println("Cannot set system time before current time.");
        } else {
          return systemTime;
        }
      } catch (DateTimeParseException e) {
        System.out.println("Invalid time format. Please try again.");
      }
    }
  }

  public void removeDeparture() {
    int trainNumber = collectTrainNumberDoesExist();

    register.removeDeparture(trainNumber);
    displayDepartures();
  }

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
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
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
      if (delay.equals(LocalTime.of(0, 0))) {
        System.out.println("Delay successfully removed for train " + trainNumber);
      } else {
        System.out.println("Delay successfully set for train " + trainNumber);
      }
      displayDepartures();
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }


  /**
   * Searches for a train departure by train number.
   */
  public void searchByTrainNumber() {
    int trainNumber = collectTrainNumberDoesExist();
    System.out.println(register.searchByTrainNumberString(trainNumber));

  }

  /**
   * Searches for train departures by destination.
   */

  public void searchByDestination() {
    String destination = collectDestination();
    System.out.println(register.searchByDestinationString(destination));

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

  /**
   * Displays the menu.
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

  /**
   * Gets the user input.
   */
  public void getUserInput() {
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
            System.out.println("Invalid input. Please try again.");
            break;
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please try again.");
        input.nextLine();
      }
    }
  }

  /**
   * This method initializes the user interface.
   */
  public void init() {
    register.readData();
    displayDepartures();
  }

  /**
   * This method starts the user interface and checks that it's working properly.
   */
  public void start() {
    getUserInput();
  }
}