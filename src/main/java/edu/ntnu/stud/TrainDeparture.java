package edu.ntnu.stud;

import java.time.DateTimeException;
import java.time.LocalTime;

/**
 * Represents a train departure. This class acts as a model for a single train departure. It stores
 * the departure time, line, train number, destination, track and delay of a train
 *
 * @author Jonathan Hubertz
 * @version 0.8
 * @since 30. october 2023
 */

public class TrainDeparture {

  private final LocalTime departureTime;
  private final String line;
  private final int trainNumber;
  private final String destination;
  private int track;
  private LocalTime delay;

  /**
   * Constructor for TrainDeparture without set track.
   *
   * @param departureTime the departure time. Immutable once initialized.
   * @param line          the line. Immutable once initialized.
   * @param trainNumber   the train number. Immutable once initialized.
   * @param destination   the destination. Immutable once initialized.
   * @param delay         the delay. Mutable.
   * @throws IllegalArgumentException if departure, delay, line or destination is null or empty, or
   *                                  if trainNumber is not a positive integer.
   */
  public TrainDeparture(LocalTime departureTime, String line, int trainNumber, String destination,
      LocalTime delay) {
    if (departureTime == null) {
      throw new IllegalArgumentException("Departure time cannot be null");
    }
    if (delay == null) {
      throw new IllegalArgumentException("Delay cannot be null");
    }
    if (line == null || line.trim().isEmpty()) {
      throw new IllegalArgumentException("Line cannot be null or empty");
    }
    if (destination == null || destination.trim().isEmpty()) {
      throw new IllegalArgumentException("Destination cannot be null or empty");
    }
    if (trainNumber <= 0) {
      throw new IllegalArgumentException("Train number must be a positive integer");
    }

    this.departureTime = departureTime;
    this.line = line;
    this.trainNumber = trainNumber;
    this.destination = destination;
    this.track = -1;
    this.delay = delay;
  }

  /**
   * Constructor for TrainDeparture with set track.
   *
   * @param departureTime the departure time. Immutable once initialized.
   * @param line          the line. Immutable once initialized.
   * @param trainNumber   the train number. Immutable once initialized.
   * @param destination   the destination. Immutable once initialized.
   * @param track         the track. Mutable.
   * @param delay         the delay. Mutable. if departure, delay, line or destination is null or
   *                      empty, or if trainNumber is not a positive integer.
   * @throws IllegalArgumentException if departure, delay, line or destination is null or empty, or
   *                                  if trainNumber is not a positive integer.
   */

  public TrainDeparture(LocalTime departureTime, String line, int trainNumber, String destination,
      int track, LocalTime delay) {
    if (departureTime == null) {
      throw new IllegalArgumentException("Departure time cannot be null");
    }
    if (delay == null) {
      throw new IllegalArgumentException("Delay cannot be null");
    }
    if (line == null || line.trim().isEmpty()) {
      throw new IllegalArgumentException("Line cannot be null or empty");
    }
    if (destination == null || destination.trim().isEmpty()) {
      throw new IllegalArgumentException("Destination cannot be null or empty");
    }
    if (trainNumber <= 0) {
      throw new IllegalArgumentException("Train number must be a positive integer");
    }

    this.departureTime = departureTime;
    this.line = line;
    this.trainNumber = trainNumber;
    this.destination = destination;
    this.track = track;
    this.delay = delay;
  }

  // Getter methods

  /**
   * Gets the departure time of the train.
   *
   * @return the departure time (LocalTime).
   */
  public LocalTime getDepartureTime() {
    return departureTime;
  }

  /**
   * Gets the line of the train.
   *
   * @return the line (String).
   */
  public String getLine() {
    return line;
  }

  /**
   * Gets the train number.
   *
   * @return the train number (int).
   */
  public int getTrainNumber() {
    return trainNumber;
  }

  /**
   * Gets the destination of the train.
   *
   * @return the destination (String).
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Gets the track of the train.
   *
   * @return the track (int).
   */
  public int getTrack() {
    return track;
  }

  /**
   * Gets the delay of the train.
   *
   * @return the delay (LocalTime).
   */
  public LocalTime getDelay() {
    return delay;
  }

  // Setter methods

  /**
   * Sets the track of the train.
   *
   * @param track the track (int).
   * @throws IllegalArgumentException if track is not a positive integer.
   */
  public void setTrack(int track) throws IllegalArgumentException {
    if (track <= 0) {
      throw new IllegalArgumentException("Track must be a positive integer");
    }
    this.track = track;
  }

  /**
   * Sets the delay of the train.
   *
   * @param delay the delay (LocalTime).
   * @throws DateTimeException if delay is a negative time.
   */
  public void setDelay(LocalTime delay) throws IllegalArgumentException {
    if (delay.isBefore(LocalTime.of(0, 0)) || delay.equals(LocalTime.of(0, 0))) {
      throw new IllegalArgumentException("Delay must be a positive time");
    }
    this.delay = delay;
  }

  // Other methods

  /**
   * Gets the departure time with the delay added.
   *
   * @return the updated departure time (LocalTime).
   */
  public LocalTime getDepartureTimeWithDelay() {
    return departureTime.plusHours(delay.getHour()).plusMinutes(delay.getMinute());
  }

  /**
   * Provides a string representation of a TrainDeparture object.
   *
   * @return A string detailing the information about the departure.
   */
  @Override
  public String toString() {
    String trackStr = this.getTrack() == -1 ? "    " : String.format("%-5d", this.getTrack());
    String delayStr =
        this.getDelay() == LocalTime.of(0, 0) ? "     " : String.format("%-5s", this.getDelay());
    return String.format("%-4d | %-15s | %-18s | %-5s | %-5s%n",
        this.getTrainNumber(),
        this.getDepartureTime(),
        this.getLine() + " " + this.getDestination(),
        trackStr,
        delayStr);
  }

}
