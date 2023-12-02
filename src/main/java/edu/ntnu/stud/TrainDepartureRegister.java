package edu.ntnu.stud;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class represents a register of TrainDeparture objects.
 *
 * @author Jonathan Hubertz
 * @version 1.0
 * @since 30. october 2023
 */
public class TrainDepartureRegister {

  private List<TrainDeparture> register;

  private final CsvTrainDepartureManager csvHandler = new CsvTrainDepartureManager();
  private LocalTime systemTime;

  private static final String PATH_OF_FILE = "src/main/resources/";
  private static final String FILE_NAME = "Departures.csv";
  private static final String ERROR = "Error: ";

  /**
   * Constructor for TrainDepartureRegister.
   */
  public TrainDepartureRegister() {
    this.register = new ArrayList<>();
    this.systemTime = LocalTime.of(12, 0);
  }

  // Getters

  /**
   * Gets the system time.
   *
   * @return the system time.
   */

  public LocalTime getSystemTime() {
    return systemTime;
  }

  /**
   * Sorts the train departures by ascending departure times.
   *
   * @return a sorted list of train departures.
   */
  public List<TrainDeparture> getDepartures() {
    register.sort(Comparator.comparing(TrainDeparture::getDepartureTime));
    return register;
  }

  // Methods for adding departures to the register

  /**
   * Adds a train departure without a set track to the permanent register.
   *
   * @param departureTime the departure time.
   * @param line          the line.
   * @param trainNumber   the train number.
   * @param destination   the destination.
   * @param delay         the delay.
   * @throws IllegalArgumentException if the train number already exists in the register.
   * @throws IllegalArgumentException if the departure time is before the current system time.
   */

  public void addTrainDeparture(LocalTime departureTime, String line, int trainNumber,
      String destination, LocalTime delay) {
    if (trainNumberExistsInPerm(trainNumber)) {
      throw new IllegalArgumentException("A train with number " + trainNumber + " already exists.");
    }
    if (departureTime.isBefore(systemTime)) {
      throw new IllegalArgumentException(
          "Cannot assign a train departure time before the current time.");
    }

    try {
      TrainDeparture newDeparture = new TrainDeparture(departureTime, line, trainNumber,
          destination, delay);
      register.add(newDeparture);
      writeDepartureToPerm(newDeparture);
    } catch (Exception e) {
      System.out.println(ERROR + e.getMessage());
    }

  }

