package edu.ntnu.stud;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;



/**
 * This class represents a register of train departures.
 *
 * @author Jonathan Hubertz
 * @version 1.1.0
 * @since 30. october 2023
 */
public class TrainDepartureRegister {

  private final HashMap<Integer, TrainDeparture> registerMap;
  private final ArrayList<TrainDeparture> registerList;

  /**
   * Constructor for TrainDepartureRegister.
   */
  public TrainDepartureRegister() {
    this.registerMap = new HashMap<>();
    this.registerList = new ArrayList<>();
  }


  /**
   * This method adds a train departure to the register.
   *
   * @param departure the train departure to be added.
   * @return true if the train departure was added, false if not.
   */
  public boolean addTrainDeparture(TrainDeparture departure) {
    int trainNumber = departure.getTrainNumber();
    if (!(registerMap.containsKey(trainNumber))) {
      registerMap.put(trainNumber, departure);
      registerList.add(departure);
      return true;
    }

    return false;

  }

  /**
   * This method retrieves a train departure from the register by train number.
   *
   * @param trainNumber the train number
   * @return the train departure with the given train number, or null if it does not exist.
   */
  public TrainDeparture searchByTrainNumber(int trainNumber) {
    return registerMap.get(trainNumber);
  }

  /**
   * This method retrieves a list of train departures from the register by destination.
   *
   * @param destination the destination
   * @return a list of train departures with the given destination, or an empty list if none exist.
   */
  public List<TrainDeparture> searchByDestination(String destination) {
    return registerList.stream()
        .filter(departure -> departure.getDestination().equals(destination))
        .collect(Collectors.toList());
  }

  /**
   * Removes train departures with a departure time before the given time.
   *
   * @param time the time to remove train departures before.
   */
  public void removeDeparturesBefore(LocalTime time) {
    registerList.removeIf(departure -> departure.getDepartureTimeWithDelay().isBefore(time));
    registerMap.entrySet()
        .removeIf(entry -> entry.getValue().getDepartureTimeWithDelay().isBefore(time));
  }

  /**
   * Sorts the train departures by departure time.
   *
   * @return a sorted list of train departures by the departure time.
   */
  public List<TrainDeparture> getSortedDepartures() {
    return registerList.stream()
        .sorted(Comparator.comparing(TrainDeparture::getDepartureTime))
        .collect(Collectors.toList());
  }


}
