package edu.ntnu.stud;

/**
 * This is the main class for the train dispatch application.
 *
 * @author Jonathan Hübertz
 * @version 0.1
 * @since 21. november 2023
 */
public class TrainDispatchApp {

  /**
   * Runs the application.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    UserInterface ui = new UserInterface();
    ui.init();
    ui.start();
  }
}
