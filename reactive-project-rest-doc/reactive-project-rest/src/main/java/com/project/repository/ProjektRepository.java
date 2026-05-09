package com.project.repository;

import com.project.model.Projekt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjektRepository extends ReactiveCrudRepository<Projekt, Integer> {

    @Query("""
        SELECT * 
        FROM projekt 
        WHERE nazwa ILIKE '%' || :nazwa || '%' 
        ORDER BY projekt_id
        LIMIT :#{#pageable.pageSize}
        OFFSET :#{#pageable.offset}
        """)
    Flux<Projekt> findByNazwaContainingIgnoreCase(String nazwa, Pageable pageable);

    @Query("""
        SELECT * 
        FROM projekt 
        WHERE nazwa ILIKE '%' || :nazwa || '%' 
        ORDER BY projekt_id
        """)
    Flux<Projekt> findByNazwaContainingIgnoreCase(String nazwa);

    @Query("""
        SELECT COUNT(*) 
        FROM projekt 
        WHERE nazwa ILIKE '%' || :nazwa || '%'
        """)
    Mono<Long> countByNazwaContainingIgnoreCase(String nazwa);
}