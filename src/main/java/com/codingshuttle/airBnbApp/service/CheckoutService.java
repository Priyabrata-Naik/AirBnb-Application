package com.codingshuttle.airBnbApp.service;

import com.codingshuttle.airBnbApp.entity.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);

}
