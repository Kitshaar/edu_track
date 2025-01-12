package com.kitshaar.edu_track.school.controllers;

import com.kitshaar.edu_track.school.Dto.registers.GetRegisterDto;
import com.kitshaar.edu_track.school.Dto.registers.RegisterDto;
import com.kitshaar.edu_track.school.services.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequestMapping("school")
@RestController
public class RegisterController {

    @Autowired
    private RegisterService service;


    @GetMapping("registers")
    public ResponseEntity<List<GetRegisterDto>> getAllRegisters()
    {
        return service.getAllRegisters();
    }

    @GetMapping("registers/{id}")
    public ResponseEntity<GetRegisterDto> getAllRegisters(@PathVariable Long id)
    {
        return service.getRegister(id);
    }

    @PostMapping("/registers")
    public ResponseEntity<String> addRegister(@Valid @RequestBody RegisterDto registerDto)
    {
        return service.addRegister(registerDto);
    }

    @PutMapping("/registers/{id}")
    public ResponseEntity<String> updateRegister( @PathVariable Long id, @RequestBody RegisterDto registerDto)
    {
        return  service.update(id, registerDto);
    }

    @DeleteMapping("/registers/{id}")
    public ResponseEntity<String> deleteRegister (@PathVariable Long id)
    {
        return service.deleteRegister(id);
    }
}
