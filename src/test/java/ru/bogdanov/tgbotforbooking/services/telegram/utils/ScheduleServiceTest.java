package ru.bogdanov.tgbotforbooking.services.telegram.utils;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.TimePeriod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ScheduleService.class})
@TestPropertySource(properties = {
        "schedule.start-day=14:30",
        "schedule.end-day=19:30",
        "schedule.slot-duration=PT30M"
})
class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    @Test
    void testGetFreeSlots_WithAvailableSlots() {
        // Given
        List<TimePeriod> freeIntervals = List.of(
                createTimePeriod("2024-01-15T14:30:00Z", "2024-01-15T19:30:00Z")
        );
        Integer duration = 30;

        // When
        Map<LocalDate, List<LocalTime>> result = scheduleService.getFreeSlots(freeIntervals, duration);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        LocalDate expectedDate = LocalDate.of(2024, 1, 15);
        assertTrue(result.containsKey(expectedDate));

        List<LocalTime> slots = result.get(expectedDate);
        assertNotNull(slots);
        assertFalse(slots.isEmpty());

        // Should contain most of the predefined slots
        assertTrue(slots.size() >= 8);
    }

    @Test
    void testGetFreeSlots_WithLongPeriod() {
        // Given
        List<TimePeriod> freeIntervals = List.of(
                createTimePeriod("2024-01-15T14:30:00Z", "2024-01-18T19:30:00Z")
        );
        Integer duration = 30;

        // When
        Map<LocalDate, List<LocalTime>> result = scheduleService.getFreeSlots(freeIntervals, duration);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        LocalDate expectedDate = LocalDate.of(2024, 1, 15);
        assertTrue(result.containsKey(expectedDate));

        List<LocalTime> slots = result.get(expectedDate);
        assertNotNull(slots);
        assertFalse(slots.isEmpty());

        // Should contain most of the predefined slots
        assertTrue(slots.size() >= 8);
    }

    @Test
    void testGetFreeSlots_WithNoAvailableSlots() {
        // Given
        List<TimePeriod> freeIntervals = List.of(
                createTimePeriod("2024-01-15T12:00:00Z", "2024-01-15T14:00:00Z")
        );
        Integer duration = 30;

        // When
        Map<LocalDate, List<LocalTime>> result = scheduleService.getFreeSlots(freeIntervals, duration);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetFreeSlots_WithMultipleDays() {
        // Given
        List<TimePeriod> freeIntervals = List.of(
                createTimePeriod("2024-01-15T14:30:00Z", "2024-01-15T19:30:00Z"),
                createTimePeriod("2024-01-16T15:00:00Z", "2024-01-16T18:00:00Z"),
                createTimePeriod("2024-01-17T00:00:00Z", "2024-01-18T00:00:00Z"),
                createTimePeriod("2024-01-18T10:00:00Z", "2024-01-19T15:00:00Z")
        );
        Integer duration = 30;

        // When
        Map<LocalDate, List<LocalTime>> result = scheduleService.getFreeSlots(freeIntervals, duration);

        // Then
        assertNotNull(result);
        assertEquals(5, result.size());

        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.of(2024, 1, 16);

        assertTrue(result.containsKey(date1));
        assertTrue(result.containsKey(date2));

        // First day should have more slots
        assertTrue(result.get(date1).size() > result.get(date2).size());
    }

    @Test
    void testGetFreeSlots_WithLongerDuration() {
        // Given
        List<TimePeriod> freeIntervals = List.of(
                createTimePeriod("2024-01-15T15:00:00Z", "2024-01-15T17:00:00Z")
        );
        Integer duration = 60; // 1 hour duration

        // When
        Map<LocalDate, List<LocalTime>> result = scheduleService.getFreeSlots(freeIntervals, duration);

        // Then
        assertNotNull(result);

        LocalDate expectedDate = LocalDate.of(2024, 1, 15);
        List<LocalTime> slots = result.get(expectedDate);

        // With 1 hour duration, fewer slots should be available in a 2-hour window
        assertTrue(slots.size() <= 3);
    }

    @Test
    void testGetFreeSlots_WithEmptyIntervals() {
        // Given
        List<TimePeriod> freeIntervals = List.of();
        Integer duration = 30;

        // When
        Map<LocalDate, List<LocalTime>> result = scheduleService.getFreeSlots(freeIntervals, duration);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testIsSlotPresentIn_WhenSlotExists() {
        // Given
        LocalTime slot = LocalTime.of(15, 0, 0);
        List<LocalTime> times = List.of(
                LocalTime.of(14, 30, 0),
                LocalTime.of(15, 0, 0),
                LocalTime.of(15, 30, 0)
        );

        // When
        boolean result = scheduleService.isSlotPresentIn(slot, times);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsSlotPresentIn_WhenSlotNotExists() {
        // Given
        LocalTime slot = LocalTime.of(16, 0, 0);
        List<LocalTime> times = List.of(
                LocalTime.of(14, 30, 0),
                LocalTime.of(15, 0, 0),
                LocalTime.of(15, 30, 0)
        );

        // When
        boolean result = scheduleService.isSlotPresentIn(slot, times);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsSlotPresentIn_WithEmptyList() {
        // Given
        LocalTime slot = LocalTime.of(15, 0, 0);
        List<LocalTime> times = List.of();

        // When
        boolean result = scheduleService.isSlotPresentIn(slot, times);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetSlots() {
        // When
        List<LocalTime> result = scheduleService.getSlots();

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Should return slots with 1 second subtracted
        assertEquals(LocalTime.of(14, 30, 0), result.get(0));
        assertEquals(LocalTime.of(15, 0, 0), result.get(1)); // 15:00:01 - 1 second = 15:00:00
        assertEquals(LocalTime.of(19, 0, 0), result.get(9)); // 19:00:01 - 1 second = 19:00:00
    }

    @Test
    void testGetSlots_SizeAndOrder() {
        // When
        List<LocalTime> result = scheduleService.getSlots();

        // Then
        assertEquals(10, result.size());

        // Verify order is maintained
        assertTrue(result.get(0).isBefore(result.get(1)));
        assertTrue(result.get(1).isBefore(result.get(2)));
    }

    private TimePeriod createTimePeriod(String start, String end) {
        TimePeriod period = new TimePeriod();
        period.setStart(new DateTime(start));
        period.setEnd(new DateTime(end));
        return period;
    }
}