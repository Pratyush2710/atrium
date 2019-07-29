@file:Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")

package ch.tutteli.atrium.domain.builders.creating.collectors

import ch.tutteli.atrium.assertions.Assertion
import ch.tutteli.atrium.assertions.AssertionGroup
import ch.tutteli.atrium.core.Option
import ch.tutteli.atrium.core.polyfills.loadSingleService
import ch.tutteli.atrium.creating.BaseAssertionPlant
import ch.tutteli.atrium.creating.BaseCollectingAssertionPlant
import ch.tutteli.atrium.creating.CollectingAssertionContainer
import ch.tutteli.atrium.domain.creating.collectors.*
import ch.tutteli.atrium.reporting.translating.Translatable

/**
 * Delegates inter alia to the implementation of [AssertionCollector].
 * In detail, it implements [AssertionCollector] by delegating to [assertionCollector]
 * which in turn delegates to the implementation via [loadSingleService].
 */
object AssertionCollectorBuilder: AssertionCollector {

    override inline fun <T> collect(
        maybeSubject: Option<T>,
        noinline assertionCreator:  CollectingAssertionContainer<T>.() -> Unit
    ): AssertionGroup = assertionCollector.collect(maybeSubject, assertionCreator)



    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <T, A : BaseAssertionPlant<T, A>, C : BaseCollectingAssertionPlant<T, A, C>> collect(
        noinline subjectProvider: () -> T,
        noinline collectingPlantFactory: (() -> T) -> C,
        noinline assertionCreator: C.() -> Unit
    ): AssertionGroup = assertionCollector.collect(subjectProvider, collectingPlantFactory, assertionCreator)

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <T, A : BaseAssertionPlant<T, A>, C : BaseCollectingAssertionPlant<T, A, C>> collectOrExplain(
        safeToCollect: Boolean,
        warningCannotEvaluate: Translatable,
        noinline subjectProvider: () -> T,
        noinline collectingPlantFactory: (() -> T) -> C,
        noinline assertionCreator: C.() -> Unit
    ): AssertionGroup = assertionCollector.collectOrExplain(
        safeToCollect, warningCannotEvaluate, subjectProvider, collectingPlantFactory, assertionCreator
    )

    /**
     * Returns [ExplainingAssertionCollectorOption] providing options to create an assertion collector which collects
     * assertions in the context of explaining assertions.
     * It inter alia delegates to the implementation of [ThrowingAssertionCollectorForExplanation] and
     * [NonThrowingAssertionCollectorForExplanation].
     */
   inline val forExplanation get() = ExplainingAssertionCollectorOption
}

/**
 * Delegates inter alia to the implementation of [ThrowingAssertionCollectorForExplanation] and
 * [NonThrowingAssertionCollectorForExplanation].
 */
object ExplainingAssertionCollectorOption {

    /**
     * Choosing this option causes the [AssertionCollector] to throw an [IllegalArgumentException] in case not a single
     * [Assertion] was collected.
     *
     * Use [doNotThrowIfNoAssertionIsCollected] if such use cases should be ignored (no exception should be thrown).
     */
    inline val throwIfNoAssertionIsCollected get() = throwingAssertionCollectorForExplanation

    /**
     * Choosing this option will ignore use cases where not a single [Assertion] was collected.
     *
     * Use [throwIfNoAssertionIsCollected] if you want that [AssertionCollector] throws an
     * [IllegalArgumentException] in such cases.
     */
    inline val doNotThrowIfNoAssertionIsCollected get() = nonThrowingAssertionCollectorForExplanation
}