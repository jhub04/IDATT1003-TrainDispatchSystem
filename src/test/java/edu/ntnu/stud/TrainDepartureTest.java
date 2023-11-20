package edu.ntnu.stud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TrainDepartureTest {

  private TrainDeparture trainDeparture;

  @BeforeEach
  void setUp() {
    LocalTime departureTime = LocalTime.of(12, 0);
    trainDeparture = new TrainDeparture(departureTime, "L1", 1, "Spikkestad", LocalTime.of(0, 0));
  }

  @Nested
  @DisplayName("Tests for the getters")
  class GettersTests {
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
    void testToString() {
      String expected = "---TrainDeparture---\n[departureTime=12:00, line=L1, trainNumber=1, destination=Spikkestad, track=-1, delay=00:00]";
      assertEquals(expected, trainDeparture.toString());
    }
  }

  @Nested
  @DisplayName("Tests for the setTrack method")
  class TrackTests {

    @Test
    void setTrack() {
      trainDeparture.setTrack(1);
      assertEquals(1, trainDeparture.getTrack());
    }

    @Test
    void setTrackThrowsException() {
      assertThrows(IllegalArgumentException.class, () -> trainDeparture.setTrack(-2));
    }
  }

  @Nested
  @DisplayName("Tests for the setDelay method")
  class DelayTests {

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

    @Test
    void testForLongDelays() {
      trainDeparture.setDelay(LocalTime.of(13, 0));
      assertEquals(LocalTime.of(1, 0), trainDeparture.getDepartureTimeWithDelay());
    }

    @Test
    void setDelayThrowsExceptionForExactZero() {
      assertThrows(DateTimeException.class, () -> trainDeparture.setDelay(LocalTime.of(0, 0)));
    }
  }

  @Nested
  @DisplayName("Tests for the constructors")
  class ConstructorTests {

    @Test
    void constructorThrowsExceptionForNullDepartureTime() {
      assertThrows(NullPointerException.class,
          () -> new TrainDeparture(null, "L1", 1, "Spikkestad", LocalTime.of(0, 0)));
    }

    @Test
    void constructorThrowsExceptionForNullDelay() {
      assertThrows(NullPointerException.class,
          () -> new TrainDeparture(LocalTime.of(12, 0), "L1", 1, "Spikkestad", null));
    }

    @Test
    void constructorThrowsExceptionForNegTrainNumber() {
      assertThrows(IllegalArgumentException.class,
          () -> new TrainDeparture(LocalTime.of(12, 0), "L1", -1, "Spikkestad", LocalTime.of(0, 0)));
    }

    @Test
    void constructorThrowsExceptionForZeroTrainNumber() {
      assertThrows(IllegalArgumentException.class,
          () -> new TrainDeparture(LocalTime.of(12, 0), "L1", 0, "Spikkestad", 1, LocalTime.of(0, 0)));
    }

    @Test
    void constructorThrowsExceptionForNegTrack() {
      assertThrows(IllegalArgumentException.class,
          () -> new TrainDeparture(LocalTime.of(12, 0), "L1", 1, "Spikkestad", -1, LocalTime.of(0, 0)));
    }

    @Test
    void constructorThrowsExceptionForZeroTrack() {
      assertThrows(IllegalArgumentException.class,
          () -> new TrainDeparture(LocalTime.of(12, 0), "L1", 1, "Spikkestad", 0, LocalTime.of(0, 0)));
    }
  }
}
