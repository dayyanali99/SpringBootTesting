package com.example.demo.student;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class StudentServiceIntegrationTest {

    @Autowired
    private StudentService underTestStudentService;

    @MockBean
    private StudentRepository studentRepository;


    @Test
    void canGetAllStudents() {
        // given
        // when
        underTestStudentService.getAllStudents();
        // then
        verify(studentRepository).findAll();
    }

    @Test
    @Disabled
    void getStudentById() {
    }

    @Test
    @Disabled
    void addStudent() {
    }

    @Test
    @Disabled
    void updateStudent() {
    }
}