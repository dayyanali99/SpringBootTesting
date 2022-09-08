package com.example.demo.student;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.example.demo.student.Gender.FEMALE;
import static com.example.demo.student.Gender.MALE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
//    @Autowired
    @MockBean
    private StudentService studentService;

    private final Student STUDENT_1 = new Student(
            1L,
            "dayyan",
            "dayyan@co.uk",
            MALE
    );
    private final Student STUDENT_2 = new Student(
            2L,
            "arina",
            "arina@co.uk",
            FEMALE
    );

    @Test
    void getAllStudents() throws Exception {
        // given
//        List<Student> students = List.of(STUDENT_1, STUDENT_2);
//        studentService.addStudent(STUDENT_1);
//        studentService.addStudent(STUDENT_2);
        given(studentService.getAllStudents()).willReturn(List.of(STUDENT_1, STUDENT_2));
        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        // then
        mockMvc
                .perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("arina"));
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