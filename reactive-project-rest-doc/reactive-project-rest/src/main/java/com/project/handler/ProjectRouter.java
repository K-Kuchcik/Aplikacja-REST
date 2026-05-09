package com.project.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProjectRouter {

    @Bean
    public RouterFunction<ServerResponse> projectsRoute(ProjectHandler projectHandler) {
        return route(GET("/api/projects/{id}"), projectHandler::findById)
                .andRoute(GET("/api/projects"), projectHandler::findAll)
                .andRoute(GET("/api/projects/search"), projectHandler::findByName)
                .andRoute(GET("/api/projects/{id}/summary"), projectHandler::summary)
                .andRoute(POST("/api/projects").and(contentType(MediaType.APPLICATION_JSON)), projectHandler::create)
                .andRoute(PUT("/api/projects/{id}").and(contentType(MediaType.APPLICATION_JSON)), projectHandler::update)
                .andRoute(DELETE("/api/projects/{id}"), projectHandler::delete);
    }
}