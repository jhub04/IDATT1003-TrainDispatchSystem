package edu.ntnu.stud;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainDepartureRegisterTest {

  private final String testPath = "src/test/resources/";
  private final String testFile = "test.csv";

  private final CsvTrainDeparturePersistence csvTrainDeparturePersistence = new CsvTrainDeparturePersistence(
      testPath, testFile);
  private final TrainDepartureRegister register = new TrainDepartureRegister(
      csvTrainDeparturePersistence);

  @BeforeEach
  void setUp() throws IOException {
    register.clearDepartures();
    register.addTrainDeparture(LocalTime.of(13, 0), "L1", 1, "spikkestad", 4,
        LocalTime.of(0, 0));
    register.addTrainDeparture(LocalTime.of(14, 0), "L2", 2, "Lillestrøm", 2, LocalTime.of(0, 0));
  }

  @Test
  void testReadData() throws IOException {
    assertEquals(2, register.getNumberOfDepartures());
  }

  @Test
  void addValidTrainDeparture() throws IOException {
    register.addTrainDeparture(LocalTime.of(15, 0), "FLY1", 3, "gardermoen",
        LocalTime.of(0, 0));
    assertEquals(3, register.getNumberOfDepartures());
  }

  @Test
  void testRemoveDeparture() throws IOException {
    register.removeDeparture(1);
    assertEquals(1, register.getNumberOfDepartures());
  }

  @Test
  void addDepartureDuplicateTrainNumber() {
    assertThrows(IllegalArgumentException.class, () -> register.addTrainDeparture(
        LocalTime.of(13, 0), "L1", 1, "spikkestad", 4, LocalTime.of(0, 0)));
  }

  @Test
  void addDepartureBeforeSystemTime() {
    register.setSystemTime(LocalTime.of(14, 0));
    assertThrows(IllegalArgumentException.class, () -> register.addTrainDeparture(
        LocalTime.of(11, 0), "L1", 3, "spikkestad", LocalTime.of(0, 0)));
  }

  @Test
  void testSetTrack() throws IOException {
    register.setTrack(1, 5);
    assertEquals(5, register.searchByTrainNumber(1).getTrack());
  }

  @Test
  void testSetDelay() throws IOException {
    register.setDelay(1, LocalTime.of(0, 5));
    assertEquals(LocalTime.of(0, 5), register.searchByTrainNumber(1).getDelay());
  }

  @Test
  void testSetSystemTime() throws IOException {
    {
      register.setSystemTime(LocalTime.of(15, 0));
      assertEquals(LocalTime.of(15, 0), register.getSystemTime());
    }
  }
  @Test
  void testSearchByTrainNumber() throws IOException {
    assertNotNull(register.searchByTrainNumber(1));
    assertNull(register.searchByTrainNumber(3));
  }

  @Test
  void testSearchByDestination() throws IOException {
    List<TrainDeparture> departures = register.searchByDestination("spikkestad");
    assertEquals(1, departures.size());
    departures = register.searchByDestination("oslo");
    assertEquals(0, departures.size());
  }

  @Test
  void testFlushPermRegister() throws IOException {
    register.clearDepartures();
    assertEquals(0, register.getNumberOfDepartures());
  }
}