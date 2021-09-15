--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 13.4

-- Started on 2021-09-15 13:39:01

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 202 (class 1259 OID 21644)
-- Name: adresse_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.adresse_id_seq1
    START WITH 8
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.adresse_id_seq1 OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 203 (class 1259 OID 21646)
-- Name: adresse; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.adresse (
    id integer DEFAULT nextval('public.adresse_id_seq1'::regclass) NOT NULL,
    rue character varying NOT NULL,
    code_postal character varying NOT NULL,
    ville character varying NOT NULL,
    id_pays integer
);


ALTER TABLE public.adresse OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 21653)
-- Name: auteur_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.auteur_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.auteur_id_seq1 OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 21655)
-- Name: auteur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.auteur (
    id integer DEFAULT nextval('public.auteur_id_seq1'::regclass) NOT NULL,
    nom character varying,
    prenom character varying,
    date_naissance character varying,
    date_deces character varying
);


ALTER TABLE public.auteur OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 21662)
-- Name: bibliotheque_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bibliotheque_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.bibliotheque_id_seq1 OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 21664)
-- Name: bibliotheque; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bibliotheque (
    id integer DEFAULT nextval('public.bibliotheque_id_seq1'::regclass) NOT NULL,
    code character varying NOT NULL,
    numero_siret character varying,
    nom character varying NOT NULL,
    id_adresse integer
);


ALTER TABLE public.bibliotheque OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 21671)
-- Name: editeur_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.editeur_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.editeur_id_seq1 OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 21673)
-- Name: editeur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.editeur (
    id integer DEFAULT nextval('public.editeur_id_seq1'::regclass) NOT NULL,
    nom_maison_edition character varying NOT NULL
);


ALTER TABLE public.editeur OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 21680)
-- Name: genre_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.genre_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.genre_id_seq1 OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 21682)
-- Name: genre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.genre (
    id integer DEFAULT nextval('public.genre_id_seq1'::regclass) NOT NULL,
    genre character varying NOT NULL
);


ALTER TABLE public.genre OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 21689)
-- Name: illustration_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.illustration_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.illustration_id_seq1 OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 21691)
-- Name: illustration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.illustration (
    id integer DEFAULT nextval('public.illustration_id_seq1'::regclass) NOT NULL,
    url character varying,
    type_illustration character varying,
    id_livre integer
);


ALTER TABLE public.illustration OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 21698)
-- Name: langue_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.langue_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.langue_id_seq1 OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 21700)
-- Name: langue; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.langue (
    id integer DEFAULT nextval('public.langue_id_seq1'::regclass) NOT NULL,
    code character varying NOT NULL,
    langue character varying NOT NULL
);


ALTER TABLE public.langue OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 21707)
-- Name: livre_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.livre_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.livre_id_seq1 OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 21709)
-- Name: livre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.livre (
    id integer DEFAULT nextval('public.livre_id_seq1'::regclass) NOT NULL,
    titre character varying NOT NULL,
    resume character varying NOT NULL,
    date_edition character varying NOT NULL,
    numero_isbn13 character varying,
    id_editeur integer,
    id_langue integer
);


ALTER TABLE public.livre OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 21716)
-- Name: many_livre_has_many_auteur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.many_livre_has_many_auteur (
    id_livre integer NOT NULL,
    id_auteur integer NOT NULL
);


ALTER TABLE public.many_livre_has_many_auteur OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 21719)
-- Name: many_livre_has_many_genre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.many_livre_has_many_genre (
    id_livre integer NOT NULL,
    id_genre integer NOT NULL
);


ALTER TABLE public.many_livre_has_many_genre OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 21722)
-- Name: many_utilisateur_has_many_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.many_utilisateur_has_many_role (
    id_utilisateur integer NOT NULL,
    id_role integer NOT NULL
);


ALTER TABLE public.many_utilisateur_has_many_role OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 21725)
-- Name: ouvrage_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ouvrage_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.ouvrage_id_seq1 OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 21727)
-- Name: ouvrage; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ouvrage (
    id integer DEFAULT nextval('public.ouvrage_id_seq1'::regclass) NOT NULL,
    id_interne character varying NOT NULL,
    emprunte boolean NOT NULL,
    id_bibliotheque integer,
    id_livre integer
);


ALTER TABLE public.ouvrage OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 21734)
-- Name: pays_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pays_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.pays_id_seq1 OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 21736)
-- Name: pays; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pays (
    id integer DEFAULT nextval('public.pays_id_seq1'::regclass) NOT NULL,
    nom character varying NOT NULL,
    code_alpha2 character varying NOT NULL,
    code_alpha3 character varying NOT NULL,
    code integer NOT NULL
);


