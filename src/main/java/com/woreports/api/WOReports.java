package com.woreports.api;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, METHOD })
@BindingAnnotation
public @interface WOReports {}
