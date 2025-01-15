package com.kitshaar.edu_track.school.controllers;


import com.kitshaar.edu_track.school.Dto.ParentTableDto;
import com.kitshaar.edu_track.school.services.ParentTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("school")
public class ParentTableController {

    @Autowired
    private ParentTableService service;

    @GetMapping("/parents")
    public ResponseEntity<List<ParentTableDto>> getAllParents()
    {
        return service.getAllParents();
    }

    @GetMapping("/parents/{id}")
    public ResponseEntity<ParentTableDto> getParent(@PathVariable Long id)
    {
        return service.getParent(id);
    }

    @PostMapping("/parents")
    public ResponseEntity<String> addParent(@Valid @RequestBody ParentTableDto parentTableDto)
    {
        return service.addParent(parentTableDto);
    }

    @PutMapping("/parents/{id}")
    public ResponseEntity<String> updateParent(@RequestBody ParentTableDto parentTableDto, @PathVariable Long id)
    {
        return service.updateParent(parentTableDto, id);
    }

    @DeleteMapping("/parents/{id}")
    public ResponseEntity<String> deleteParent(@PathVariable Long id)
    {
        return service.deleteParent(id);
    }

}
