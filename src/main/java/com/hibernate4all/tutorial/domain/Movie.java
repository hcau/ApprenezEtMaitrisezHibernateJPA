package com.hibernate4all.tutorial.domain;


import javax.persistence.*;
import java.util.*;

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

    /**
     * Comment faire une association bidirectionnelle?
     * Association bidirectionnelle: pouvoir accéder à Review depuis
     * votre entité movie; en gros faire un movie.getReview();
     * Hibernate permet ce genre d'association, même ce n'est pas
     * naturel d'un point de vue base de données.
     * ==> Le sens Movie->Review n'existe pas en Base de données,
     * seul le sens Review->Movie est matérialisé.
     *
     * Notre Movie pourra contenir plusieurs entités Review.
     *        _________               _________
     *       |        |               |       |
     *       | Review | 1 --------> * | Movie |
     *       |        |               |       |
     *       ----------               ---------
     *
     * On crée donc une liste de Review.
     * private List<Review> reviews;
     *
     * Maintenant, on va choisir l'annotation.
     * Un Movie peut avoir plusieurs Review.
     * @OneToMany
     * On peut aussi dire: quel côté de l'association contient
     * la clé étrangère.
     * La clé étrangère de movie_id est contenu dans Review.
     * ==> mappedBy = "movie"  ==> ça veut dire que dans Review,
     * l'attribut movie correspond à la clé étrangère ==> (classe esclave)
     *
     * cascade = CascadeType.ALL ==> ça veut dire que les opérations
     * faites sur Movie vont être cascadée vers Review.
     * Cascade All : on propage les actions ...
     * ... effectuées sur l'entité vers l'association.
     * Exemple : persist, delete ...
     * Par exemple, si on décide de sauvegarder Movie, ça va sauvegarder
     * les Reviews contenues dans ce Movie.
     *
     * orphanRemoval = true ==> évite les orphelins
     * Par exemple si on fait : review.setMovie(null)
     * Si Hibernate, dans sa session voit une entité Review qui n'est pas
     * associée à une entité Movie, alors Hibernate va supprimer cette
     * entité Review qui est orpheline.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "movie")
    private List<Review> reviews = new ArrayList<>();

    public Movie addReview(Review review){
        if(review != null){
            // On renseigne les deux sens (bidirectionnel).
            this.reviews.add(review);
            review.setMovie(this);
        }
        return this; // aspect fluent, on retourne this.
    }

    public Movie removeReview(Review review){
        if(review != null){
            // On renseigne les deux sens (bidirectionnel).
            this.reviews.remove(review);
            review.setMovie(null);
        }
        return this; // aspect fluent, on retourne this.
    }


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

    public List<Review> getReviews() {
        //return reviews;
        return Collections.unmodifiableList(reviews);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    public Movie withReviews(List<Review> reviews) {
        this.reviews = reviews;
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
