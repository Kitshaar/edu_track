package com.kitshaar.edu_track.admin.controllers;

import com.kitshaar.edu_track.admin.models.Payment;
import com.kitshaar.edu_track.admin.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class PaymentController {

	@Autowired
    PaymentService service;

//	GET /payments - Get all payments.
	@GetMapping("payments")
	public ResponseEntity<List<Payment>> getAllPayments() {
		return service.getAllPayments();
	}

//	GET /payments/{id} - Get payment by ID.
	@GetMapping("payments/{id}")
	public ResponseEntity<Payment> getPayment(@PathVariable Integer id) {
		return service.getPayment(id);
	}

//	POST /payments - Create a new payment.
	@PostMapping("payments")
	public ResponseEntity<String> addPayment(@RequestBody Payment payment) {
		return service.addPayment(payment);
	}

//	PUT /payments/{id} - Update an existing payment.
	@PutMapping("payments/{id}")
	public ResponseEntity<String> updatePayment(@PathVariable Integer id, @RequestBody Payment payment) {
		return service.updatePayment(id, payment);
	}

//	DELETE /payments/{id} - Delete a payment.
	@DeleteMapping("payments/{id}")
	public ResponseEntity<String> deletePayment(@PathVariable Integer id) {
		return service.deletePayment(id);
	}
}
