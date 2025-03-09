package xyz.eliabdiel.service;

import java.util.Map;
import java.util.function.Function;

public class MockBookingStatusService implements Function<MockBookingStatusService.BookingRequest, MockBookingStatusService.BookingResponse> {

    @Override
    public BookingResponse apply(BookingRequest bookingRequest) {
        Status status = BOOKINGS.getOrDefault(bookingRequest.bookingId(), Status.PENDING);
        return new BookingResponse(bookingRequest.bookingId, status.name());
    }

    public record BookingRequest(String bookingId){}
    public record BookingResponse(String bookingId, String status){}
    public enum Status { PENDING, CONFIRMED, CANCELLED }

    private static final Map<String, Status> BOOKINGS =
            Map.of(
                    "H001", Status.PENDING,
                    "H002", Status.CONFIRMED,
                    "H003", Status.CANCELLED
            );
}