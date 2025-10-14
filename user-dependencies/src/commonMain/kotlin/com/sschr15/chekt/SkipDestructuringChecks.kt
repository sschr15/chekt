package com.sschr15.chekt

/**
 * Marks a function, class, or file to skip destructuring checks.
 * 
 * When using this compiler plugin, the plugin will check that all elements of a destructuring declaration are used
 * if the destructed item is a [Collection] (or any subtype).
 * If this behavior is not desired, adding `@SkipDestructuringChecks` will disable it.
 * 
 * As of now, this check is never performed on lambda parameters, but all other locations within a function body
 * are checked.
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.EXPRESSION,
)
@Retention(AnnotationRetention.SOURCE)
public annotation class SkipDestructuringChecks
