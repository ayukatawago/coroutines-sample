package project

enum class Variant {
    BLOCKING,    // Request1Blocking
    BACKGROUND,  // Request2Background
    CALLBACKS,   // Request3Callbacks
    COROUTINE,   // Request4Coroutine
    PROGRESS,    // Request5Progress
    CANCELLABLE, // Request5Progress (too)
    CONCURRENT,  // Request6Concurrent
    FUTURE,      // Request7Future
    GATHER,      // Request8Gather
    ACTOR        // Request9Actor
}