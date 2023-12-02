package edu.ntnu.stud;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class represents a register of train departures.
 *
 * @author Jonathan Hubertz
 * @version 0.4
 * @since 30. october 2023
 */
public class TrainDepartureRegister {

  private List<TrainDeparture> register;
  private LocalTime systemTime;

  private final String pathOfFile = "src/main/java/edu/ntnu/stud";
  private final String fileName = "Departures.csv";

  private final DataHandler dataHandler = new DataHandler();

  /**
   * Constructor for TrainDepartureRegister.
   */
  public TrainDepartureRegister() {
    this.register = new ArrayList<>();
    this.systemTime = LocalTime.of(12, 0);
  }

  /**
   * This method adds a train departure without a set track to the register.
   *
   * @param departureTime the departure time
   * @param line          the line
   * @param trainNumber   the train number
   * @param destination   the destination
   * @param delay         the delay
   */

  public void addTrainDeparture(LocalTime departureTime, String line, int trainNumber,
      String destination, LocalTime delay) {
    if (searchByTrainNumber(trainNumber) != null) {
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
      try {
        dataHandler.writeDepartureToCsv(newDeparture, pathOfFile,
            fileName);
      } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  /**
   * This method adds a train departure with a set track to the register.
   *
   * @param departureTime the departure time
   * @param line          the line
   * @param trainNumber   the train number
   * @param destination   the destination
   * @param track         the track
   * @param delay         the delay
   */
  public void addTrainDeparture(LocalTime departureTime, String line, int trainNumber,
      String destination, int track, LocalTime delay) {
    if (searchByTrainNumber(trainNumber) != null) {
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
      try {
        dataHandler.writeDepartureToCsv(newDeparture, pathOfFile,
            fileName);
      } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Removes a departure from the register
   */

  public void removeDeparture(int trainNumber) {
    try {
      register.remove(searchByTrainNumber(trainNumber));
      dataHandler.removeDepartureFromCsv(trainNumber, pathOfFile,
          fileName);
    } catch (IOException e) {
      System.out.println("Error" + e.getMessage());
    }
  }

  /**
   * Gets the system time.
   *
   * @return the system time
   */

  public LocalTime getSystemTime() {
    return systemTime;
  }

  /**
   * Sets the system time.
   *
   * @param time the time to set
   * @return true if the time was set, false if the time is before the current system time.
   */
  public boolean setSystemTime(LocalTime time) {
    if (time.isBefore(systemTime)) {
      return false;
    } else {
      removeDeparturesBefore(time);
      systemTime = time;
    }
    return true;

  }

  /**
   * This method sets the track of a train departure.
   *
   * @param trainNumber the train number
   * @param track       the track
   */
  public void setTrack(int trainNumber, int track) {
    register.forEach(departure -> {
      if (departure.getTrainNumber() == trainNumber) {
        departure.setTrack(track);
        try {
          dataHandler.updateDepartureToCsv(departure, pathOfFile,
              fileName);
        } catch (IOException e) {
          System.out.println("Error updating CSV file: " + e.getMessage());
        }
      }
    });
  }

  /**
   * This method sets the delay of a train departure.
   *
   * @param trainNumber the train number
   * @param delay       the delay
   */
  public void setDelay(int trainNumber, LocalTime delay) {
    register.forEach(departure -> {
      if (departure.getTrainNumber() == trainNumber) {
        departure.setDelay(delay);
        try {
          dataHandler.updateDepartureToCsv(departure, pathOfFile,
              fileName);
        } catch (IOException e) {
          System.out.println("Error updating CSV file: " + e.getMessage());
        }
      }
    });
  }

  /**
   * This method retrieves a train departure from the register by train number.
   *
   * @param trainNumber the train number
   * @return the train departure with the given train number, or null if it does not exist.
   */
  public TrainDeparture searchByTrainNumber(int trainNumber) {
    return register.stream()
        .filter(departure -> departure.getTrainNumber() == trainNumber)
        .findFirst()
        .orElse(null);
  }

  /**
   * This method retrieves a list of train departures from the register by destination.
   *
   * @param destination the destination
   * @return a list of train departures with the given destination, or an empty list if none exist.
   */
  public List<TrainDeparture> searchByDestination(String destination) {
    return register.stream()
        .filter(departure -> departure.getDestination().toLowerCase().equals(destination))
        .toList();
  }

  /**
   * Removes train departures with a departure time before the given time.
   *
   * @param time the time to remove train departures before.
   */
  public void removeDeparturesBefore(LocalTime time) {
    register.removeIf(departure -> departure.getDepartureTimeWithDelay().isBefore(time));
  }

  /**
   * Sorts the train departures by departure time.
   *
   * @return a sorted list of train departures by the departure time.
   */
  public List<TrainDeparture> getDepartures() {
    register.sort(Comparator.comparing(TrainDeparture::getDepartureTime));
    return register;
  }

  /**
   * The number of departures in the register.
   *
   * @return the number of departures in the register.
   */
  public int getNumberOfDepartures() {
    return register.size();
  }

  /**
   * The formatting of the header and separator of the string representations.
   *
   * @return the header and separator.
   */
  private String formatDepartures() {
    String time = "Time: " + systemTime.toString() + "\n";
    String header = String.format("%-4s | %-15s | %-18s | %-5s | %-5s%n",
        "Nr", "Departure Time", "Destination", "Track", "Delay");
    String separator = String.format("%-4s | %-15s | %-18s | %-5s | %-5s%n",
        "----", "---------------", "------------------", "-----", "-----");
    return time + header + separator;
  }

  /**
   * A string representation of a train departure with the given train number.
   *
   * @param trainNumber the train number
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

  public void readData() {
    try {
      this.register = dataHandler.readCsv(register, pathOfFile,
          fileName);
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * A string representation of train departures with the given destination.
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
   * A string representation of the register.
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


}
