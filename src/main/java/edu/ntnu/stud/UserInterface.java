package edu.ntnu.stud;

import java.time.LocalTime;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

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
    System.out.println("Enter departure time (hh:mm): ");
    String departureTimeString = input.nextLine();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    LocalTime departureTime = LocalTime.parse(departureTimeString, formatter);

    System.out.println("Enter line: ");
    String line = input.nextLine();

    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    input.nextLine();

    System.out.println("Enter destination: ");
    String destination = input.nextLine();

    System.out.println("Enter track (-1 if unknown): ");
    int track = input.nextInt();
    input.nextLine();

    System.out.println("Enter delay (hh:mm): ");
    String delayString = input.nextLine();
    LocalTime delay = LocalTime.parse(delayString, formatter);

    if (track == -1) {
      register.addTrainDeparture(departureTime, line, trainNumber, destination,
          delay);
    } else {
      register.addTrainDeparture(departureTime, line, trainNumber, destination, track,
          delay);
    }
  }

  /**
   * Sets the track of a train departure.
   */

  public void setTrack() {
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    System.out.println("Enter track: ");
    int track = input.nextInt();
    register.setTrack(trainNumber, track);

  }

  /**
   * Sets the delay of a train departure.
   */

  public void setDelay() {
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    input.nextLine();
    System.out.println("Enter delay (hh:mm): ");
    String delayString = input.nextLine();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    LocalTime delay = LocalTime.parse(delayString, formatter);
    register.setDelay(trainNumber, delay);

  }

  /**
   * Searches for a train departure by train number.
   */
  public void searchByTrainNumber() {
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    System.out.println(register.searchByTrainNumber(trainNumber));

  }

  /**
   * Searches for train departures by destination.
   */

  public void searchByDestination() {
    System.out.println("Enter destination: ");
    String destination = input.nextLine();
    System.out.println(register.searchByDestination(destination));

  }

  /**
   * Sets the system time.
   */

  public void setSystemTime() {
    System.out.println("Enter new system time (hh:mm): ");
    LocalTime systemTime = LocalTime.parse(input.nextLine());
    if (!register.setSystemTime(systemTime)) {
      System.out.println("Invalid time");
      setSystemTime();
    }
    register.setSystemTime(systemTime);
  }

  /**
   * Displays the menu.
   */

  public void displayMenu() {
    System.out.println("Time: " + register.getSystemTime());
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
    register.addTrainDeparture(LocalTime.of(15, 5), "L1", 1, "Spikkestad",
        LocalTime.of(0, 0));
    register.addTrainDeparture(LocalTime.of(16, 0), "RE11", 70, "Skien",
        LocalTime.of(0, 0));
    register.addTrainDeparture(LocalTime.of(16, 10), "L2", 110, "Ski", LocalTime.of(0, 5));
    register.addTrainDeparture(LocalTime.of(15, 30), "L1", 170, "Spikkestad",
        LocalTime.of(0, 0));
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