package edu.ntnu.stud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.ArrayList;

/**
 * This class reads/writes TrainDeparture objects of/to the permanent register.
 *
 * @author Jonathan Hubertz
 * @version 0.1
 * @since 1. december 2023
 */
public class CsvTrainDeparturePersistence implements TrainDeparturePersistence {

  private final String pathOfFile;
  private final String fileName;
  private static final String FILE_DOES_NOT_EXIST = "File does not exist: ";
  private static final String ERROR = "Error ";
  private static final String CSV_HEADER = "Departure time,Line,Train number,Destination,Track,Delay,";
  private static final int DEPARTURE_DATA_LENGTH = 6;

  /**
   * Constructor for CsvTrainDeparturePersistence.
   *
   * @param pathOfFile the path of the csv file.
   * @param fileName   the name of the csv file.
   */
  public CsvTrainDeparturePersistence(String pathOfFile, String fileName) {
    this.pathOfFile = pathOfFile;
    this.fileName = fileName;
  }
  // Methods that writes to a csv file

  /**
   * Writes a single new TrainDeparture object to the csv file.
   *
   * @param departure the departure to write to file.
   * @throws IOException if the file already exists.
   */
  @Override
  public void writeDeparture(TrainDeparture departure) throws IOException {
    Path path = Paths.get(pathOfFile + fileName);
    String formattedDeparture = departure.getDepartureTime() + "," + departure
        .getLine() + "," + departure.getTrainNumber() + "," + departure
        .getDestination() + "," + departure.getTrack() + "," + departure.getDelay() + ",";

    try {
      if (!Files.exists(path) || Files.size(path) == 0) {
        Files.write(path, Collections.singletonList(CSV_HEADER), StandardOpenOption.CREATE);
      }

      Files.write(path, Collections.singletonList(formattedDeparture), StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new IOException(ERROR + "writing to file: " + path, e);
    }


  }

  /**
   * Removes a TrainDeparture object from the csv file.
   *
   * @param trainNumber the train number of the departure to remove.
   * @throws IOException if the file does not exist.
   */
  @Override
  public void removeDeparture(int trainNumber) throws IOException {
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
      throw new IOException(ERROR + "reading file: " + path);
    }
    // --

    try {
      Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new IOException(ERROR + "writing to file: " + path, e);
    }
  }

  /**
   * Updates the values of a TrainDeparture object to the csv file.
   *
   * @param departure the TrainDeparture object that's to be updated.
   * @throws IOException if the file does not exist
   */
  @Override
  public void updateDeparture(TrainDeparture departure) throws IOException {
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
   * @throws IOException if the file does not exist.
   */
  @Override
  public List<TrainDeparture> readDepartures() throws IOException {
    Path path = Paths.get(pathOfFile, fileName);
    if (!Files.exists(path)) {
      throw new IOException(FILE_DOES_NOT_EXIST + path);
    }

    List<TrainDeparture> departures = new ArrayList<>();

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

  // Used for testing

  /**
   * Removes all the data from the csv file.
   *
   * @throws IOException if the file does not exist.
   */
  @Override
  public void clearDepartures() throws IOException {
    Path path = Paths.get(pathOfFile + fileName);

    if (!Files.exists(path)) {
      throw new IOException("This file does not exist. Try with another name");
    }

    Files.write(path, Collections.singletonList(CSV_HEADER), StandardOpenOption.TRUNCATE_EXISTING);
  }
}
