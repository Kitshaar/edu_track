package com.kitshaar.edu_track.school.repositories;

import com.kitshaar.edu_track.school.models.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterRepo extends JpaRepository<Register, Long> {

    @Query("SELECT r FROM Register r JOIN FETCH r.classTable")
    List<Register> findAllWithClassTable();

}