ALTER TABLE public.pays OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 21743)
-- Name: pret_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pret_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.pret_id_seq1 OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 21745)
-- Name: pret; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pret (
    id integer DEFAULT nextval('public.pret_id_seq1'::regclass) NOT NULL,
    date_emprunt date NOT NULL,
    date_restitution date,
    date_prolongation date,
    prolongation boolean NOT NULL,
    restitution boolean NOT NULL,
    id_ouvrage integer,
    id_utilisateur integer
);


ALTER TABLE public.pret OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 21749)
-- Name: role_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_id_seq1
    START WITH 1
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.role_id_seq1 OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 21751)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id integer DEFAULT nextval('public.role_id_seq1'::regclass) NOT NULL,
    role character varying NOT NULL,
    description character varying
);


ALTER TABLE public.role OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 21758)
-- Name: utilisateur_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utilisateur_id_seq
    START WITH 3
    INCREMENT BY 1
    MINVALUE -2147483648
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.utilisateur_id_seq OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 21760)
-- Name: utilisateur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utilisateur (
    id integer DEFAULT nextval('public.utilisateur_id_seq'::regclass) NOT NULL,
    username character varying NOT NULL,
    prenom character varying NOT NULL,
    nom character varying NOT NULL,
    email character varying NOT NULL,
    password character varying NOT NULL,
    numero_telephone character varying NOT NULL,
    public_id character varying NOT NULL,
    date_creation_compte date,
    numero_abonne character varying,
    matricule character varying,
    date_entree date,
    date_sortie date,
    id_adresse integer
);


ALTER TABLE public.utilisateur OWNER TO postgres;

--
-- TOC entry 2962 (class 2606 OID 21768)
-- Name: adresse adresse_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.adresse
    ADD CONSTRAINT adresse_pk PRIMARY KEY (id);


--
-- TOC entry 2964 (class 2606 OID 21770)
-- Name: auteur auteur_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auteur
    ADD CONSTRAINT auteur_pk PRIMARY KEY (id);


--
-- TOC entry 2966 (class 2606 OID 21772)
-- Name: bibliotheque bibliotheque_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bibliotheque
    ADD CONSTRAINT bibliotheque_pk PRIMARY KEY (id);


--
-- TOC entry 2968 (class 2606 OID 21774)
-- Name: bibliotheque bibliotheque_uq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bibliotheque
    ADD CONSTRAINT bibliotheque_uq UNIQUE (id_adresse);


--
-- TOC entry 2970 (class 2606 OID 21776)
-- Name: editeur editeur_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.editeur
    ADD CONSTRAINT editeur_pk PRIMARY KEY (id);


--
-- TOC entry 2994 (class 2606 OID 21778)
-- Name: utilisateur email_uq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT email_uq UNIQUE (email);


--
-- TOC entry 2972 (class 2606 OID 21780)
-- Name: genre genre_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT genre_pk PRIMARY KEY (id);


--
-- TOC entry 2974 (class 2606 OID 21782)
-- Name: illustration illustration_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.illustration
    ADD CONSTRAINT illustration_pk PRIMARY KEY (id);


--
-- TOC entry 2976 (class 2606 OID 21784)
-- Name: langue langue_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.langue
    ADD CONSTRAINT langue_pk PRIMARY KEY (id);


--
-- TOC entry 2978 (class 2606 OID 21786)
-- Name: livre livre_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livre
    ADD CONSTRAINT livre_pk PRIMARY KEY (id);


--
-- TOC entry 2980 (class 2606 OID 21788)
-- Name: many_livre_has_many_auteur many_livre_has_many_auteur_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.many_livre_has_many_auteur
    ADD CONSTRAINT many_livre_has_many_auteur_pk PRIMARY KEY (id_livre, id_auteur);


--
-- TOC entry 2982 (class 2606 OID 21790)
-- Name: many_livre_has_many_genre many_livre_has_many_genre_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.many_livre_has_many_genre
    ADD CONSTRAINT many_livre_has_many_genre_pk PRIMARY KEY (id_livre, id_genre);


--
-- TOC entry 2984 (class 2606 OID 21792)
-- Name: many_utilisateur_has_many_role many_utilisateur_has_many_role_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.many_utilisateur_has_many_role
    ADD CONSTRAINT many_utilisateur_has_many_role_pk PRIMARY KEY (id_utilisateur, id_role);


--
-- TOC entry 2986 (class 2606 OID 21794)
-- Name: ouvrage ouvrage_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ouvrage
    ADD CONSTRAINT ouvrage_pk PRIMARY KEY (id);


