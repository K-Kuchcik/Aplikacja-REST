package com.project.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.model.Projekt;
import com.project.service.ProjektService;
import com.project.validation.ValidationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Projekt")
public class ProjektRestController {

    private final ProjektService projektService;
    private final ValidationService<Projekt> validator;

    @GetMapping("/projekty/{projektId}")
    public ResponseEntity<Projekt> getProjekt(@PathVariable("projektId") Integer projektId) {
        return ResponseEntity.of(projektService.getProjekt(projektId));
    }

    @PostMapping(path = "/projekty")
    public ResponseEntity<Void> createProjekt(@RequestBody Projekt projekt) {
        validator.validate(projekt);

        Projekt createdProjekt = projektService.setProjekt(projekt);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{projektId}")
                .buildAndExpand(createdProjekt.getProjektId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/projekty/{projektId}")
    public ResponseEntity<Void> updateProjekt(@RequestBody Projekt projekt,
                                              @PathVariable("projektId") Integer projektId) {
        validator.validate(projekt);

        return projektService.getProjekt(projektId)
                .map(p -> {
                    projektService.setProjekt(projekt);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/projekty/{projektId}")
    public ResponseEntity<Void> deleteProjekt(@PathVariable("projektId") Integer projektId) {
        return projektService.getProjekt(projektId)
                .map(p -> {
                    projektService.deleteProjekt(projektId);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/projekty")
    public Page<Projekt> getProjekty(Pageable pageable) {
        return projektService.getProjekty(pageable);
    }

    @GetMapping(value = "/projekty", params = "nazwa")
    public Page<Projekt> getProjektyByNazwa(@RequestParam(name = "nazwa") String nazwa, Pageable pageable) {
        return projektService.searchByNazwa(nazwa, pageable);
    }
}