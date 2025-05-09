package com.eugenepelipets.popcornlist.storage.movie;

import java.util.Collection;

import com.eugenepelipets.popcornlist.model.Mpa;

public interface MpaStorage {

    Collection<Mpa> findAll();

    Mpa getMpaById(int id);

}