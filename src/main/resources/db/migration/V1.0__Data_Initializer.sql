BEGIN;
-- EA-01
INSERT INTO edificios (
        direccion,
        nombre,
        numero_pisos,
        fecha_creacion,
        fecha_modificacion,
        usuario_creacion_id,
        usuario_modificacion_id
    )
VALUES (
        'Av. Artemis 132',
        'Edificio Artemis',
        3,
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    );
-- zona medidores
insert into unidades (
        edificio_id,
        area_metros_cuadrados,
        numero_piso,
        numero_puerta,
        tipo_acabado,
        fecha_creacion,
        fecha_modificacion,
        usuario_creacion_id,
        usuario_modificacion_id
    )
VALUES (
        1,
        5,
        0,
        'A0',
        'CASCO_GRIS',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    );
-- primer piso
insert into unidades (
        edificio_id,
        area_metros_cuadrados,
        numero_piso,
        numero_puerta,
        tipo_acabado,
        fecha_creacion,
        fecha_modificacion,
        usuario_creacion_id,
        usuario_modificacion_id
    )
VALUES (
        1,
        40,
        1,
        'A1',
        'BASICO',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    ),
    (
        1,
        100,
        1,
        'A2',
        'ESTANDAR',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    ),
    (
        1,
        90,
        1,
        'A3',
        'SEMI_LUJO',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    );
-- segundo piso
insert into unidades (
        edificio_id,
        area_metros_cuadrados,
        numero_piso,
        numero_puerta,
        tipo_acabado,
        fecha_creacion,
        fecha_modificacion,
        usuario_creacion_id,
        usuario_modificacion_id
    )
VALUES (
        1,
        40,
        2,
        'B1',
        'BASICO',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    ),
    (
        1,
        100,
        2,
        'B2',
        'ESTANDAR',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    ),
    (
        1,
        90,
        2,
        'B3',
        'SEMI_LUJO',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    );
-- tercer piso
insert into unidades (
        edificio_id,
        area_metros_cuadrados,
        numero_piso,
        numero_puerta,
        tipo_acabado,
        fecha_creacion,
        fecha_modificacion,
        usuario_creacion_id,
        usuario_modificacion_id
    )
VALUES (
        1,
        40,
        3,
        'C1',
        'BASICO',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    ),
    (
        1,
        100,
        3,
        'C2',
        'ESTANDAR',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    ),
    (
        1,
        90,
        3,
        'C3',
        'SEMI_LUJO',
        '2025-01-01 10:10',
        '2025-01-01 10:10',
        1,
        1
    );
--
INSERT into medidores (unidad_id, fecha_creacion, fecha_modificacion, numero_serie, tipo_medidor_code, usuario_creacion_id, usuario_modificacion_id)
VALUES -- medidores generales
    (
        1,
        '2026-04-22 10:10',
        '2026-04-22 10:10',
        'E0000',
        'GENERAL_ELECTRICIDAD',
        1,
        1
    ),
    (
        1,
        '2026-04-22 10:10',
        '2026-04-22 10:10',
        'A0000',
        'GENERAL_AGUA',
        1,
        1
    );
-- medidores A1
INSERT into medidores (
        unidad_id,
        fecha_creacion,
        fecha_modificacion,
        numero_serie,
        tipo_medidor_code,
        usuario_creacion_id,
        usuario_modificacion_id
    )
VALUES (
        2,
        '2026-04-22 10:10',
        '2026-04-22 10:10',
        'ME-A1-000',
        'ELECTRICIDAD',
        1,
        1
    ),
    (
        2,
        '2026-04-22 10:10',
        '2026-04-22 10:10',
        'MA-A1-000',
        'AGUA',
        1,
        1
    );
-- medidores A2
INSERT into medidores (
        unidad_id,
        fecha_creacion,
        fecha_modificacion,
        numero_serie,
        tipo_medidor_code,
        usuario_creacion_id,
        usuario_modificacion_id
    )
VALUES (
        3,
        '2026-04-22 10:10',
        '2026-04-22 10:10',
        'ME-A2-000',
        'ELECTRICIDAD',
        1,
        1
    ),
    (
        3,
        '2026-04-22 10:10',
        '2026-04-22 10:10',
        'MA-A2-000',
        'AGUA',
        1,
        1
    );
