package com.hibernate4all.tutorial.service;

import com.hibernate4all.tutorial.domain.Movie;
import com.hibernate4all.tutorial.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    // Le dirty checking
    // Règles d'or : gestion des transactions et des sessions.
    @Transactional // Spring va ouvrir une session avec la transaction.
    public void updateDescription(Long id, String description){
        Movie movie = repository.find(id);
        // Avant de lancer la fonction ci-dessous, la description est null.
        movie.setDescription(description);
        // On a changé un attribut de movie, donc Hibernate va détecter un changement de l'objet
        // et va se charger automatiquement d'envoyer, au moment opportun, la requête en base de données.

        // Ici le moment opportun sera au moment du commit, au moment où il va flusher sa session.
        // DEBUG o.hibernate.SQL.logStatement update Movie set description=?, name=? where id=?
        // Hibernate va envoyer toutes les informations à la base de données (par défaut).

        // Comment fonctionne le dirty checking?
        // Quand on récupère l'entité (repository.find(id)), Hibernate va le mettre dans sa map de session,
        // parallèlement il le met dans une collection, une sorte de photographie de cette entité
        // ==> on parle de "snapshot" des entités, de l'état de l'entité.
        // Ensuite, au moment du flush(), il va faire une comparaison entre votre objet et la photo que lui
        // a prise. S'il y a une différence, il va faire l'update() en base de données.




    }


}
