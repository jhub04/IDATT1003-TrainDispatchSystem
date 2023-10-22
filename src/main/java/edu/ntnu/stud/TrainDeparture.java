package edu.ntnu.stud;

import java.time.DateTimeException;
import java.time.LocalTime;

/**
 * Represents a train departure.
 *
 * <p>Role/Responsibility:</p>
 * This class acts as a model for a single train departure. It stores the departure time, line,
 * train number, destination, track and delay of a train
 *
 * <p>Data and Data Types:</p>
 * - departureTime (Localtime): The departure time for the train. - line (String): The line the
 * train is on. - trainNumber (int): The train number. - destination (String): The destination of
 * the train. - track (int): The track the train departs from. - delay (LocalTime): The delay of
 * the train, if any.
 *
 * <p>Reasons for Data Types Choices:</p>
 * - departureTime: LocalTime is a good choice because it is naturally represents a time of day
 * without a date. - line: String is suitable for general text. - trainNumber: int is suitable for
 * this number. - destination: String is suitable for general text. - track: int is suitable for
 * this number. - delay: Chose LocalTime so that it is easy to add to the departure time.
 *
 * <p>Immutable Data upon Creation:</p>
 * departureTime, line, trainNumber and destination are immutable upon creation. This is because
 * they won't ever change.
 *
 * <p>Handling Invalid Data </p>
 * - Throwing a NullPointerException if any of the initial parameters are null.
 * - Throwing an IllegalArgumentException if track is not a positive integer.
 * - Throwing a DateTimeException if delay is a negative time.
 */

public class TrainDeparture {

  private final LocalTime departureTime;
  private final String line;
  private final int trainNumber;
  private final String destination;
  private int track;
  private LocalTime delay;

  /**
   * Constructor for TrainDeparture.
   *
   * @param departureTime the departure time. Immutable once initialized.
   * @param line          the line. Immutable once initialized.
   * @param trainNumber   the train number. Immutable once initialized.
   * @param destination   the destination. Immutable once initialized.
   * @param delay         the delay. Mutable.
   * @throws NullPointerException if any of the parameters are null.
   */
  public TrainDeparture(LocalTime departureTime, String line, int trainNumber, String destination,
      LocalTime delay) {
    if (departureTime == null || line == null || destination == null || delay == null) {
      throw new NullPointerException("Null values are not allowed");
    }
    this.departureTime = departureTime;
    this.line = line;
    this.trainNumber = trainNumber;
    this.destination = destination;
    this.track = -1;
    this.delay = delay;
  }

  // Getter methods

  /**
   * Gets the departure time of the train.
   *
   * @return the departure time.
   */
  public LocalTime getDepartureTime() {
    return departureTime;
  }

  /**
   * Gets the line of the train.
   *
   * @return the line.
   */
  public String getLine() {
    return line;
  }

  /**
   * Gets the train number.
   *
   * @return the train number.
   */
  public int getTrainNumber() {
    return trainNumber;
  }

  /**
   * Gets the destination of the train.
   *
   * @return the destination.
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Gets the track of the train.
   *
   * @return the track.
   */
  public int getTrack() {
    return track;
  }

  /**
   * Gets the delay of the train.
   *
   * @return the delay.
   */
  public LocalTime getDelay() {
    return delay;
  }

  // Setter methods

  /**
   * Sets the track of the train.
   *
   * @param track the track.
   * @throws IllegalArgumentException if track is not a positive integer.
   */
  public void setTrack(int track) {
    if (track < 0) {
      throw new IllegalArgumentException("Track must be a positive integer");
    }
    this.track = track;
  }

  /**
   * Sets the delay of the train.
   *
   * @param delay the delay.
   * @throws DateTimeException if delay is a negative time.
   */
  public void setDelay(LocalTime delay) {
    if (delay.isBefore(LocalTime.of(0, 0))) {
      throw new DateTimeException("Delay must be a positive time");
    }
    this.delay = delay;
  }

  // Other methods

  /**
   * Gets the departure time with the delay added.
   *
   * @return the updated departure time.
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
