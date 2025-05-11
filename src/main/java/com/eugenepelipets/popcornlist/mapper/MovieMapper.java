package com.eugenepelipets.popcornlist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.eugenepelipets.popcornlist.dto.*;
import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.model.Movie;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {GenreMapper.class, MpaMapper.class})
public interface MovieMapper {
    @Mapping(source = "mpa.id", target = "mpa.id")
    @Mapping(source = "genreIds", target = "genres", qualifiedByName = "mapGenreIds")
    Movie toEntity(MovieRequestDto dto);

    @Mapping(source = "genres", target = "genres", qualifiedByName = "mapGenres")
    MovieResponseDto toDto(Movie movie);

     @Named("mapGenreIds")
    default List<Genre> mapGenreIds(List<Integer> ids) {
        return ids == null ? null
                : ids.stream()
                        .map(id -> {
                            Genre g = new Genre();
                            g.setId(id);
                            return g;
                        })
                        .collect(Collectors.toList());
    }

    @Named("mapGenres")
    default List<GenreDto> mapGenres(List<Genre> genres) {
        return genres == null ? null
                : genres.stream()
                        .map(g -> new GenreDto(g.getId(), g.getName()))
                        .collect(Collectors.toList());
    }
}
