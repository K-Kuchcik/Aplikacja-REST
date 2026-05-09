package com.project.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.Projekt;
import com.project.repository.ProjektRepository;
import com.project.repository.ZadanieRepository;

@Service
public class ProjektServiceImpl implements ProjektService {

    private static final Logger logger = LoggerFactory.getLogger(ProjektServiceImpl.class);

    private final ProjektRepository projektRepository;
    private final ZadanieRepository zadanieRepository;

    @Autowired
    public ProjektServiceImpl(ProjektRepository projektRepository, ZadanieRepository zadanieRepository) {
        this.projektRepository = projektRepository;
        this.zadanieRepository = zadanieRepository;
    }

    @Override
    public Optional<Projekt> getProjekt(Integer projektId) {
        return projektRepository.findById(projektId);
    }

    @Override
    public Projekt setProjekt(Projekt projekt) {
        logger.info("Zapisywanie projektu: {}", projekt.getNazwa());
        return projektRepository.save(projekt);
    }

    @Override
    @Transactional
    public void deleteProjekt(Integer projektId) {
        logger.info("Usuwanie projektu o ID={}", projektId);
        projektRepository.findById(projektId).ifPresent(projekt -> {
            zadanieRepository.deleteAll(zadanieRepository.findZadaniaProjektu(projektId));
            projekt.getStudenci().clear();
            projektRepository.save(projekt);
            projektRepository.delete(projekt);
        });
    }

    @Override
    public Page<Projekt> getProjekty(Pageable pageable) {
        return projektRepository.findAll(pageable);
    }

    @Override
    public Page<Projekt> searchByNazwa(String nazwa, Pageable pageable) {
        return projektRepository.findByNazwaContainingIgnoreCase(nazwa, pageable);
    }
}
