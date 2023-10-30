package com.hibernate4all.tutorial.controller;

import com.hibernate4all.tutorial.domain.Movie;
import com.hibernate4all.tutorial.repository.MovieRepository;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieRepository repository;

    public MovieController(MovieRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/")
    public Movie create(@RequestBody Movie movie){
        // On devrait solliciter la couche service, mais ici pas grand intérêt,
        // donc utilisation directe de MovieRepository.
        repository.persist(movie);
        return movie;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> get(@PathVariable("id") Long id){
//        Movie proxy = repository.getReference(id);
//        Movie movie = (Movie) Hibernate.unproxy(proxy);
        Movie movie = repository.getReference(id);
        if (movie == null){
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(movie);
        }
    }

    @PutMapping("/")
    public ResponseEntity<Movie> update(@RequestBody Movie movie){
        Optional<Movie> result =  repository.update(movie);

        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(result.get());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Movie> delete(@PathVariable("id") Long id){
        boolean removed = repository.remove(id);
        return  removed? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


}