  /**
   * Adds a train departure with a set track to the permanent register.
   *
   * @param departureTime the departure time.
   * @param line          the line.
   * @param trainNumber   the train number.
   * @param destination   the destination.
   * @param track         the track.
   * @param delay         the delay.
   * @throws IllegalArgumentException if the train number already exists in the register.
   * @throws IllegalArgumentException if the departure time is before the current system time.
   */
  public void addTrainDeparture(LocalTime departureTime, String line, int trainNumber,
      String destination, int track, LocalTime delay) {
    if (trainNumberExistsInPerm(trainNumber)) {
      throw new IllegalArgumentException("A train with number " + trainNumber + " already exists.");
    }
    if (departureTime.isBefore(systemTime)) {
      throw new IllegalArgumentException(
          "Cannot assign a train departure time before the current time.");
    }

    try {
      TrainDeparture newDeparture = new TrainDeparture(departureTime, line, trainNumber,
          destination, track, delay);
      register.add(newDeparture);
      writeDepartureToPerm(newDeparture);
    } catch (Exception e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  // Methods related to removing departures from the register

  /**
   * Removes a departure from the permanent register.
   *
   * @param trainNumber the train number to remove.
   */
  public void removeDeparture(int trainNumber) {
    try {
      register.remove(searchByTrainNumber(trainNumber));
      csvHandler.removeDepartureFromCsv(trainNumber, PATH_OF_FILE,
          FILE_NAME);
    } catch (IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  /**
   * Removes train departures with a departure time before the given time from the temporary
   * register.
   *
   * @param time the time to remove train departures before.
   */
  public void removeDeparturesBefore(LocalTime time) {
    register.removeIf(departure -> departure.getDepartureTimeWithDelay().isBefore(time));
  }

  // Setters

  /**
   * Sets the track of a train departure.
   *
   * @param trainNumber the train number.
   * @param track       the track.
   */
  public void setTrack(int trainNumber, int track) {
    register.forEach(departure -> {
      if (departure.getTrainNumber() == trainNumber) {
        departure.setTrack(track);
        try {
          csvHandler.updateDepartureToCsv(departure, PATH_OF_FILE,
              FILE_NAME);
        } catch (IOException e) {
          System.out.println(ERROR + "didn't update CSV file");
        }
      }
    });
  }

  /**
   * Sets the delay of a train departure.
   *
   * @param trainNumber the train number.
   * @param delay       the delay.
   */
  public void setDelay(int trainNumber, LocalTime delay) {
    register.forEach(departure -> {
      if (departure.getTrainNumber() == trainNumber) {
        departure.setDelay(delay);
        try {
          csvHandler.updateDepartureToCsv(departure, PATH_OF_FILE,
              FILE_NAME);
        } catch (IOException e) {
          System.out.println(ERROR + "didn't update CSV file");
        }
      }
    });
  }

  /**
   * Sets the system time.
   *
   * @param time the time to set.
   */
  public void setSystemTime(LocalTime time) {
    removeDeparturesBefore(time);
    systemTime = time;
  }

  // Methods related to searching the register

  /**
   * Retrieves a train departure from the temporary register by train number.
   *
   * @param trainNumber the train number.
   * @return the TrainDeparture object with the given train number, or null if it doesn't exist.
   */
  public TrainDeparture searchByTrainNumber(int trainNumber) {
    return register.stream()
        .filter(departure -> departure.getTrainNumber() == trainNumber)
        .findFirst()
        .orElse(null);
  }

  /**
   * Retrieves a list of train departures from the temporary register by destination.
   *
   * @param destination the destination.
   * @return a list of train departures with the given destination, or an empty list if none exist.
   */
  public List<TrainDeparture> searchByDestination(String destination) {
    return register.stream()
        .filter(departure -> departure.getDestination().toLowerCase().equals(destination))
        .toList();
  }

  // Methods related to string representations of the register

  /**
   * A string representation of a train departure with a given train number that exists in the
   * temporary register.
   *
   * @param trainNumber the train number.
   * @return a string representation of a train departure with the given train number.
   */
  public String searchByTrainNumberString(int trainNumber) {
    TrainDeparture departure = searchByTrainNumber(trainNumber);
    if (departure == null) {
      return "No train departures with train number " + trainNumber + " found.";
    } else {
      return formatDepartures() + departure;
    }
  }

  /**
   * A string representation of train departures with the given destination in the temporary
   * register.
   *
   * @param destination the destination
   * @return a string representation of train departures with the given destination.
   */
  public String searchByDestinationString(String destination) {
    List<TrainDeparture> departures = searchByDestination(destination);
    if (departures.isEmpty()) {
      return "No train departures with destination " + destination + " found.";
    } else {
      String newDepartures = departures.stream()
          .map(TrainDeparture::toString)
          .collect(Collectors.joining());

      return formatDepartures() + newDepartures;
    }
  }

  /**
   * A string representation of the temporary register.
   *
   * @return a string representation of the register.
   */
  @Override
  public String toString() {
    String newDepartures = getDepartures().stream()
        .map(TrainDeparture::toString)
        .collect(Collectors.joining());

    return formatDepartures() + newDepartures;
  }

  // Method only used for testing

  /**
   * The number of departures in the temporary register.
   *
   * @return the number of departures in the register.
   */

  public int getNumberOfDepartures() {
    return register.size();
  }

  // Methods related to reading, writing and scanning of the permanent register

  /**
   * Reads the permanent register.
   */
  public void readData() {
    try {
      this.register = csvHandler.readCsv(register, PATH_OF_FILE,
          FILE_NAME);
    } catch (IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  /**
   * Checks if a train number exists in the permanent register.
   *
   * @param trainNumber the train number.
   * @return true if the train number exists in the permanent register, false if not.
   */
  public boolean trainNumberExistsInPerm(int trainNumber) {
    try {
      if (csvHandler.trainNumberExistsInCsv(trainNumber, PATH_OF_FILE,
          FILE_NAME)) {
        return true;
      }
    } catch (IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
    return false;
  }

  /**
   * Writes a train departure to the permanent register.
   *
   * @param departure the departure to write.
   */
  private void writeDepartureToPerm(TrainDeparture departure) {
    try {
      csvHandler.writeDepartureToCsv(departure, PATH_OF_FILE,
          FILE_NAME);
    } catch (IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  // Helper method

  /**
   * The formatting of the header and separator of the string representations.
   *
   * @return the formatting of the string represention.
   */
  private String formatDepartures() {
    String time = "\nTime: " + systemTime.toString() + "\n";
    String header = String.format("%-4s | %-15s | %-18s | %-5s | %-5s%n",
        "Nr", "Departure Time", "Destination", "Track", "Delay");
    String separator = String.format("%-4s | %-15s | %-18s | %-5s | %-5s%n",
        "----", "---------------", "------------------", "-----", "-----");
    return time + header + separator;
  }

}
