package com.project.handler;

import com.project.model.Projekt;
import com.project.service.ProjektService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ProjectHandler {

    private final ProjektService projectService;

    public Mono<ServerResponse> findById(ServerRequest request) {
        return projectService
                .findById(Integer.valueOf(request.pathVariable("id")))
                .flatMap(projekt -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(projekt))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findAll(), Projekt.class);
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        String nazwa = request.queryParam("nazwa").orElse("");

        int page = request.queryParam("page")
                .map(Integer::parseInt)
                .orElse(0);

        int size = request.queryParam("size")
                .map(Integer::parseInt)
                .orElse(20);

        Pageable pageable = PageRequest.of(page, size);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNazwa(nazwa, pageable), Projekt.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request
                .bodyToMono(Projekt.class)
                .flatMap(projectService::create)
                .flatMap(p -> ServerResponse
                        .created(URI.create("/api/projects/" + p.getProjektId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(p));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        int pathId = Integer.valueOf(request.pathVariable("id"));

        return request.bodyToMono(Projekt.class)
                .doOnNext(projekt -> projekt.setProjektId(pathId))
                .flatMap(projectService::update)
                .flatMap(p -> ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return projectService
                .delete(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> summary(ServerRequest request) {
        Integer projektId = Integer.valueOf(request.pathVariable("id"));

        return projectService.summary(projektId)
                .flatMap(summary -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(summary))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}