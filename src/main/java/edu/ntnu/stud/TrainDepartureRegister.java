package edu.ntnu.stud;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * This class represents a register of train departures.
 *
 * @author Jonathan Hubertz
 * @version 0.4
 * @since 30. october 2023
 */
public class TrainDepartureRegister {

  private final HashMap<Integer, TrainDeparture> register;
  private LocalTime systemTime;

  /**
   * Constructor for TrainDepartureRegister.
   */
  public TrainDepartureRegister() {
    this.register = new HashMap<>();
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
   * @return true if the train departure was added, false if a trainDeparture with the same
   * trainNumber already exists.
   */
  public boolean addTrainDeparture(LocalTime departureTime, String line, int trainNumber,
      String destination, LocalTime delay) {
    return register.putIfAbsent(trainNumber,
        new TrainDeparture(departureTime, line, trainNumber, destination, delay)) == null;
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
   * @return true if the train departure was added, false if a trainDeparture with the same
   * trainNumber already exists.
   */
  public boolean addTrainDeparture(LocalTime departureTime, String line, int trainNumber,
      String destination, int track, LocalTime delay) {
    return register.putIfAbsent(trainNumber,
        new TrainDeparture(departureTime, line, trainNumber, destination, track, delay)) == null;
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
    register.get(trainNumber).setTrack(track);
  }

  /**
   * This method sets the delay of a train departure.
   *
   * @param trainNumber the train number
   * @param delay       the delay
   */
  public void setDelay(int trainNumber, LocalTime delay) {
    register.get(trainNumber).setDelay(delay);
  }

  /**
   * This method retrieves a train departure from the register by train number.
   *
   * @param trainNumber the train number
   * @return the train departure with the given train number, or null if it does not exist.
   */
  public TrainDeparture searchByTrainNumber(int trainNumber) {
    return register.get(trainNumber);
  }

  /**
   * This method retrieves a list of train departures from the register by destination.
   *
   * @param destination the destination
   * @return a list of train departures with the given destination, or an empty list if none exist.
   */
  public List<TrainDeparture> searchByDestination(String destination) {
    return register.values().stream()
        .filter(departure -> departure.getDestination().equals(destination))
        .toList();
  }

  /**
   * Removes train departures with a departure time before the given time.
   *
   * @param time the time to remove train departures before.
   */
  public void removeDeparturesBefore(LocalTime time) {
    register.entrySet()
        .removeIf(entry -> entry.getValue().getDepartureTimeWithDelay().isBefore(time));
  }

  /**
   * Sorts the train departures by departure time.
   *
   * @return a sorted list of train departures by the departure time.
   */
  public List<TrainDeparture> getSortedDepartures() {
    List<TrainDeparture> temp = new ArrayList<>();
    register.values().stream()
        .sorted(Comparator.comparing(TrainDeparture::getDepartureTimeWithDelay))
        .forEach(temp::add);
    return temp;
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
   * A string representation of the register.
   *
   * @return a string representation of the register.
   */

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    // Header
    sb.append(String.format("%-4s | %-15s | %-18s | %-5s | %-5s%n",
        "Nr", "Departure Time", "Destination", "Track", "Delay"));

    // Separator
    sb.append(String.format("%-4s | %-15s | %-18s | %-5s | %-5s%n",
        "----", "---------------", "------------------", "-----", "-----"));

    for (TrainDeparture departure : register.values()) {
      sb.append(String.format("%-4d | %-15s | %-18s | %-5d | %-5s%n",
          departure.getTrainNumber(),
          departure.getDepartureTime(),
          departure.getLine() + " " + departure.getDestination(),
          departure.getTrack(),
          departure.getDelay()));
    }

    return sb.toString();
  }


}
