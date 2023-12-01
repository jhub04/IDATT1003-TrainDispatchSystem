package edu.ntnu.stud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class DataHandler {

  TrainDepartureRegister register = new TrainDepartureRegister();

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

  public void readAFileAsCsv(String pathOfFile, String fileName) throws IOException {
    Path path = Paths.get(pathOfFile + "/" + fileName);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    if (Files.exists(path)) {
      List<String[]> departuresList = Files.lines(path)
          .filter(line -> !line.trim().isEmpty())
          .map(line -> line.split(","))
          .toList();

      for (int i = 1; i < departuresList.size(); i++) {
        String[] departures = departuresList.get(i);

        if (departures.length != 6) {
          System.out.println("Skipping invalid line: " + Arrays.toString(departures));
          continue;
        }

        try {
          LocalTime departureTime = LocalTime.parse(departures[0], formatter);
          String line = departures[1];
          int trainNumber = Integer.parseInt(departures[2]);
          String destination = departures[3];
          int track = Integer.parseInt(departures[4]);
          LocalTime delay = LocalTime.parse(departures[5], formatter);
          register.addTrainDeparture(departureTime, line, trainNumber, destination, track, delay);
        } catch (DateTimeParseException | NumberFormatException e) {
          System.out.println("Error parsing line " + (i + 1) + ": " + Arrays.toString(departures));
        } catch (IllegalArgumentException e) {
          System.out.println("Error: " + e.getMessage());
        }
      }
    } else {
      throw new IOException("File does not exist: " + path);
    }


  }

}