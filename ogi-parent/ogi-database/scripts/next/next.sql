INSERT INTO TR_DOCUMENT_TYPE(DOT_ID, DOT_CODE, DOT_LABEL, DOT_ZONELIST) VALUES (1, 'PHOTO', 'Photo du bien', 'NO');
UPDATE TA_DOCUMENT SET DOT_ID=1;
ALTER TABLE TA_DOCUMENT DROP COLUMN DOC_TYPE;
ALTER TABLE TA_DOCUMENT  CHANGE COLUMN DOC_PATH DOC_PATH VARCHAR(1024) NULL ;


INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('TITRE_PROP_SL', 'Titre de propriété', 'SALE');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('PLAN_SL', 'Plan', 'SALE');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('DIAGNOSTIC_SL', 'Diagnostiques', 'SALE');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('ASSAINISEMENT_SL', 'Rapport d''assainissement', 'SALE');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('RG_COPRO_SL', 'Règlement copropriété', 'SALE');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('APPEL_FONDS_SL', 'Appels de fonds', 'SALE');


INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('TITRE_PROP_RT', 'Titre de propriété', 'RENT');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('PLAN_RT', 'Plan', 'RENT');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('DIAGNOSTIC_RT', 'Diagnostiques', 'RENT');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('ASSAINISEMENT_RT', 'Rapport d''assainissement', 'RENT');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('RG_COPRO_RT', 'Règlement copropriété', 'RENT');
INSERT INTO TR_DOCUMENT_TYPE(DOT_CODE, DOT_LABEL, DOT_ZONELIST) 
VALUES ('APPEL_FONDS_RT', 'Appels de fonds', 'RENT');

COMMIT;
