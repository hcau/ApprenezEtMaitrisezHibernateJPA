package com.hibernate4all.tutorial.domain;

import javax.persistence.*;
import java.util.Objects;




@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String author;
    private String content;

    /**
     * Dans la table des associations, nous avons vu que
     * Review est propriétaire de l'association, donc c'est
     * dans la classe Review qu'il y a l'id de Movie,
     * la table possède une colonne d'association.
     * Donc, ce que l'on va vouloir, ce n'est pas d'accéder,
     * à l'id de Movie depuis Review, mais c'est d' accéder
     * à l'objet en entier.
     *
     * Movie est une association, on ne va pas l'ajouter dans
     * Equals() , dans le hashCode() ou dans le toString().
     *
     * Quelle est la bonne annotation pour cette association?
     * Il faut se poser la question fonctionnellement.
     * On sait qu'une entité Movie peut contenir plusieurs entités Review.
     * Si on prend le sens inverse, plusieurs entités Review
     * peuvent être associées à une entité Movie.
     *
     *  _________               _________
     * |        |               |       |
     * | Review | 1 --------> * | Movie |
     * |        |               |       |
     * ----------               ---------
     *
     * Une Review est associée à un Movie.
     * Un Movie a plusieurs Review (avis).
     * Prenons le sens inverse de la ligne précédente :
     * ==> Plusieurs Review peuvent être associées à une entité Movie.
     * ==> @ManyToOne
     *
     * Fonctionnellement, on sait qu'une entité Movie peut contenir
     * plusieurs entités Reviews.
     * Si on prend le sens inverse :
     * Plusieurs entités Review peuvent être associées à une entité Movie.
     *
     * Au niveau de la notation, on a ici en premier le sens
     * de l'entité, donc Review (on est dans la classe Review).
     * => Plusieurs Review
     * Plusieurs Review peuvent être associés à un Movie. ==> un Movie
     * @ManyToOne
     *
     */

    /**
     Hibernate va comprendre que Movie va être mappé sur
     la colonne movie_id. C'est un mapping implicite par convention.
     Normalement, il faudrait @JoinColumn()
     @JoinColumn(name = "movie_id) indique une clé étrangère,
     si absente, elle sera déduite par convention (entité associée_id)

      Par défaut, Hibernate dans les dernières version JPA est EAGER
      sur les associations @ManyToOne ou @OneToOne.
     (Hibernate s'aligne maintenant avec JPA : EAGER sur @ManyToOne)
     Cela engendre un chargement automatique de l'association quand
     on charge Review, alors que ce n'est pas ce que l'on souhaite.
     On souhaite avoir la maîtrise de ce chargement.
     */

    /**
     * Comment faire une association bidirectionnelle?
     * Association bidirectionnelle: pouvoir accéder à Review depuis
     * votre entité movie; en gros faire un movie.getReview();
     * Hibernate permet ce genre d'association, même ce n'est pas
     * naturel d'un point de vue base de données.
     * ==> Le sens Movie->Review n'existe pas en Base de données,
     * seul le sens Review->Movie est matérialisé.
     * Cf l'entité Movie
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public Review withAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Review withContent(String content) {
        this.content = content;
        return this;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    public Review withMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((!(obj instanceof Review))) return false;
        Review other = (Review) obj;
        // pas d'identifiant fonctionnel.
        if(id == null && other.getId() == null){
            return Objects.equals(author, other.getAuthor())
                    && Objects.equals(content, other.getContent());
        }
        return id != null && Objects.equals( id, other.getId());
    }

    @Override
    public int hashCode() {
        return 32;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
