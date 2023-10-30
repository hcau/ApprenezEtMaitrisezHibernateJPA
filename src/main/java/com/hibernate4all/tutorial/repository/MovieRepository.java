package com.hibernate4all.tutorial.repository;

import com.hibernate4all.tutorial.domain.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Repository
public class MovieRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    @PersistenceContext // ajout d'un entityManager
    EntityManager entityManager;

    @Transactional
    public void persist(Movie movie) {
        //throw new UnsupportedOperationException();

        // demander si movie est dans sa session
        //LOGGER.trace("entityManager.contains() : "+entityManager.contains(movie));
        entityManager.persist(movie);
        //LOGGER.trace("entityManager.contains() : "+entityManager.contains(movie));
        // on demande à entityManager de détacher l'entité movie et donc de le supprimer de sa session.
        //entityManager.detach(movie);
        //LOGGER.trace("entityManager.contains() : "+entityManager.contains(movie));
    }

    // Comprendre le flush()
    // Le flush() force les modifications en cours à être envoyées en base (cf schéma).
    // Hibernate refait un flush() automatiquement à la fermeture de la session
    @Transactional
    public void persist2(Movie movie) {
        entityManager.persist(movie);
        entityManager.flush(); // on force à faire un flush()
    }


    public Movie find(Long id) {
        // les 3 règles d'or
        // Opération de récupération et pas d'écriture. ==>Spring n'a pas besoin d'ouvrir une transaction.
        // Les sessions :
        // demander si movie est dans sa session
        //return entityManager.find(Movie.class, id);
        Movie result = entityManager.find(Movie.class, id);
        //LOGGER.trace("entityManager.contains() : "+entityManager.contains(result));
        return result;
    }


    public List<Movie> getAll() {
        //throw new UnsupportedOperationException(); utilisé lors de la création de méthode vide
        // Ici, on fait select * from Movie
        return entityManager.createQuery("from Movie", Movie.class).getResultList();
    }

    /**
     * Cette méthode va faire deux choses :
     * une sélection et un update si besoin (d'ou la présence de @TRANSACTIONL)
     */
    @Transactional
    public Movie merge(Movie movie) {
        // pourquoi la méthode merge() renvoie un Movie?
        // entityManager va d'abord récupérer la vraie version de cette entité en base de données.
        // S'il y a une différence entre l'entité nouvellement dans l'état MANAGED et la version récupérée
        // en base de données (ce ne sont pas les mêmes versions), alors l'entityManager fera un update().
        // Si pas de différence, alors pas d'update.
        return entityManager.merge(movie);
    }

    @Transactional
    public Optional<Movie> update(Movie movie) {
        if (movie.getId() == null) {
            return Optional.empty();
        }
        Movie bdMovie = entityManager.find(Movie.class, movie.getId());
        if (bdMovie != null) {
            bdMovie.setDescription(movie.getDescription());
            bdMovie.setName(movie.getName());
        }
        // On peut soit faire une exception ou renvoyer un null pour
        // indiquer l'absence du movie en base de données.
        // Ici, on retourne un Optional.
        return Optional.ofNullable(bdMovie);
    }


    // Comprendre le cache de premier niveau.
    @Transactional
    public Movie merge2(Movie movie) {
        // Hibernate effectue un select, pour avoir l'état en base de données.
        // mais l'update ne devrait se faire qu'à la sortie de la méthode,
        // au moment du flush() et du commit().
        // ==> deuxième utilité du cache de session : stocker les modifs
        // et les envoyer en BDD au bon moment.
        entityManager.merge(movie);

        // On récupère le résultat, on va le chercher dans le cache.
        Movie result = entityManager.find(Movie.class, movie.getId());
        return result;
    }

    //    @Transactional // remove ==> écriture
//    public void remove(long id) {
//        // on part d'un état MANAGED
//        Movie movie = entityManager.find(Movie.class, id);
//        entityManager.remove(movie);
//    }
    @Transactional // remove ==> écriture
    public boolean remove(Long id) {
        boolean result = false;
        if (id != null) {
            Movie movie = entityManager.find(Movie.class, id);
            if(movie != null){
                entityManager.remove(movie);
                result = true;
            }
        }
        return result;
    }

    // Comprendre le cache de premier niveau.
    @Transactional // remove ==> écriture
    public void remove2(long id) {
        // On faut 3 requêtes find(). ==> Hibernate va faire
        // qu'une seule requête.
        Movie movie = entityManager.find(Movie.class, id);
        // Va retrouvé dans son cache l'entité désirée.
        // parce qu'on est dans la session.
        // Cela évite de lancer des requêtes SQL pour rien
        Movie movie1 = entityManager.find(Movie.class, id);
        Movie movie2 = entityManager.find(Movie.class, id);
        entityManager.remove(movie);
    }


    public Movie getReference(long id) {
        // Cette méthode getReference() est un peu particulière, car elle ne récupère pas,
        // ou en tout cas, pas tout de suite, les données de la base de données.
        // On ne récupère que l'entité qui est en base. Pour récupérer les autres attributs, il faut être dans
        // l'état MANAGED, sinon cela provoque une exception.
        // C'est là qu'intervient une nouvelle notion, la notion de proxy.
        // Les proxies sont des références, c'est un concept fondamental d'Hibernate.
        // La notion de proxy est tout simplement une référence vers une entité de la base qui n'est
        // pas entièrement chargée et qui est échangeable à la demande pour peu qu'on soit
        // encore dans la session ouverte.

        // Nous sommes hors d'une session.
        // On constate qu'il n'y a aucune requête SQL lancée, alors que l'entité Movie
        // était dans la session mais sous forme de proxy, donc elle n'est pas entièrement chargée.
        return entityManager.getReference(Movie.class, id);
    }


    // Si j'ai envie de charger l'entité (d'avoir accès aux autres attributs due l'id,
    // je peux me débrouiller pour qu'on soit dans la session
    // et accéder à un de ses attributs.

    // On voit qu'une requête vers la base de données est réalisée.

    @Transactional // J'ouvre une session.
    public Movie getReference2(long id) {
        Movie result = entityManager.getReference(Movie.class, id);
        LOGGER.trace("movie name : " + result.getName());
        return result;
    }


    // Pas de session ouverte.
    public Movie getReference3(long id) {
        return entityManager.getReference(Movie.class, id);
    }


}
