package edu.ntnu.stud;

import java.io.IOException;
import java.util.List;

/**
 * This interface handles operations realted to the persistence of TrainDeparture objects.
 */
public interface TrainDeparturePersistence {

  void writeDepartureToCsv(TrainDeparture departure) throws IOException;

  List<TrainDeparture> readCsv(List<TrainDeparture> departures) throws IOException;

  void removeDepartureFromCsv(int trainNumber) throws IOException;

  void updateDepartureToCsv(TrainDeparture departure) throws IOException;

  boolean trainNumberExistsInCsv(int trainNumber) throws IOException;

  void flushCsv() throws IOException;
}
