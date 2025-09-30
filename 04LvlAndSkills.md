# 📈 MVP Punkt 4: Level-System mit Skills
**Lead:** DenZone

## 🔹 Level-Logik
_Level-Logik wird über den RPG Bereich implementiert. Hier wird lediglich das Interface mit den Player Daten implementiert._

## 🔹 Skill-Tree-Struktur
_Der Skill-Baum ist als verzweigtes Modell aufgebaut.
Jeder Knoten repräsentiert einen Skill, der durch das Erfüllen von Voraussetzungen
(z.B. Attribute oder anderen Skills) freigeschaltet werden kann.
Beispiel: Ein Magier kann sich zwischen Feuermagie und Wassermagie entscheiden, wobei beide Pfade eigene Skills und Upgrades bieten._

## 🔹 Learning-by-Doing
_Skills werden durch wiederholte Nutzung verbessert.
Umso höher das Skill-level, desto mehr Schaden/Heilung wird verursacht, aber das aufleveln des Skills mit Lerning-by-Doing dauert immer länger.
Z.b. Skill Stufe 1 auf 2: 10x Nutzung, Stufe 2 auf 3: 20x Nutzung, Stufe 3 auf 4: 40x Nutzung, usw._


## 🔹 Erste Fähigkeiten/Skillsets pro Rolle
-   **Tank:**
    -   **Aktive Fähigkeiten:**
        -   `Shield Block`: Verringert eingehenden Schaden für kurze Zeit.
        -   `Melee Taunt`: Zieht die Aufmerksamkeit eines Gegners auf sich mittels Nahkampf-Hit.
        -   `Ranged Taunt`: Zieht die Aufmerksamkeit eines Gegners auf sich mittels Fernkampf-Hit.
        -   `Destructive Clench`: Stunnt einen Gegner für kurze Zeit.
    -   **Passive Fähigkeiten:**
        -   `Increased Life`: Erhöht die maximalen Lebenspunkte.
        -   `Resilience`: Erholt sich schneller von negativen Zuständen.
-   **DPS:**
    -   **Nahkampf:**
        -   **Passive Fähigkeiten:**
            -   `Killer Instinct`: Erhöht den kritischen Trefferwert.
            -   `Relentless Assault`: Erhöht die Angriffsgeschwindigkeit.
        -   **Dolche:**
            -   `Backstab`: Verursacht erhöhten Schaden, wenn der Gegner von hinten getroffen wird.
            -   `Flurry`: Führt eine schnelle Abfolge von Angriffen aus.
        -   **Schwerter:**
            -   `Cleave`: Ein mächtiger Schlag, der mehrere Gegner trifft.
            -   `Charge`: Stürmt auf einen Gegner zu und versetzt ihm einen Hieb.
        -   **Äxte:**
            -   `Rending Strike`: Verursacht Blutungsschaden über Zeit.
            -   `Crushing Blow`: Ein schwerer Schlag, der Rüstung durchdringt.
        -   **Speere:**
            -   `Piercing Thrust`: Durchbohrt den Gegner und verursacht zusätzlichen Schaden.
            -   `Sweeping Polearm`: Dreht sich im Kreis und trifft alle Gegner in der Nähe, um sie wegzustoßen.
    -   **Fernkampf:**
        -   **Magier:**
            -   **Passive Fähigkeiten:**
                -   `Mana Efficiency`: Reduziert die Manakosten für alle Zauber.
                -   `Elemental Bound`: Erhöht den Schaden vom ausgewählten Element.
            -   **Feuer Magier:**
                -   `Fireball`: Wirft einen Feuerball, der Schaden über Zeit verursacht.
                -   `Flame Shield`: Erhöht die Feuerresistenz für kurze Zeit.
            -   **Wasser Magier:**
                -   `Water Jet`: Schießt einen Wasserstrahl, der Gegner wegstößt.
                -   `Ice Barrier`: Erhöht die Wasserresistenz für kurze Zeit.
            -   **Eis Magier:**
                -   `Frost Bolt`: Verlangsamt Gegner in der Nähe.
                -   `Ice Armor`: Erhöht die Eisresistenz für kurze Zeit.
            -   **Blitz Magier:**
                -   `Change Lightning`: Blitze springen auf nahe Gegner über.
                -   `Static Field`: Erhöht die Blitzresistenz für kurze Zeit.
        -   **Schütze:**
            -   **Aktive Fähigkeiten:**
                -   `Power Shot`: Ein gezielter Schuss, der erhöhten Schaden verursacht.
                -   `Multi Arrow`: Schießt mehrere Pfeile auf einmal ab.
            -   **Passive Fähigkeiten:**
                -   `Agility Boost`: Erhöht die Beweglichkeit.
                -   `Arrow Recovery`: Chance, Pfeile zurückzugewinnen.
-   **Supporter:**
    -   **Aktive Fähigkeiten:**
        -   `Heal`: Stellt die Lebenspunkte eines Verbündeten wieder her.
        -   `Group Heal`: Stellt die Lebenspunkte aller Verbündeten in der Nähe wieder her.
        -   `Cleanse`: Entfernt negative Zustände von einem Verbündeten.
        -   `War Horn`: Erhöht den Schaden und die Verteidigung aller Verbündeten in der Nähe für kurze Zeit.
    -   **Passive Fähigkeiten:**
        -   `Faith`: Regeneriert langsam Lebenspunkte über Zeit.
        -   `Guardian Angel`: Erhalte ein Schild für kurze Zeit, wenn das Leben auf 10 % fällt.

## 🔹 Technische Umsetzung
_Datenstruktur, UI, Trigger-Logik_

## 🔹 Verknüpfung mit Items und Rollen
_Welche Skills hängen an Items oder Rollen?_