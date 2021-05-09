-- ############## TEMPLATE ###############
-- DELIMITER $$
-- CREATE PROCEDURE add_movie()
-- BEGIN
-- -- Insert something here
-- END
-- $$
-- DELIMITER ;
-- ############## TEMPLATE ###############




DROP PROCEDURE IF EXISTS add_movie;
DELIMITER $$
CREATE PROCEDURE add_movie(IN g_name varchar(32), IN s_name varchar(100), IN m_title VARCHAR(100), IN m_year INT, IN m_director VARCHAR(100))
proc_label:BEGIN
	DECLARE g_id int;
    DECLARE s_id varchar(10);
    DECLARE m_id varchar(10);
    DECLARE m_bool int;

    -- Movie logic
    SET m_bool := (select exists(select * from movies m where m.title = m_title AND m.year = m_year AND m.director = m_director));
    IF (m_bool = 1) THEN
		select concat("0") as result;
		LEAVE proc_label;
	END IF;
    IF (m_bool = 0) THEN
		CALL getNewMovieId(m_id);
    END IF;

    CALL getGenres(g_name, g_id);
    CALL getStars(s_name, s_id);

    -- Function that checks whether g_id or s_id is null
    IF(g_id IS NULL) THEN
		CALL createNewGenre(g_name, g_id);
    END IF;
    IF(s_id IS NULL) THEN
		CALL createNewStar(s_name, s_id);
    END IF;

	-- INSERTING INTO MOVIE
    INSERT INTO movies VALUES(m_id, m_title, m_year, m_director);

    -- INSERTING INTO Stars_in_movie and genres in movie
    INSERT INTO stars_in_movies VALUES(s_id, m_id);
	INSERT INTO genres_in_movies VALUE(g_id, m_id);

	select concat("1") as result, m_id, g_id, s_id;
END
$$
DELIMITER ;






-- #################################
-- OUR MINI CREATE PROCEDURES
-- #################################
DROP PROCEDURE IF EXISTS createNewStar;
DELIMITER $$
CREATE PROCEDURE createNewStar(IN s_name varchar(100), OUT s_id varchar(10))
BEGIN
    DECLARE maxID varchar(10);
    SET maxID := (select REPLACE(max(s.id), SUBSTRING(max(s.id), 4), CONVERT(SUBSTRING(max(s.id), 4),UNSIGNED INTEGER) + 1) from stars s);
    INSERT INTO stars VALUES(maxID, s_name, null);
    SET s_id := maxID;
END
$$
DELIMITER ;


DROP PROCEDURE IF EXISTS getNewMovieId;
DELIMITER $$
CREATE PROCEDURE getNewMovieId(OUT m_id varchar(10))
BEGIN
    DECLARE maxID varchar(10);
    SET maxID := (select REPLACE(max(m.id), SUBSTRING(max(m.id), 4), CONVERT(SUBSTRING(max(m.id), 4),UNSIGNED INTEGER) + 1) from movies m);
    SET m_id := maxID;
END
$$
DELIMITER ;


DROP PROCEDURE IF EXISTS getStars;
-- Function that returns the s_id
DELIMITER $$
CREATE PROCEDURE getStars(IN s_name varchar(100), out s_id varchar(10))
BEGIN
    set s_id := (select s.id from stars s where s.name = s_name limit 1);
END
$$
DELIMITER ;



DROP PROCEDURE IF EXISTS createNewGenre;
-- This is the function that creates a new genre and returns the new id
DELIMITER $$
CREATE PROCEDURE createNewGenre(IN g_name varchar(32), OUT g_id int)
BEGIN
    DECLARE maxID int;
    SET maxID := (select max(g.id)+1 from genres g);
    INSERT INTO genres VALUES(maxID, g_name);
    SET g_id := maxID;
END
$$
DELIMITER ;



DROP PROCEDURE IF EXISTS getGenres;
-- Function that returns the g_id
DELIMITER $$
CREATE PROCEDURE getGenres(IN g_name varchar(32), OUT g_id int)
BEGIN
    set g_id := (select g.id from genres g where g.name = g_name);
END
$$
DELIMITER ;


-- ##################################
-- TESTERS
-- ##################################


-- DROP PROCEDURE IF EXISTS test;
-- DELIMITER $$
-- CREATE PROCEDURE test()
-- BEGIN
-- 	DECLARE m_id varchar(10);
--     call getNewMovieId(m_id);
--     select m_id;
-- END
-- $$
-- DELIMITER ;

-- DROP PROCEDURE IF EXISTS test2;
-- DELIMITER $$
-- CREATE PROCEDURE test2(IN s_name varchar(100))
-- BEGIN
-- 	DECLARE s_id varchar(10);
--     call createNewStar(s_name, s_id);
--     select s_id;
-- END
-- $$
-- DELIMITER ;


-- DROP PROCEDURE IF EXISTS test3;
-- DELIMITER $$
-- CREATE PROCEDURE test3(IN g_name varchar(100))
-- BEGIN
-- 	DECLARE g_id int;
--     call createNewGenre(g_name, g_id);
--     select g_id;
-- END
-- $$
-- DELIMITER ;


-- DROP PROCEDURE IF EXISTS test4;
-- DELIMITER $$
-- CREATE PROCEDURE test4(IN s_name varchar(100))
-- BEGIN
-- 	DECLARE s_id varchar(10);
--     call getStars(s_name, s_id);
--     select s_id;
-- END
-- $$
-- DELIMITER ;


-- DROP PROCEDURE IF EXISTS test5;
-- DELIMITER $$
-- CREATE PROCEDURE test5(IN g_name varchar(32))
-- BEGIN
-- 	DECLARE g_id int;
--     call getGenres(g_name, g_id);
--     select g_id;
-- END
-- $$
-- DELIMITER ;



-- Stored procedure for adding single star
DROP PROCEDURE IF EXISTS createNewStarID
DELIMITER $$
CREATE PROCEDURE createNewStarID(IN s_name varchar(100), IN s_year INT)
BEGIN
    DECLARE maxID varchar(10);
    SET maxID := (select REPLACE(max(s.id), SUBSTRING(max(s.id), 4), CONVERT(SUBSTRING(max(s.id), 4),UNSIGNED INTEGER) + 1) from stars s);
    INSERT INTO stars VALUES(maxID, s_name, s_year);
    select maxID;
END
$$
DELIMITER ;
