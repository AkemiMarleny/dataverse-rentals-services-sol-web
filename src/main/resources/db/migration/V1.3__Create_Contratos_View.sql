CREATE VIEW v_contratos AS
SELECT 
    c.id,
    c.unidad_id,
    c.persona_id,
    c.fecha_inicio,
    c.fecha_fin,
    c.fecha_cierre,
    c.alquiler_mensual,
    c.pdf_url,
    c.fecha_creacion,
    c.usuario_creacion_id,
    CASE 
        WHEN fecha_cierre is not null THEN 'CULMINADO'
        WHEN fecha_fin < NOW() THEN 'VENCIDO'
        WHEN (fecha_fin - INTERVAL '30 days') < NOW() THEN 'POR_VENCER'
    ELSE 'VIGENTE'
    END AS estado
FROM contratos as c;
