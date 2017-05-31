package ch.tutteli.atrium.reporting

import ch.tutteli.atrium.AtriumFactory
import ch.tutteli.atrium.reporting.translating.ITranslatable
import ch.tutteli.atrium.reporting.translating.ITranslationProvider
import ch.tutteli.atrium.reporting.translating.ITranslator
import ch.tutteli.atrium.reporting.translating.UsingDefaultTranslator
import java.util.*

/**
 * A builder to create an [IReporter] consisting of an [IObjectFormatter] which is used by an
 * [IAssertionFormatter] which in turn is used by the created [IReporter].
 */
class ReporterBuilder(private val assertionFormatter: IAssertionFormatter) {

    /**
     * Uses [AtriumFactory.newOnlyFailureReporter] as [IReporter].
     */
    fun buildOnlyFailureReporting(): IReporter
        = AtriumFactory.newOnlyFailureReporter(assertionFormatter)

    companion object {

        /**
         * Uses [UsingDefaultTranslator] which does not translate
         * but uses the [ITranslatable]'s [getDefault][ITranslatable.getDefault].
         */
        fun withoutTranslations(): ObjectFormatterBuilder
            = ObjectFormatterBuilder(UsingDefaultTranslator)

        /**
         * Uses [AtriumFactory.newTranslator] with the given [translationProvider] and uses [locale] as primary
         * [Locale] and the optional [fallbackLocales] as fallback [Locale]s.
         */
        fun withTranslations(translationProvider: ITranslationProvider, locale: Locale, vararg fallbackLocales: Locale)
            = ObjectFormatterBuilder(AtriumFactory.newTranslator(translationProvider, locale, *fallbackLocales))

        /**
         * Uses [UsingDefaultTranslator] as [ITranslator]
         * and [AtriumFactory.newDetailedObjectFormatter] as [IObjectFormatter].
         */
        fun withDetailedObjectFormatter(): AssertionFormatterBuilder
            = withoutTranslations().withDetailedObjectFormatter()
    }

    class ObjectFormatterBuilder(private val translator: ITranslator) {
        /**
         * Uses [AtriumFactory.newDetailedObjectFormatter] as [IObjectFormatter].
         */
        fun withDetailedObjectFormatter(): AssertionFormatterBuilder
            = AssertionFormatterBuilder(AtriumFactory.newDetailedObjectFormatter(translator), translator)
    }

    class AssertionFormatterBuilder(private val objectFormatter: IObjectFormatter, private val translator: ITranslator) {
        /**
         * Uses [AtriumFactory.newSameLineAssertionFormatter] as [IAssertionFormatter].
         */
        fun withSameLineAssertionMessageFormatter(): ReporterBuilder
            = ReporterBuilder(AtriumFactory.newSameLineAssertionFormatter(objectFormatter, translator))
    }
}
