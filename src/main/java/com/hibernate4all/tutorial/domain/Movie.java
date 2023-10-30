package com.hibernate4all.tutorial.domain;


import org.springframework.stereotype.Repository;

import javax.persistence.*;

@Entity
@Table(name = "MOVIE")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // On passe les annotations sur les attributs, les fields en Anglais.
    // On appelle cela du mapping en field access.
    @Column(nullable = false) // contrainte not null en base de données
    private String name;

    // mapping explicite : on nomme explicitement les noms des colonnes
    // grâce à @Column.
    @Column(name = "description")
    private String description;
    public String getDescription() {
        return description;
    }

    // Indique que l'attibut ne doit pas être mappé avec la BD.
    //@Transient
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
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
}
