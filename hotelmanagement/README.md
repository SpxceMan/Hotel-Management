# Hotel Management System (JavaFX)

Small demo JavaFX application for a Hotel Management System.

Features
- Primary dashboard with navigation.
- Room management form (add room) with basic validation.
- In-memory storage of rooms (prints added rooms to console).

Project structure
- `src/main/java/com/hotel/`
  - `App.java` — main Application; scene management
  - `PrimaryController.java` — dashboard and navigation
  - `SecondaryController.java` — room form, validation, in-memory storage
- `src/main/resources/com/hotel/`
  - `primary.fxml` — primary layout (VBox)
  - `secondary.fxml` — room form (GridPane)
- `module-info.java` — Java module declaration
- `pom.xml` — Maven configuration (Java 17, JavaFX 17)

Requirements
- Java 17 (JDK 17+)
- Maven 3.6+ (or use your Maven wrapper)
- Internet access to download JavaFX artifacts from Maven Central (pom.xml pins JavaFX 17.0.8)

Run (recommended)

From the project root run:

```bash
mvn clean javafx:run
```

Windows (using Maven wrapper if available):

```powershell
mvnw.cmd clean javafx:run
```

Run from IDE

If your IDE requires an explicit JavaFX SDK on the module path, add VM options similar to:

```
--module-path /path/to/javafx-sdk-17/lib --add-modules=javafx.controls,javafx.fxml
```

On Windows (example):

```
--module-path "C:\path\to\javafx-sdk-17\lib" --add-modules=javafx.controls,javafx.fxml
```

Build jar (advanced)

```bash
mvn clean package
```

If you want to run the packaged JAR you may need to provide the JavaFX SDK on the module path:

```bash
java --module-path /path/to/javafx-sdk-17/lib --add-modules=javafx.controls,javafx.fxml -jar target/hotelmanagement-1.0-SNAPSHOT.jar
```

Notes
- Adding a room prints a line like `Added room: Room[number=101, type=Single, price=50.0]` to the console.
- Validation: all fields required and price must be numeric.
- This is a simple demo; persistence is not implemented.

If you want, I can run a quick `mvn -q -DskipTests=true package` to verify compilation locally. (Requires JDK 17 available in the environment.)
