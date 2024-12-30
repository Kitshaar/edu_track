package com.kitshaar.edu_track.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.kitshaar.edu_track.models.User_table;

@Repository
public interface UserTableRepo extends JpaRepository<User_table, Integer> {

	
}
