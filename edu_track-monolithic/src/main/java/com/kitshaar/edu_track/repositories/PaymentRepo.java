package com.kitshaar.edu_track.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kitshaar.edu_track.models.Payment;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {

}
