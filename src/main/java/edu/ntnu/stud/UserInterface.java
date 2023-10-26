package edu.ntnu.stud;

import java.time.LocalTime;

/**
 * This is the user inteface class for the train dispatch application.
 */
public class UserInterface {

  /**
   * This method initializes the user interface.
   */
  public void init() {

  }

  /**
   * This method starts the user interface and checks that it's working properly.
   */
  public void start() {
    TrainDeparture td1 = new TrainDeparture(LocalTime.of(15, 5), "L1", 1, "Spikkestad",
        LocalTime.of(0, 0));
    TrainDeparture td2 = new TrainDeparture(LocalTime.of(16, 0), "RE11", 70, "Skien",
        LocalTime.of(0, 0));
    TrainDeparture td3 = new TrainDeparture(LocalTime.of(16, 10), "L2", 110, "Ski",
        LocalTime.of(0, 5));

    System.out.println(td1);
    System.out.println(td2);
    System.out.println(td3);
  }

}
