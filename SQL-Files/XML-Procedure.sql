DROP PROCEDURE IF EXISTS addXMLStar;
DELIMITER $$
CREATE PROCEDURE addXMLStar(IN s_name varchar(100), IN s_year INT)
BEGIN
-- Checking whether star exists
	DECLARE exist_ID varchar(10);
    SET exist_ID := (select s.id from stars s where s.name = s_name and s.birthYear = s_year limit 1);
    select concat("0") as result;
    IF(exist_ID is NULL) THEN
		SET exist_ID := (select s.id from stars s where s.name = s_name limit 1);
        select concat("0") as result;
	END IF;
    IF(exist_ID is NULL) THEN
		CALL createNewStarID(s_name, s_year);
        select concat("1") as result;
    END IF;
END
$$
DELIMITER ;