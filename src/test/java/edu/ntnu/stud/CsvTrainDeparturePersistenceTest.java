package edu.ntnu.stud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvTrainDeparturePersistenceTest {

  private CsvTrainDeparturePersistence csvTrainDeparturePersistence;
  private final String testPath = "src/test/resources/";
  private final String testFile = "test.csv";

  @BeforeEach
  void setUp() throws IOException {
    csvTrainDeparturePersistence = new CsvTrainDeparturePersistence(testPath, testFile);
    csvTrainDeparturePersistence.clearDepartures();
  }

  @Test
  void testWriteDeparture() throws IOException {
    TrainDeparture departure = new TrainDeparture(LocalTime.of(13, 0), "L1", 1, "spikkestad", 4, LocalTime.of(0, 0));
    csvTrainDeparturePersistence.writeDeparture(departure);

    List<TrainDeparture> departures = csvTrainDeparturePersistence.readDepartures();
    assertFalse(departures.isEmpty());
  }

  @Test
  void testRemoveDeparture() throws IOException {
    TrainDeparture departure = new TrainDeparture(LocalTime.of(13, 0), "L1", 1, "spikkestad", 4, LocalTime.of(0, 0));
    csvTrainDeparturePersistence.writeDeparture(departure);
    csvTrainDeparturePersistence.removeDeparture(1);

    List<TrainDeparture> departures = csvTrainDeparturePersistence.readDepartures();
    assertTrue(departures.isEmpty());
  }

  @Test
  void testUpdateDeparture() throws IOException {
    TrainDeparture departure = new TrainDeparture(LocalTime.of(13, 0), "L1", 1, "spikkestad", 4, LocalTime.of(0, 0));
    csvTrainDeparturePersistence.writeDeparture(departure);

    departure.setTrack(5);
    csvTrainDeparturePersistence.updateDeparture(departure);

    List<TrainDeparture> departures = csvTrainDeparturePersistence.readDepartures();
    assertEquals(5, departures.get(0).getTrack());
  }

  @Test
  void testReadDepartures() throws IOException {
    TrainDeparture departure = new TrainDeparture(LocalTime.of(13, 0), "L1", 1, "spikkestad", 4, LocalTime.of(0, 0));
    csvTrainDeparturePersistence.writeDeparture(departure);

    List<TrainDeparture> departures = csvTrainDeparturePersistence.readDepartures();
    assertEquals(1, departures.size());
  }

  @Test
  void testClearDepartures() throws IOException {
    TrainDeparture departure = new TrainDeparture(LocalTime.of(13, 0), "L1", 1, "spikkestad", 4, LocalTime.of(0, 0));
    csvTrainDeparturePersistence.writeDeparture(departure);
    csvTrainDeparturePersistence.clearDepartures();

    List<TrainDeparture> departures = csvTrainDeparturePersistence.readDepartures();
    assertTrue(departures.isEmpty());
  }

  @Test
  void testReadDeparturesWithInvalidFile() {
    CsvTrainDeparturePersistence invalidCsvTrainDeparturePersistence = new CsvTrainDeparturePersistence(testPath, "invalid.csv");
    assertThrows(IOException.class, invalidCsvTrainDeparturePersistence::readDepartures);
  }

  @Test
  void testClearDeparturesWithInvalidFile() {
    CsvTrainDeparturePersistence invalidCsvTrainDeparturePersistence = new CsvTrainDeparturePersistence(testPath, "invalid.csv");
    assertThrows(IOException.class, invalidCsvTrainDeparturePersistence::clearDepartures);
  }
}


