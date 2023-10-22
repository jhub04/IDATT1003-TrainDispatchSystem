package edu.ntnu.stud;

import java.time.DateTimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class TrainDepartureTest {

  private TrainDeparture trainDeparture;

  @BeforeEach
  void setUp() {
    trainDeparture = new TrainDeparture(LocalTime.of(12, 0), "L1", 1, "Spikkestad",
        LocalTime.of(0, 0));
  }

  @Test
  void getDepartureTime() {
    assertEquals(LocalTime.of(12, 0), trainDeparture.getDepartureTime());
  }

  @Test
  void getLine() {
    assertEquals("L1", trainDeparture.getLine());
  }

  @Test
  void getTrainNumber() {
    assertEquals(1, trainDeparture.getTrainNumber());
  }

  @Test
  void getDestination() {
    assertEquals("Spikkestad", trainDeparture.getDestination());
  }

  @Test
  void getTrack() {
    assertEquals(-1, trainDeparture.getTrack());
  }

  @Test
  void getDelay() {
    assertEquals(LocalTime.of(0, 0), trainDeparture.getDelay());
  }

  @Test
  void setTrack() {
    trainDeparture.setTrack(1);
    assertEquals(1, trainDeparture.getTrack());
  }

  @Test
  void setTrackThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> trainDeparture.setTrack(-2));
  }

  @Test
  void setDelay() {
    trainDeparture.setDelay(LocalTime.of(0, 1));
    assertEquals(LocalTime.of(0, 1), trainDeparture.getDelay());
  }

  @Test
  void setDelayThrowsException() {
    assertThrows(DateTimeException.class, () -> trainDeparture.setDelay(LocalTime.of(0, -1)));
  }

  @Test
  void getDepartureTimeWithDelay() {
    trainDeparture.setDelay(LocalTime.of(0, 1));
    assertEquals(LocalTime.of(12, 1), trainDeparture.getDepartureTimeWithDelay());
  }
}
