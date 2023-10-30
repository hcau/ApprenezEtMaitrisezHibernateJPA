package com.hibernate4all.tutorial.service;

import com.hibernate4all.tutorial.config.PersistenceConfigTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) // exécuter dans le contexte Spring
@ContextConfiguration(classes = {PersistenceConfigTest.class})// Indiquer les classes de configuration qu'à besoin Spring pour s'initialiser
// utilise le datasource déjà configuré,idem pour transactionManager ==> cf PersistenceConfig.java
@SqlConfig(dataSource = "dataSourceH2", transactionManager = "transactionManager")
// lancer le script sql situé à cette adresse:
@Sql("/datas/datas-test.sql")
public class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Test
    public void updateDescription_casNominal(){
        movieService.updateDescription(-2L, "Super film, mais j'ai oublié le pitch.");
    }
}
