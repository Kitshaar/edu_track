package com.kitshaar.edu_track.admin.repositories;

import com.kitshaar.edu_track.admin.models.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTableRepo extends JpaRepository<UserTable, Integer> {

	
}
