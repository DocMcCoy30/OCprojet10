-- Diff code generated with pgModeler (PostgreSQL Database Modeler)
-- pgModeler version: 0.9.3
-- Diff date: 2021-09-02 11:21:20
-- Source model: DB_P10_Biliotheque
-- Database: DB_P7_Bibliotheque
-- PostgreSQL version: 13.0

-- [ Diff summary ]
-- Dropped objects: 0
-- Created objects: 4
-- Changed objects: 2
-- Truncated tables: 0

SET search_path=public,pg_catalog;
-- ddl-end --


-- [ Created objects ] --
-- object: public.reservation_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.reservation_id_seq CASCADE;
CREATE SEQUENCE public.reservation_id_seq
	INCREMENT BY 1
	MINVALUE -2147483648
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --

-- object: public.reservation | type: TABLE --
-- DROP TABLE IF EXISTS public.reservation CASCADE;
CREATE TABLE public.reservation (
	id integer NOT NULL DEFAULT nextval('public.reservation_id_seq'::regclass),
	date_reservation date,
	expiree boolean,
	id_utilisateur integer,
	id_ouvrage integer,
	CONSTRAINT reservation_pk PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.reservation OWNER TO postgres;
-- ddl-end --



-- [ Changed objects ] --
ALTER SEQUENCE public.adresse_id_seq1
	START WITH 1
;
-- ddl-end --
ALTER SEQUENCE public.utilisateur_id_seq
	START WITH 1
;
-- ddl-end --


-- [ Created foreign keys ] --
-- object: utilisateur_fk | type: CONSTRAINT --
-- ALTER TABLE public.reservation DROP CONSTRAINT IF EXISTS utilisateur_fk CASCADE;
ALTER TABLE public.reservation ADD CONSTRAINT utilisateur_fk FOREIGN KEY (id_utilisateur)
REFERENCES public.utilisateur (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: ouvrage_fk | type: CONSTRAINT --
-- ALTER TABLE public.reservation DROP CONSTRAINT IF EXISTS ouvrage_fk CASCADE;
ALTER TABLE public.reservation ADD CONSTRAINT ouvrage_fk FOREIGN KEY (id_ouvrage)
REFERENCES public.ouvrage (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --
