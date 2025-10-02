# 👑 MVP Punkt 5: Bosse mit Mechaniken
**Lead:** DuCelele

## 🔹 Boss-Typen
_Wie viele Bosse? Pro Dungeon einer?_
Pro Dungeon ein Boss, der am Ende der letzten Ebene wartet. 
Unterschiedliche Dungeons haben unterschiedliche Bosstypen.
(z.B. ein Nahkampf-Boss in einem engen Dungeon, ein Fernkampf-Boss in einem weiten Dungeon)

## 🔹 Mechaniken
_Welche einzigartigen Fähigkeiten oder Phasen haben sie?_
Wie in Elden Ring oder Dark Souls, sollten Bosse mehrere Phasen haben, die unterschiedliche Mechaniken und Strategien erfordern.

## 🔹 Design und Atmosphäre
_Wie unterscheiden sich Bosse visuell und spielerisch?_
Bosse sollten sich visuell stark von normalen Mobs abheben, um ihre Bedeutung zu unterstreichen. 
Dies kann durch Größe, Farbgebung, Animationen und Soundeffekte erreicht werden. 
Spielerisch sollten Bosse herausfordernde Mechaniken bieten, die Teamarbeit und strategisches Denken erfordern.

## 🔹 Loot und Belohnung
_Welche Items oder Fortschritte hängen an Bossen?_
Bosse sollten einzigartige und mächtige Items droppen, die nur durch das Besiegen des Bosses erhältlich sind. Dies könnte spezielle Waffen, Rüstungen oder kosmetische Gegenstände umfassen. Zusätzlich könnten Bosse Fortschritte im Spiel freischalten, wie z.B. neue Fähigkeiten oder Zugang zu neuen Dungeons.

## 🔹 Technische Umsetzung
_AI, Animation, Trigger, Events_
- **Modell/Animation**
  - Start mit Vanilla-Re-Skins + Shader (Zeitersparnis). Blockbench-Modelle für Post-MVP.
- **AI-Basis**
  - Gemeinsame `BossEntity`-Basisklasse für spezielle Boss-Mechaniken.
  - AI-Goals pro Boss: `PhaseOneGoal`, `PhaseTwoGoal`, `EnrageGoal` etc.
## 🔹 Verknüpfung mit Dungeons und Mobs
_Bossräume, Minions, Dungeon-Ende_
- **Bossräume**
  - Einzigartige Arenen mit speziellen Umgebungsmechaniken (z.B. Fallen, Plattformen).
- **Minions**
  - Bosse können Minions beschwören, die den Kampf erschweren und strategische Herausforderungen bieten.
- **Dungeon-Ende**
  - Das Besiegen des Bosses schließt den Dungeon ab und ermöglicht den Spielern, ihre Belohnungen zu sammeln und zum Hauptspiel zurückzukehren.
- MVP: bearbeitete Vanilla-Sounds (Pitch, Filter). TODO: Custom-Soundset später.