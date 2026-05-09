package com.project.init;

import com.project.model.Projekt;
import com.project.service.ProjektService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ProjektService projectService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        var saveProjects = Flux.just("Projekt 1", "Projekt 2", "Projekt 3", "Projekt 4", "Projekt 5")
                .map(nazwa -> Projekt.builder()
                        .nazwa(nazwa)
                        .opis(String.format("Opis testowy projektu - %s", nazwa))
                        .dataOddania(LocalDate.of(2026, 7, 1))
                        .build())
                .flatMap(projectService::create);

        projectService.deleteAll()
                .thenMany(saveProjects)
                .thenMany(projectService.findAll())
                .subscribe(p -> log.info("projekt: {} {}", p.getProjektId(), p.getNazwa()));
    }
}