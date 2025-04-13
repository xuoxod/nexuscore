package com.gmail.xuoxod.nexuscore.format;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A utility class for formatting text output for better readability.
 * Provides static methods for common tasks like key-value pair alignment,
 * simple table formatting, padding, and indentation.
 *
 * Methods generally return formatted strings, allowing the caller to decide
 * where the output goes (console, file, log, etc.).
 *
 * Margins:
 * - Left margins can be controlled using indentation parameters.
 * - Top/Bottom margins can be added by the caller (e.g., adding "\n").
 */
public final class PrettyPrinter {

    // --- Defaults ---
    private static final int DEFAULT_INDENT_SPACES = 4;
    private static final char DEFAULT_INDENT_CHAR = ' ';
    private static final String DEFAULT_KV_SEPARATOR = ": ";
    private static final int DEFAULT_TABLE_PADDING = 1; // Spaces around cell content
    private static final char DEFAULT_TABLE_BORDER_CHAR = '-';
    private static final char DEFAULT_TABLE_CORNER_CHAR = '+';
    private static final char DEFAULT_TABLE_VERTICAL_CHAR = '|';
    private static final char DEFAULT_PAD_CHAR = ' ';

    // Private constructor to prevent instantiation
    private PrettyPrinter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // --- Padding Methods ---

    /**
     * Pads a string on the right with spaces to reach the specified width.
     * If the string is already wider than or equal to the width, it's returned
     * unchanged.
     * Handles null input string gracefully (treats as empty).
     *
     * @param text  The string to pad.
     * @param width The target width.
     * @return The padded string.
     */

    public static String padRight(String text, int width) {
        return padRight(text, width, DEFAULT_PAD_CHAR);
    }

    public static String padRight(String text, int width, char padChar) {
        // FIX: Handle width <= 0
        if (width <= 0) {
            return "";
        }
        String actualText = (text == null) ? "" : text;
        int padLen = width - actualText.length();
        if (padLen <= 0) {
            return actualText;
        }
        StringBuilder sb = new StringBuilder(actualText);
        for (int i = 0; i < padLen; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    public static String padLeft(String text, int width) {
        return padLeft(text, width, DEFAULT_PAD_CHAR);
    }

    public static String padLeft(String text, int width, char padChar) {
        // FIX: Handle width <= 0
        if (width <= 0) {
            return "";
        }
        String actualText = (text == null) ? "" : text;
        int padLen = width - actualText.length();
        if (padLen <= 0) {
            return actualText;
        }
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < padLen; i++) {
            sb.append(padChar);
        }
        sb.append(actualText);
        return sb.toString();
    }
    // --- Indentation Methods ---

    /**
     * Creates an indentation string using the default indent character (space)
     * and default number of spaces per level (4).
     *
     * @param level The desired indentation level (0 for no indent).
     * @return A string containing the calculated indentation.
     */
    public static String indent(int level) {
        return indent(level, DEFAULT_INDENT_SPACES, DEFAULT_INDENT_CHAR);
    }

    /**
     * Creates an indentation string.
     *
     * @param level         The desired indentation level (0 for no indent).
     * @param charsPerLevel The number of characters for each indentation level.
     * @param indentChar    The character to use for indentation (e.g., ' ' or
     *                      '\t').
     * @return A string containing the calculated indentation. Returns empty string
     *         if level <= 0.
     */
    public static String indent(int level, int charsPerLevel, char indentChar) {
        if (level <= 0 || charsPerLevel <= 0) {
            return "";
        }
        int totalChars = level * charsPerLevel;
        StringBuilder sb = new StringBuilder(totalChars);
        for (int i = 0; i < totalChars; i++) {
            sb.append(indentChar);
        }
        return sb.toString();
    }

    /**
     * Prepends indentation to each line of a given multi-line string.
     * Uses default indentation settings.
     *
     * @param text  The text to indent (can contain newlines).
     * @param level The indentation level.
     * @return The indented text, with each line prefixed by the indent string.
     */
    public static String indentLines(String text, int level) {
        return indentLines(text, level, DEFAULT_INDENT_SPACES, DEFAULT_INDENT_CHAR);
    }

    /**
     * Prepends indentation to each line of a given multi-line string.
     *
     * @param text          The text to indent (can contain newlines).
     * @param level         The indentation level.
     * @param charsPerLevel The number of characters per level.
     * @param indentChar    The character for indentation.
     * @return The indented text, with each line prefixed by the indent string.
     *         Handles null input gracefully.
     */
    public static String indentLines(String text, int level, int charsPerLevel, char indentChar) {
        if (text == null || text.isEmpty() || level <= 0) {
            return (text == null) ? "" : text;
        }
        String indentPrefix = indent(level, charsPerLevel, indentChar);
        if (indentPrefix.isEmpty()) {
            return text;
        }
        // Split by lines, preserving empty lines at the end if any
        String[] lines = text.split("\\R", -1); // \\R matches any Unicode newline sequence
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            result.append(indentPrefix).append(lines[i]);
            if (i < lines.length - 1) {
                result.append(System.lineSeparator()); // Use system-specific line separator
            }
        }
        return result.toString();
    }

