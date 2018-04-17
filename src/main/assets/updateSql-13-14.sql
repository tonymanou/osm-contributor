ALTER TABLE 'AREA' RENAME TO 'AREA_TMP';
CREATE TABLE `AREA` (`ID` VARCHAR PRIMARY KEY, `NORTH` DOUBLE PRECISION NOT NULL, `SOUTH` DOUBLE PRECISION NOT NULL, `EAST` DOUBLE PRECISION NOT NULL, `WEAST` DOUBLE PRECISION NOT NULL, `UPDATE_DATE` BIGINT);
INSERT INTO 'AREA' ( ID, NORTH, SOUTH, EAST, WEAST, UPDATE_DATE ) SELECT  ID, NORTH, SOUTH, EAST, WEAST, UPDATE_DATE FROM 'AREA_TMP';
DROP TABLE 'AREA_TMP';`