--
-- TOC entry 2988 (class 2606 OID 21796)
-- Name: pays pays_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pays
    ADD CONSTRAINT pays_pk PRIMARY KEY (id);


--
-- TOC entry 2990 (class 2606 OID 21798)
-- Name: pret pret_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pret
    ADD CONSTRAINT pret_pk PRIMARY KEY (id);


--
-- TOC entry 2992 (class 2606 OID 21800)
-- Name: role role_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pk PRIMARY KEY (id);


--
-- TOC entry 2996 (class 2606 OID 21802)
-- Name: utilisateur username_uq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT username_uq UNIQUE (username);


--
-- TOC entry 2998 (class 2606 OID 21804)
-- Name: utilisateur utilisateur_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_pk PRIMARY KEY (id);


--
-- TOC entry 3000 (class 2606 OID 21806)
-- Name: utilisateur utilisateur_uq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_uq UNIQUE (id_adresse);


--
-- TOC entry 3016 (class 2606 OID 21807)
-- Name: utilisateur adresse_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT adresse_fk FOREIGN KEY (id_adresse) REFERENCES public.adresse(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3002 (class 2606 OID 21812)
-- Name: bibliotheque adresse_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bibliotheque
    ADD CONSTRAINT adresse_fk FOREIGN KEY (id_adresse) REFERENCES public.adresse(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3006 (class 2606 OID 21817)
-- Name: many_livre_has_many_auteur auteur_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.many_livre_has_many_auteur
    ADD CONSTRAINT auteur_fk FOREIGN KEY (id_auteur) REFERENCES public.auteur(id) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3012 (class 2606 OID 21822)
-- Name: ouvrage bibliotheque_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ouvrage
    ADD CONSTRAINT bibliotheque_fk FOREIGN KEY (id_bibliotheque) REFERENCES public.bibliotheque(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3004 (class 2606 OID 21827)
-- Name: livre editeur_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livre
    ADD CONSTRAINT editeur_fk FOREIGN KEY (id_editeur) REFERENCES public.editeur(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3008 (class 2606 OID 21832)
-- Name: many_livre_has_many_genre genre_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.many_livre_has_many_genre
    ADD CONSTRAINT genre_fk FOREIGN KEY (id_genre) REFERENCES public.genre(id) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3005 (class 2606 OID 21837)
-- Name: livre langue_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livre
    ADD CONSTRAINT langue_fk FOREIGN KEY (id_langue) REFERENCES public.langue(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3003 (class 2606 OID 21842)
-- Name: illustration livre_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.illustration
    ADD CONSTRAINT livre_fk FOREIGN KEY (id_livre) REFERENCES public.livre(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3009 (class 2606 OID 21847)
-- Name: many_livre_has_many_genre livre_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.many_livre_has_many_genre
    ADD CONSTRAINT livre_fk FOREIGN KEY (id_livre) REFERENCES public.livre(id) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3013 (class 2606 OID 21852)
-- Name: ouvrage livre_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ouvrage
    ADD CONSTRAINT livre_fk FOREIGN KEY (id_livre) REFERENCES public.livre(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3007 (class 2606 OID 21857)
-- Name: many_livre_has_many_auteur livre_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.many_livre_has_many_auteur
    ADD CONSTRAINT livre_fk FOREIGN KEY (id_livre) REFERENCES public.livre(id) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3014 (class 2606 OID 21862)
-- Name: pret ouvrage_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pret
    ADD CONSTRAINT ouvrage_fk FOREIGN KEY (id_ouvrage) REFERENCES public.ouvrage(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3001 (class 2606 OID 21867)
-- Name: adresse pays_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.adresse
    ADD CONSTRAINT pays_fk FOREIGN KEY (id_pays) REFERENCES public.pays(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3010 (class 2606 OID 21872)
-- Name: many_utilisateur_has_many_role role_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.many_utilisateur_has_many_role
    ADD CONSTRAINT role_fk FOREIGN KEY (id_role) REFERENCES public.role(id) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3011 (class 2606 OID 21877)
-- Name: many_utilisateur_has_many_role utilisateur_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.many_utilisateur_has_many_role
    ADD CONSTRAINT utilisateur_fk FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3015 (class 2606 OID 21882)
-- Name: pret utilisateur_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pret
    ADD CONSTRAINT utilisateur_fk FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id) MATCH FULL ON UPDATE CASCADE ON DELETE SET NULL;


-- Completed on 2021-09-15 13:39:01

--
-- PostgreSQL database dump complete
--

