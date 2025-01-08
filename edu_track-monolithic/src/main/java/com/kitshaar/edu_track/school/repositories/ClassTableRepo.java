package com.kitshaar.edu_track.school.repositories;

import com.kitshaar.edu_track.school.models.ClassTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassTableRepo extends JpaRepository<ClassTable, Long> {
}
