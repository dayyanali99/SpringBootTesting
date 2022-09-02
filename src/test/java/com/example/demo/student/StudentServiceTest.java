package com.example.demo.student;

import com.example.demo.student.exception.ApiRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService underTest;
    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;


    @Test
    void canGetAllStudents() {
        // given
        // when
        underTest.getAllStudents();
        // then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        // given
        Student student = new Student(
                "jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );
        given(studentRepository.selectExistsEmail(anyString())).willReturn(false);
        // when
        underTest.addStudent(student);
        // then
        verify(studentRepository).save(studentArgumentCaptor.capture());
        assertThat(studentArgumentCaptor.getValue()).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        Student student = new Student(
                "jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );
        given(studentRepository.selectExistsEmail(anyString())).willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent() {
        // given
        long id = 10;
        given(studentRepository.existsById(id)).willReturn(true);
        // when
        underTest.deleteStudent(id);
        // then
        verify(studentRepository).deleteById(id);
    }

    @Test
    void willThrowWhenStudentDoesNotExist() {

        long id = 10;
        // given
        given(studentRepository.existsById(anyLong())).willReturn(false);
        // when
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + id + " does not exists");

//        assertThrows(StudentNotFoundException.class, () -> underTest.deleteStudent(id));
        // then
        verify(studentRepository, never()).deleteById(id);
    }
}