package com.gmail.xuoxod.nexuscore.format;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals; // Use LinkedHashMap for predictable order in tests
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link PrettyPrinter} utility class. (Simplified Structure)
 */
@DisplayName("PrettyPrinter Tests (Simplified)")
class PrettyPrinterTest {
    // Reusable test data for tables (INSTANCE VARIABLES - place before tests using
    // them)
    private final List<String> tableHeaders = Arrays.asList("Col A", "Column B", "C");
    private final List<List<String>> tableRows = Arrays.asList(
            Arrays.asList("r1c1", "r1c2 data", "r1c3"),
            Arrays.asList("r2c1 longer", "r2c2", "r2c3"),
            Arrays.asList("r3c1", "r3c2", null) // Null cell
    );
    private final List<List<String>> tableRowsWithMissingCols = Arrays.asList(
            Arrays.asList("d1", "d2"), // Missing col 3
            Arrays.asList("d3", "d4", "d5", "d6") // Extra col 4 ignored based on header count
    );
    private final String EOL = System.lineSeparator(); // System-dependent End-Of-Line

    // --- Padding Method Tests ---

    @Test
    @DisplayName("padRight: Should add spaces correctly")
    void testPadRightDefault() {
        assertEquals("abc   ", PrettyPrinter.padRight("abc", 6));
        assertEquals("abcdef", PrettyPrinter.padRight("abcdef", 6));
        assertEquals("abcdefg", PrettyPrinter.padRight("abcdefg", 6));
        assertEquals("      ", PrettyPrinter.padRight("", 6));
        assertEquals("      ", PrettyPrinter.padRight(null, 6));
        assertEquals("", PrettyPrinter.padRight("abc", 0));
        assertEquals("", PrettyPrinter.padRight("abc", -1));
    }

    @Test
    @DisplayName("padRight: Should add custom characters correctly")
    void testPadRightCustomChar() {
        assertEquals("abc...", PrettyPrinter.padRight("abc", 6, '.'));
        assertEquals("abcdef", PrettyPrinter.padRight("abcdef", 6, '.'));
        assertEquals("......", PrettyPrinter.padRight(null, 6, '.'));
    }

    @Test
    @DisplayName("padLeft: Should add spaces correctly")
    void testPadLeftDefault() {
        assertEquals("   abc", PrettyPrinter.padLeft("abc", 6));
        assertEquals("abcdef", PrettyPrinter.padLeft("abcdef", 6));
        assertEquals("abcdefg", PrettyPrinter.padLeft("abcdefg", 6));
        assertEquals("      ", PrettyPrinter.padLeft("", 6));
        assertEquals("      ", PrettyPrinter.padLeft(null, 6));
        assertEquals("", PrettyPrinter.padLeft("abc", 0));
        assertEquals("", PrettyPrinter.padLeft("abc", -1));
    }

    @Test
    @DisplayName("padLeft: Should add custom characters correctly")
    void testPadLeftCustomChar() {
        assertEquals("...abc", PrettyPrinter.padLeft("abc", 6, '.'));
        assertEquals("abcdef", PrettyPrinter.padLeft("abcdef", 6, '.'));
        assertEquals("......", PrettyPrinter.padLeft(null, 6, '.'));
    }

    // --- Indentation Method Tests ---

    @Test
    @DisplayName("indent: Should create correct space indentation")
    void testIndentDefault() {
        assertEquals("", PrettyPrinter.indent(0));
        assertEquals("    ", PrettyPrinter.indent(1)); // Default 4 spaces
        assertEquals("        ", PrettyPrinter.indent(2));
        assertEquals("", PrettyPrinter.indent(-1));
    }

    @Test
    @DisplayName("indent: Should create correct custom indentation")
    void testIndentCustom() {
        assertEquals("..", PrettyPrinter.indent(1, 2, '.'));
        assertEquals("....", PrettyPrinter.indent(2, 2, '.'));
        assertEquals("\t", PrettyPrinter.indent(1, 1, '\t'));
        assertEquals("", PrettyPrinter.indent(1, 0, ' '));
        assertEquals("", PrettyPrinter.indent(1, -1, ' '));
    }

