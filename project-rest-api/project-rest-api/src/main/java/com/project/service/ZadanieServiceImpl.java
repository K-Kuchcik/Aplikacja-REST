package com.project.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.repository.ProjektRepository;
import com.project.repository.ZadanieRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ZadanieServiceImpl implements ZadanieService {

    private static final Logger logger = LoggerFactory.getLogger(ZadanieServiceImpl.class);

    private final ZadanieRepository zadanieRepository;
    private final ProjektRepository projektRepository;

    @Autowired
    public ZadanieServiceImpl(ZadanieRepository zadanieRepository, ProjektRepository projektRepository) {
        this.zadanieRepository = zadanieRepository;
        this.projektRepository = projektRepository;
    }

    @Override
    public Optional<Zadanie> getZadanie(Integer zadanieId) {
        return zadanieRepository.findById(zadanieId);
    }

    @Override
    public Zadanie setZadanie(Zadanie zadanie) {
        if (zadanie.getProjekt() == null || zadanie.getProjekt().getProjektId() == null) {
            throw new IllegalArgumentException("Zadanie musi zawierać projekt.projektId");
        }

        Integer projektId = zadanie.getProjekt().getProjektId();
        Projekt projekt = projektRepository.findById(projektId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono projektu o ID=" + projektId));

        zadanie.setProjekt(projekt);
        logger.info("Zapisywanie zadania: {} dla projektu {}", zadanie.getNazwa(), projektId);
        return zadanieRepository.save(zadanie);
    }

    @Override
    public void deleteZadanie(Integer zadanieId) {
        logger.info("Usuwanie zadania o ID={}", zadanieId);
        zadanieRepository.deleteById(zadanieId);
    }

    @Override
    public Page<Zadanie> getZadania(Pageable pageable) {
        return zadanieRepository.findAll(pageable);
    }

    @Override
    public Page<Zadanie> getZadaniaProjektu(Integer projektId, Pageable pageable) {
        return zadanieRepository.findZadaniaProjektu(projektId, pageable);
    }
}
