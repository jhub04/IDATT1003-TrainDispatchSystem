package edu.ntnu.stud;

import java.time.DateTimeException;
import java.time.LocalTime;

/**
 * Represents a train departure. This class acts as a model for a single train departure. It stores
 * the departure time, line, train number, destination, track and delay of a train
 *
 * @author Jonathan Hubertz
 * @version 1.0.0
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
   * @throws NullPointerException if any of the parameters are null.
   */
  public TrainDeparture(LocalTime departureTime, String line, int trainNumber, String destination,
      LocalTime delay) throws NullPointerException {
    if (departureTime == null || delay == null) {
      throw new NullPointerException("Null values are not allowed");
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
   * @param delay         the delay. Mutable.
   */

  public TrainDeparture(LocalTime departureTime, String line, int trainNumber, String destination,
      int track, LocalTime delay) {
    if (departureTime == null || delay == null) {
      throw new NullPointerException("Null values are not allowed");
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
  public void setDelay(LocalTime delay) throws DateTimeException {
    if (delay.isBefore(LocalTime.of(0, 0)) || delay.equals(LocalTime.of(0, 0))) {
      throw new DateTimeException("Delay must be a positive time");
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
   * Provides a string representation of a Traindeparture object.
   *
   * @return A string detailing the information about the departure.
   */
  @Override
  public String toString() {
    return "---TrainDeparture---\n[departureTime=" + departureTime + ", line=" + line
        + ", trainNumber=" + trainNumber + ", destination=" + destination + ", track=" + track
        + ", delay=" + delay + "]";
  }

}
