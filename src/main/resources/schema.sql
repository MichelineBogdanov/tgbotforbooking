--
-- PostgreSQL database dump
--

-- Dumped from database version 14.6
-- Dumped by pg_dump version 14.6

-- Started on 2025-03-04 20:50:52

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
-- TOC entry 213 (class 1259 OID 25500)
-- Name: notifications_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.notifications_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.notifications_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 212 (class 1259 OID 25429)
-- Name: notifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notifications (
    id bigint DEFAULT nextval('public.notifications_seq'::regclass) NOT NULL,
    notification_datetime timestamp with time zone NOT NULL,
    visit_id bigint
);


ALTER TABLE public.notifications OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 25501)
-- Name: services_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.services_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.services_seq OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 25417)
-- Name: services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.services (
    id bigint DEFAULT nextval('public.services_seq'::regclass) NOT NULL,
    name character varying(255) NOT NULL,
    price bigint
);


ALTER TABLE public.services OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 25502)
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_seq OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 25374)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint DEFAULT nextval('public.users_seq'::regclass) NOT NULL,
    tg_account character varying(255) NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    chat_id bigint,
    tg_user_id bigint NOT NULL,
    is_active boolean,
    notifications_on boolean
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 25503)
-- Name: visits_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.visits_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.visits_seq OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 25381)
-- Name: visits; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.visits (
    id bigint DEFAULT nextval('public.visits_seq'::regclass) NOT NULL,
    visit_datetime timestamp with time zone NOT NULL,
    user_id bigint NOT NULL,
    google_event_id character varying(255),
    service_id bigint
);


ALTER TABLE public.visits OWNER TO postgres;

--
-- TOC entry 3190 (class 2606 OID 25433)
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- TOC entry 3188 (class 2606 OID 25421)
-- Name: services services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.services
    ADD CONSTRAINT services_pkey PRIMARY KEY (id);


--
-- TOC entry 3184 (class 2606 OID 25378)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3186 (class 2606 OID 25385)
-- Name: visits visits_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.visits
    ADD CONSTRAINT visits_pkey PRIMARY KEY (id);


--
-- TOC entry 3193 (class 2606 OID 25440)
-- Name: notifications notifications_VISITS_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT "notifications_VISITS_fk" FOREIGN KEY (visit_id) REFERENCES public.visits(id) NOT VALID;


--
-- TOC entry 3192 (class 2606 OID 25424)
-- Name: visits visits_SERVICE_ID_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.visits
    ADD CONSTRAINT "visits_SERVICE_ID_fkey" FOREIGN KEY (service_id) REFERENCES public.services(id) NOT VALID;


--
-- TOC entry 3191 (class 2606 OID 25386)
-- Name: visits visits_USER_ID_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.visits
    ADD CONSTRAINT "visits_USER_ID_fkey" FOREIGN KEY (user_id) REFERENCES public.users(id);


-- Completed on 2025-03-04 20:50:53

--
-- PostgreSQL database dump complete
--