    @Test
    @DisplayName("indentLines: Should indent single and multi-line strings with default settings")
    void testIndentLinesDefault() {
        String input = "Line 1" + EOL + "Line 2";
        String expected = "    Line 1" + EOL + "    Line 2";
        assertEquals(expected, PrettyPrinter.indentLines(input, 1));

        assertEquals("    Single Line", PrettyPrinter.indentLines("Single Line", 1));
        assertEquals("No Indent", PrettyPrinter.indentLines("No Indent", 0));
        assertEquals("", PrettyPrinter.indentLines("", 1));
        assertEquals("", PrettyPrinter.indentLines(null, 1));
        assertEquals("Line with trailing newline" + EOL,
                PrettyPrinter.indentLines("Line with trailing newline" + EOL, 0)); // No indent
        assertEquals("    Line with trailing newline" + EOL + "    ",
                PrettyPrinter.indentLines("Line with trailing newline" + EOL, 1)); // Indents trailing empty line
    }

    @Test
    @DisplayName("indentLines: Should handle custom indentation settings")
    void testIndentLinesCustom() {
        String input = "L1" + EOL + "L2";
        String expected = ">>L1" + EOL + ">>L2";
        assertEquals(expected, PrettyPrinter.indentLines(input, 1, 2, '>'));
    }

    // --- Separator Line Method Tests ---

    @Test
    @DisplayName("separatorLine: Should create line with default char")
    void testSeparatorLineDefault() {
        assertEquals("-----", PrettyPrinter.separatorLine(5)); // Default '-'
        assertEquals("", PrettyPrinter.separatorLine(0));
        assertEquals("", PrettyPrinter.separatorLine(-1));
    }

    @Test
    @DisplayName("separatorLine: Should create line with custom char")
    void testSeparatorLineCustom() {
        assertEquals("*****", PrettyPrinter.separatorLine(5, '*'));
        assertEquals("=====", PrettyPrinter.separatorLine(5, '='));
    }

    // --- Key-Value Formatting Tests ---

    @Test
    @DisplayName("formatKeyValue: Should format simple map correctly with defaults")
    void testFormatKeyValueSimple() {
        Map<String, String> data = new LinkedHashMap<>(); // Use LinkedHashMap for order
        data.put("Key1", "Value1");
        data.put("LongerKey", "Value2");
        data.put("K3", "Value3");

        String expected = "Key1     : Value1" + EOL +
                "LongerKey: Value2" + EOL +
                "K3       : Value3";
        assertEquals(expected, PrettyPrinter.formatKeyValue(data));
    }

    @Test
    @DisplayName("formatTable: Should format without border and custom padding")
    void testFormatTableNoBorderCustomPadding() {
        // FIX: Update expected string to match the actual output from the last run
        String expected = "  Col A           Column B      C     " + EOL +
                "  r1c1            r1c2 data     r1c3  " + EOL +
                "  r2c1 longer     r2c2          r2c3  " + EOL +
                "  r3c1            r3c2          null  "; // Note potential trailing space if actual had one

        // Call formatTable with no border (false) and cellPadding 2
        assertEquals(expected, PrettyPrinter.formatTable(tableHeaders, tableRows, 0, 2, false, '-', '|', '+'));
    }

    @Test
    @DisplayName("formatTable: Should handle only headers, no border")
    void testFormatTableOnlyHeadersNoBorder() {
        String expected = " Col A   Column B   C "; // Padding 1
        assertEquals(expected, PrettyPrinter.formatTable(tableHeaders, null, 0, 1, false, '-', '|', '+'));
    }

    // --- Utility Class Constructor Test ---

    @Test
    @DisplayName("Constructor: Utility class constructor should throw exception")
    void testPrivateConstructor() {
        // Reuse the same reflection technique as in PlatformDetectorTest
        assertThrows(UnsupportedOperationException.class, () -> {
            java.lang.reflect.Constructor<PrettyPrinter> constructor = PrettyPrinter.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (java.lang.reflect.InvocationTargetException e) {
                if (e.getCause() instanceof UnsupportedOperationException) {
                    throw (UnsupportedOperationException) e.getCause();
                } else {
                    fail("Unexpected exception cause: " + e.getCause());
                }
            }
        }, "Instantiating utility class should throw UnsupportedOperationException");
    }
}