-- medidores A3
INSERT into medidores (
        unidad_id,
        fecha_creacion,
        fecha_modificacion,
        numero_serie,
        tipo_medidor_code,
        usuario_creacion_id,
        usuario_modificacion_id
    )
VALUES (
        4,
        '2026-04-22 10:10',
        '2026-04-22 10:10',
        'ME-A3-000',
        'ELECTRICIDAD',
        1,
        1
    ),
    (
        4,
        '2026-04-22 10:10',
        '2026-04-22 10:10',
        'MA-A3-000',
        'AGUA',
        1,
        1
    );
-- E0000
insert into lecturas_historicas (
        medidor_id,
        fecha_lectura,
        valor_lectura,
        fecha_creacion,
        usuario_creacion_id,
        fecha_modificacion,
        usuario_modificacion_id
    )
values (
        1,
        '2026-03-01 10:10',
        0,
        '2026-03-01 10:10',
        1,
        '2026-03-01 10:10',
        1
    ),
    (
        1,
        '2026-03-10 10:10',
        100,
        '2026-03-10 10:10',
        1,
        '2026-03-10 10:10',
        1
    ),
    (
        1,
        '2026-04-02 10:10',
        300,
        '2026-04-02 10:10',
        1,
        '2026-04-02 10:10',
        1
    ),
    (
        1,
        '2026-04-29 10:10',
        900,
        '2026-04-29 10:10',
        1,
        '2026-04-29 10:10',
        1
    );
-- A0000
insert into lecturas_historicas (
        medidor_id,
        fecha_lectura,
        valor_lectura,
        fecha_creacion,
        usuario_creacion_id,
        fecha_modificacion,
        usuario_modificacion_id
    )
values (
        2,
        '2026-04-02 10:10',
        1500,
        '2026-04-02 10:10',
        1,
        '2026-04-02 10:10',
        1
    ),
    (
        2,
        '2026-04-22 10:10',
        3000,
        '2026-04-22 10:10',
        1,
        '2026-04-22 10:10',
        1
    );
-- ME-A1-000
insert into lecturas_historicas (
        medidor_id,
        fecha_lectura,
        valor_lectura,
        fecha_creacion,
        usuario_creacion_id,
        fecha_modificacion,
        usuario_modificacion_id
    )
values (
        3,
        '2026-04-02 10:10',
        4500,
        '2026-04-02 10:10',
        1,
        '2026-04-02 10:10',
        1
    ),
    (
        3,
        '2026-04-22 10:10',
        4800,
        '2026-04-22 10:10',
        1,
        '2026-04-22 10:10',
        1
    );
-- MA-A1-000
insert into lecturas_historicas (
        medidor_id,
        fecha_lectura,
        valor_lectura,
        fecha_creacion,
        usuario_creacion_id,
        fecha_modificacion,
        usuario_modificacion_id
    )
values (
        4,
        '2026-04-02 10:10',
        9900,
        '2026-04-02 10:10',
        1,
        '2026-04-02 10:10',
        1
    ),
    (
        4,
        '2026-04-22 10:10',
        10350,
        '2026-04-22 10:10',
        1,
        '2026-04-22 10:10',
        1
    );
-- A1
insert into contratos(
        unidad_id,
        alquiler_mensual,
        fecha_cierre,
        fecha_creacion,
        fecha_fin,
        fecha_inicio,
        pdf_url,
        usuario_creacion_id
    )
values (
        2,
        255.0,
        null,
        '2026-01-01 10:10',
        '2027-01-01 10:10',
        '2026-01-01 10:10',
        null,
        1
    );
-- A2
insert into contratos(
        unidad_id,
        alquiler_mensual,
        fecha_cierre,
        fecha_creacion,
        fecha_fin,
        fecha_inicio,
        pdf_url,
        usuario_creacion_id
    )
values (
        3,
        355.0,
        null,
        '2026-01-01 10:10',
        '2027-01-01 10:10',
        '2026-01-01 10:10',
        null,
        1
    );
-- A3
insert into contratos(
        unidad_id,
        alquiler_mensual,
        fecha_cierre,
        fecha_creacion,
        fecha_fin,
        fecha_inicio,
        pdf_url,
        usuario_creacion_id
    )
values (
        4,
        455.0,
        null,
        '2026-01-01 10:10',
        '2027-01-01 10:10',
        '2026-01-01 10:10',
        null,
        1
    );
--
COMMIT;