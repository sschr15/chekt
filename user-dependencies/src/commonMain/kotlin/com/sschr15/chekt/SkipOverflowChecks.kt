package com.sschr15.chekt

/**
 * Marks an expression to skip overflow checks.
 * This also disables negative remainder checks.
 * Any called functions will still have checks applied unless also marked
 * or built without the compiler plugin (such as the standard libraries).
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FILE,
)
@Retention(AnnotationRetention.SOURCE)
public annotation class SkipOverflowChecks
