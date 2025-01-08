package com.kitshaar.edu_track.school.repositories;

import com.kitshaar.edu_track.school.models.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRepo extends JpaRepository<Register, Long> {
}
