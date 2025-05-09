package com.eugenepelipets.popcornlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eugenepelipets.popcornlist.model.Mpa;
import com.eugenepelipets.popcornlist.storage.movie.MpaDbStorage;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    public Collection<Mpa> findAll() {
        return mpaDbStorage.findAll();
    }

    public Mpa getMpaById(int id) {
        return mpaDbStorage.getMpaById(id);
    }
}