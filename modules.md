# Module Chekt User Dependencies

The user-facing compile-time dependencies for Chekt.

The documentation shown hides the internal members that the compiler plugin modifies
calls to. To view that, see the Compilation Dependencies documentation or the source code.

# Module Chekt Compilation Dependencies

The compile-time dependencies for Chekt. This is necessary for the compiler plugin.

All functions are meant for internal use by the compiler plugin. Annotations are the only
public API, but the functions provided may be called by an end user.

For a public-API-only module, see the User Dependencies documentation.

# Module Chekt Compiler Plugin

The compiler plugin for Chekt.

# Module Chekt Gradle Plugin

The Gradle plugin for Chekt.

The Gradle plugin configures the compiler plugin for use in Gradle projects.
It also adds a dependency on the user dependencies automatically.
