package com.github.erneo97.medical_clinic.controller;

import com.github.erneo97.medical_clinic.mapper.VisitMapperImpl;
import com.github.erneo97.medical_clinic.model.Doctor;
import com.github.erneo97.medical_clinic.model.Visit;
import com.github.erneo97.medical_clinic.service.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(VisitController.class)
@Import(VisitMapperImpl.class)
class VisitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisitService visitService;

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
}