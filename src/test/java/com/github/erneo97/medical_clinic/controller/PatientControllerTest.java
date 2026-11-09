package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.config.SecurityConfig;
import com.github.erneo97.medical_clinic.dto.patient.PatientCreateCommand;
import com.github.erneo97.medical_clinic.exeption.PatientAlreadyExistsException;
import com.github.erneo97.medical_clinic.exeption.PatientNotFoundException;
import com.github.erneo97.medical_clinic.mapper.PatientMapper;
import com.github.erneo97.medical_clinic.mapper.PatientMapperImpl;
import com.github.erneo97.medical_clinic.model.Patient;
import com.github.erneo97.medical_clinic.model.User;
import com.github.erneo97.medical_clinic.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@Import({PatientMapperImpl.class, SecurityConfig.class})
class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientMapper patientMapper;

    @MockitoBean
    private PatientService patientService;

    @Test
    @WithMockUser
    void findAll_returnsPatients() throws Exception {
        // given
        when(patientService.findAll()).thenReturn(List.of(getPatient()));

        // when
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("anna@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("Anna"))
                .andExpect(jsonPath("$[0].lastName").value("Nowak"));

        // then
        verify(patientService).findAll();
    }

    @Test
    @WithMockUser
    void findById_patientExists_returnsPatientWithoutIdCardNo() throws Exception {
        // given
        Patient patient = getPatient(
                1L, "anna@example.com", "Anna", "Nowak"
        );
        when(patientService.findById(1L)).thenReturn(patient);

        mockMvc.perform(get("/patients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("anna@example.com"))
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Nowak"))
                .andExpect(jsonPath("$.idCardNo").doesNotExist());
    }

    @Test
    void findById_patientExistsNoAuthorization_returns401NoAuthorization() throws Exception {
        // given
        Patient patient = getPatient(
                1L, "anna@example.com", "Anna", "Nowak"
        );
        when(patientService.findById(1L)).thenReturn(patient);

        mockMvc.perform(get("/patients/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void findById_patientDoesNotExist_returns404() throws Exception {
        // given
        PatientNotFoundException exception = new PatientNotFoundException(1L);

        when(patientService.findById(1L)).thenThrow(exception);

        // when
        mockMvc.perform(get("/patients/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(exception.getMessage()));

        // then
        verify(patientService).findById(1L);
    }

    @Test
    @WithMockUser
    void findAll_returnsTwoPatients() throws Exception {
        // given
        Patient first = getPatient(1L, "anna@example.com", "Anna", "Nowak");
        Patient second = getPatient(2L, "jan@example.com", "Jan", "Kowalski");

        when(patientService.findAll()).thenReturn(List.of(first, second));

        // when + then
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Jan"))
                .andExpect(jsonPath("$[1].lastName").value("Kowalski"));
    }

    @Test
    void create_validPatient_returns201() throws Exception {
        when(patientService.create(any()))
                .thenReturn(getPatient(1L, "anna@example.com", "Anna", "Nowak"));

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPatientJson()))
                .andExpect(status().isCreated());

        verify(patientService).create(any());
    }

    @Test
    void create_emptyFirstName_returns400() throws Exception {
        String invalidJson = """
                {
                    "email": "anna@example.com",
                    "password": "Password123!",
                    "idCardNo": "ABC123456",
                    "firstName": "",
                    "lastName": "Nowak",
                    "phoneNumber": "123456789",
                    "birthday": "1995-05-10"
                }
                """;

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("firstName")));

        verify(patientService, never()).create(any());
    }

    @Test
    void create_emailAlreadyExists_returns409() throws Exception {
        PatientCreateCommand command = getPatientCreateCommand();
        PatientAlreadyExistsException exception =
                new PatientAlreadyExistsException("Patient with email " + command.email() + " already exists", HttpStatus.CONFLICT);

        when(patientService.create(any())).thenThrow(exception);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPatientJson()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(exception.getMessage()));

        verify(patientService).create(any());
    }

    @Test
    @WithMockUser
    void delete_patientExists_returns204() throws Exception {
        mockMvc.perform(delete("/patients/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(patientService).removeById(1L);
    }

    @Test
    @WithMockUser
    void delete_patientDoesNotExist_returns404() throws Exception {
        doThrow(new PatientNotFoundException(1L))
                .when(patientService).removeById(1L);

        mockMvc.perform(delete("/patients/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(patientService).removeById(1L);
    }

    @Test
    void create_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail")
                        .value("request body is not valid JSON"));

        verifyNoInteractions(patientService);
    }

    @Test
    @WithMockUser
    void findById_invalidIdType_returns400() throws Exception {
        mockMvc.perform(get("/patients/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").isNotEmpty());

        verifyNoInteractions(patientService);
    }

    @Test
    @WithMockUser
    void findById_unexpectedException_returns500() throws Exception {
        when(patientService.findById(1L))
                .thenThrow(new IllegalStateException("Database connection failed"));

        mockMvc.perform(get("/patients/{id}", 1L))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("Unknown error"))
                .andExpect(content().string(
                        org.hamcrest.Matchers.not(
                                org.hamcrest.Matchers.containsString("Database connection failed")
                        )
                ));

        verify(patientService).findById(1L);
    }

    @Test
    @WithMockUser
    void delete_withoutId_returns405() throws Exception {
        mockMvc.perform(delete("/patients"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.detail")
                        .value("method DELETE is not supported here"));

        verifyNoInteractions(patientService);
    }

    private Patient getPatient(Long id, String email, String firstName, String lastName) {
        User user = User.builder()
                .email(email)
                .build();

        return Patient.builder()
                .id(id)
                .user(user)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber("+48123456789")
                .birthday(LocalDate.of(1995, 5, 10))
                .build();
    }

    private PatientCreateCommand getPatientCreateCommand() {
        return PatientCreateCommand.builder()
                .email("anna@example.com")
                .password("Password123!")
                .idCardNo("ABC123456")
                .firstName("Anna")
                .lastName("Nowak")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(1995, 5, 10))
                .build();
    }

    private Patient getPatient() {
        User user = User.builder()
                .email("anna@example.com")
                .build();

        return Patient.builder()
                .id(1L)
                .user(user)
                .firstName("Anna")
                .lastName("Nowak")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(1995, 5, 10))
                .build();
    }

    private String validPatientJson() {
        return """
                {
                    "email": "anna@example.com",
                    "password": "Password123!",
                    "idCardNo": "ABC123456",
                    "firstName": "Anna",
                    "lastName": "Nowak",
                    "phoneNumber": "123456789",
                    "birthday": "1995-05-10"
                }
                """;
    }
}