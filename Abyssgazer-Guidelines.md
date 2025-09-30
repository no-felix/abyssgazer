# Abyssgazer Entwickler-Guidelines

Diese Richtlinien definieren unseren gemeinsamen Qualitätsstandard für Code, Git-Workflow und Projektzusammenarbeit.

## Kotlin & Plattform

- **Sprache:** Wir entwickeln in Kotlin auf **JDK 21** und nutzen moderne Sprachfeatures (z.B. Records, Pattern Matching on `when`) nur, wenn sie mit der Ziel-JVM und der Fabric-Toolchain kompatibel sind.
- **Kotlin Coding Conventions:** Wir orientieren uns am offiziellen [Kotlin Coding Style](https://kotlinlang.org/docs/coding-conventions.html). Dazu gehören klare `package`-Strukturen, `camelCase` für Funktionen/Variablen und `PascalCase` für Klassen/Objects.
- **Null-Sicherheit:** Wir bevorzugen `val`, setzen Null-Sicherheit (`?.`, `?:`, `requireNotNull`) konsequent ein und kapseln unsichere Java/Fabric-Calls.
- **Fabric-/Minecraft-spezifisch:** Wir beachten Yarn-Mappings, verwenden registrierte IDs (`Identifier`) konsistent und trennen Client- von Server-spezifischer Logik. Mixins setzen wir nur ein, wenn kein offizieller Hook verfügbar ist, und dokumentieren sie mit aussagekräftigem Javadoc/KDoc.

## Code-Qualität & Tests

- **Projektstruktur:** Wir strukturieren Komponenten nach Domänen (`dimension`, `mobs`, `level`, `items`, `boss`) und lagern gemeinsame Utilities in klar abgegrenzte Pakete aus.
- **Dokumentation:** Wir beschreiben öffentliche APIs mit KDoc und dokumentieren komplexe Mixin- oder Registry-Logik stets nachvollziehbar.
- **Gameplay-Tests:** Wir verzichten auf Headless- oder Server-only-Tests, da Minecraft dort häufig zickt, und prüfen stattdessen relevante Features über `./gradlew runClient` per Gameplay-Session.

## Git-Workflow

- **Branches:** Wir arbeiten auf thematisch benannten Feature-Branches und nutzen Präfixe wie `dimension/feature/...`, `items/feature/...` usw.
- **Lineare History:** Wir konfigurieren `git pull` standardmäßig auf **Rebase**, sodass Merge-Commits aus Feature-Branches den Hauptverlauf nicht aufblähen.

  ```ini
  [pull]
      rebase = true
  ```

- **Commit Signing:** Wir signieren alle Commits via SSH-Schlüssel und verwenden dazu folgende Git-Konfiguration:

  ```ini
  [commit]
      gpgsign = true
  [gpg]
      format = ssh
  [user]
    signingKey = ~/.ssh/id_ed25519.pub   # Pfad zu unserem Public-Key eintragen
  ```

- **Rebasing & Force-Push:** Wir rebasen Feature-Branches regelmäßig auf die Zielbasis (z.B. `develop`) und aktualisieren die Remote-History anschließend mit `--force-with-lease`.

## Commit-Nachrichten

- **Format:** Wir schreiben Commits im Stil `:gitmoji: <Conventional-Summary>`: ein passender Gitmoji gefolgt von einer kurzen Aussage im Imperativ oder als „Add/Implement…“. Optional betten wir den Conventional-Typ (`feat`, `fix`, `chore`, …) ein.
- **Beispiele aus dem Repo:**
  - `🧑‍💻 Add dungeon testing logic in dev env`
  - `✨ Implement dungeon layout generation and connection logic`
  - `🏗️ Add initial classes for dimension`
  - `🔀 Fix Gradle wrapper permissions and improve CI setup (#3)`
  - `👷 Add cache gradle step to build workflow`
- **Scope & Body:** Wir ergänzen bei Bedarf einen Scope (`dimension`, `items`, …) und fügen bei komplexen Änderungen einen erläuternden Body mit Listen oder Issue-Verweisen hinzu.

## Pull Requests

- **Basis:** Wir eröffnen PRs standardmäßig gegen `develop` und rebasen den Feature-Branch vorher.
- **Inhalt:** Wir liefern eine kurze Zusammenfassung, gegebenenfalls einen Screenshot/Video bei UI- oder Gameplay-Änderungen sowie einen Link zum Issue oder MVP-Feature.
- **Checks:** Wir stellen sicher, dass die CI-Ausführung (`./gradlew build`) grün ist und keine _ungeprüften_ TODOs verbleiben.
- **Reviews:** Wir holen mindestens ein Review ein, adressieren Kommentare zeitnah und binden Reviewer bei großen Änderungen früh ein.
- **Merge-Check:** Bevor wir nach einem Approval mergen, prüfen wir ein letztes Mal, ob der Branch sauber auf der Zielbasis rebased ist, damit die History linear bleibt.

## Tooling & Infrastruktur

- **IDE:** Wir nutzen IntelliJ IDEA Ultimate mit Kotlin- und Minecraft-Development-Plugin.
- **Build:** Wir bauen ausschließlich mit dem mitgelieferten Gradle Wrapper (`./gradlew`) und committen keine lokalen Gradle-Installationen.
- **Secrets & Konfiguration:** Wir speichern keine Access-Tokens oder privaten Keys im Repository; `.env`-Dateien oder lokale Gradle-Props bleiben in `.gitignore`.

---

Wir halten diese Richtlinien lebendig: Sobald sich Best Practices ändern oder ein neuer Workflow entsteht, aktualisieren wir dieses Dokument und weisen in der PR-Beschreibung darauf hin.
