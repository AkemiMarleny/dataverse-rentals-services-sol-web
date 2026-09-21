--
-- PostgreSQL database dump
--

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: personas; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.personas (id, apellidos, correo, fecha_creacion, fecha_modificacion, fecha_nacimiento, genero_code, nombre_comercial, nombres, numero_celular, numero_documento, persona_juridica_checksum, persona_natural_checksum, persona_natural_negocio_checksum, razon_social, tipo_documento_code, tipo_persona_code, usuario_creacion_id, usuario_modificacion_id) FROM stdin;
1	Sys	info@dataverse.com	2026-06-05 11:49:39.710268	2026-06-05 11:49:39.710287	1990-10-10	FEMENINO	\N	Dataverse	(+51) 987654212	89876567	\N	7f0a0983a69dc109d14052e1ca61deee	\N	\N	DNI	NATURAL_SIN_NEGOCIO	1	1
2	Admi	admin@dataverse.com	2026-06-05 11:49:39.897193	2026-06-05 11:49:39.897197	1990-10-10	MASCULINO	\N	Dataverse	(+51) 997654212	12345678	\N	bf5266002fcade154e4047b1d02c3d29	\N	\N	DNI	NATURAL_SIN_NEGOCIO	1	1
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.usuarios (id, contrasena_hash, estado_code, fecha_creacion, fecha_modificacion, fecha_ultimo_login, mfa_backup_codes, mfa_enabled, mfa_secret, mfa_setup_required, nombre_usuario, persona_id, rol_code, usuario_creacion_id, usuario_modificacion_id) FROM stdin;
1	$2a$10$x2tdEt2gl.7XH4tYwWjEXuHWGaYzRZfFI8OjgQm0wRS0b8GF0afvS	ACTIVO	2026-06-05 11:49:39.884807	\N	\N	\N	t	\N	t	dataversesys	1	SYSTEM	1	\N
2	$2a$10$x2tdEt2gl.7XH4tYwWjEXuHWGaYzRZfFI8OjgQm0wRS0b8GF0afvS	ACTIVO	2026-06-05 11:49:39.982223	\N	\N	\N	t	\N	t	dataverseadmin	2	ADMINISTRADOR	1	\N
\.


--
-- Data for Name: categorias_gasto; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.categorias_gasto (id, descripcion, fecha_creacion, fecha_modificacion, nombre, usuario_creacion_id, usuario_modificacion_id) FROM stdin;
\.


--
-- Data for Name: edificios; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.edificios (id, direccion, fecha_creacion, fecha_modificacion, nombre, numero_pisos, usuario_creacion_id, usuario_modificacion_id) FROM stdin;
\.


--
-- Data for Name: unidades; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.unidades (id, area_metros_cuadrados, edificio_id, fecha_creacion, fecha_modificacion, numero_piso, numero_puerta, tipo_acabado_code, usuario_creacion_id, usuario_modificacion_id) FROM stdin;
\.


--
-- Data for Name: contratos; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.contratos (id, alquiler_mensual, fecha_cierre, fecha_creacion, fecha_fin, fecha_inicio, pdf_url, persona_id, unidad_id, usuario_creacion_id) FROM stdin;
\.


--
-- Data for Name: propuestas_liquidacion; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.propuestas_liquidacion (id, aprobador_id, codigo, edificio_id, estado_code, fecha_aprobacion, fecha_calculo, fecha_creacion, fecha_fin_periodo, fecha_inicio_periodo, monto_total, usuario_creacion_id) FROM stdin;
\.


--
-- Data for Name: detalle_propuestas_liquidacion; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.detalle_propuestas_liquidacion (id, descripcion, fecha_creacion, monto, propuesta_liquidacion_id, tipo_concepto_propuesta, usuario_creacion_id) FROM stdin;
\.


--
-- Data for Name: recibos; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.recibos (id, codigo, contrato_id, estado_code, fecha_creacion, fecha_emision, fecha_vencimiento, monto_pagado, monto_total) FROM stdin;
\.


--
-- Data for Name: detalles_recibo; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.detalles_recibo (id, descripcion, fecha_creacion, monto, recibo_id, tipo_concepto, usuario_creacion_id) FROM stdin;
\.


--
-- Data for Name: gastos_operativos; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.gastos_operativos (id, categoria_gasto_id, descripcion, edificio_id, fecha_creacion, fecha_gasto, fecha_modificacion, monto, tipo_gasto_code, usuario_creacion_id, usuario_modificacion_id) FROM stdin;
\.


--
-- Data for Name: medidores; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.medidores (id, fecha_creacion, fecha_modificacion, numero_serie, tipo_medidor_code, unidad_id, usuario_creacion_id, usuario_modificacion_id) FROM stdin;
\.


--
-- Data for Name: lecturas_historicas; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.lecturas_historicas (id, fecha_creacion, fecha_lectura, fecha_modificacion, medidor_id, usuario_creacion_id, usuario_modificacion_id, valor_lectura) FROM stdin;
\.


--
-- Data for Name: pagos; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.pagos (id, estado_codigo, fecha_aprobacion, fecha_creacion, fecha_rechazo, monto_pagado, motivo_rechazo, observaciones, recibo_id, url_voucher, usuario_creacion_id, validador_id) FROM stdin;
\.


--
-- Data for Name: refresh_tokens; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.refresh_tokens (id, created_at, expiry_date, ip_address, revoked, token, user_agent, usuario_id) FROM stdin;
\.


--
-- Name: categorias_gasto_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.categorias_gasto_id_seq', 1, false);


--
-- Name: contratos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.contratos_id_seq', 1, false);


--
-- Name: detalle_propuestas_liquidacion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.detalle_propuestas_liquidacion_id_seq', 1, false);


--
-- Name: detalles_recibo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.detalles_recibo_id_seq', 1, false);


--
-- Name: edificios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.edificios_id_seq', 1, false);


--
-- Name: gastos_operativos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.gastos_operativos_id_seq', 1, false);


--
-- Name: lecturas_historicas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.lecturas_historicas_id_seq', 1, false);


--
-- Name: medidores_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.medidores_id_seq', 1, false);


--
-- Name: pagos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.pagos_id_seq', 1, false);


--
-- Name: personas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.personas_id_seq', 2, true);


--
-- Name: propuestas_liquidacion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.propuestas_liquidacion_id_seq', 1, false);


--
-- Name: recibos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.recibos_id_seq', 1, false);


--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.refresh_tokens_id_seq', 1, false);


--
-- Name: unidades_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.unidades_id_seq', 1, false);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 2, true);


--
-- PostgreSQL database dump complete
--
