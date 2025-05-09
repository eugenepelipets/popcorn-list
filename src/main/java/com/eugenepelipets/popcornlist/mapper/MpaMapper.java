package com.eugenepelipets.popcornlist.mapper;

import org.mapstruct.Mapper;
import com.eugenepelipets.popcornlist.dto.MpaDto;
import com.eugenepelipets.popcornlist.model.Mpa;

@Mapper(componentModel = "spring")
public interface MpaMapper {
    MpaDto toDto(Mpa mpa);
    Mpa toEntity(MpaDto dto);
}