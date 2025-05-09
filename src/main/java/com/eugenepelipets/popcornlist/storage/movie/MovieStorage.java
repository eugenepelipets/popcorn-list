package com.eugenepelipets.popcornlist.storage.movie;

import java.util.Collection;

import com.eugenepelipets.popcornlist.model.Movie;

public interface MovieStorage {

    Movie create(Movie movie);

    Movie update(Movie movie);

    Collection<Movie> findAll();

    Movie getMovieById(int id);

}

