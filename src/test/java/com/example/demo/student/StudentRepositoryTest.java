package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTestRepo;

    @AfterEach
    void tearDown() {
        underTestRepo.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentEmailExists() {
        // given
        String email = "jamila@gmail.com";
        Student student = new Student(
                "jamila",
                email,
                Gender.FEMALE
        );
        underTestRepo.save(student);
        // when
        Boolean expected = underTestRepo.selectExistsEmail(email);
        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfStudentEmailDoesNotExists() {
        // given
        String email = "jamila@gmail.com";
        // when
        Boolean expected = underTestRepo.selectExistsEmail(email);
        // then
        assertThat(expected).isFalse();
    }
}