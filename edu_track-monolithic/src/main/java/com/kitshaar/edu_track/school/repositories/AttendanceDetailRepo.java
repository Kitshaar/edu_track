package com.kitshaar.edu_track.school.repositories;

import com.kitshaar.edu_track.school.models.AttendanceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceDetailRepo extends JpaRepository<AttendanceDetail, Long> {
    @Query("SELECT ad FROM AttendanceDetail ad JOIN FETCH ad.student")
    List<AttendanceDetail> findAllWithStudentTable();
}