    // --- Separator Line Method ---

    /**
     * Creates a separator line of the specified width using the default character
     * '-'.
     *
     * @param width The desired width of the separator line.
     * @return The separator line string. Returns empty string if width <= 0.
     */
    public static String separatorLine(int width) {
        return separatorLine(width, DEFAULT_TABLE_BORDER_CHAR);
    }

    /**
     * Creates a separator line of the specified width using the specified
     * character.
     *
     * @param width    The desired width of the separator line.
     * @param lineChar The character to use for the line.
     * @return The separator line string. Returns empty string if width <= 0.
     */
    public static String separatorLine(int width, char lineChar) {
        if (width <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < width; i++) {
            sb.append(lineChar);
        }
        return sb.toString();
    }

    // --- Key-Value Formatting ---

    /**
     * Formats a Map of key-value pairs into a multi-line string with aligned
     * values.
     * Uses default indentation (0) and separator (": "). Keys and values are
     * converted
     * to strings using their toString() method. Handles null map or values
     * gracefully.
     * Order depends on the Map implementation provided.
     *
     * @param data The map containing key-value pairs. Keys should be strings.
     * @return A formatted string representation of the map, or empty string if map
     *         is null/empty.
     */
    // FIX: Ensure null values are handled consistently if needed (using
    // Objects.toString)
    public static String formatKeyValue(Map<String, ?> data, int indentLevel, String separator) {
        // ... (existing implementation is likely okay, but double-check null handling)
        // ...
        // The existing Objects.toString(entry.getValue(), "null") is correct here.
        // No changes needed in formatKeyValue based on test failures.
        // ... (rest of formatKeyValue) ...
        if (data == null || data.isEmpty()) {
            return "";
        }

        int maxKeyLength = 0;
        for (String key : data.keySet()) {
            // Use "null" string for null keys in length calculation
            maxKeyLength = Math.max(maxKeyLength, Objects.toString(key, "null").length());
        }

        String indentStr = indent(indentLevel);
        String actualSeparator = (separator == null) ? DEFAULT_KV_SEPARATOR : separator;
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, ?> entry : data.entrySet()) {
            String key = Objects.toString(entry.getKey(), "null"); // Handle null keys
            String valueStr = Objects.toString(entry.getValue(), "null"); // Handle null values

            if (result.length() > 0) {
                result.append(System.lineSeparator());
            }
            result.append(indentStr)
                    .append(padRight(key, maxKeyLength)) // Use padded key
                    .append(actualSeparator)
                    .append(valueStr);
        }

