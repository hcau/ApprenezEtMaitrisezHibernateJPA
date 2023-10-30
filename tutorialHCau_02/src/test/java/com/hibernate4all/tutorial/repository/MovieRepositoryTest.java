package com.hibernate4all.tutorial.repository;

import com.hibernate4all.tutorial.config.PersistenceConfigTest;
import com.hibernate4all.tutorial.domain.Movie;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class) // Exécuter dans le contexte Spring
@ContextConfiguration(classes = {PersistenceConfigTest.class})// Indiquer les classes de configuration qu'à besoin Spring pour s'initialiser
// utilise le datasource déjà configuré,idem pour transactionManager ==> cf PersistenceConfig.java
@SqlConfig(dataSource = "dataSourceH2", transactionManager = "transactionManager")
// lancer le script sql situé à cette adresse:
@Sql("/datas/datas-test.sql")
public class MovieRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepositoryTest.class);

    @Autowired
    private MovieRepository repository;

    @Test
    public void save_casNominal(){
        Movie movie = new Movie();
        movie.setName("Inception");
        repository.persist(movie);
        System.out.println("fin de test");
    }

    // Comprendre le flush()
    @Test
    public void comprendreLeFlush(){
        Movie movie = new Movie();
        movie.setName("Inception");
        repository.persist2(movie);
    }


    // On va simuler un objet détaché.
    // Il ne sera pas vraiment détaché dans le sens préalablement attaché.
    @Test
    public void merge_casSimule(){
        Movie movie = new Movie();
        movie.setName("Inception 2");
        // Ici, on a déterminé l'id de manière programmatique, normalement c'est Hibernate qui s'en occupe.
        // On veut simuler un objet détaché.
        movie.setId(-1L);
        // on attache l'objet
        Movie mergeMovie = repository.merge(movie);
        assertThat(mergeMovie.getName()).as("le nom du film n'a pas été mis à jour").isEqualTo("Inception 2");
    }

    // Comprendre le cache de premier niveau.
    @Test
    public void merge_casSimule2(){
        Movie movie = new Movie();
        movie.setName("Inception 2");
        movie.setId(-1L);
        Movie mergeMovie = repository.merge2(movie);
        assertThat(mergeMovie.getName()).as("le nom du film n'a pas été mis à jour").isEqualTo("Inception 2");
    }


    @Test
    public void find_CasNominal(){
        Movie memento = repository.find(-2L);
        assertThat(memento.getName()).as("mauvais film récupéré").isEqualTo("Memento");
    }

    @Test
    public void getAll_CasNominal(){
        List<Movie> movies = repository.getAll();
        assertThat(movies).as("l'ensemble de la liste n'a pas été récupéré").hasSize(2);
    }

    @Test
    public void remove_CasNominal(){
        repository.remove(-2L);
        List<Movie> movies = repository.getAll();
        assertThat(movies).as("le film n'a pas été supprimé").hasSize(1);
    }


    // Comprendre le cache de premier niveau.
    @Test
    public void remove_CasNominal2(){
        repository.remove2(-2L);
        List<Movie> movies = repository.getAll();
        assertThat(movies).as("le film n'a pas été supprimé").hasSize(1);
    }


    @Test
    public void getReference_casNominal(){
        Movie movie = repository.getReference(-2L);
        assertThat(movie.getId()).as("la référence n'a pas été correctement chargée").isEqualTo(-2L);
    }

    @Test
    public void getReference2_casNominal(){
        Movie movie = repository.getReference2(-2L);
        assertThat(movie.getId()).as("la référence n'a pas été correctement chargée").isEqualTo(-2L);
    }

    @Test
    public void getReference_fail(){
        // On va passer dans un état DETACHED (getReference3 n'a pas de session).
        // DETACHED car la session se ferme ==> le proxy récupéré sera donc DETACHED.
        // ==> provoque une erreur de test : on peut uniquement récupérer la référence,
        // ici id mais pas les autres attributs.
        // org.hibernate.LazyInitializationException: could not initialize proxy
        // [com.hibernate4all.tutorial.domain.Movie#-2] - no Session
        // On a provoqué une lazy initialisation exception.
        // C'est un des problèmes très fréquent avec Hibernate : c'est le chargement à la demande dans
        // des endroits ou ne devrait pas charger à la demande.
        // Typiquement, ici, on est dans un état DETACHED et on accède à getName()

        // On comprend l'importance d'être dans une session au moment où on accède aux données.
        // 2ième règle d'or : pour éviter ce genre de problème.
        assertThrows(LazyInitializationException.class, () -> {
            Movie movie = repository.getReference3(-2L);
            LOGGER.trace("movie name : " + movie.getName());
            //LOGGER.trace("movie name : " + movie.getId());
            assertThat(movie.getId()).as("la référence n'a pas été correctement chargée").isEqualTo(-2L);
        } );
    }


}
