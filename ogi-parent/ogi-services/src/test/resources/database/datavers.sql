INSERT INTO TA_ADDRESS(ADD_ID, ADD_NUMBER, ADD_STREET, ADD_ADDITIONAL, ADD_POSTALCODE, ADD_CITY, ADD_CEDEX, ADD_LATITUDE, ADD_LONGITUDE)
VALUES(1, '27bis', 'Avenue Nationale', NULL, '40230', 'Saint Vincent de Tyrosse', NULL, NULL, NULL);

INSERT INTO TR_CATEGORY (CAT_ID, CAT_CODE, CAT_LABEL) VALUES
(1, 'HSE', 'Maison'),
(2, 'APT', 'Appartement'),
(3, 'PLT', 'Terrain'),
(4, 'GRG', 'Garage');

INSERT INTO TR_TYPE (TYP_ID, TYP_LABEL, CAT_ID) VALUES 
(1, 'Ferme', 1),
(2, 'Villa', 1),
(3, 'Mitoyenne', 1),
(4, 'Landaise', 1);

INSERT INTO TR_EQUIPMENT (EQP_ID, EQP_LABEL, CAT_ID) VALUES
(1, 'Cheminée', 1),
(2, 'Climatisation', 1);

INSERT INTO TR_DIAGNOSIS(DIA_ID, DIA_LABEL)
VALUE('Termite', 'Electrique');

INSERT INTO TA_PROPERTY (PRO_ID, PRO_REFERENCE, PRO_COS, PRO_HOUSING_ESTATE, PRO_LAND_AREA, PRO_MODIFICATION_DATE, PRO_VERSION, ADD_ID, CAT_ID, TYP_ID) 
VALUES (1, 'ref1', 2.8, 0, 2700, '2013-04-01 15:32:35', 1, 1, 1, 1);

INSERT INTO TA_PROPERTY_BUILT (PRB_AREA, PRB_BUILD_DATE, PRB_DPE_CLASS_GES, PRB_DPE_CLASS_KWH, PRB_DPE_GES, PRB_DPE_KWH, PRB_FLOOR_LEVEL, PRB_NB_FLOOR, PRB_ORIENTATION, PRB_PARKING, PRO_ID) VALUES
(178, '1960-07-19', NULL, NULL, NULL, NULL, NULL, NULL, 'S', NULL, 1);

INSERT INTO TA_PROPERTY_LIVABLE (PRL_HEATING, PRL_HOT_WATER, PRL_NB_BATHROOM, PRL_NB_BEDROOM, PRL_NB_ROOM, PRL_NB_SHOWERROOM, PRL_NB_WC, PRO_ID) VALUES
('bois', 'cumulus', 1, 3, NULL, 1, 2, 1);


INSERT INTO TA_DESCRIPTION(DSC_ID, PRO_ID, DSC_TYPE, DSC_LABEL) VALUES
(1, 1, 'VITRINE', 'Ferme située dans un endroit calme (vitrine)'),
(2, 1, 'WEBSITE_PERSO', 'Ferme située dans un endroit calme (site)'),
(3, 1, 'ETAT', 'Quelques travaux à prévoir : toiture et cloture'),
(50, 1, 'APP', 'Ferme description OGIGrand favori du concours de perche, le champion olympique a été battu par l''espoir allemand Raphael Holzdeppe. Mots-clés : Athlétisme, Renaud Lavillenie. PARTAGER. RÉAGIR0 · Abonnez-vous au. Nouvel Observateur. Renaud Lavillenie aux Mondiaux ...');


INSERT INTO TJ_PRP_EQP (PRO_ID, EQP_ID) VALUES
(1, 1),
(1, 2);

INSERT INTO TA_ROOM(ROO_ID, PRO_ID, ROO_AREA, ROO_CARREZ, ROO_DESCRIPTION, ROO_FLOOR, ROO_FLOOR_LEVEL, ROO_LIVABLE, ROO_NB_WINDOW, ROO_ORIENTATION, ROO_TYPE, ROO_VIEW, ROO_WALL) VALUES
(1, 1, 11, 1, 'Chambre secondaire', 'Moquette', 1, 1, 1, 'N', 'chambre', null, 'Tapisserie'),
(2, 1, 23, 1, 'Salon ouvert sur la cuisine', 'Carrelage', 1, 1, null, 'SE', 'Salon', null, null);

INSERT INTO TJ_PRP_DIA(PRP_ID, DIA_ID, DIA_DATE)
VALUES(1, 1, STR_TO_DATE('2013-06-08', '%Y-%m-%j'));

INSERT INTO TA_PHOTO(PHO_ID, PHO_PATH, PHO_ORDER, PRO_ID)
VALUES (1, 'CHEMIN 1', '1', 1),
(2, 'CHEMIN 2', '2', 1)
;
