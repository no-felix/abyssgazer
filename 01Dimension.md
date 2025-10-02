###  1. Prozedural generierte Dimension
**Lead:** Theo

- **Kurzbeschreibung Dimension:**  
    Eine Dimension wird von ihrer maximalen Höhe bis zur minimalen Höhe ausgenutzt.
    Sie besteht aus mehreren Ebenen, die jeweils unterschiedliche Umgebungen, 
    Mobs und Herausforderungen bieten.
    Das heißt ein Dungeon am Ende jeder Ebene zum Übergang in die neue Ebene.
    Dabei kann der Rest der Ebene als Vorbereitungs- und Erkundungsbereich genutzt werden.

    **Was passiert am Ende einer Dimension?**
    - Es gibt ein Portal, welches die Spieler in die nächste Dimension bringt
    - Es gibt einen Boss, der das Portal bewacht
    - Das Portal könnte in der Mitte des Abyss am "Boden" der Dimension sein


- **Technische Umsetzung:**  
  _Welche Methoden/Technologien werden verwendet (z. B. Noise-Algorithmen, Chunk-Handling)?_


- **Design/Themes der Ebenen:**
    Die Themen der Ebenen können sich nur in den Blöcken unterscheiden, aber auch in der Struktur
    oder den kompletten Aufbau der Ebene. So kann eine Ebene zum Beispiel auch eine Einkerbung in
    die Seiten des Abyss haben, wodurch eine größere Fläche für die Ebene entsteht. Es können auch
    einzelne Objekte, wie zum Beispiel kleine Felsvorsprünge aus dem Abyss ragen.
    Ebenen können auch Effekte haben. 


- **Dungeon-Struktur:**  
    Eine Ebene kann aus 50-150 Blöcken Höhe bestehen. Der Dungeon kann entweder die ganze Ebene umfassen
    oder nur einen Teil der Ebene. Dadurch kann es auch mehrere Dungeons pro Ebene geben. Das heißt, 
    es gibt mindestens einen Dungeon pro Ebene. Ein Dungeon kann auf die übliche Art und Weise aufgebaut
    sein, also mit Räumen, Korridoren usw. Aber es kann auch andere Strukturen geben, wie zum Beispiel
    eine große Höhle oder ein Labyrinth.
   
 
- **Performance-Strategien:**  
    Standardoptimierungen -> es wird nur das geladen was der Spieler sehen kann. Chunkchaching.
    Generell sollten wenn möglich maximal 3 Ebenen generiert werden.

- **Abhängigkeiten zu anderen Punkten:**  
    Biome (Ebenen):
    - Welche Mobs/Bosse gibt es in der Ebene?
    - Welche Items gibt es in der Ebene?
    - Alles zusammen definieren?