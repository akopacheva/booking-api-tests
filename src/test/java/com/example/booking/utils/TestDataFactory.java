package com.example.booking.utils;

import com.example.booking.models.Booking;
import com.example.booking.models.BookingDates;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Generates random, valid booking data so that tests never depend on hardcoded values. */
public final class TestDataFactory {

    private static final Faker FAKER = new Faker();
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    private TestDataFactory() {}

    public static Booking randomBooking() {
        LocalDate checkin = LocalDate.now().plusDays(FAKER.number().numberBetween(1, 60));
        LocalDate checkout = checkin.plusDays(FAKER.number().numberBetween(1, 14));

        return Booking.builder()
                .firstname(FAKER.name().firstName())
                .lastname(FAKER.name().lastName())
                .totalprice(FAKER.number().numberBetween(50, 2000))
                .depositpaid(FAKER.bool().bool())
                .bookingdates(BookingDates.builder()
                        .checkin(checkin.format(ISO))
                        .checkout(checkout.format(ISO))
                        .build())
                .additionalneeds(FAKER.options().option("Breakfast", "Late checkout", "Sea view", "Parking"))
                .build();
    }
}
