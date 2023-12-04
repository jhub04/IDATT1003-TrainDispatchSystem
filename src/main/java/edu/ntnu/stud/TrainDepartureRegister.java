package edu.ntnu.stud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

  private final String pathOfFile;
  private final String fileName;
  private static final String ERROR = "Error: ";

  // Constructors

  /**
   * Constructor with file path and name.
   */
  public TrainDepartureRegister(String pathOfFile, String fileName) {
    this.register = new ArrayList<>();
    this.systemTime = LocalTime.of(0, 0);
    this.pathOfFile = pathOfFile;
    this.fileName = fileName;
  }

  /**
   * Default constructor for TrainDepartureRegister.
   */
  public TrainDepartureRegister() {
    this("src/main/resources/", "Departures.csv");
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
      String destination, LocalTime delay) throws IllegalArgumentException {
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
      String destination, int track, LocalTime delay) throws IllegalArgumentException {
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
      csvHandler.removeDepartureFromCsv(trainNumber, pathOfFile,
          fileName);
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
  // Generated by Copilot --
  public void removeDeparturesBefore(LocalTime time) {
    register.removeIf(departure -> departure.getDepartureTimeWithDelay().isBefore(time));
  }
  // --

  // Setters

  /**
   * Sets the track of a train departure.
   *
   * @param trainNumber the train number.
   * @param track       the track.
   */
  public void setTrack(int trainNumber, int track) {
    for (TrainDeparture departure : register) {
      if (departure.getTrainNumber() == trainNumber) {
        try {
          departure.setTrack(track);
          csvHandler.updateDepartureToCsv(departure, pathOfFile, fileName);
          return;
        } catch (IOException e) {
          System.out.println(ERROR + "didn't update Csv file - " + e.getMessage());
          return;
        } catch (IllegalArgumentException e) {
          System.out.println(ERROR + e.getMessage());
          return;
        }
      }
    }
    System.out.println(ERROR + "train number not found.");
  }


  /**
   * Sets the delay of a train departure.
   *
   * @param trainNumber the train number.
   * @param delay       the delay.
   */
  public void setDelay(int trainNumber, LocalTime delay) {
    for (TrainDeparture departure : register) {
      if (departure.getTrainNumber() == trainNumber) {
        try {
          departure.setDelay(delay);
          csvHandler.updateDepartureToCsv(departure, pathOfFile, fileName);
          return;
        } catch (IOException e) {
          System.out.println(ERROR + "didn't update Csv file - " + e.getMessage());
          return;
        } catch (IllegalArgumentException e) {
          System.out.println(ERROR + e.getMessage());
          return;
        }
      }
    }
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
  // Generated by Copilot --
  public TrainDeparture searchByTrainNumber(int trainNumber) {
    return register.stream()
        .filter(departure -> departure.getTrainNumber() == trainNumber)
        .findFirst()
        .orElse(null);
  }
  // --

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

  // Methods used for testing

  /**
   * The number of departures in the temporary register.
   *
   * @return the number of departures in the register.
   */

  int getNumberOfDepartures() {
    return register.size();
  }

  /**
   * Removes all the departures from the permanent register.
   */

  void flushPermRegister() {
    try {
      csvHandler.flushCsv(pathOfFile, fileName);
    } catch (IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  // Methods related to reading, writing and scanning of the permanent register

  /**
   * Reads the permanent register.
   */
  public void readData() {
    try {
      this.register.clear();
      this.register = csvHandler.readCsv(register, pathOfFile,
          fileName);
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
      if (csvHandler.trainNumberExistsInCsv(trainNumber, pathOfFile,
          fileName)) {
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
      csvHandler.writeDepartureToCsv(departure, pathOfFile,
          fileName);
    } catch (IOException e) {
      System.out.println(ERROR + e.getMessage());
    }
  }

  // Helper method

  /**
   * The formatting of the header and separator of the string representations.
   *
   * @return the formatting of the string representation.
   */
  // Help from ChatGPT --
  private String formatDepartures() {
    String time = "\nTime: " + systemTime.toString() + "\n";
    String header = String.format("%-4s | %-15s | %-18s | %-5s | %-5s%n",
        "Nr", "Departure Time", "Destination", "Track", "Delay");
    String separator = String.format("%-4s | %-15s | %-18s | %-5s | %-5s%n",
        "----", "---------------", "------------------", "-----", "-----");
    return time + header + separator;
  }
  // --

  /**
   * This class reads/writes TrainDeparture objects of/to the permanent register.
   *
   * @author Jonathan Hubertz
   * @version 0.1
   * @since 1. december 2023
   */
  static class CsvTrainDepartureManager {

    private static final String FILE_DOES_NOT_EXIST = "File does not exist: ";
    private static final String ERROR = "Error ";
    private static final String CSV_HEADER = "Departure time,Line,Train number,Destination,Track,Delay,";
    private static final int DEPARTURE_DATA_LENGTH = 6;

    // Methods that create or write to a csv file

    /**
     * Creates a new csv file and writes the TrainDeparture objects in the TrainDepartureRegister to
     * it.
     *
     * @param registerToWrite the register to write to file.
     * @param pathOfFile      the path of the file.
     * @param fileName        the name of the file.
     * @throws IOException if the file already exists.
     */
    void makeCsv(TrainDepartureRegister registerToWrite, String pathOfFile,
        String fileName)
        throws IOException {
      Path path = Paths.get(pathOfFile + fileName);
      List<String> data = new ArrayList<>();

      if (Files.exists(path)) {
        throw new IOException("This file already exists. Try with another name");
      } else {
        Path finishedDocument = Files.createFile(path);
        data.add(CSV_HEADER);
        registerToWrite.getDepartures().stream()
            .map(trainDeparture -> trainDeparture.getDepartureTime() + "," + trainDeparture.getLine()
                + ","
                + trainDeparture.getTrainNumber() + "," + trainDeparture.getDestination() + ","
                + trainDeparture.getTrack() + "," + trainDeparture.getDelay() + ",")
            .forEach(data::add);
        Files.write(finishedDocument, data);
      }
    }

    /**
     * Writes a single new TrainDeparture object to the csv file.
     *
     * @param departure  the departure to write to file.
     * @param pathOfFile the path of the file.
     * @param fileName   the name of the file.
     * @throws IOException if the file already exists.
     */
    void writeDepartureToCsv(TrainDeparture departure, String pathOfFile, String fileName)
        throws IOException {
      Path path = Paths.get(pathOfFile + fileName);
      String formattedDeparture = departure.getDepartureTime() + "," + departure
          .getLine() + "," + departure.getTrainNumber() + "," + departure
          .getDestination() + "," + departure.getTrack() + "," + departure.getDelay() + ",";

      if (!Files.exists(path) || Files.size(path) == 0) {
        Files.write(path, Collections.singletonList(CSV_HEADER), StandardOpenOption.CREATE);
      }

      Files.write(path, Collections.singletonList(formattedDeparture), StandardOpenOption.APPEND);
    }

    /**
     * Removes a TrainDeparture object from the csv file.
     *
     * @param trainNumber the train number of the departure to remove.
     * @param pathOfFile  the path of the csv file.
     * @param fileName    the name of the csv file.
     * @throws IOException if the file does not exist.
     */
    void removeDepartureFromCsv(int trainNumber, String pathOfFile, String fileName)
        throws IOException {
      Path path = Paths.get(pathOfFile, fileName);
      if (!Files.exists(path)) {
        throw new IOException(FILE_DOES_NOT_EXIST + path);
      }

      // SonarLint suggested this code --
      List<String> lines;
      try (Stream<String> stream = Files.lines(path)) {
        lines = stream
            .filter(line -> !line.contains("," + trainNumber + ","))
            .toList();
      } catch (IOException e) {
        throw new IOException(ERROR + "reading file: " + path, e);
      }
      // --

      Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Updates the values of a TrainDeparture object to the csv file.
     *
     * @param departure  the TrainDeparture object that's to be updated.
     * @param pathOfFile the path of the csv file
     * @param fileName   the name of the csv file
     * @throws IOException if the file does not exist
     */
    void updateDepartureToCsv(TrainDeparture departure, String pathOfFile,
        String fileName) throws IOException {
      Path path = Paths.get(pathOfFile, fileName);
      if (!Files.exists(path)) {
        throw new IOException(FILE_DOES_NOT_EXIST + path);
      }

      String updatedLine =
          departure.getDepartureTime() + "," + departure.getLine() + ","
              + departure.getTrainNumber() + ","
              + departure.getDestination() + "," + departure.getTrack() + ","
              + departure.getDelay();

      // SonarLint suggested this code --
      List<String> lines;
      try (Stream<String> stream = Files.lines(path)) {
        lines = stream
            .map(line -> {
              String[] fields = line.split(",");
              if (fields.length > 2 && fields[2].equals(
                  String.valueOf(departure.getTrainNumber()))) {
                return updatedLine;
              }
              return line;
            })
            .toList();
      }
      // --

      Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // Methods that read or scans a csv file

    /**
     * Reads the csv file and adds the TrainDeparture object to the TrainDepartureRegister object.
     *
     * @param departures the departures to add.
     * @param pathOfFile the path of the csv file.
     * @param fileName   the name of the csv file.
     * @throws IOException if the file does not exist.
     */

    List<TrainDeparture> readCsv(List<TrainDeparture> departures, String pathOfFile,
        String fileName) throws IOException {
      Path path = Paths.get(pathOfFile, fileName);
      if (!Files.exists(path)) {
        throw new IOException(FILE_DOES_NOT_EXIST + path);
      }

      // SonarLint suggested this code --
      try (Stream<String> lines = Files.lines(path)) {
        List<String[]> departuresList = lines
            .filter(line -> !line.trim().isEmpty())
            .map(line -> line.split(","))
            .toList();
        // --

        for (int i = 1; i < departuresList.size(); i++) {
          String[] departureData = departuresList.get(i);

          if (departureData.length != DEPARTURE_DATA_LENGTH) {
            System.out.println("Skipping invalid line: " + Arrays.toString(departureData));
            continue;
          }

          try {
            LocalTime departureTime = LocalTime.parse(departureData[0]);
            String line = departureData[1];
            int trainNumber = Integer.parseInt(departureData[2]);
            String destination = departureData[3];
            int track = Integer.parseInt(departureData[4]);
            LocalTime delay = LocalTime.parse(departureData[5]);

            departures.add(
                new TrainDeparture(departureTime, line, trainNumber, destination, track, delay));
          } catch (DateTimeParseException | NumberFormatException e) {
            System.out.println(
                ERROR + "parsing line " + (i + 1) + ": " + Arrays.toString(departureData));
          }
        }
      }

      return departures;
    }


    /**
     * Checks if a train number exists in the csv file.
     *
     * @param trainNumber the train number to check.
     * @param pathOfFile  the path of the csv file.
     * @param fileName    the name of the csv file.
     * @return true if the train number exists in the csv file, false otherwise.
     * @throws IOException if the file does not exist
     */
    boolean trainNumberExistsInCsv(int trainNumber, String pathOfFile, String fileName)
        throws IOException {
      Path path = Paths.get(pathOfFile + fileName);
      if (!Files.exists(path)) {
        throw new IOException(FILE_DOES_NOT_EXIST + path);
      }

      // SonarLint suggested this code --
      List<String> lines;
      try (Stream<String> stream = Files.lines(path)) {
        lines = stream
            .filter(line -> line.contains("," + trainNumber + ","))
            .toList();
      } catch (IOException e) {
        throw new IOException(ERROR + "reading file: " + path);
      }
      // --

      return !lines.isEmpty();
    }

    // Used for testing

    /**
     * Removes all the data from the csv file.
     *
     * @param pathOfFile the path of the file.
     * @param fileName   the name of the file.
     * @throws IOException if the file does not exist.
     */
    void flushCsv(String pathOfFile, String fileName) throws IOException {
      Path path = Paths.get(pathOfFile + fileName);

      if (!Files.exists(path)) {
        throw new IOException("This file does not exist. Try with another name");
      }

      Files.write(path, Collections.singletonList(CSV_HEADER), StandardOpenOption.TRUNCATE_EXISTING);
    }
  }

}
