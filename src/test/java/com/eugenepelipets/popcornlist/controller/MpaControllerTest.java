package com.eugenepelipets.popcornlist.controller;

import com.eugenepelipets.popcornlist.dto.MpaDto;
import com.eugenepelipets.popcornlist.mapper.MpaMapper;
import com.eugenepelipets.popcornlist.model.Mpa;
import com.eugenepelipets.popcornlist.service.MpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MpaControllerTest {

    @Mock
    private MpaService mpaService;

    @Mock
    private MpaMapper mpaMapper;

    @InjectMocks
    private MpaController mpaController;

    private Mpa testMpa;
    private MpaDto testDto;

    @BeforeEach
    void setUp() {
        testMpa = new Mpa(1, "PG-13");

        testDto = new MpaDto();
        testDto.setId(1);
        testDto.setName("PG-13");
    }

    @Test
    void getAllMpaRatings_ShouldReturnMpas() {
        when(mpaService.findAll()).thenReturn(Collections.singletonList(testMpa));
        when(mpaMapper.toDto(testMpa)).thenReturn(testDto);

        ResponseEntity<List<MpaDto>> response = mpaController.getAllMpaRatings();

        assertEquals(1, response.getBody().size());
        assertEquals(testDto, response.getBody().get(0));
        verify(mpaService, times(1)).findAll();
        verify(mpaMapper, times(1)).toDto(testMpa);
    }

    @Test
    void getMpaRatingById_ShouldReturnMpa() {
        when(mpaService.getMpaById(1)).thenReturn(testMpa);
        when(mpaMapper.toDto(testMpa)).thenReturn(testDto);

        ResponseEntity<MpaDto> response = mpaController.getMpaRatingById(1);

        assertEquals(testDto, response.getBody());
        verify(mpaService, times(1)).getMpaById(1);
        verify(mpaMapper, times(1)).toDto(testMpa);
    }
}
