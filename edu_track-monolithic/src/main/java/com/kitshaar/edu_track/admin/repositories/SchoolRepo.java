package com.kitshaar.edu_track.admin.repositories;

import com.kitshaar.edu_track.admin.models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepo extends JpaRepository<School, Integer> {

}
