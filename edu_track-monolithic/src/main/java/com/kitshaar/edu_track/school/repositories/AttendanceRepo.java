package com.kitshaar.edu_track.school.repositories;

import com.kitshaar.edu_track.school.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a JOIN FETCH a.classTable")
    List<Attendance> findAllWithClassTable();
}
