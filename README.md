<div align="center">

# 🌌 NexusCore 🛠️

**Your Central Hub for Robust & Reusable Java Utilities**

</div>

---

[![Java Version](https://img.shields.io/badge/Java-23%2B-%23ED8B00?logo=openjdk)](https://openjdk.java.net/)
[![Build Tool](https://img.shields.io/badge/Build-Maven-%23C71A36?logo=apache-maven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
<!-- Add other badges as needed -->
<!-- [![Build Status](https://github.com/xuoxod/NexusCore/actions/workflows/maven.yml/badge.svg)](https://github.com/xuoxod/NexusCore/actions/workflows/maven.yml) -->
<!-- [![Maven Central](https://img.shields.io/maven-central/v/com.gmail.xuoxod.nexuscore/NexusCore.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.gmail.xuoxod.nexuscore%20AND%20a:NexusCore) -->

---

## 📖 Description

**NexusCore** is a foundational Java library providing a collection of meticulously crafted, reusable utility classes. It's designed to accelerate development by offering reliable solutions for common programming tasks, ensuring consistency and reducing boilerplate code across your projects.

Think of it as a developer's multi-tool 🔧 – whether you need to identify the operating system, format console output elegantly, or handle file operations cleanly, NexusCore aims to provide a straightforward and efficient API. Built using modern Java features (like `java.nio.file`) and thoroughly tested, it serves as a dependable core for building sophisticated applications.

---

## ✨ Core Features

NexusCore currently offers the following utility modules:

*   ### 💻 **`platform.PlatformDetector`**
    *   Detects the underlying Operating System (Windows, macOS, Linux, Solaris, Unknown).
    *   Provides simple boolean checks (`isWindows()`, `isMac()`, etc.).
    *   Returns an `OsFamily` enum for easy switching.
    *   Retrieves the raw `os.name` system property.

*   ### 🎨 **`format.PrettyPrinter`**
    *   Formats data for clean, readable console output.
    *   Aligns key-value pairs automatically.
    *   Generates simple text-based tables with borders and padding.
    *   Provides string padding (left/right) and indentation utilities.
    *   Configurable separators, padding characters, and indentation levels.

*   ### 📁 **`io.FileUtils`**
    *   Simplifies file input/output using `java.nio.file`.
    *   Reads/Writes entire files as `String` (with charset control, defaults to UTF-8).
    *   Reads/Writes entire files as `byte[]`.
    *   Reads files line-by-line into a `List<String>`.
    *   Supports appending to existing files.
    *   Automatically creates parent directories when writing.
    *   Includes helpers like `exists()`, `createDirectories()`, `delete()`.

---

## 🚀 Getting Started

### Prerequisites

To build and use NexusCore, you'll need:

*   <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original-wordmark.svg" alt="Java" width="20" height="20"/> **Java Development Kit (JDK):** Version **23** or later (as specified in `pom.xml`).
*   <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/apache/apache-original-wordmark.svg" alt="Maven" width="20" height="20"/> **Apache Maven:** Version 3.6.x or later recommended.

### Installation & Building

1.  **Clone the Repository:**
    Open your terminal or command prompt:
    ```bash
    # Using HTTPS (Recommended for public read-only access)
    git clone https://github.com/xuoxod/NexusCore.git

    # Or using SSH (If you have SSH keys set up with GitHub)
    # git clone git@github.com:xuoxod/NexusCore.git
    ```
    *(Remember to replace the URL if your repository location is different)*

2.  **Navigate into the Project Directory:**
    ```bash
    cd NexusCore
    ```

3.  **Build with Maven:**
    This command compiles the code, runs all unit tests, and packages the library into a JAR file.
    ```bash
    mvn clean install
    ```
    *   On success, you'll find the library JAR (e.g., `NexusCore-1.0-SNAPSHOT.jar`) inside the `target/` directory.
    *   This also installs the library into your local Maven repository (`~/.m2/repository`), making it available for other local projects.

---

## 💡 Usage

### Adding NexusCore as a Dependency

To use NexusCore in your own Maven project, add the following dependency block to your `pom.xml` file within the `<dependencies>` section:

```xml
<dependency>
    <groupId>com.gmail.xuoxod.nexuscore</groupId>
    <artifactId>NexusCore</artifactId>
    <!-- Use the appropriate version -->
    <version>1.0-SNAPSHOT</version>
</dependency>
# nexuscore
