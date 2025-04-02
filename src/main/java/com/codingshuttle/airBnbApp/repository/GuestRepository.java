package com.codingshuttle.airBnbApp.repository;

import com.codingshuttle.airBnbApp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}