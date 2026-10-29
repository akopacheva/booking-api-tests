package com.example.booking.models;

import lombok.Data;

/** Response of POST /booking */
@Data
public class BookingResponse {
    private Integer bookingid;
    private Booking booking;
}
