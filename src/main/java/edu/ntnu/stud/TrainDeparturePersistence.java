package edu.ntnu.stud;

import java.io.IOException;
import java.util.List;

/**
 * This interface handles operations related to the persistence of TrainDeparture objects.
 */
public interface TrainDeparturePersistence {

  void writeDeparture(TrainDeparture departure) throws IOException;

  List<TrainDeparture> readDepartures() throws IOException;

  void removeDeparture(int trainNumber) throws IOException;

  void updateDeparture(TrainDeparture departure) throws IOException;

  void clearDepartures() throws IOException;
}
