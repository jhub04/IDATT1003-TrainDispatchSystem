package edu.ntnu.stud;

import java.time.LocalTime;

/**
 * This is the user inteface class for the train dispatch application.
 *
 * @author Jonathan Hübertz
 * @version 0.1
 * @since 21. november 2023
 */
public class UserInterface {

  private final TrainDepartureRegister register;


  /**
   * Constructor for UserInterface.
   */
  public UserInterface() {
    this.register = new TrainDepartureRegister();

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
  }

  /**
   * This method starts the user interface and checks that it's working properly.
   */
  public void start() {
    System.out.println("Searching for train departure with train number 1:");
    System.out.println(register.searchByTrainNumber(1));
    System.out.println("Searching by destionation Ski:");
    System.out.println(register.searchByDestination("Ski"));
    System.out.println("Remove departures before 15:55");
    register.removeDeparturesBefore(LocalTime.of(15, 55));
    System.out.println(register);
    System.out.println("Adding a valid train departure");
    register.addTrainDeparture(LocalTime.of(17, 0), "LE12", 178, "Lillestrøm", LocalTime.of(0, 5));
    System.out.println(register);
    System.out.println("Sorting by departure time");
    System.out.println(register.getSortedDepartures());

  }

}