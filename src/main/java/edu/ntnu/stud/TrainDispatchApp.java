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
   * This is the main method of the application.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    UserInterface ui = new UserInterface();
    System.out.println("Welcome to the train dispatch application!");
    ui.init();
    ui.start();
  }
}
