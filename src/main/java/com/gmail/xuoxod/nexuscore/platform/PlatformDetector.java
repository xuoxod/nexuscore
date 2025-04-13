package com.gmail.xuoxod.nexuscore.platform;

import java.util.Locale;

/**
 * Utility class to detect the underlying operating system.
 * Provides methods to check for common OS families.
 */
public final class PlatformDetector {

    private static final String OS_NAME_PROPERTY = "os.name";
    private static final String OS_NAME = System.getProperty(OS_NAME_PROPERTY);
    private static final String OS_NAME_LOWER = (OS_NAME != null) ? OS_NAME.toLowerCase(Locale.ENGLISH) : "unknown";

    // Private constructor to prevent instantiation of this utility class.
    private PlatformDetector() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Gets the raw OS name string from the system property "os.name".
     *
     * @return The operating system name, or null if the property is not accessible.
     */
    public static String getOsName() {
        return OS_NAME;
    }

    /**
     * Checks if the current operating system is a version of Windows.
     *
     * @return true if the OS name contains "win", false otherwise.
     */
    public static boolean isWindows() {
        return OS_NAME_LOWER.contains("win");
    }

    /**
     * Checks if the current operating system is a version of Linux or other
     * Unix-like OS (excluding macOS).
     * Covers Linux, AIX, etc.
     *
     * @return true if the OS name contains "nix", "nux", or "aix", false otherwise.
     */
    public static boolean isLinux() {
        // Common indicators for Linux/Unix variants
        return OS_NAME_LOWER.contains("nix") || OS_NAME_LOWER.contains("nux") || OS_NAME_LOWER.contains("aix");
    }

    /**
     * Checks if the current operating system is macOS (Mac OS X).
     *
     * @return true if the OS name contains "mac", false otherwise.
     */
    public static boolean isMac() {
        return OS_NAME_LOWER.contains("mac");
    }

    /**
     * Checks if the current operating system is Solaris.
     *
     * @return true if the OS name contains "sunos", false otherwise.
     */
    public static boolean isSolaris() {
        return OS_NAME_LOWER.contains("sunos");
    }

    /**
     * Provides a simple classification of the OS family.
     *
     * @return An enum representing the detected OS family (WINDOWS, LINUX, MAC,
     *         SOLARIS, UNKNOWN).
     */
    public static OsFamily getOsFamily() {
        if (isWindows()) {
            return OsFamily.WINDOWS;
        } else if (isMac()) { // Check Mac before Linux as macOS might contain "unix"
            return OsFamily.MAC;
        } else if (isLinux()) {
            return OsFamily.LINUX;
        } else if (isSolaris()) {
            return OsFamily.SOLARIS;
        } else {
            return OsFamily.UNKNOWN;
        }
    }

    /**
     * Enum representing common operating system families.
     */
    public enum OsFamily {
        WINDOWS,
        MAC,
        LINUX, // Includes generic Unix-like systems identified by isLinux()
        SOLARIS,
        UNKNOWN
    }

    // Optional: Add a simple main method for quick testing during development
    public static void main(String[] args) {
        System.out.println("OS Name: " + getOsName());
        System.out.println("OS Family: " + getOsFamily());
        System.out.println("Is Windows? " + isWindows());
        System.out.println("Is Linux/Unix? " + isLinux());
        System.out.println("Is Mac? " + isMac());
        System.out.println("Is Solaris? " + isSolaris());
    }

}
