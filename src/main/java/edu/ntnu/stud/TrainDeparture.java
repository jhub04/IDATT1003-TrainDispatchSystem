package edu.ntnu.stud;

import java.time.LocalTime;

/**
 * Represents a train departure. This class acts as a model for a single train departure. It stores
 * the departure time, line, train number, destination, track and delay of a train
 *
 * @author Jonathan Hubertz
 * @version 1.0
 * @since 30. october 2023
 */
public class TrainDeparture {

  private final LocalTime departureTime;
  private final String line;
  private final int trainNumber;
  private final String destination;
  private int track;
  private LocalTime delay;

  private static final String ERROR = "Error: ";

  // Constructors

  /**
   * Constructor for TrainDeparture without initial track.
   *
   * @param departureTime the departure time. Immutable once initialized.
   * @param line          the line. Immutable once initialized.
   * @param trainNumber   the train number. Immutable once initialized.
   * @param destination   the destination. Immutable once initialized.
   * @param delay         the delay. Mutable.
   * @throws IllegalArgumentException if departure is null.
   * @throws IllegalArgumentException if delay is null.
   * @throws IllegalArgumentException if line is null or an empty String.
   * @throws IllegalArgumentException if destination is null or an empty String.
   */
  public TrainDeparture(LocalTime departureTime, String line, int trainNumber, String destination,
      LocalTime delay) {
    if (departureTime == null) {
      throw new IllegalArgumentException(ERROR + "departure time cannot be null");
    }
    if (delay == null) {
      throw new IllegalArgumentException(ERROR + "delay cannot be null");
    }
    if (line == null || line.trim().isEmpty()) {
      throw new IllegalArgumentException(ERROR + "line cannot be null or empty");
    }
    if (destination == null || destination.trim().isEmpty()) {
      throw new IllegalArgumentException(ERROR + "destination cannot be null or empty");
    }
    if (trainNumber <= 0 || trainNumber > 999) {
      throw new IllegalArgumentException(ERROR + "train number must be between 1 and 999");
    }

    this.departureTime = departureTime;
    this.line = line;
    this.trainNumber = trainNumber;
    this.destination = destination;
    this.track = -1;
    this.delay = delay;
  }

  /**
   * Constructor for TrainDeparture with initial track.
   *
   * @param departureTime the departure time. Immutable once initialized.
   * @param line          the line. Immutable once initialized.
   * @param trainNumber   the train number. Immutable once initialized.
   * @param destination   the destination. Immutable once initialized.
   * @param track         the track. Mutable.
   * @param delay         the delay. Mutable.
   * @throws IllegalArgumentException if departure is null.
   * @throws IllegalArgumentException if delay is null.
   * @throws IllegalArgumentException if line is null or an empty String.
   * @throws IllegalArgumentException if destination is null or an empty String.
   */
  public TrainDeparture(LocalTime departureTime, String line, int trainNumber, String destination,
      int track, LocalTime delay) {
    if (departureTime == null) {
      throw new IllegalArgumentException(ERROR + "departure time cannot be null");
    }
    if (delay == null) {
      throw new IllegalArgumentException(ERROR + "delay cannot be null");
    }
    if (line == null || line.trim().isEmpty()) {
      throw new IllegalArgumentException(ERROR + "line cannot be null or empty");
    }
    if (destination == null || destination.trim().isEmpty()) {
      throw new IllegalArgumentException(ERROR + "destination cannot be null or empty");
    }
    if (trainNumber <= 0 || trainNumber > 999) {
      throw new IllegalArgumentException(ERROR + "train number must be between 1 and 999");
    }
    if (track <= 0 || track > 99) {
      throw new IllegalArgumentException(ERROR + "track must be between 1 and 99");
    }

    this.departureTime = departureTime;
    this.line = line;
    this.trainNumber = trainNumber;
    this.destination = destination;
    this.track = track;
    this.delay = delay;
  }

  // Getters

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

  // Setters

  /**
   * Sets the track of the train.
   *
   * @param track the track.
   * @throws IllegalArgumentException if the track is not between 1 and 99.
   */
  public void setTrack(int track) throws IllegalArgumentException {
    if (track <= 0 || track > 99) {
      throw new IllegalArgumentException(ERROR + "track must be between 1 and 99");
    }
    this.track = track;
  }

  /**
   * Sets the delay of the train.
   *
   * @param delay the delay.
   */
  public void setDelay(LocalTime delay) {
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
   * String representation of a TrainDeparture object.
   *
   * @return A string detailing the information about the departure.
   */
  @Override
  // Help from ChatGPT
  // --
  public String toString() {
    String trackStr = this.getTrack() == -1 ? "    " : String.format("%-5d", this.getTrack());
    String delayStr =
        this.getDelay() == LocalTime.of(0, 0) ? "     " : String.format("%-5s", this.getDelay());
    return String.format("%-4d | %-15s | %-18s | %-5s | %-5s%n",
        this.getTrainNumber(),
        this.getDepartureTime(),
        this.getLine() + " " + this.getDestination().substring(0, 1).toUpperCase()
            + this.getDestination().substring(1),
        trackStr,
        delayStr);
  }
  // --

}
