package edu.ntnu.stud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    void departureTime() {
      assertEquals(LocalTime.of(12, 0), trainDeparture.getDepartureTime());
    }

    @Test
    void line() {
      assertEquals("L1", trainDeparture.getLine());
    }

    @Test
    void trainNumber() {
      assertEquals(1, trainDeparture.getTrainNumber());
    }

    @Test
    void destination() {
      assertEquals("Spikkestad", trainDeparture.getDestination());
    }

    @Test
    void track() {
      assertEquals(-1, trainDeparture.getTrack());
    }

    @Test
    void delay() {
      assertEquals(LocalTime.of(0, 0), trainDeparture.getDelay());
    }

  }

  @Nested
  @DisplayName("Tests for the setTrack method")
  class TrackTests {

    @Test
    void setValidTrack() {
      trainDeparture.setTrack(1);
      assertEquals(1, trainDeparture.getTrack());
    }

    @Test
    void initialTrackValue() {
      assertEquals(-1, trainDeparture.getTrack());
    }

    @Test
    void negativeTrack() {
      assertThrows(IllegalArgumentException.class, () -> trainDeparture.setTrack(-2));
    }

    @Test
    void zeroTrack() {
      assertThrows(IllegalArgumentException.class, () -> trainDeparture.setTrack(0));
    }

    @Test
    void tooHighTrack() {
      assertThrows(IllegalArgumentException.class, () -> trainDeparture.setTrack(100));
    }

    @Test
    void negativeOneTrack() {
      assertThrows(IllegalArgumentException.class, () -> trainDeparture.setTrack(-1));
    }
  }

  @Nested
  @DisplayName("Tests for the setDelay method")
  class DelayTests {

    @Test
    void setValidDelay() {
      trainDeparture.setDelay(LocalTime.of(0, 1));
      assertEquals(LocalTime.of(0, 1), trainDeparture.getDelay());
    }

    @Test
    void correctDepartureTimeWithDelay() {
      trainDeparture.setDelay(LocalTime.of(0, 1));
      assertEquals(LocalTime.of(12, 1), trainDeparture.getDepartureTimeWithDelay());
    }

    @Test
    void longDelays() {
      trainDeparture.setDelay(LocalTime.of(13, 0));
      assertEquals(LocalTime.of(1, 0), trainDeparture.getDepartureTimeWithDelay());
    }
  }

  @Nested
  @DisplayName("Tests for the constructors")
  class ConstructorTests {

    @Test
    void validConstructor() {
      TrainDeparture trainDeparture = new TrainDeparture(LocalTime.of(12, 0), "L1", 1, "Spikkestad",
          1, LocalTime.of(0, 0));
      assertDoesNotThrow(() -> trainDeparture.setTrack(1));
      assertDoesNotThrow(() -> trainDeparture.setDelay(LocalTime.of(0, 1)));
    }

    @Test
    void nullDepartureTime() {
      LocalTime departureTime = null;
      String line = "FLY1";
      int trainNumber = 14;
      String destination = "Gardermoen";
      LocalTime delay = LocalTime.of(0, 0);

      assertThrows(IllegalArgumentException.class,
          () -> new TrainDeparture(departureTime, line, trainNumber, destination, delay));
    }

    @Test
    void nullDelay() {
      LocalTime departureTime = LocalTime.of(12, 0);
      String line = "L1";
      int trainNumber = 1;
      String destination = "Spikkestad";
      LocalTime delay = null;
      assertThrows(IllegalArgumentException.class,
          () -> new TrainDeparture(departureTime, line, trainNumber, destination, delay));
    }

    @Test
    void negativeTrainNumber() {
      LocalTime departureTime = LocalTime.of(12, 0);
      String line = "L1";
      int trainNumber = -1;
      String destination = "Spikkestad";
      LocalTime delay = LocalTime.of(0, 0);
      assertThrows(IllegalArgumentException.class,
          () -> new TrainDeparture(departureTime, line, trainNumber, destination, delay));
    }


    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1000})
    void invalidTrainNumbers(int trainNumber) {
      LocalTime departureTime = LocalTime.of(12, 0);
      String line = "L1";
      String destination = "Spikkestad";
      LocalTime delay = LocalTime.of(0, 0);
      assertThrows(IllegalArgumentException.class,
          () -> new TrainDeparture(departureTime, line, trainNumber, destination, delay));
    }
  }
}
