CREATE OR REPLACE FUNCTION distance_km(lat1 double precision, lon1 double precision,
                                          lat2 double precision, lon2 double precision)
RETURNS double precision AS $$
BEGIN
    RETURN 6371 * acos(
        GREATEST(-1.0, LEAST(1.0,
            sin(radians(lat1)) * sin(radians(lat2)) +
            cos(radians(lat1)) * cos(radians(lat2)) * cos(radians(lon2) - radians(lon1))
        ))
    );
END;
$$ LANGUAGE plpgsql;
