package com.eugenepelipets.popcornlist.mapper;

import org.mapstruct.Mapper;
import com.eugenepelipets.popcornlist.dto.GenreDto;
import com.eugenepelipets.popcornlist.model.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toDto(Genre genre);
    Genre toEntity(GenreDto dto);
}