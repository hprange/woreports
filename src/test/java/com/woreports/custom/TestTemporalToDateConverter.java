package com.woreports.custom;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestTemporalToDateConverter {
    private static int minute(Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    private static int hour(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private static int day(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static int month(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    private static int year(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    private static int second(Calendar calendar) {
        return calendar.get(Calendar.SECOND);
    }

    private TemporalToDateConverter converter;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        converter = new TemporalToDateConverter("fieldName");
    }

    @Test
    public void convertLocalDateToDateWhenEvaluatingExpression() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("fieldName", LocalDate.of(2021, 11, 4));

        Object result = converter.evaluate(fields, emptyMap(), emptyMap());

        assertThat(result, instanceOf(Date.class));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) result);

        assertThat(year(calendar), is(2021));
        assertThat(month(calendar), is(11));
        assertThat(day(calendar), is(4));
        assertThat(hour(calendar), is(0));
        assertThat(minute(calendar), is(0));
        assertThat(second(calendar), is(0));
    }

    @Test
    public void convertLocalDateToDateUsingTimeZoneParameterWhenEvaluatingExpression() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        Map<String, Object> fields = new HashMap<>();
        fields.put("fieldName", LocalDate.of(2021, 11, 4));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("US/Eastern"));

        Object result = converter.evaluate(fields, emptyMap(), parameters);

        assertThat(result, instanceOf(Date.class));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) result);

        assertThat(year(calendar), is(2021));
        assertThat(month(calendar), is(11));
        assertThat(day(calendar), is(4));
        assertThat(hour(calendar), is(1));
        assertThat(minute(calendar), is(0));
        assertThat(second(calendar), is(0));
    }

    @Test
    public void convertLocalDateTimeToDateWhenEvaluatingExpression() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        Map<String, Object> fields = new HashMap<>();
        fields.put("fieldName", LocalDateTime.of(2021, 11, 4, 10, 56, 24));

        Object result = converter.evaluate(fields, emptyMap(), emptyMap());

        assertThat(result, instanceOf(Date.class));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) result);

        assertThat(year(calendar), is(2021));
        assertThat(month(calendar), is(11));
        assertThat(day(calendar), is(4));
        assertThat(hour(calendar), is(10));
        assertThat(minute(calendar), is(56));
        assertThat(second(calendar), is(24));
    }

    @Test
    public void convertLocalDateTimeToDateUsingTimeZoneParameterWhenEvaluatingExpression() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("fieldName", LocalDateTime.of(2021, 11, 4, 10, 56, 24));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("US/Eastern"));

        Object result = converter.evaluate(fields, emptyMap(), parameters);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) result);

        assertThat(year(calendar), is(2021));
        assertThat(month(calendar), is(11));
        assertThat(day(calendar), is(4));
        assertThat(hour(calendar), is(11));
        assertThat(minute(calendar), is(56));
        assertThat(second(calendar), is(24));
    }

    @Test
    public void convertOffsetDateTimeToDateWhenEvaluatingExpression() {
        Map<String, Object> fields = new HashMap<>();
        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        fields.put("fieldName", OffsetDateTime.of(2021, 11, 4, 10, 56, 24, 0, offset));

        Object result = converter.evaluate(fields, emptyMap(), emptyMap());

        assertThat(result, instanceOf(Date.class));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) result);

        assertThat(year(calendar), is(2021));
        assertThat(month(calendar), is(11));
        assertThat(day(calendar), is(4));
        assertThat(hour(calendar), is(10));
        assertThat(minute(calendar), is(56));
        assertThat(second(calendar), is(24));
    }

    @Test
    public void convertZonedDateTimeToDateWhenEvaluatingExpression() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("fieldName", ZonedDateTime.of(2021, 11, 4, 10, 56, 24, 0, ZoneId.systemDefault()));

        Object result = converter.evaluate(fields, emptyMap(), emptyMap());

        assertThat(result, instanceOf(Date.class));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) result);

        assertThat(year(calendar), is(2021));
        assertThat(month(calendar), is(11));
        assertThat(day(calendar), is(4));
        assertThat(hour(calendar), is(10));
        assertThat(minute(calendar), is(56));
        assertThat(second(calendar), is(24));
    }

    @Test
    public void returnNullIfValueNullWhenEvaluatingExpression() throws Exception {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("fieldName", null);

        Object result = converter.evaluate(fields, emptyMap(), emptyMap());

        assertThat(result, nullValue());
    }

    @Test
    public void throwExceptionIfFieldNotFoundWhenEvaluatingExpression() throws Exception {
        Map<String, Object> fields = new HashMap<>();
        fields.put("unknown", LocalDate.of(2021, 11, 4));

        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(is("There's no field 'fieldName' in this report."));

        converter.evaluate(fields, emptyMap(), emptyMap());
    }

    @Test
    public void throwExceptionIfValueIsNotLocalDateWhenEvaluatingExpression() throws Exception {
        Map<String, Object> fields = new HashMap<>();
        fields.put("fieldName", "I'm a String");

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(is("Expecting a java.time.temporal.Temporal, but got a java.lang.String."));

        converter.evaluate(fields, emptyMap(), emptyMap());
    }

    @Test
    public void throwExceptionIfTimeZoneParameterIsNotATimeZoneWhenEvaluatingExpression() throws Exception {
        Map<String, Object> fields = new HashMap<>();
        fields.put("fieldName", LocalDate.of(2021, 11, 4));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TIME_ZONE", "TimeZone");

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(is("Expecting a java.util.TimeZone, but got a java.lang.String."));

        converter.evaluate(fields, emptyMap(), parameters);
    }
}
