package com.project.service;

import com.project.model.Projekt;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ProjektService {

    Flux<Projekt> findAll();

    Mono<Projekt> findById(Integer projektId);

    Flux<Projekt> findByNazwa(String nazwa, Pageable pageable);

    Mono<Projekt> create(Projekt projekt);

    Mono<Projekt> update(Projekt projekt);

    Mono<Void> delete(Integer projektId);

    Mono<Void> deleteAll();

    Flux<Projekt> findAllBy(Pageable pageable);

    Mono<Long> count();

    Mono<Long> countByNazwa(String nazwa);

    Mono<Map<String, Object>> summary(Integer projektId);
}