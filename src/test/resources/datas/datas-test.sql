
-- astuce à connaitre, sur H2, ajouter ces lignes : SET REFERENTIAL_INTEGRITY FALSE;
SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE REVIEW;
TRUNCATE TABLE MOVIE;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO MOVIE (name,certification, id) VALUES ('Inception', 1, -1L);
INSERT INTO MOVIE (name,certification, id) VALUES ('Memento', 2, -2L);