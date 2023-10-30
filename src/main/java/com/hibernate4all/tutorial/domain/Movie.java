package com.hibernate4all.tutorial.domain;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MOVIE")
public class Movie {

//    public enum Certification{
//        TOUS_PUBLIC, INTERDIT_MOINS_12, INTERDIT_MOINS_16, INTERDIT_MOINS_18
//    }
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

    //@Enumerated // On indique à Hibernate que c'est une énumération.
    //@Enumerated(EnumType.STRING)
    // Utilisation du converter CertificationAttributeConverter ==> on retourne un nombre
    private Certification certification;


    public Long getId() {
        return id;
    }

    public void  setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Movie withName(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    public Movie withCertification(Certification certification) {
        this.certification = certification;
        return this;
    }


    public String getDescription() {
        return description;
    }

    // Indique que l'attibut ne doit pas être mappé avec la BD.
    //@Transient
    public void setDescription(String description) {
        this.description = description;
    }

    public Movie withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((!(obj instanceof Movie))) return false;
        Movie other = (Movie) obj;
        // pas d'identifiant fonctionnel.
        if(id == null && other.getId() == null){
            return Objects.equals(name, other.getName())
                    && Objects.equals(description, other.getDescription())
                    && Objects.equals(certification, other.getCertification());
        }
        return id != null && Objects.equals( id, other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(31);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", certification=" + certification +
                ", description='" + description + '\'' +
                '}';
    }
}
