--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.14
-- Dumped by pg_dump version 9.5.14

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- Name: get_customer_service(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_customer_service(p_customer_id integer) RETURNS character varying
    LANGUAGE plpgsql
    AS $$ 
DECLARE
    total_payment NUMERIC ; 
    service_level VARCHAR (25) ;
BEGIN
 -- get the rate based on film_id
     SELECT
 INTO total_payment SUM (amount)
     FROM
 payment
     WHERE
 customer_id = p_customer_id ; 
  
   CASE
      WHEN total_payment > 200 THEN
         service_level = 'Platinum' ;
      WHEN total_payment > 100 THEN
 service_level = 'Gold' ;
      ELSE
         service_level = 'Silver' ;
   END CASE ;
 
   RETURN service_level ;
END ; $$;


ALTER FUNCTION public.get_customer_service(p_customer_id integer) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: component_amonium_meter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component_amonium_meter (
    id integer NOT NULL,
    compdescription text,
    component_type_id integer,
    createdtime timestamp with time zone,
    endtime timestamp with time zone,
    ogr_geometry public.geometry,
    sensor_object_id text,
    datasource_id integer
);


ALTER TABLE public.component_amonium_meter OWNER TO postgres;

--
-- Name: component_energy_meter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component_energy_meter (
    id integer NOT NULL,
    compdescription text,
    component_type_id integer,
    createdtime timestamp with time zone,
    endtime timestamp with time zone,
    ogr_geometry public.geometry,
    sensor_object_id text,
    datasource_id integer
);


ALTER TABLE public.component_energy_meter OWNER TO postgres;

--
-- Name: component_flow_meter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component_flow_meter (
    id integer NOT NULL,
    compdescription text,
    component_type_id integer,
    createdtime timestamp with time zone,
    endtime timestamp with time zone,
    ogr_geometry public.geometry,
    sensor_object_id text,
    datasource_id integer
);


ALTER TABLE public.component_flow_meter OWNER TO postgres;

--
-- Name: component_level_meter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component_level_meter (
    id integer NOT NULL,
    compdescription text,
    component_type_id integer,
    createdtime timestamp with time zone,
    endtime timestamp with time zone,
    ogr_geometry public.geometry,
    sensor_object_id text,
    datasource_id integer
);


ALTER TABLE public.component_level_meter OWNER TO postgres;

--
-- Name: component_motor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component_motor (
    id integer NOT NULL,
    compdescription text,
    component_type_id integer,
    createdtime timestamp with time zone,
    endtime timestamp with time zone,
    ogr_geometry public.geometry,
    sensor_object_id text,
    datasource_id integer
);


ALTER TABLE public.component_motor OWNER TO postgres;

--
-- Name: component_nitrates_meter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component_nitrates_meter (
    id integer NOT NULL,
    compdescription text,
    component_type_id integer,
    createdtime timestamp with time zone,
    endtime timestamp with time zone,
    ogr_geometry public.geometry,
    sensor_object_id text,
    datasource_id integer
);


ALTER TABLE public.component_nitrates_meter OWNER TO postgres;

--
-- Name: component_rat_spear; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component_rat_spear (
    id integer NOT NULL,
    compdescription text,
    component_type_id integer,
    createdtime timestamp with time zone,
    endtime timestamp with time zone,
    ogr_geometry public.geometry,
    sensor_object_id text,
    datasource_id integer
);


ALTER TABLE public.component_rat_spear OWNER TO postgres;

--
-- Name: component_star_controls; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component_star_controls (
    id integer NOT NULL,
    compdescription text,
    component_type_id integer,
    createdtime timestamp with time zone,
    endtime timestamp with time zone,
    ogr_geometry public.geometry,
    sensor_object_id text,
    datasource_id integer
);


ALTER TABLE public.component_star_controls OWNER TO postgres;

--
-- Name: component_thermometer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component_thermometer (
    id integer NOT NULL,
    compdescription text,
    component_type_id integer,
    createdtime timestamp with time zone,
    endtime timestamp with time zone,
    ogr_geometry public.geometry,
    sensor_object_id text,
    datasource_id integer
);


ALTER TABLE public.component_thermometer OWNER TO postgres;

--
-- Name: config_aggregation_and_store; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_aggregation_and_store (
    calculation integer NOT NULL,
    data_io_id integer NOT NULL,
    aggregation_type integer NOT NULL,
    unit text,
    store boolean,
    template_type integer NOT NULL,
    aggregate boolean,
    scale_to_unit numeric,
    status_on boolean,
    max numeric(9,2),
    high numeric(9,2),
    low numeric(9,2),
    min numeric(9,2)
);


ALTER TABLE public.config_aggregation_and_store OWNER TO postgres;

--
-- Name: config_aggregation_calculation_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_aggregation_calculation_type (
    type integer NOT NULL,
    name text,
    formula text
);


ALTER TABLE public.config_aggregation_calculation_type OWNER TO postgres;

--
-- Name: config_aggregationtype_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.config_aggregationtype_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.config_aggregationtype_id_seq OWNER TO postgres;

--
-- Name: config_aggregationtype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_aggregationtype (
    type integer DEFAULT nextval('public.config_aggregationtype_id_seq'::regclass) NOT NULL,
    name text,
    minutes integer,
    aggregation_calculation_type integer
);


ALTER TABLE public.config_aggregationtype OWNER TO postgres;

--
-- Name: config_calculation_and_store; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_calculation_and_store (
    calculation integer NOT NULL,
    data_io_id integer NOT NULL,
    formula text,
    template_type integer NOT NULL
);


ALTER TABLE public.config_calculation_and_store OWNER TO postgres;

--
-- Name: config_calculationtype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_calculationtype (
    type integer NOT NULL,
    name text
);


ALTER TABLE public.config_calculationtype OWNER TO postgres;

--
-- Name: config_component_type_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.config_component_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.config_component_type_seq OWNER TO postgres;

--
-- Name: config_component_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_component_type (
    id uuid NOT NULL,
    name text,
    type integer DEFAULT nextval('public.config_component_type_seq'::regclass),
    datastore_id uuid,
    component_table_name text,
    wfs text,
    edittime timestamp without time zone,
    wfs_layer text,
    field_id text,
    field_name text,
    field_description text,
    z_order integer
);


ALTER TABLE public.config_component_type OWNER TO postgres;

--
-- Name: config_dashboard; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_dashboard (
    id integer NOT NULL,
    title text NOT NULL,
    cards text NOT NULL
);


ALTER TABLE public.config_dashboard OWNER TO postgres;

--
-- Name: config_dashboard_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.config_dashboard_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.config_dashboard_id_seq OWNER TO postgres;

--
-- Name: config_dashboard_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.config_dashboard_id_seq OWNED BY public.config_dashboard.id;


--
-- Name: config_data_io; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_data_io (
    id integer NOT NULL,
    sensor_object_name text,
    sensor_object_alias text,
    unit text,
    measurement_type integer,
    template_type integer,
    sensor_object_id text,
    description text,
    sensor_object_node_id text,
    datasource_id integer,
    sensor_object_description text,
    datasource_name text,
    is_battery_status boolean
);


ALTER TABLE public.config_data_io OWNER TO postgres;

--
-- Name: config_data_store; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_data_store (
    id uuid NOT NULL,
    name text,
    server text,
    database text,
    port text,
    "user" text,
    passw text,
    schema text
);


ALTER TABLE public.config_data_store OWNER TO postgres;

--
-- Name: config_measurement_template_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.config_measurement_template_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.config_measurement_template_seq OWNER TO postgres;

--
-- Name: config_measurement_template; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_measurement_template (
    id uuid NOT NULL,
    measurement_alias text,
    measurement_name text,
    measurement_type integer,
    template_type integer DEFAULT nextval('public.config_measurement_template_seq'::regclass)
);


ALTER TABLE public.config_measurement_template OWNER TO postgres;

--
-- Name: config_measurement_template_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.config_measurement_template_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.config_measurement_template_type_id_seq OWNER TO postgres;

--
-- Name: config_measurement_template_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_measurement_template_type (
    id integer DEFAULT nextval('public.config_measurement_template_type_id_seq'::regclass) NOT NULL,
    name text,
    searchtext text
);


ALTER TABLE public.config_measurement_template_type OWNER TO postgres;

--
-- Name: config_measurement_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.config_measurement_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.config_measurement_type_id_seq OWNER TO postgres;

--
-- Name: config_measurement_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_measurement_type (
    id integer DEFAULT nextval('public.config_measurement_type_id_seq'::regclass) NOT NULL,
    name text,
    is_signal_strength boolean,
    is_battery_status boolean,
    language text DEFAULT 'da'::text NOT NULL
);


ALTER TABLE public.config_measurement_type OWNER TO postgres;

--
-- Name: config_object_component_data_io_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_object_component_data_io_relation (
    id integer NOT NULL,
    relation_id uuid NOT NULL,
    object_type integer,
    object_key text,
    relation_type integer,
    component_type integer,
    component_key integer,
    data_io_key integer,
    data_io_type integer,
    createtime timestamp with time zone,
    endtime timestamp with time zone
);


ALTER TABLE public.config_object_component_data_io_relation OWNER TO postgres;

--
-- Name: config_object_component_data_io_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.config_object_component_data_io_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.config_object_component_data_io_relation_id_seq OWNER TO postgres;

--
-- Name: config_object_component_data_io_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.config_object_component_data_io_relation_id_seq OWNED BY public.config_object_component_data_io_relation.id;


--
-- Name: config_object_type_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.config_object_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.config_object_type_seq OWNER TO postgres;

--
-- Name: config_object_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_object_type (
    id uuid NOT NULL,
    name text,
    type integer DEFAULT nextval('public.config_object_type_seq'::regclass),
    datastore_id uuid,
    key_description text,
    object_table_name text,
    wfs text,
    edittime timestamp without time zone,
    wfs_layer text,
    field_id text,
    field_name text,
    field_description text,
    z_order integer
);


ALTER TABLE public.config_object_type OWNER TO postgres;

--
-- Name: config_status_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_status_types (
    type integer NOT NULL,
    name character varying(255) NOT NULL,
    value numeric
);


ALTER TABLE public.config_status_types OWNER TO postgres;

--
-- Name: config_support_layer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_support_layer (
    id integer NOT NULL,
    name text NOT NULL,
    wfs text NOT NULL,
    z_order integer
);


ALTER TABLE public.config_support_layer OWNER TO postgres;

--
-- Name: config_support_layer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.config_support_layer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.config_support_layer_id_seq OWNER TO postgres;

--
-- Name: config_support_layer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.config_support_layer_id_seq OWNED BY public.config_support_layer.id;


--
-- Name: config_unit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.config_unit (
    id integer NOT NULL,
    name text,
    description text,
    language text NOT NULL
);


ALTER TABLE public.config_unit OWNER TO postgres;

--
-- Name: data_data_store; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.data_data_store (
    id integer NOT NULL,
    name text,
    server text,
    database text,
    port text,
    "user" text,
    passw text
);


ALTER TABLE public.data_data_store OWNER TO postgres;

--
-- Name: datasource; Type: TABLE; Schema: public; Owner: hepwat
--

CREATE TABLE public.datasource (
    datasource_id integer NOT NULL,
    name character varying(255),
    url text NOT NULL,
    authentication_type text,
    description text,
    username character varying(255),
    password character varying(255),
    updated timestamp without time zone,
    dashboard_url text,
    robot_started boolean,
    notification_on boolean,
    availability integer
);


ALTER TABLE public.datasource OWNER TO hepwat;

--
-- Name: onfig_component_type_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.onfig_component_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.onfig_component_type_seq OWNER TO postgres;

--
-- Name: sensor_object; Type: TABLE; Schema: public; Owner: hepwat
--

CREATE TABLE public.sensor_object (
    datasource_id integer NOT NULL,
    sensor_object_id character varying(255) NOT NULL,
    name character varying(255),
    description text,
    updated timestamp without time zone,
    location text,
    ogr_geometry public.geometry,
    configured boolean DEFAULT false,
    name_alias text
);


ALTER TABLE public.sensor_object OWNER TO hepwat;

--
-- Name: sensor_object_nodes_seq; Type: SEQUENCE; Schema: public; Owner: hepwat
--

CREATE SEQUENCE public.sensor_object_nodes_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sensor_object_nodes_seq OWNER TO hepwat;

--
-- Name: sensor_object_nodes; Type: TABLE; Schema: public; Owner: hepwat
--

CREATE TABLE public.sensor_object_nodes (
    datasource_id integer NOT NULL,
    sensor_object_id character varying(255) NOT NULL,
    sensor_object_node_id character varying(255) NOT NULL,
    sensor_object_node_key integer DEFAULT nextval('public.sensor_object_nodes_seq'::regclass) NOT NULL,
    name character varying(255),
    description text,
    datatype character varying(255),
    nodetype_old character varying(255),
    nodedomain text,
    readable boolean DEFAULT false NOT NULL,
    writeable boolean DEFAULT false NOT NULL,
    "interval" integer DEFAULT 10000 NOT NULL,
    updated timestamp without time zone,
    unit text,
    lastrun timestamp with time zone DEFAULT '1970-01-01 01:00:00+01'::timestamp with time zone,
    status text,
    sensor_log boolean,
    configured boolean DEFAULT false,
    lastvalue character varying(255) DEFAULT '-999'::integer,
    name_alias text,
    nodetype integer
);


ALTER TABLE public.sensor_object_nodes OWNER TO hepwat;

--
-- Name: services_configuration_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.services_configuration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.services_configuration_id_seq OWNER TO postgres;

--
-- Name: services_configuration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.services_configuration (
    service_id integer DEFAULT nextval('public.services_configuration_id_seq'::regclass) NOT NULL,
    description text,
    name text,
    type text,
    kafka_broker text,
    client_id text,
    topic_group_id text,
    seek_to_end boolean,
    auto_offset text,
    service_aggtype integer,
    calculation_types text,
    output_topic text,
    state_topic text,
    mongodb_datastore_id integer,
    aggregation_types text,
    service_calctype integer,
    input_topic text,
    state_dir text,
    commit_interval integer,
    aggregation_interval integer,
    skip_filter boolean,
    recycle_data_interval integer
);


ALTER TABLE public.services_configuration OWNER TO postgres;

--
-- Name: services_processing_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.services_processing_config (
    agg_type integer NOT NULL,
    calc_type integer NOT NULL,
    topic text,
    collection text,
    store integer,
    data_type integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.services_processing_config OWNER TO postgres;

--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_dashboard ALTER COLUMN id SET DEFAULT nextval('public.config_dashboard_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_object_component_data_io_relation ALTER COLUMN id SET DEFAULT nextval('public.config_object_component_data_io_relation_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_support_layer ALTER COLUMN id SET DEFAULT nextval('public.config_support_layer_id_seq'::regclass);


--
-- Name: calculation_and_store_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_calculation_and_store
    ADD CONSTRAINT calculation_and_store_pkey PRIMARY KEY (calculation, data_io_id, template_type);


--
-- Name: component_amonium_meter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.component_amonium_meter
    ADD CONSTRAINT component_amonium_meter_pkey PRIMARY KEY (id);


--
-- Name: component_energy_meter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.component_energy_meter
    ADD CONSTRAINT component_energy_meter_pkey PRIMARY KEY (id);


--
-- Name: component_flow_meter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.component_flow_meter
    ADD CONSTRAINT component_flow_meter_pkey PRIMARY KEY (id);


--
-- Name: component_level_meter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.component_level_meter
    ADD CONSTRAINT component_level_meter_pkey PRIMARY KEY (id);


--
-- Name: component_motor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.component_motor
    ADD CONSTRAINT component_motor_pkey PRIMARY KEY (id);


--
-- Name: component_nitrates_meter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.component_nitrates_meter
    ADD CONSTRAINT component_nitrates_meter_pkey PRIMARY KEY (id);


--
-- Name: component_rat_spear_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.component_rat_spear
    ADD CONSTRAINT component_rat_spear_pkey PRIMARY KEY (id);


--
-- Name: component_star_controls_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.component_star_controls
    ADD CONSTRAINT component_star_controls_pkey PRIMARY KEY (id);


--
-- Name: component_thermometer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.component_thermometer
    ADD CONSTRAINT component_thermometer_pkey PRIMARY KEY (id);


--
-- Name: config_aggregation_and_store_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_aggregation_and_store
    ADD CONSTRAINT config_aggregation_and_store_pkey PRIMARY KEY (calculation, data_io_id, aggregation_type, template_type);


--
-- Name: config_aggregation_calculation_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_aggregation_calculation_type
    ADD CONSTRAINT config_aggregation_calculation_type_pkey PRIMARY KEY (type);


--
-- Name: config_aggregationtype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_aggregationtype
    ADD CONSTRAINT config_aggregationtype_pkey PRIMARY KEY (type);


--
-- Name: config_calculationtype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_calculationtype
    ADD CONSTRAINT config_calculationtype_pkey PRIMARY KEY (type);


--
-- Name: config_component_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_component_type
    ADD CONSTRAINT config_component_type_pkey PRIMARY KEY (id);


--
-- Name: config_dashboard_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_dashboard
    ADD CONSTRAINT config_dashboard_pkey PRIMARY KEY (id);


--
-- Name: config_data_io_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_data_io
    ADD CONSTRAINT config_data_io_pkey PRIMARY KEY (id);


--
-- Name: config_data_store_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_data_store
    ADD CONSTRAINT config_data_store_pkey PRIMARY KEY (id);


--
-- Name: config_measurement_template_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_measurement_template
    ADD CONSTRAINT config_measurement_template_pkey PRIMARY KEY (id);


--
-- Name: config_measurement_template_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_measurement_template_type
    ADD CONSTRAINT config_measurement_template_type_pkey PRIMARY KEY (id);


--
-- Name: config_measurement_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_measurement_type
    ADD CONSTRAINT config_measurement_type_pkey PRIMARY KEY (id, language);


--
-- Name: config_object_component_data_io_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_object_component_data_io_relation
    ADD CONSTRAINT config_object_component_data_io_relation_pkey PRIMARY KEY (id, relation_id);


--
-- Name: config_object_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_object_type
    ADD CONSTRAINT config_object_type_pkey PRIMARY KEY (id);


--
-- Name: config_status_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_status_types
    ADD CONSTRAINT config_status_types_pkey PRIMARY KEY (type);


--
-- Name: config_support_layer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_support_layer
    ADD CONSTRAINT config_support_layer_pkey PRIMARY KEY (id);


--
-- Name: config_unit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.config_unit
    ADD CONSTRAINT config_unit_pkey PRIMARY KEY (id, language);


--
-- Name: data_aggregationtype_store_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.services_processing_config
    ADD CONSTRAINT data_aggregationtype_store_pkey PRIMARY KEY (agg_type, calc_type, data_type);


--
-- Name: data_data_store_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.data_data_store
    ADD CONSTRAINT data_data_store_pkey PRIMARY KEY (id);


--
-- Name: datasource_pkey; Type: CONSTRAINT; Schema: public; Owner: hepwat
--

ALTER TABLE ONLY public.datasource
    ADD CONSTRAINT datasource_pkey PRIMARY KEY (datasource_id);


--
-- Name: sensor_object_nodes_pkey; Type: CONSTRAINT; Schema: public; Owner: hepwat
--

ALTER TABLE ONLY public.sensor_object_nodes
    ADD CONSTRAINT sensor_object_nodes_pkey PRIMARY KEY (sensor_object_id, datasource_id, sensor_object_node_id);


--
-- Name: sensor_object_pkey; Type: CONSTRAINT; Schema: public; Owner: hepwat
--

ALTER TABLE ONLY public.sensor_object
    ADD CONSTRAINT sensor_object_pkey PRIMARY KEY (datasource_id, sensor_object_id);


--
-- Name: services_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.services_configuration
    ADD CONSTRAINT services_configuration_pkey PRIMARY KEY (service_id);


--
-- Name: idx_sensor_object_node_key; Type: INDEX; Schema: public; Owner: hepwat
--

CREATE INDEX idx_sensor_object_node_key ON public.sensor_object_nodes USING btree (sensor_object_node_key, sensor_object_node_id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

