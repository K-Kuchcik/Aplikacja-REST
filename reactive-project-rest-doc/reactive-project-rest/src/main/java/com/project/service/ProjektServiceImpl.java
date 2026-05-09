package com.project.service;

import com.project.model.Projekt;
import com.project.repository.ProjektRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjektServiceImpl implements ProjektService {

    private final ProjektRepository projektRepository;

    @Override
    public Flux<Projekt> findAll() {
        return projektRepository.findAll();
    }

    @Override
    public Mono<Projekt> findById(Integer projektId) {
        return projektRepository.findById(projektId);
    }

    @Override
    public Flux<Projekt> findByNazwa(String nazwa, Pageable pageable) {
        return projektRepository.findByNazwaContainingIgnoreCase(nazwa, pageable);
    }

    @Override
    public Mono<Projekt> create(Projekt projekt) {
        projekt.setProjektId(null);
        return projektRepository.save(projekt);
    }

    @Override
    public Mono<Projekt> update(Projekt projekt) {
        if (projekt.getProjektId() == null) {
            return Mono.empty();
        }

        return projektRepository.existsById(projekt.getProjektId())
                .flatMap(exists -> exists ? projektRepository.save(projekt) : Mono.empty());
    }

    @Override
    public Mono<Void> delete(Integer projektId) {
        return projektRepository.deleteById(projektId);
    }

    @Override
    public Mono<Void> deleteAll() {
        return projektRepository.deleteAll();
    }

    @Override
    public Flux<Projekt> findAllBy(Pageable pageable) {
        return projektRepository.findAll()
                .skip(pageable.getOffset())
                .take(pageable.getPageSize());
    }

    @Override
    public Mono<Long> count() {
        return projektRepository.count();
    }

    @Override
    public Mono<Long> countByNazwa(String nazwa) {
        return projektRepository.countByNazwaContainingIgnoreCase(nazwa);
    }

    @Override
    public Mono<Map<String, Object>> summary(Integer projektId) {
        return findById(projektId)
                .map(projekt -> {
                    Map<String, Object> summary = new LinkedHashMap<>();
                    summary.put("projektId", projekt.getProjektId());
                    summary.put("nazwa", projekt.getNazwa());
                    summary.put("opis", projekt.getOpis());
                    summary.put("createdDate", projekt.getCreatedDate());
                    summary.put("lastModifiedDate", projekt.getLastModifiedDate());
                    summary.put("dataOddania", projekt.getDataOddania());
                    return summary;
                });
    }
}