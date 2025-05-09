package com.eugenepelipets.popcornlist.storage.movie;

import java.util.Collection;

import com.eugenepelipets.popcornlist.model.Genre;

public interface GenreStorage {

    Collection<Genre> findAll();

    Genre getGenreById(int id);

}