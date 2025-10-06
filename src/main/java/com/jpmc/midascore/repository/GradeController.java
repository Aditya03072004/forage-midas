package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.Grade;
import com.jpmc.midascore.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository;

    @PostMapping("/grades")
    public ResponseEntity<String> saveGrades(@RequestBody List<Grade> grades) {
        gradeRepository.saveAll(grades);
        return ResponseEntity.ok("Grades saved successfully!");
    }
}


