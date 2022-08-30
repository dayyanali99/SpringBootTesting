package com.example.demo.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.example.demo.student.Gender.FEMALE;
import static com.example.demo.student.Gender.MALE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class StudentControllerTest {
    private final ObjectWriter objectWriter = new ObjectMapper().writer();
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

    private MockMvc mockMvc;
    @Mock
    private StudentService studentService;
    @InjectMocks
    private StudentController underTestController;

    @Captor
    ArgumentCaptor<Student> argumentCaptor;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(underTestController).build();
    }

    @Test
    void getAllStudents() throws Exception {
        // given
        List<Student> students = List.of(STUDENT_1, STUDENT_2);
        given(studentService.getAllStudents()).willReturn(students);
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

        verify(studentService).getAllStudents();
    }

    @Test
    void checkIfGetStudentByIdIsSuccess() throws Exception {

        given(studentService.getStudentById(STUDENT_1.getId())).willReturn(STUDENT_1);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("dayyan"));

        verify(studentService).getStudentById(STUDENT_1.getId());
    }

    @Test
    void checkIfStudentIsAdded() throws Exception {
        // given
        String student = objectWriter.writeValueAsString(STUDENT_2);
        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(student);
        // then
        mockMvc
                .perform(mockRequest)
                .andExpect(status().isCreated());

        verify(studentService).addStudent(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(STUDENT_2);
    }

    @Test
    void checkIfGivenAStudentTheRecordIsUpdatedSuccessfully() throws Exception {
        // given
        STUDENT_1.setName("shamoon");
        String studentAsString = objectWriter.writeValueAsString(STUDENT_1);
        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentAsString);
        // then
        mockMvc
                .perform(mockRequest)
                .andExpect(status().isOk());

        verify(studentService).updateStudent(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo("shamoon");
    }

    @Test
    void checkIfDeleteStudentWorks() throws Exception {
        // given
        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/api/v1/students/2")
                .contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc
                .perform(mockRequest)
                .andExpect(status().isOk());
    }
}