package edu.ntnu.stud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class TrainDepartureRegisterTest {

  private TrainDepartureRegister register;

  @BeforeEach
  void setUp() {
    register = new TrainDepartureRegister();

    register.addTrainDeparture(LocalTime.of(15, 5), "L1", 1, "Spikkestad",
        LocalTime.of(0, 0));
    register.addTrainDeparture(LocalTime.of(16, 0), "RE11", 70, "Skien",
        LocalTime.of(0, 0));
    register.addTrainDeparture(LocalTime.of(16, 10), "L2", 110, "Ski", LocalTime.of(0, 5));
  }

  @Nested
  @DisplayName("Tests for the addTrainDeparture")
  class addTrainDepartureTest {

    @Test
    void shouldAddTrainDeparture() {
      assertTrue(register.addTrainDeparture(LocalTime.of(14, 0), "RE11", 2, "Skien",
          LocalTime.of(0, 0)));
    }

    @Test
    void shouldNotAddSameTrainDeparture() {
      assertFalse(register.addTrainDeparture(LocalTime.of(12, 0), "L1", 1, "Spikkestad",
          LocalTime.of(0, 0)));

    }

    @Test
    void shouldNotAddSameTrainDepartureButWithTrack() {
      assertFalse(register.addTrainDeparture(LocalTime.of(16, 0), "RE11", 70, "Skien", 3,
          LocalTime.of(0, 0)));
    }

    @Test
    void shouldNotAddTrainDepartureWithSameTrainNumber() {
      assertFalse(register.addTrainDeparture(LocalTime.of(16, 10), "L2", 70, "Ski",
          LocalTime.of(0, 5)));
    }

    @Test
    void shouldNotAddTrainDepartureWithSameTrainNumberButWithTrack() {
      assertFalse(register.addTrainDeparture(LocalTime.of(16, 10), "L2", 70, "Ski", 3,
          LocalTime.of(0, 5)));
    }

    @Nested
    @DisplayName("Tests for setTrack")
    class setTrackTest {

      @Test
      void shouldSetTrack() {
        register.setTrack(1, 3);
        assertEquals(3, register.searchByTrainNumber(1).getTrack());
      }

      @Test
      void shouldNotSetTrack() {
        assertThrows(IllegalArgumentException.class, () -> register.setTrack(1, -3));
      }
    }

    @Nested
    @DisplayName("Tests for searchByTrainNumber")
    class searchByTrainNumberTest {

      @Test
      void shouldFindTrainDeparture() {
        assertNotNull(register.searchByTrainNumber(1));
        assertEquals(LocalTime.of(15, 5), register.searchByTrainNumber(1).getDepartureTime());
        assertEquals("L1", register.searchByTrainNumber(1).getLine());
      }

      @Test
      void shouldNotFindTrainDeparture() {
        assertNull(register.searchByTrainNumber(2));
      }

    }

    @Nested
    @DisplayName("Tests for searchByDestination")
    class searchByDestinationTest {

      @Test
      void shouldFindTrainDeparture() {
        assertEquals(1, register.searchByDestination("Spikkestad").size());
        assertEquals(LocalTime.of(16, 0),
            register.searchByDestination("Skien").get(0).getDepartureTime());
      }

      @Test
      void shouldNotFindTrainDeparture() {
        assertEquals(0, register.searchByDestination("Oslo").size());
      }

    }

    @Nested
    @DisplayName("Tests for removeDeparturesBefore")
    class removesDeparturesBeforeTest {

      @Test
      void shouldRemoveTrainDeparture() {
        register.removeDeparturesBefore(LocalTime.of(16, 11));
        assertEquals(1, register.getNumberOfDepartures());
      }

      @Test
      void shouldNotRemoveTrainDepartureIfExactTime() {
        register.removeDeparturesBefore(LocalTime.of(16, 0));
        assertEquals(2, register.getNumberOfDepartures());
      }


      @Test
      void shouldNotRemoveTrainDepartureIfAfterTime() {
        register.removeDeparturesBefore(LocalTime.of(15, 50));
        assertEquals(2, register.getNumberOfDepartures());
      }

      @Test
      void shouldNotRemoveTrainDepartureIfTimeIsBeforeDepartureTimeWithDelay() {
        register.removeDeparturesBefore(LocalTime.of(16, 11));
        assertEquals(1, register.getNumberOfDepartures());
      }

      @Test
      void shouldNotRemoveTrainDepartureIfTimeIsEqualToDepartureTimeWithDelay() {
        register.removeDeparturesBefore(LocalTime.of(16, 15));
        assertEquals(1, register.getNumberOfDepartures());
      }

      @Test
      void shouldNotRemoveTrainDepartureIfTimeIsAfterDepartureTimeWithDelay() {
        register.removeDeparturesBefore(LocalTime.of(16, 16));
        assertEquals(0, register.getNumberOfDepartures());
      }
    }

    @Nested
    @DisplayName("Tests for getDepartures")
    class getSortedDeparturesTest {

      @Test
      void shouldSortTrainDeparturesByAscendingOrder() {
        assertEquals(LocalTime.of(15, 5),
            register.getDepartures().get(0).getDepartureTime());
        assertEquals(LocalTime.of(16, 0),
            register.getDepartures().get(1).getDepartureTime());
        assertEquals(LocalTime.of(16, 10),
            register.getDepartures().get(2).getDepartureTime());
      }

      @Test
      void shouldNotSortTrainDeparturesByDescendingOrder() {
        assertNotEquals(LocalTime.of(16, 10),
            register.getDepartures().get(1).getDepartureTime());
        assertNotEquals(LocalTime.of(16, 0),
            register.getDepartures().get(0).getDepartureTime());
        assertNotEquals(LocalTime.of(15, 5),
            register.getDepartures().get(2).getDepartureTime());
      }

      @Test
      void shouldSortTrainDeparturesByAscendingOrderWithDelay() {
        assertEquals(LocalTime.of(15, 5),
            register.getDepartures().get(0).getDepartureTimeWithDelay());
        assertEquals(LocalTime.of(16, 0),
            register.getDepartures().get(1).getDepartureTimeWithDelay());
        assertEquals(LocalTime.of(16, 15),
            register.getDepartures().get(2).getDepartureTimeWithDelay());
      }
    }
  }
}
