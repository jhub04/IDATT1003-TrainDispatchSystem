package edu.ntnu.stud;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvTrainDepartureManagerTest {

  private final String testPath = "src/test/resources/";
  private final String testFile = "test.csv";
  private final CsvTrainDepartureManager manager = new CsvTrainDepartureManager();
  private final TrainDepartureRegister register = new TrainDepartureRegister(testPath, testFile);

  @BeforeEach
  void setUp() {
    register.flushPermRegister();
    register.addTrainDeparture(LocalTime.of(13, 0), "L1", 1, "Spikkestad", 4,
        LocalTime.of(0, 0));
    register.addTrainDeparture(LocalTime.of(13, 0), "L2", 2, "Lillestrøm", 2, LocalTime.of(0, 0));
  }

  @Test
  void testMakeCsv() throws IOException {
    manager.makeCsv(register, testPath, "testMake.csv");
    Path path = Paths.get(testPath + "testMake.csv");
    assertTrue(Files.exists(path));
    List<String> lines = Files.readAllLines(path);
    assertEquals("Departure time,Line,Train number,Destination,Track,Delay,", lines.get(0));
    assertEquals(3, lines.size());
    Files.delete(path);
  }

  @Test
  void testWriteDepartureToCsv() throws IOException {
    TrainDeparture departure = new TrainDeparture(LocalTime.of(14, 0), "FLY1", 3, "Gardermoen", 2,
        LocalTime.of(0, 0));
    manager.writeDepartureToCsv(departure, testPath, "testWrite.csv");

    assertTrue(manager.trainNumberExistsInCsv(3, testPath, "testWrite.csv"));
    Files.delete(Path.of(testPath + "testWrite.csv"));
  }

  @Test
  void testRemoveDepartureFromCsv() throws IOException {
    manager.removeDepartureFromCsv(1, testPath, testFile);
    assertFalse(manager.trainNumberExistsInCsv(1, testPath, testFile));
  }

  @Test
  void testUpdateDepartureToCsv() throws IOException {
    TrainDeparture departure = new TrainDeparture(LocalTime.of(15, 0), "L1", 1, "Spikkestad", 5,
        LocalTime.of(0, 0));
    manager.updateDepartureToCsv(departure, testPath, testFile);
    assertTrue(manager.trainNumberExistsInCsv(1, testPath, testFile));
  }

  @Test
  void testReadCsv() throws IOException {
    List<TrainDeparture> departures = new ArrayList<>();
    departures.add(new TrainDeparture(LocalTime.of(13, 0), "L1", 4, "Spikkestad", 4,
        LocalTime.of(0, 0)));
    manager.readCsv(departures, testPath, testFile);
    assertEquals(3, departures.size());
  }

  @Test
  void testTrainNumberExistsInCsv() throws IOException {
    assertTrue(manager.trainNumberExistsInCsv(1, testPath, testFile));
    assertFalse(manager.trainNumberExistsInCsv(3, testPath, testFile));
  }

  @Test
  void testFlushCsv() throws IOException {
    manager.flushCsv(testPath, "test.csv");
    Path path = Paths.get(testPath + "test.csv");
    assertTrue(Files.exists(path));
    List<String> lines = Files.readAllLines(path);
    assertEquals(1, lines.size());
    assertEquals("Departure time,Line,Train number,Destination,Track,Delay,", lines.get(0));
    Files.delete(path); // cleanup
  }
}