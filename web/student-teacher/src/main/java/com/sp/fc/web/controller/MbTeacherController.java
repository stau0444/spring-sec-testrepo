package com.sp.fc.web.controller;

import com.sp.fc.web.student.Student;
import com.sp.fc.web.teacher.Teacher;
import com.sp.fc.web.student.StudentManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class MbTeacherController{

    private final StudentManager studentManager;

    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    @GetMapping("/students")
    public List<Student> studentList(@AuthenticationPrincipal Teacher teacher){
        return studentManager.myStudents(teacher.getId());
    }
}
