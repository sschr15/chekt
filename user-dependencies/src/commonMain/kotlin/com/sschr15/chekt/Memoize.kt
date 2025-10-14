package com.sschr15.chekt

/**
 * Marks a function to be memoized by the compiler plugin.
 * This creates an underlying field and method pair that
 * caches results based on the function's parameters.
 *
 * Note that this assumes that the function's inputs are
 * hashable. If any input type lacks a `hashCode` override,
 * memoization capabilities will be limited.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
public annotation class Memoize
