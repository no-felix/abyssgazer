# 👹 MVP Punkt 2: Eigene Horror Mobs und Dungeon Mobs

**Lead:** Felix

Ziel: Archetypen und Systeme definieren, nicht einzelne Kreaturen. Wir wollen schnell ein MVP aufsetzen, das stimmungsvoll wirkt, sich gut erweitern lässt und keinen unnötigen Implementierungsaufwand erzeugt.

## 🔹 Mob-Typen (Archetypen)

- **Standard (Trash)**
  - Häufige Spawns, niedrige Lebenspunkte, dienen als Druck- und Ressourcenfresser.
  - Variationsachsen: Nahkampf vs. Fernkampf, Einzelgänger vs. Schwarm, Kontroll- vs. Burst-Schaden.
- **Elite (Miniboss)**
  - Limitierte Spawns, erhalten signature Skills und bessere Resistenzen.
  - Variationsachsen: Support (Buff/Debuff), Tank (hohe HP, Block), Controller (Flächenkontrolle).
- **Boss**
  - Abschluss pro Dungeon, mehrere Phasen, Mechaniken, die Teamplay fördern.
  - Variationsachsen: Positionierungspuzzle, Schaden-Race, Adds-Management.

> TODO: Später Sub-Tiers definieren (z. B. „Swarm“-Standard, „Support“-Elite) sobald konkrete Dungeons feststehen.

## 🔹 Design- und Verhaltensbausteine

- **Optik-Hooks**
  - Visuelle Ausgestaltung orientiert sich an der jeweiligen Dungeon-Etage/Sub-Dimension (Stylesheet von Dimension).
  - Sub-Dungeons liefern den Mobs Styles (per Tags/Material-Presets); Basismobs übernehmen die Vorgaben ohne eigene Optik-Logik.
- **Bewegungsprofile**
  - Basis: „Sprint & Pause“ (kurze Angriffsfenster), „Schweben“ (langsam, ignoriert Hindernisse), „Sprungangriff“.
  - Erweiterungen:
    - **Stalker** – bleibt außerhalb der Sichtlinie, nutzt Tarnung/Teleport, attackiert bei offenen Flanken.
    - **Hit-and-Run** – hält Distanz, setzt Harass-Projektile ein, sprintet für kurze Burst-Fenster heran.
    - **Harrier** – kreist um Ziele, dash’t periodisch durch Spieler (Knockback/DoT).
    - **Suppressor** – kontrolliert Raum mit Flächenfeuer, rückt nach, wenn Spieler Deckung suchen.
- **Interaktions-Pattern**
  - Tarnung/Phasing, Selbstheilung (an fixen Punkten), Kettenreaktionen (Explode on death).
- **Schadensarten**
  - Physisch, Verderbnis (DoT + Stat-Debuff), Mental (Furcht/Verwirrung – späteres Feature).

## 🔹 Playstyle-Rollen

- **Pressure Dealer (Standard)** – erzeugt konstanten Schaden und zwingt Spieler zu Reaktionen.
- **Disruptor (Standard/Elite)** – entzieht Ressourcen, setzt Debuffs, zieht Spieler aus Position.
- **Anchor (Elite)** – hält Spieler in Zone, blockt Projektil, verstärkt Verbündete.
- **Stalker/Assassin (Elite)** – kombiniert Stalker-Bewegung mit Burst-Spikes; zwingt zu Awareness und Gegenmaßnahmen.
- **Suppress & Pounce (Elite)** – unterdrückt Fernkampf mit Flächendruck, wechselt bei Schwäche des Spielers in Nahkampf-Bursts.
- **Burst Boss** – öffnet kurze DPS-Fenster, in denen das Team maximalen Schaden liefern muss.
- **Attrition Boss** – zieht Kampf in die Länge, fordert Ressourcen-Management (Heilung, Buffs).

> Hinweis: Jede Rolle sollte klare Counter-Mechaniken haben (Healer cleansed Debuff, Tank blockt, DPS muss Positionierung nutzen).

## 🔹 Spawn- und Progressionslogik

- **Dungeon-Seeding**
  - Pro Ebene eine Mischung aus Standard-Archetypen, definiert über Pools (z. B. „Control-heavy“ vs. „Burst-heavy“).
- **Raum-Typen**
  - Brutkammern (kleine Räume, hoher Standard-Spawn), Ritualräume (Elite-Spawn), Bossarena (einzigartig, verriegelt).
- **Skalierung**
  - Werte-Multiplikatoren je Dungeon-Tier (HP, Damage, Resistenz, ggf. Fähigkeit freischalten).
- **Respawn-Regeln**
  - Standard spawnen nach Dungeon-Reset, Elite/Boss nur einmal pro Run.

> TODO: Tiefer gehende Dynamik (z. B. Lärm- oder Licht-Level als Spawn-Trigger) für Post-MVP evaluieren.

## 🔹 Technische Umsetzung (MVP-tauglich)

- **Modell/Animation**
  - Start mit Vanilla-Re-Skins + Shader (Zeitersparnis). Blockbench-Modelle für Post-MVP.
- **AI-Basis**
  - Gemeinsame `AbyssEntity`-Basisklasse für Resistenz-/Debuff-Handling.
  - AI-Goals pro Archetyp: `PressureAttackGoal`, `DisruptorDebuffGoal`, `AnchorZoneGoal` etc.
- **Sound & FX**
  - MVP: bearbeitete Vanilla-Sounds (Pitch, Filter). TODO: Custom-Soundset später.
- **Performance**
  - Cap bei ~30 aktiven Mobs pro Dungeon-Instanz.
  - AI-Throttling, wenn Spieler > X Blöcke entfernt (Tick-Skip).

## 🔹 Verknüpfung mit Dungeons & Items

- **Loot-Kategorien (eventuell)**
  - Standard: Crafting-Grundstoffe.
  - Elite: Essenzen/Buffs (z. B. „Resonanzkern“ für Skill-Upgrade).
  - Boss: Einzigartiges Relikt/Special Drop.
- **Schwierigkeits-Matrix**
  - Tabelle definieren: Dungeon-Tier x Mob-Archetyp -> Stat-Multiplikator, aktive Fähigkeiten, Drop-Chance.
- **Rollen-Synergien**
  - Healer cleansed Debuffs; Tank mitigiert Physis; DPS nutzt DPS-Fenster.
- **Item-Hooks**
  - Drop-Affixe als Flags (z. B. Debuff-Resistenz, Bonus gegen bestimmte Archetypen).

> TODO: Nach Fertigstellung der ersten Items Loot-Tabellen und Drop-Raten feinjustieren.
