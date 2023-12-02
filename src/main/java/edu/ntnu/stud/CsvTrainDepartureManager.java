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
import java.util.List;
import java.util.stream.Stream;

/**
 * This class handles the data.
 *
 * @author Jonathan Hubertz
 * @version 0.1
 * @since 1. december 2023
 */
public class CsvTrainDepartureManager {

  private static final String FILE_DOES_NOT_EXIST = "File does not exist: ";
  private static final String ERROR = "Error ";

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
  public void makeCsv(TrainDepartureRegister registerToWrite, String pathOfFile,
      String fileName)
      throws IOException {
    Path path = Paths.get(pathOfFile + fileName);
    List<String> data = new ArrayList<>();

    if (Files.exists(path)) {
      throw new IOException("This file already exists. Try with another name");
    } else {
      Path finishedDocument = Files.createFile(path);
      data.add("Departure time,Line,Train number,Destination,Track,Delay");
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
  public void writeDepartureToCsv(TrainDeparture departure, String pathOfFile,
      String fileName)
      throws IOException {
    Path path = Paths.get(pathOfFile + fileName);
    String formattedDeparture = departure.getDepartureTime() + "," + departure
        .getLine() + "," + departure.getTrainNumber() + "," + departure
        .getDestination() + "," + departure.getTrack() + "," + departure.getDelay() + ",";

    if (!Files.exists(path)) {
      Files.write(path,
          Collections.singletonList("Departure time,Line,Train number,Destination,Track,Delay"),
          StandardOpenOption.CREATE);
    }

    Files.write(path, Collections.singletonList(formattedDeparture),
        StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);


  }

  /**
   * Removes a TrainDeparture object from the csv file.
   *
   * @param trainNumber the train number of the departure to remove.
   * @param pathOfFile  the path of the csv file.
   * @param fileName    the name of the csv file.
   * @throws IOException if the file does not exist.
   */
  public void removeDepartureFromCsv(int trainNumber, String pathOfFile, String fileName)
      throws IOException {
    Path path = Paths.get(pathOfFile, fileName);
    if (!Files.exists(path)) {
      throw new IOException(FILE_DOES_NOT_EXIST + path);
    }

    List<String> lines;
    try (Stream<String> stream = Files.lines(path)) {
      lines = stream
          .filter(line -> !line.contains("," + trainNumber + ","))
          .toList();
    } catch (IOException e) {
      throw new IOException(ERROR + "reading file: " + path, e);
    }

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
  public void updateDepartureToCsv(TrainDeparture departure, String pathOfFile,
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

  public List<TrainDeparture> readCsv(List<TrainDeparture> departures, String pathOfFile,
      String fileName) throws IOException {
    Path path = Paths.get(pathOfFile, fileName);
    if (!Files.exists(path)) {
      throw new IOException(FILE_DOES_NOT_EXIST + path);
    }

    try (Stream<String> lines = Files.lines(path)) {
      List<String[]> departuresList = lines
          .filter(line -> !line.trim().isEmpty())
          .map(line -> line.split(","))
          .toList();

      for (int i = 1; i < departuresList.size(); i++) {
        String[] departureData = departuresList.get(i);

        if (departureData.length != 6) {
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
  public boolean trainNumberExistsInCsv(int trainNumber, String pathOfFile, String fileName)
      throws IOException {
    Path path = Paths.get(pathOfFile + fileName);
    if (!Files.exists(path)) {
      throw new IOException(FILE_DOES_NOT_EXIST + path);
    }

    List<String> lines;
    try (Stream<String> stream = Files.lines(path)) {
      lines = stream
          .filter(line -> line.contains("," + trainNumber + ","))
          .toList();
    } catch (IOException e) {
      throw new IOException(ERROR + "reading file: " + path);
    }

    return !lines.isEmpty();
  }
}