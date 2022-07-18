package com.herman.markdown_dsl.elements

import com.herman.markdown_dsl.ElementContainerBuilder
import com.herman.markdown_dsl.MarkdownElement

/**
 * ## ATX Header Size Marker
 *
 * Used for size configuration of [Heading] elements
 *
 * Markdown output will look like:
 *
 * # [H1]
 * ## [H2]
 * ### [H3]
 * #### [H4]
 * ##### [H5]
 * ###### [H6]
 */
enum class HeadingSizeMarker(
    internal val tag: String
) {
    H1("#"), H2("##"), H3("###"), H4("####"), H5("#####"), H6("######")
}

/**
 * ## [ATX-Stiled Heading](https://daringfireball.net/projects/markdown/syntax#header)
 *
 * To ensure correctness and compatibility, every heading will be seperated from previous content by a new line.
 * Note that Markdown headings don't support multiline texts so actual content will be turned into a single line.
 *
 * <br></br>
 *
 * ### Usage:
 *
 * ```
 * markdown {
 *     heading("Heading")
 * }
 * ```
 * That will produce:
 *
 * # Heading
 * _Note the blank line before the actual heading_
 *
 * By default, heading will be created with as H1 heading.
 * If you need a heading with a specific size, set a  heading [size] to one of the values specified in [HeadingSizeMarker]:
 * ```
 * markdown {
 *     heading(HeadingSize.H2)("Heading")
 * }
 * ```
 * This will produce:
 *
 * ## Heading
 *
 *
 * @param text Content for the heading
 * @param size Custom size style for this heading, see [HeadingSizeMarker]
 */
internal class Heading(
    private val text: String,
    private val size: HeadingSizeMarker
) : MarkdownElement() {

    override fun toMarkdown(): String = buildString {
        // For compatibility separate heading from previous content with a new line
        appendLine()
        // Append heading tags
        append(size.tag)
        // For compatibility separate heading tag from heading content
        append(" ")
        // Append sanitised content
        append(sanitiseContent(text))
    }
}

/** ## SetextHeader Style Marker
 *
 * Used for size configuration of [UnderlinedHeading] elements
 *
 * Markdown output will look like:
 *
 * ```
 * H1
 * ==
 * ```
 *
 * ```
 * H2
 * --
 * ```
 */
enum class UnderlinedHeadingStyle(
    internal val tag: String
) {
    H1("="),
    H2("-")
}

/**
 * ## [Setext-Stiled Heading](https://daringfireball.net/projects/markdown/syntax#header)
 *
 * To ensure correctness and compatibility, every heading will be seperated from previous content by a new line.
 * Note that Markdown headings don't support multiline texts so actual content will be turned into a single line.
 *
 * <br></br>
 *
 * ### Usage:
 *
 * ```
 * markdown {
 *     underlinedHeading("Heading")
 * }
 * ```
 * That will produce:
 *
 * Heading
 * =======
 * _Note the blank line before the actual heading_
 *
 * By default, heading will be created with as H1 heading.
 * If you need a heading of a specific type, set a  heading [size]
 * to one of the values specified in [UnderlinedHeadingStyle]:
 * ```
 * markdown {
 *     underlinedHeading(HeadingSize.H2)("Heading")
 * }
 * ```
 * This will produce:
 *
 * Heading
 * -------
 *
 *
 * @param text Content for the heading
 * @param size Custom size style for this heading, see [UnderlinedHeadingStyle]
 */
internal class UnderlinedHeading(
    private val text: String,
    private val size: UnderlinedHeadingStyle
) : MarkdownElement() {

    override fun toMarkdown(): String = buildString {
        // For compatibility separate heading from previous content with a new line
        appendLine()
        // Append sanitised content
        val sanitisedContent = sanitiseContent(text)
        append(sanitisedContent)
        // Separate content from tag
        appendLine()
        // Underline header
        // 2 new line and 1x space at the end of content
        repeat(length - 2) {
            append(size.tag)
        }
    }
}

private fun sanitiseContent(
    content: String
): String = buildString {
    content.lineSequence().forEach { content ->
        if (content.isNotBlank()) {
            append(content)
            append(" ")
        }
    }
}.trim()

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class HeadingBuilderMarker

@HeadingBuilderMarker
interface HeadingContainerBuilder : ElementContainerBuilder {
    fun heading(
        content: String,
        style: HeadingSizeMarker = HeadingSizeMarker.H1
    ) {
        addToContainer(Heading(content, style))
    }

    fun underlinedHeading(
        content: String,
        style: UnderlinedHeadingStyle = UnderlinedHeadingStyle.H1
    ) {
        addToContainer(UnderlinedHeading(content, style))
    }
}

inline fun HeadingContainerBuilder.heading(
    style: HeadingSizeMarker = HeadingSizeMarker.H1,
    text: () -> String
) {
    heading(text(), style)
}

inline fun HeadingContainerBuilder.underlinedHeading(
    style: UnderlinedHeadingStyle = UnderlinedHeadingStyle.H1,
    text: () -> String
) {
    underlinedHeading(text(), style)
}

