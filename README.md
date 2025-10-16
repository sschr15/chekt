# Chekt

A Kotlin compiler plugin providing some useful-for-scripting checks.

Current features include:

- Integer overflow and numeric precision checks
  - All integer operations have checks when working with both `Int` and `Long`, for built-in operations
    as well as the Iterable extension functions
  - Converting from `Int` to `Float` or from `Long` to either floating-point type is checked for precision,
    where any number too large to be guaranteed to be exactly represented will throw an exception
  - Performing remainder operations will warn if the result would be negative
    (warns up to once, can be disabled on any platform other than wasm<sup>1</sup>)
  - Checks can be disabled with `@SkipOverflowChecks`
- Collection destructuring checks
  - When destructuring from a collection (e.g. `val (a, b, c) = myList`), all elements of the list must be accounted
    for, otherwise an exception is thrown
  - Anonymous parameter names do function (for a three-element list, calling `val (a, _, _) = myList` will silence the
    checks without requiring unused names for the other parameters, while still enforcing the list to have a size of 3)
  - Checks can be disabled with `@SkipDestructuringChecks`
- `@Memoize` annotation on functions
  - When the annotation is applied to the function, the plugin will add a map of inputs previously used to the corresponding
    outputs. This assumes the function does not have side effects or its callers are not dependent on the side effects.
  - Special handling done for zero through three arguments to avoid list instantiation (instead opting for object instantiation)
  - When annotating a method with zero arguments, the annotation essentially becomes a fancy `lazy`

<small><sup>1</sup> On JVM, setting `chekt.warnOnNegativeRemainder` to "false" will disable the warning. On native platforms,
setting `KT_REM_WARNINGS` environment variable to `false` will disable the warning. On JS, setting any value to a
cookie called `chekt.warnOnNegativeRemainder` will disable the warning.</small>

Hopeful eventual features include:
- enum-exhaustive maps
- whatever else I can think of
