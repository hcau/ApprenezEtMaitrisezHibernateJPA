package com.hibernate4all.tutorial.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    /**
     * Comme la table d'association ne possède pas d'attributs autres que les id,
     * on peut se permettre de ne pas la matérialiser sous forme d'entité
     * et donc simplifier un peu le dode via la notation @ManyToMany.
     */

    /**
     * On veut que le propriétaire de la relation soit du côté Movie, c'est-à-dire
     * que lorsque je vais persister un Movie, je veux que les genres associés
     * soient aussi persistés.
     * Dans la classe Genre, on utilise mappedBy.
     */
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();
    public Long getId() {
        return id;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre withName(String name) {
        this.name = name;
        return this;
    }


    /**
     * Dans cette entité, on a un identifiant fonctionnel.
     * Le nom (name) est unique à travers tous les genres
     * et il est aussi immuable.
     * ==> on peut donc s'en servir comme identifiant fonctionnel
     * et générer le equals() et le hashCode() en conséquence.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
