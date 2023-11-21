package edu.ntnu.stud;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * This class represents a register of train departures.
 *
 * @author Jonathan Hubertz
 * @version 0.4
 * @since 30. october 2023
 */
public class TrainDepartureRegister {

  private final HashMap<Integer, TrainDeparture> register;

  /**
   * Constructor for TrainDepartureRegister.
   */
  public TrainDepartureRegister() {
    this.register = new HashMap<>();
  }


  /**
   * This method adds a train departure to the register.
   *
   * @param departure the train departure to be added.
   * @return true if the train departure was added, false if not.
   */
  public boolean addTrainDeparture(TrainDeparture departure) {
    int trainNumber = departure.getTrainNumber();
    if (register.containsKey(trainNumber)) {
      return false;
    }
    register.put(trainNumber, departure);
    return true;
  }

  /**
   * This method retrieves a train departure from the register by train number.
   *
   * @param trainNumber the train number
   * @return the train departure with the given train number, or null if it does not exist.
   */
  public TrainDeparture searchByTrainNumber(int trainNumber) {
    return register.get(trainNumber);
  }

  /**
   * This method retrieves a list of train departures from the register by destination.
   *
   * @param destination the destination
   * @return a list of train departures with the given destination, or an empty list if none exist.
   */
  public List<TrainDeparture> searchByDestination(String destination) {
    return register.values().stream()
        .filter(departure -> departure.getDestination().equals(destination))
        .toList();
  }

  /**
   * Removes train departures with a departure time before the given time.
   *
   * @param time the time to remove train departures before.
   */
  public void removeDeparturesBefore(LocalTime time) {
    register.entrySet().removeIf(entry -> entry.getValue().getDepartureTimeWithDelay().isBefore(time));
  }

  /**
   * Sorts the train departures by departure time.
   *
   * @return a sorted list of train departures by the departure time.
   */
  public List<TrainDeparture> getSortedDepartures() {
    List<TrainDeparture> temp = new ArrayList<>();
    register.values().stream()
        .sorted(Comparator.comparing(TrainDeparture::getDepartureTimeWithDelay))
        .forEach(temp::add);
    return temp;
  }


}