        return result.toString();
    }

    // Overload remains the same
    public static String formatKeyValue(Map<String, ?> data) {
        return formatKeyValue(data, 0, DEFAULT_KV_SEPARATOR);
    }

    // --- Simple Table Formatting ---

    public static String formatTable(List<String> headers, List<List<String>> rows) {
        return formatTable(headers, rows, 0, DEFAULT_TABLE_PADDING, true,
                DEFAULT_TABLE_BORDER_CHAR, DEFAULT_TABLE_VERTICAL_CHAR, DEFAULT_TABLE_CORNER_CHAR);
    }

    public static String formatTable(List<String> headers, List<List<String>> rows,
            int indentLevel, int cellPadding, boolean withBorder,
            char borderChar, char verticalChar, char cornerChar) {

        List<String> safeHeaders = (headers == null) ? Collections.emptyList() : headers;
        List<List<String>> safeRows = (rows == null) ? Collections.emptyList() : rows;
        if (safeHeaders.isEmpty() && safeRows.isEmpty()) {
            return "";
        }

        int numColumns = safeHeaders.size();
        if (numColumns == 0 && !safeRows.isEmpty()) {
            for (List<String> row : safeRows) {
                if (row != null && row.size() > numColumns) {
                    numColumns = row.size();
                }
            }
        }
        if (numColumns == 0)
            return "";

        int[] columnWidths = new int[numColumns];
        int padding = Math.max(0, cellPadding);

        // Calculate column widths based on headers
        for (int i = 0; i < safeHeaders.size(); i++) {
            // FIX: Use "null" for width calculation
            columnWidths[i] = Math.max(columnWidths[i], Objects.toString(safeHeaders.get(i), "null").length());
        }

        // Calculate column widths based on rows
        for (List<String> row : safeRows) {
            if (row != null) {
                for (int i = 0; i < Math.min(numColumns, row.size()); i++) {
                    // FIX: Use "null" for width calculation
                    columnWidths[i] = Math.max(columnWidths[i], Objects.toString(row.get(i), "null").length());
                }
            }
        }

        String indentStr = indent(indentLevel);
        StringBuilder table = new StringBuilder();
        String paddingStr = String.join("", Collections.nCopies(padding, " "));

        // --- Create Border Line ---
        String borderLine = null;
        if (withBorder) {
            StringBuilder bl = new StringBuilder();
            bl.append(cornerChar);
            for (int i = 0; i < numColumns; i++) {
                bl.append(separatorLine(columnWidths[i] + padding * 2, borderChar));
                bl.append(cornerChar);
            }
            borderLine = bl.toString();
            table.append(indentStr).append(borderLine).append(System.lineSeparator());
        }

        // --- Format Header ---
        if (!safeHeaders.isEmpty()) {
            table.append(indentStr);
            if (withBorder)
                table.append(verticalChar);
            for (int i = 0; i < numColumns; i++) {
                // FIX: Use "null" for header display if header itself is null
                String header = (i < safeHeaders.size()) ? Objects.toString(safeHeaders.get(i), "null") : "";
                String cellContent = paddingStr + padRight(header, columnWidths[i], ' ') + paddingStr;
                table.append(cellContent);
                if (withBorder)
                    table.append(verticalChar);
                else if (i < numColumns - 1)
                    table.append(" "); // Single space separator for no border
            }
            table.append(System.lineSeparator());
            if (withBorder) {
                table.append(indentStr).append(borderLine).append(System.lineSeparator());
            }
            // ... (optional no-border header separator logic remains commented out) ...
        }

        // --- Format Rows ---
        for (List<String> row : safeRows) {
            table.append(indentStr);
            if (withBorder)
                table.append(verticalChar);
            for (int i = 0; i < numColumns; i++) {
                String cell = "";
                if (row != null && i < row.size()) {
                    // FIX: Use "null" for cell display
                    cell = Objects.toString(row.get(i), "null");
                } else if (row == null) {
                    // Handle case where entire row is null? Treat as empty cells.
                    cell = "null"; // Or "" depending on desired output for completely null rows
                }
                // If row is not null but shorter than numColumns, cell remains "" implicitly
                // handled by Objects.toString below?
                // Let's ensure short rows get padded correctly - the Objects.toString handles
                // the null case if i >= row.size()
                // Re-simplifying the cell fetching:
                String cellValue = (row != null && i < row.size()) ? row.get(i) : null;
                // FIX: Use "null" for cell display consistently
                cell = Objects.toString(cellValue, "null");

                String cellContent = paddingStr + padRight(cell, columnWidths[i], ' ') + paddingStr;
                table.append(cellContent);
                if (withBorder)
                    table.append(verticalChar);
                else if (i < numColumns - 1)
                    table.append(" "); // Single space separator for no border
            }
            table.append(System.lineSeparator());
        }

        // --- Add Bottom Border ---
        // Logic seems okay, but ensure it handles the case where only headers exist
        // correctly
        if (withBorder && !safeRows.isEmpty()) {
            table.append(indentStr).append(borderLine).append(System.lineSeparator());
        } else if (withBorder && safeHeaders.isEmpty() && safeRows.isEmpty()) {
            table.setLength(0); // Correct: remove top border if table totally empty
        }
        // No change needed for header-only case with border (header separator acts as
        // bottom)

        // Remove trailing newline if present
        if (table.length() > 0
                && table.substring(table.length() - System.lineSeparator().length()).equals(System.lineSeparator())) {
            table.setLength(table.length() - System.lineSeparator().length());
        }

        return table.toString();
    }

    // --- Potential Future Additions ---
    // - formatList(List<?> items, boolean numbered, int indentLevel)
    // - Centered padding/alignment
    // - Word wrapping within table cells
    // - More sophisticated border styles
    // - Fluent builder API
    // (PrettyPrinter.builder().withIndent(2).build().formatTable(...))
}
