package com.eugenepelipets.popcornlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.storage.movie.GenreDbStorage;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    public Collection<Genre> findAll() {
        return genreDbStorage.findAll();
    }

    public Genre getGenreById(int id) {
        return genreDbStorage.getGenreById(id);
    }
}