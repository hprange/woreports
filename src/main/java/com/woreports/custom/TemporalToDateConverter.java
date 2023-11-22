package com.woreports.custom;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.NoSuchElementException;

import ar.com.fdvs.dj.domain.CustomExpression;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TemporalToDateConverter implements CustomExpression {
    private final String fieldName;

    public TemporalToDateConverter(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object evaluate(Map fields, Map variables, Map parameters) {
        if (!fields.containsKey(fieldName)) {
            throw new NoSuchElementException(String.format("There's no field '%s' in this report.", fieldName));
        }

        Object object = fields.get(fieldName);

        if (object == null) {
            return null;
        }

        if (!(object instanceof Temporal)) {
            throw new IllegalStateException(String.format("Expecting a %s, but got a %s.", Temporal.class.getName(), object.getClass().getName()));
        }

        Temporal temporal = null;

        if (object instanceof LocalDate) {
            temporal = ((LocalDate) object).atStartOfDay(ZoneId.systemDefault());
        } else if (object instanceof LocalDateTime) {
            temporal = ((LocalDateTime) object).atZone(ZoneId.systemDefault());
        } else {
            temporal = (Temporal) object;
        }

        return Date.from(Instant.from(temporal));
    }

    @Override
    public String getClassName() {
        return Date.class.getName();
    }
}
