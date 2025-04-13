package com.gmail.xuoxod.nexuscore.platform;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link PlatformDetector} utility class.
 */
@DisplayName("PlatformDetector Tests")
class PlatformDetectorTest {

    @Test
    @DisplayName("Should return a non-null OS name string")
    void testGetOsName() {
        String osName = PlatformDetector.getOsName();
        System.out.println("Detected OS Name for Test: " + osName); // Log for info
        assertNotNull(osName, "OS name should not be null");
        assertFalse(osName.isEmpty(), "OS name should not be empty");
    }

    @Test
    @DisplayName("Boolean OS checks should return consistent results")
    void testBooleanChecksConsistency() {
        // We can't know which OS this test runs on, but we can check basic consistency.
        // At most one of these primary checks should be true (unless OS is
        // unknown/unusual).
        boolean isWin = PlatformDetector.isWindows();
        boolean isMac = PlatformDetector.isMac();
        boolean isLin = PlatformDetector.isLinux(); // Includes Unix-like
        boolean isSol = PlatformDetector.isSolaris();

        int trueCount = 0;
        if (isWin)
            trueCount++;
        if (isMac)
            trueCount++;
        // Note: Linux check might overlap with Solaris or other Unix on some systems,
        // but typically not with Windows or Mac.
        // A simple check: not both Win and Mac are true.
        assertFalse(isWin && isMac, "Cannot be both Windows and Mac");

        // Just ensure they return a boolean without error
        assertDoesNotThrow(() -> PlatformDetector.isWindows());
        assertDoesNotThrow(() -> PlatformDetector.isMac());
        assertDoesNotThrow(() -> PlatformDetector.isLinux());
        assertDoesNotThrow(() -> PlatformDetector.isSolaris());
    }

    @Test
    @DisplayName("Should return a valid OS Family enum")
    void testGetOsFamily() {
        PlatformDetector.OsFamily family = PlatformDetector.getOsFamily();
        System.out.println("Detected OS Family for Test: " + family); // Log for info
        assertNotNull(family, "OS Family should not be null");

        // Check consistency between boolean checks and the returned family
        switch (family) {
            case WINDOWS:
                assertTrue(PlatformDetector.isWindows(), "Family is WINDOWS, isWindows() should be true");
                break;
            case MAC:
                assertTrue(PlatformDetector.isMac(), "Family is MAC, isMac() should be true");
                // macOS contains "unix" sometimes, so isLinux() might be true, but isMac()
                // takes precedence.
                break;
            case LINUX:
                assertTrue(PlatformDetector.isLinux(), "Family is LINUX, isLinux() should be true");
                assertFalse(PlatformDetector.isWindows(), "Family is LINUX, isWindows() should be false");
                assertFalse(PlatformDetector.isMac(), "Family is LINUX, isMac() should be false");
                break;
            case SOLARIS:
                assertTrue(PlatformDetector.isSolaris(), "Family is SOLARIS, isSolaris() should be true");
                // Solaris might also trigger isLinux() depending on exact os.name, but
                // isSolaris() takes precedence.
                break;
            case UNKNOWN:
                // If unknown, none of the specific checks should ideally be true,
                // but this depends heavily on the actual os.name string.
                System.out.println("OS Family detected as UNKNOWN. OS Name: " + PlatformDetector.getOsName());
                break;
            default:
                fail("Unexpected OS Family value: " + family);
        }
    }

    @Test
    @DisplayName("Utility class constructor should throw exception")
    void testPrivateConstructor() {
        // This test uses reflection to ensure the private constructor prevents
        // instantiation
        assertThrows(UnsupportedOperationException.class, () -> {
            java.lang.reflect.Constructor<PlatformDetector> constructor = PlatformDetector.class
                    .getDeclaredConstructor();
            constructor.setAccessible(true); // Make private constructor accessible
            try {
                constructor.newInstance(); // Try to instantiate
            } catch (java.lang.reflect.InvocationTargetException e) {
                // The actual exception thrown by the constructor is wrapped, so we catch
                // InvocationTargetException
                // and check its cause.
                if (e.getCause() instanceof UnsupportedOperationException) {
                    throw (UnsupportedOperationException) e.getCause(); // Re-throw the expected exception
                } else {
                    fail("Unexpected exception cause: " + e.getCause());
                }
            }
        }, "Instantiating utility class should throw UnsupportedOperationException");
    }
}
