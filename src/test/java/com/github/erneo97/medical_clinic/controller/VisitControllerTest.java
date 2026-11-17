package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.dto.BookingSummaryDto;
import com.github.erneo97.medical_clinic.dto.doctor.DoctorDto;
import com.github.erneo97.medical_clinic.dto.patient.PatientDto;
import com.github.erneo97.medical_clinic.dto.visit.VisitDto;
import com.github.erneo97.medical_clinic.exeption.InvalidSortPropertyException;
import com.github.erneo97.medical_clinic.exeption.PatientNotFoundException;
import com.github.erneo97.medical_clinic.exeption.VisitAlreadyBookedException;
import com.github.erneo97.medical_clinic.facade.VisitBookingFacade;
import com.github.erneo97.medical_clinic.mapper.VisitMapperImpl;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Role;
import com.github.erneo97.medical_clinic.model.Specialization;
import com.github.erneo97.medical_clinic.model.Visit;
import com.github.erneo97.medical_clinic.service.DoctorService;
import com.github.erneo97.medical_clinic.service.PatientService;
import com.github.erneo97.medical_clinic.service.VisitService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@WebMvcTest(VisitController.class)
@Import(VisitMapperImpl.class)
class VisitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;
    @MockitoBean
    private DoctorService doctorService;
    @MockitoBean
    private VisitService visitService;
    @MockitoBean
    private VisitBookingFacade facade;

    private static Visit visitById(long id) {
        LocalDateTime start = LocalDateTime.of(2030, 1, 1, 15, 0, 0).plusHours(id);
        return Visit.builder().id(id).doctor(Doctor.builder().id(1L).build())
                .startsAt(start).endsAt(start.plusMinutes(45)).build();
    }

    @Test
    void getVisit_pageSizeSort_passesPageableAndReturnsPageResponse() throws Exception {
        // given
        when(visitService.findAll(any())).thenAnswer(invocation -> new PageImpl<>(
                List.of(visitById(6), visitById(7)), invocation.getArgument(0), 12));
        // when + then
        mockMvc.perform(get("/visits")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "startsAt,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(12))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.content[0].id").value(6));
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(visitService).findAll(captor.capture());

        assertEquals(1, captor.getValue().getPageNumber());
        assertEquals(5, captor.getValue().getPageSize());
    }

    @Test
    void getSessions_noParameters_usesPageableDefault() throws Exception {
        // given
        when(visitService.findAll(any())).thenAnswer(invocation -> new PageImpl<>(
                List.of(), invocation.getArgument(0), 0));
        // when
        mockMvc.perform(get("/visits")).andExpect(status().isOk());
        // then
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(visitService).findAll(captor.capture());
        assertEquals(0, captor.getValue().getPageNumber());
        assertEquals(5, captor.getValue().getPageSize());
        assertEquals(Sort.by("startsAt"), captor.getValue().getSort());
    }

    @Test
    void getSessions_sizeAboveLimit_isCappedAt100() throws Exception {
        // given
        when(visitService.findAll(any())).thenAnswer(invocation -> new PageImpl<>(
                List.of(), invocation.getArgument(0), 0));
        // when
        mockMvc.perform(get("/visits").param("size", "5000")).andExpect(status().isOk());
        // then
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(visitService).findAll(captor.capture());
        assertEquals(100, captor.getValue().getPageSize());
    }

    @Test
    void getSessions_sortNotAllowed_returns400WithAllowedFields() throws Exception {
        // given
        when(visitService.findAll(any())).thenThrow(new InvalidSortPropertyException(
                "abrakadabra", List.of("startsAt", "endsAt")));

        // when + then
        mockMvc.perform(get("/visits").param("sort", "abrakadabra"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail")
                        .value("cannot sort by 'abrakadabra', allowed: startsAt, endsAt"));
    }

    @Test
    void bookVisit_free_Slot_returnSummaryFromFacade() throws Exception {
        // given
        LocalDateTime start = LocalDateTime.of(2030, 3, 10, 9, 0);
        Long visitId = 3L, patientId = 2L, doctorId = 1L;


        when(facade.book(visitId, patientId)).thenReturn(new BookingSummaryDto(
                new VisitDto(visitId, doctorId, patientId, start, start.plusMinutes(45)),
                new PatientDto(patientId, "anna.nowak@example.com", "Anna", "Nowak", "500600700",
                        LocalDate.of(1990, 5, 12)),
                new DoctorDto(doctorId, doctorId, "marek@marek.com", "Marek", "Kwiecien", Specialization.CARDIOLOGIST,
                        List.of("FitClub Centrum")),
                3));

        // when + then
        mockMvc.perform(patch(String.format("/visits/%s/book/%s", visitId, patientId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visit.id").value(visitId))
                .andExpect(jsonPath("$.patient.firstName").value("Anna"))
                .andExpect(jsonPath("$.doctor.lastName").value("Kwiecien"))
                .andExpect(jsonPath("$.freeSlotsThatDay").value(3));
        verifyNoInteractions(visitService);
    }

    @Test
    void bookVisit_alreadyBooked_return409() throws Exception {
        // given
        when(facade.book(any(), any())).thenThrow(new VisitAlreadyBookedException("This visit is already booked"));

        // when + then
        mockMvc.perform(patch("/visits/3/book/44"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail")
                        .value("This visit is already booked"));
    }

    @Test
    void bookVisit_patientNotExists_return404() throws Exception {
        // given
        when(facade.book(any(), any())).thenThrow(new PatientNotFoundException("Patient not found"));

        // when + then
        mockMvc.perform(patch("/visits/3/book/44"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail")
                        .value("Patient not found"));
    }
}