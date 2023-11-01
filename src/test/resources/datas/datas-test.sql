
-- astuce à connaitre, sur H2, ajouter ces lignes : SET REFERENTIAL_INTEGRITY FALSE;
SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE Review;
TRUNCATE TABLE Movie;
TRUNCATE TABLE Genre;
TRUNCATE TABLE Movie_Genre;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO Movie (name,certification, id) VALUES ('Inception', 1, -1L);
INSERT INTO Movie (name,certification, id) VALUES ('Memento', 2, -2L);

INSERT INTO Review (author, content, movie_id, id) VALUES ('Max', 'au top', -1L, -1L);
INSERT INTO Review (author, content, movie_id, id) VALUES ('Ernest', 'bof bof', -1L, -2L);

--On pourrai associer ce genre à des Movie, mais on ne va pas le faire ici
INSERT INTO Genre (name, id) VALUES ('Action', -1L);


