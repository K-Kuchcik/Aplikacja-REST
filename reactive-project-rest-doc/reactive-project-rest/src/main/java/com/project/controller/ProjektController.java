package com.project.controller;

import com.project.model.Projekt;
import com.project.service.ProjektService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjektController {

    private final ProjektService projectService;

    @GetMapping
    public reactor.core.publisher.Flux<Projekt> findAll() {
        return projectService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Projekt>> findById(@PathVariable Integer id) {
        return projectService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public reactor.core.publisher.Flux<Projekt> findByName(
            @RequestParam(defaultValue = "") String nazwa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return projectService.findByNazwa(nazwa, pageable);
    }

    @GetMapping("/{id}/summary")
    public Mono<ResponseEntity<Map<String, Object>>> summary(@PathVariable Integer id) {
        return projectService.summary(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Projekt>> create(@RequestBody Projekt projekt) {
        return projectService.create(projekt)
                .map(saved -> ResponseEntity
                        .created(URI.create("/api/projects/" + saved.getProjektId()))
                        .body(saved));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Void>> update(@PathVariable Integer id, @RequestBody Projekt projekt) {
        projekt.setProjektId(id);

        return projectService.update(projekt)
                .map(saved -> ResponseEntity.noContent().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Integer id) {
        return projectService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}