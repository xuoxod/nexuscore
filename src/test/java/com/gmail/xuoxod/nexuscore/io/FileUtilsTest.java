package com.gmail.xuoxod.nexuscore.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets; // Import TempDir
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow; // Use Path from java.nio.file
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for the {@link FileUtils} utility class.
 * Uses JUnit 5's @TempDir for creating temporary files and directories.
 */
@DisplayName("FileUtils Tests")
class FileUtilsTest {

    // JUnit 5 will inject a temporary directory path before each test
    @TempDir
    Path tempDir;

    private final String EOL = System.lineSeparator();

    // --- Read Tests ---

    @Test
    @DisplayName("readFileToString: Should read file content correctly (UTF-8)")
    void testReadFileToStringDefault() throws IOException {
        Path testFile = tempDir.resolve("testReadString.txt");
        String expectedContent = "Hello World!" + EOL + "Line 2";
        Files.write(testFile, expectedContent.getBytes(StandardCharsets.UTF_8));

        String actualContent = FileUtils.readFileToString(testFile.toString());
        assertEquals(expectedContent, actualContent);
    }

    @Test
    @DisplayName("readFileToString: Should read file content correctly (UTF-16)")
    void testReadFileToStringCustomCharset() throws IOException {
        Path testFile = tempDir.resolve("testReadString_UTF16.txt");
        String expectedContent = "你好世界"; // Example non-ASCII content
        Charset charset = StandardCharsets.UTF_16;
        Files.write(testFile, expectedContent.getBytes(charset));

        String actualContent = FileUtils.readFileToString(testFile.toString(), charset);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    @DisplayName("readFileToString: Should throw IOException for non-existent file")
    void testReadFileToStringNonExistent() {
        Path nonExistentFile = tempDir.resolve("nonexistent.txt");
        assertThrows(IOException.class, () -> {
            FileUtils.readFileToString(nonExistentFile.toString());
        });
    }

    @Test
    @DisplayName("readLines: Should read lines correctly (UTF-8)")
    void testReadLinesDefault() throws IOException {
        Path testFile = tempDir.resolve("testReadLines.txt");
        List<String> expectedLines = Arrays.asList("Line 1", "Line 2", "Line 3");
        Files.write(testFile, expectedLines, StandardCharsets.UTF_8);

        List<String> actualLines = FileUtils.readLines(testFile.toString());
        assertEquals(expectedLines, actualLines);
    }

    @Test
    @DisplayName("readLines: Should read lines correctly (ISO-8859-1)")
    void testReadLinesCustomCharset() throws IOException {
        Path testFile = tempDir.resolve("testReadLines_ISO.txt");
        List<String> expectedLines = Arrays.asList("Línea 1", "Línea 2"); // Example with accents
        Charset charset = StandardCharsets.ISO_8859_1;
        Files.write(testFile, expectedLines, charset);

        List<String> actualLines = FileUtils.readLines(testFile.toString(), charset);
        assertEquals(expectedLines, actualLines);
    }

    @Test
    @DisplayName("readFileToBytes: Should read file content correctly")
    void testReadFileToBytes() throws IOException {
        Path testFile = tempDir.resolve("testReadBytes.bin");
        byte[] expectedBytes = new byte[] { 0x01, 0x02, (byte) 0xFF, 0x00, 0x4A };
        Files.write(testFile, expectedBytes);

        byte[] actualBytes = FileUtils.readFileToBytes(testFile.toString());
        assertArrayEquals(expectedBytes, actualBytes);
    }

    // --- Write Tests ---

    @Test
    @DisplayName("writeFileFromString: Should write string correctly (UTF-8, Overwrite)")
    void testWriteFileFromStringDefault() throws IOException {
        Path testFile = tempDir.resolve("testWriteString.txt");
        String content = "Writing this content." + EOL + "Second line.";

        FileUtils.writeFileFromString(testFile.toString(), content);

        String readContent = new String(Files.readAllBytes(testFile), StandardCharsets.UTF_8);
        assertEquals(content, readContent);

        // Test overwrite
        String newContent = "Overwritten.";
        FileUtils.writeFileFromString(testFile.toString(), newContent);
        readContent = new String(Files.readAllBytes(testFile), StandardCharsets.UTF_8);
        assertEquals(newContent, readContent);
    }

    @Test
    @DisplayName("writeFileFromString: Should write string correctly (UTF-16, Append)")
    void testWriteFileFromStringCustomCharsetAppend() throws IOException {
        Path testFile = tempDir.resolve("testWriteString_UTF16_Append.txt");
        String content1 = "你好";
        String content2 = "世界";
        Charset charset = StandardCharsets.UTF_16;

        // Write first part
        FileUtils.writeFileFromString(testFile.toString(), content1, charset, false); // Overwrite initially
        String readContent1 = new String(Files.readAllBytes(testFile), charset);
        assertEquals(content1, readContent1);

        // Append second part
        FileUtils.writeFileFromString(testFile.toString(), content2, charset, true); // Append
        String readContentCombined = new String(Files.readAllBytes(testFile), charset);
        // FIX: Expect the BOM character (\uFEFF) from the second write's BOM
        assertEquals(content1 + "\uFEFF" + content2, readContentCombined);

    }

    @Test
    @DisplayName("writeFileFromBytes: Should write bytes correctly (Overwrite)")
    void testWriteFileFromBytesDefault() throws IOException {
        Path testFile = tempDir.resolve("testWriteBytes.bin");
        byte[] data = new byte[] { 0x10, 0x20, 0x30 };

        FileUtils.writeFileFromBytes(testFile.toString(), data);

        byte[] readData = Files.readAllBytes(testFile);
        assertArrayEquals(data, readData);

        // Test overwrite
        byte[] newData = new byte[] { 0x40, 0x50 };
        FileUtils.writeFileFromBytes(testFile.toString(), newData);
        readData = Files.readAllBytes(testFile);
        assertArrayEquals(newData, readData);
    }

    @Test
    @DisplayName("writeFileFromBytes: Should write bytes correctly (Append)")
    void testWriteFileFromBytesAppend() throws IOException {
        Path testFile = tempDir.resolve("testWriteBytesAppend.bin");
        byte[] data1 = new byte[] { 0x01, 0x02 };
        byte[] data2 = new byte[] { 0x03, 0x04 };
        byte[] expectedCombined = new byte[] { 0x01, 0x02, 0x03, 0x04 };

        // Write first part
        FileUtils.writeFileFromBytes(testFile.toString(), data1, false); // Overwrite
        byte[] readData1 = Files.readAllBytes(testFile);
        assertArrayEquals(data1, readData1);

        // Append second part
        FileUtils.writeFileFromBytes(testFile.toString(), data2, true); // Append
        byte[] readDataCombined = Files.readAllBytes(testFile);
        assertArrayEquals(expectedCombined, readDataCombined);
    }

    @Test
    @DisplayName("writeFile: Should create parent directories if they do not exist")
    void testWriteFileCreatesDirs() throws IOException {
        Path deepFile = tempDir.resolve("subdir1/subdir2/deepFile.txt");
        String content = "Deep content";

        // Ensure parent dirs don't exist initially
        assertFalse(Files.exists(deepFile.getParent()));

        FileUtils.writeFileFromString(deepFile.toString(), content);

        // Verify file and content
        assertTrue(Files.exists(deepFile));
        String readContent = new String(Files.readAllBytes(deepFile), StandardCharsets.UTF_8);
        assertEquals(content, readContent);
        // Verify parent dirs were created
        assertTrue(Files.isDirectory(deepFile.getParent()));
        assertTrue(Files.isDirectory(deepFile.getParent().getParent()));
    }

    // --- Helper Method Tests ---

    @Test
    @DisplayName("exists: Should return true for existing file/dir, false otherwise")
    void testExists() throws IOException {
        Path existingFile = tempDir.resolve("existing.txt");
        Files.createFile(existingFile);
        Path existingDir = tempDir.resolve("existingDir");
        Files.createDirectory(existingDir);
        Path nonExistent = tempDir.resolve("nonexistent");

        assertTrue(FileUtils.exists(existingFile.toString()));
        assertTrue(FileUtils.exists(existingDir.toString()));
        assertFalse(FileUtils.exists(nonExistent.toString()));
    }

    @Test
    @DisplayName("createDirectories: Should create single and multiple directories")
    void testCreateDirectories() throws IOException {
        Path singleDir = tempDir.resolve("newDir");
        Path multiDir = tempDir.resolve("parent/child");

        assertFalse(Files.exists(singleDir));
        assertFalse(Files.exists(multiDir.getParent()));
        assertFalse(Files.exists(multiDir));

        FileUtils.createDirectories(singleDir.toString());
        FileUtils.createDirectories(multiDir.toString());

        assertTrue(Files.isDirectory(singleDir));
        assertTrue(Files.isDirectory(multiDir.getParent()));
        assertTrue(Files.isDirectory(multiDir));

        // Test creating existing directory (should not throw error)
        assertDoesNotThrow(() -> FileUtils.createDirectories(singleDir.toString()));
    }

    @Test
    @DisplayName("delete: Should delete existing file, return true")
    void testDeleteExistingFile() throws IOException {
        Path fileToDelete = tempDir.resolve("deleteMe.txt");
        Files.createFile(fileToDelete);
        assertTrue(Files.exists(fileToDelete));

        boolean result = FileUtils.delete(fileToDelete.toString());

        assertTrue(result);
        assertFalse(Files.exists(fileToDelete));
    }

    @Test
    @DisplayName("delete: Should return false for non-existent file")
    void testDeleteNonExistentFile() throws IOException {
        Path nonExistentFile = tempDir.resolve("alreadyGone.txt");
        assertFalse(Files.exists(nonExistentFile));

        boolean result = FileUtils.delete(nonExistentFile.toString());

        assertFalse(result);
    }

    @Test
    @DisplayName("delete: Should throw IOException for non-empty directory")
    void testDeleteNonEmptyDirectory() throws IOException {
        Path dirToDelete = tempDir.resolve("deleteDir");
        Path fileInside = dirToDelete.resolve("somefile.txt");
        Files.createDirectory(dirToDelete);
        Files.createFile(fileInside);

        assertTrue(Files.exists(dirToDelete));
        assertTrue(Files.exists(fileInside));

        // FileUtils.delete uses Files.deleteIfExists which throws
        // DirectoryNotEmptyException
        assertThrows(IOException.class, () -> {
            FileUtils.delete(dirToDelete.toString());
        });

        // Clean up manually for test isolation if needed, though @TempDir handles it
        Files.delete(fileInside);
        Files.delete(dirToDelete);
    }

    @Test
    @DisplayName("delete: Should delete empty directory, return true")
    void testDeleteEmptyDirectory() throws IOException {
        Path dirToDelete = tempDir.resolve("emptyDeleteDir");
        Files.createDirectory(dirToDelete);
        assertTrue(Files.exists(dirToDelete));

        boolean result = FileUtils.delete(dirToDelete.toString());

        assertTrue(result);
        assertFalse(Files.exists(dirToDelete));
    }

    // --- Utility Class Constructor Test ---

    @Test
    @DisplayName("Constructor: Utility class constructor should throw exception")
    void testPrivateConstructor() {
        assertThrows(UnsupportedOperationException.class, () -> {
            java.lang.reflect.Constructor<FileUtils> constructor = FileUtils.class.getDeclaredConstructor();
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
