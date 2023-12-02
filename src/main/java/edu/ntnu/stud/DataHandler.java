package edu.ntnu.stud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * This class handles the data.
 *
 * @author Jonathan Hubertz
 * @version 0.1
 * @since 1. december 2023
 */
public class DataHandler {

  /**
   * Writes a register of departures to a csv file.
   *
   * @param registerToWrite the register to write to file
   * @param pathOfFile      the path of the file
   * @param fileName        the name of the file
   * @throws IOException if the file already exists
   */
  public void writeAFileAsCsv(TrainDepartureRegister registerToWrite, String pathOfFile,
      String fileName)
      throws IOException {
    Path path = Paths.get(pathOfFile + "/" + fileName);
    List<String> data = new ArrayList<>();

    if (Files.exists(path)) {
      throw new IOException("This file already exists. Try with other name");
    } else {
      Path finishedDocument = Files.createFile(path);
      data.add("Departure time,Line,Train number,Destination,Track,Delay");
      for (TrainDeparture trainDeparture : registerToWrite.getDepartures()) {
        data.add(trainDeparture.getDepartureTime() + "," + trainDeparture.getLine() + ","
            + trainDeparture.getTrainNumber() + "," + trainDeparture.getDestination() + ","
            + trainDeparture.getTrack() + "," + trainDeparture.getDelay());
      }
      Files.write(finishedDocument, data);
    }
  }

  /**
   * Reads a csv file and adds the departures to a register.
   *
   * @param departures the departures to add
   * @param pathOfFile the path of the csv file
   * @param fileName   the name of the csv file
   * @throws IOException if the file does not exist
   */

  public List<TrainDeparture> readCsv(List<TrainDeparture> departures, String pathOfFile,
      String fileName)
      throws IOException {
    Path path = Paths.get(pathOfFile, fileName);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    if (Files.exists(path)) {
      List<String[]> departuresList = Files.lines(path)
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
          LocalTime departureTime = LocalTime.parse(departureData[0], formatter);
          String line = departureData[1];
          int trainNumber = Integer.parseInt(departureData[2]);
          String destination = departureData[3];
          int track = Integer.parseInt(departureData[4]);
          LocalTime delay = LocalTime.parse(departureData[5], formatter);

          departures.add(
              new TrainDeparture(departureTime, line, trainNumber, destination, track, delay));
        } catch (DateTimeParseException | NumberFormatException e) {
          System.out.println(
              "Error parsing line " + (i + 1) + ": " + Arrays.toString(departureData));
        }
      }
    } else {
      throw new IOException("File does not exist: " + path);
    }
    return departures;
  }


  /**
   * Writes a single departure to a csv file.
   *
   * @param departure  the departure to write to file
   * @param pathOfFile the path of the file
   * @param fileName   the name of the file
   * @throws IOException if the file already exists
   */

  public void writeDepartureToCsv(TrainDeparture departure, String pathOfFile,
      String fileName)
      throws IOException {
    Path path = Paths.get(pathOfFile + "/" + fileName);
    List<String> data = new ArrayList<>();
    String formattedDeparture = departure.getDepartureTime() + "," + departure
        .getLine() + "," + departure.getTrainNumber() + "," + departure
        .getDestination() + "," + departure.getTrack() + "," + departure.getDelay() + ",";

    if (!Files.exists(path)) {
      data.add("Departure time,Line,Train number,Destination,Track,Delay");
    }

    data.add(formattedDeparture);
    Files.write(path, Collections.singletonList(formattedDeparture), StandardOpenOption.CREATE,
        StandardOpenOption.APPEND, StandardOpenOption.WRITE);


  }

  public void removeDepartureFromCsv(int trainNumber, String pathOfFile, String fileName)
      throws IOException {
    Path path = Paths.get(pathOfFile + "/" + fileName);
    if (!Files.exists(path)) {
      throw new IOException("File does not exist: " + path);
    }

    List<String> lines = Files.lines(path)
        .filter(line -> !line.contains("," + trainNumber + ","))
        .toList();

    Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public void updateDepartureToCsv(TrainDeparture updatedDeparture, String pathOfFile,
      String fileName) throws IOException {
    Path path = Paths.get(pathOfFile + "/" + fileName);
    if (!Files.exists(path)) {
      throw new IOException("File does not exist: " + path);
    }

    String updatedLine =
        updatedDeparture.getDepartureTime() + "," + updatedDeparture.getLine() + ","
            + updatedDeparture.getTrainNumber() + ","
            + updatedDeparture.getDestination() + "," + updatedDeparture.getTrack() + ","
            + updatedDeparture.getDelay() + ",";

    List<String> lines = Files.lines(path)
        .map(line -> {
          String[] fields = line.split(",");
          if (fields.length > 2 && fields[2].equals(String.valueOf(updatedDeparture.getTrainNumber()))) {
            return updatedLine;
          }
          return line;
        })
        .toList();

    Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
  }
}