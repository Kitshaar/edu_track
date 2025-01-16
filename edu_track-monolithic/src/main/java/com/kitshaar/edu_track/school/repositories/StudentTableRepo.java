package com.kitshaar.edu_track.school.repositories;


import com.kitshaar.edu_track.school.Dto.students.GetStudentTableDto;
import com.kitshaar.edu_track.school.models.StudentTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTableRepo extends JpaRepository<StudentTable, Long> {

    @Query("SELECT new com.kitshaar.edu_track.school.Dto.students.GetStudentTableDto( " +
            "s.studentId, s.name, p.name, p.address, c.className) " +
            "FROM StudentTable s " +
            "JOIN s.parent p " +
            "JOIN s.classTable c")
    List<GetStudentTableDto> findAllStudentDetails();
}
