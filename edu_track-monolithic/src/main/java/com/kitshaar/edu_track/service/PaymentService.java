package com.kitshaar.edu_track.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kitshaar.edu_track.models.Payment;
import com.kitshaar.edu_track.repositories.PaymentRepo;

@Service
public class PaymentService {

	@Autowired
	PaymentRepo repo;

	@Transactional
	public ResponseEntity<List<Payment>> getAllPayments() {
		try {
			return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	@Transactional
	public ResponseEntity<Payment> getPayment(Integer id) {
		try {
			return new ResponseEntity<>(repo.findById(id).get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new Payment(), HttpStatus.BAD_REQUEST);
	}

	@Transactional
	public ResponseEntity<String> addPayment(Payment payment) {
		if (payment == null) {
			return new ResponseEntity<>("Entity being sent is null. It is not allowed", HttpStatus.BAD_REQUEST);
		}
		try {
			repo.save(payment); // Try to save the user
			return new ResponseEntity<>("Success", HttpStatus.CREATED);

		} catch (OptimisticLockingFailureException e) {
			// Handle OptimisticLockingFailureException
			return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
					HttpStatus.CONFLICT);
		} catch (Exception e) {
			// Catch any other exceptions that might occur
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public ResponseEntity<String> updatePayment(Integer id, Payment payment) {
		if (id == null || !repo.existsById(id)) {
			return new ResponseEntity<>("Id being sent is null or doesn't exist. It is not allowed",
					HttpStatus.BAD_REQUEST);
		}
		if (payment == null) {
			return new ResponseEntity<>("Entity being sent is null. It is not allowed", HttpStatus.BAD_REQUEST);
		}
		try {
			Payment oldEntry = repo.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("User with id " + id + " does not exist"));

			if (payment.getSchoolId() != null)
				oldEntry.setSchoolId(payment.getSchoolId());
			if (payment.getPaymentId() != null)
				oldEntry.setPaymentId(payment.getPaymentId());
			if (payment.getAmount() != null)
				oldEntry.setAmount(payment.getAmount());
			if (payment.getDueDate() != null)
				oldEntry.setDueDate(payment.getDueDate());
			if (payment.getStatus() != null)
				oldEntry.setStatus(payment.getStatus());

			// Save the updated entity
			repo.save(oldEntry);
			return new ResponseEntity<>("User updated successfully.", HttpStatus.OK);

		} catch (OptimisticLockingFailureException e) {
			// Handle OptimisticLockingFailureException
			return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
					HttpStatus.CONFLICT);
		} catch (Exception e) {
			// Catch any other exceptions that might occur
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public ResponseEntity<String> deletePayment(Integer id) {
		if (id == null) {
			return new ResponseEntity<>("Id being sent is null. It is not allowed", HttpStatus.BAD_REQUEST);
		}

		try {
			// Check if the user exists first
			if (!repo.existsById(id)) {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			}

			// Try to delete the user
			repo.deleteById(id);

			return new ResponseEntity<>("Successfull deletion", HttpStatus.OK);
		} catch (Exception e) {
			// Catch any other exceptions that might occur
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
