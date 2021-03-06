DROP TABLE IF EXISTS TA_EVENT;
CREATE TABLE TA_EVENT (
	EVT_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	PRO_ID INT NOT NULL,
	PRO_REFERENCE VARCHAR(64) NOT NULL,
	EVT_TYPE VARCHAR(32) NOT NULL,
	EVT_TIMESTAMP TIMESTAMP NOT NULL
);

ALTER TABLE TA_EVENT 
ADD UNIQUE INDEX `IDX_UNI_EVT` (PRO_ID ASC, PRO_REFERENCE ASC, EVT_TYPE ASC);

-- insert events
INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) 
SELECT PRO_ID, PRO_REFERENCE, 'PRP_UPDATE', NOW() FROM TA_PROPERTY;


DELIMITER $$
DROP TRIGGER IF EXISTS TRG_ADDRESS_01$$
CREATE TRIGGER TRG_ADDRESS_01 
BEFORE UPDATE ON TA_ADDRESS FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);

	-- Handle only first prp
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref 
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.ADD_ID=NEW.ADD_ID LIMIT 0,1;
	
	-- If event doesn't exist nothing todo. This mean TA_PROPERTY is not inserted
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	END IF;
END
$$

-- TA_PROPERTY
DROP TRIGGER IF EXISTS TRG_PROPERTY_03$$
CREATE TRIGGER TRG_PROPERTY_03 
AFTER INSERT ON TA_PROPERTY FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref 
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=NEW.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$

DROP TRIGGER IF EXISTS TRG_PROPERTY_01$$
CREATE TRIGGER TRG_PROPERTY_01 
BEFORE UPDATE ON TA_PROPERTY FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref 
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=NEW.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$

DROP TRIGGER IF EXISTS TA_PROPERTY_02$$
CREATE TRIGGER TA_PROPERTY_02 
BEFORE DELETE ON TA_PROPERTY FOR EACH ROW
BEGIN
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT p.PRO_ID, p.PRO_REFERENCE into property_id, property_ref 
	FROM TA_PROPERTY p WHERE p.PRO_ID=OLD.PRO_ID;

	DELETE FROM TA_EVENT WHERE PRO_ID=property_id;
	INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_DELETE', NOW());

END
$$

-- TA_PROPERTY_BUILT
DROP TRIGGER IF EXISTS TA_PROPERTY_BUILT_01$$
CREATE TRIGGER TA_PROPERTY_BUILT_01 
BEFORE UPDATE ON TA_PROPERTY_BUILT FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref 
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=NEW.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$


-- TA_PROPERTY_LIVABLE
DROP TRIGGER IF EXISTS TA_PROPERTY_LIVABLE_01$$
CREATE TRIGGER TA_PROPERTY_LIVABLE_01 
BEFORE UPDATE ON TA_PROPERTY_LIVABLE FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=NEW.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$


-- TA_PROPERTY_PLOT
DROP TRIGGER IF EXISTS TA_PROPERTY_PLOT_01$$
CREATE TRIGGER TA_PROPERTY_PLOT_01 
BEFORE UPDATE ON TA_PROPERTY_PLOT FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=NEW.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$


-- TA_DESCRIPTION
DROP TRIGGER IF EXISTS TRG_DESCRIPTION_01$$
CREATE TRIGGER TRG_DESCRIPTION_01 
BEFORE UPDATE ON TA_DESCRIPTION FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=NEW.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$

DROP TRIGGER IF EXISTS TRG_DESCRIPTION_02$$
CREATE TRIGGER TRG_DESCRIPTION_02 
BEFORE DELETE ON TA_DESCRIPTION FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=OLD.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$

-- TA_DOCUMENT
DROP TRIGGER IF EXISTS TRG_DOCUMENT_01$$
CREATE TRIGGER TRG_DOCUMENT_01 
BEFORE UPDATE ON TA_DOCUMENT FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	-- Handle only one property
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	JOIN TJ_PRP_DOC tj ON tj.PRO_ID=p.PRO_ID
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE tj.DOC_ID=NEW.DOC_ID LIMIT 0,1;

	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;
END
$$

DROP TRIGGER IF EXISTS TRG_PRP_DOC_02$$
CREATE TRIGGER TRG_PRP_DOC_02 
BEFORE DELETE ON TA_PRP_DOC FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	-- Handle only one property
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	JOIN TJ_PRP_DOC tj ON tj.PRO_ID=p.PRO_ID
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE tj.DOC_ID=OLD.DOC_ID LIMIT 0,1;

	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;
END
$$

-- TA_SALE
DROP TRIGGER IF EXISTS TRG_SALE_01$$
CREATE TRIGGER TRG_SALE_01 
BEFORE UPDATE ON TA_SALE FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=NEW.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$

DROP TRIGGER IF EXISTS TRG_SALE_02$$
CREATE TRIGGER TRG_SALE_02 
BEFORE DELETE ON TA_SALE FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=OLD.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$

-- TA_RENT
DROP TRIGGER IF EXISTS TRG_RENT_01$$
CREATE TRIGGER TRG_RENT_01 
BEFORE UPDATE ON TA_RENT FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=NEW.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$

DROP TRIGGER IF EXISTS TRG_RENT_02$$
CREATE TRIGGER TRG_RENT_02 
BEFORE DELETE ON TA_RENT FOR EACH ROW
BEGIN
	DECLARE event_id INTEGER;
	DECLARE property_id INTEGER;
	DECLARE property_ref VARCHAR(64);
	
	SELECT EVT_ID, p.PRO_ID, p.PRO_REFERENCE into event_id, property_id, property_ref
	FROM TA_PROPERTY p
	LEFT JOIN TA_EVENT e ON p.PRO_REFERENCE=e.PRO_REFERENCE AND EVT_TYPE='PRP_UPDATE'
	WHERE p.PRO_ID=OLD.PRO_ID;
	
	IF event_id IS NOT NULL THEN
		UPDATE TA_EVENT SET EVT_TIMESTAMP=NOW() WHERE EVT_ID=event_id;
	ELSE
		INSERT INTO TA_EVENT(PRO_ID, PRO_REFERENCE, EVT_TYPE, EVT_TIMESTAMP) VALUES (property_id, property_ref, 'PRP_UPDATE', NOW());
	END IF;

END
$$