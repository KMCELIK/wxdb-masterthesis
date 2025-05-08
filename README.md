# wxdb-masterthesis
„Java Spring Anwendung zur Beschaffung, Verarbeitung und Integration von Wetterdaten in eine lokale Datenbank.

Voraussetzungen und Vorbereitung für die Installation: 

JDK Java Temurin 17 installieren
1. Seite öffnen:
 Temurin 17 Download
2. Passendes Installationspaket auswählen:
Stelle sicher, dass du die Version für dein Betriebssystem auswählst (z. B. Windows, x86_64 Architektur).
3. Download starten:
Lade die Installationsdatei herunter.
4. Installation durchführen:
Führe die heruntergeladene Datei aus und folge den Installationsschritten (Standardoptionen sind meistens ausreichend).
5. Installation überprüfen:
Öffne ein Terminal (z. B. die Eingabeaufforderung auf Windows) und tippe:
java -version
Du solltest eine Ausgabe in der Art sehen:
openjdk version "17.x.x" ... Temurin ...
IDE vorbereiten (z. B. Eclipse)
1. Eclipse öffnen (oder deine bevorzugte IDE).
2. JDK einbinden:
o Gehe zu: Window > Preferences > Java > Installed JREs.
o Klicke auf Add… und wähle den Pfad zu deinem Temurin 17 JDK aus.
o Setze das Häkchen bei diesem JDK, damit es als Standard verwendet wird.
Hinweis: Auch andere IDEs (z. B. IntelliJ, VSCode) können verwendet werden. Wichtig ist nur, dass Temurin 17 korrekt eingebunden ist.

Repository herunterladen
➔ Einfachste Methode: GitHub Desktop
1. GitHub Desktop öffnen. (Download: https://desktop.github.com/download/)
2. Repository klonen:
o Klicke auf Repository > Clone repository….
o Gib die Repository-URL ein oder suche es in deinem Account.
o Wähle den lokalen Speicherort aus.
o Starte den Klonvorgang.
➔ Alternative Methode: Kommandozeile (falls git bevorzugt wird)
1. Öffne dein Terminal oder die Eingabeaufforderung.
2. Nutze den Befehl:
git clone https://github.com/KMCELIK/wxdb-masterthesis.git
Alternativ kann das Repository direkt über die GitHub-Webseite heruntergeladen werden:
• Klicke auf den grünen „Code“-Button im Repository.
• Dort stehen dir zwei Optionen zur Verfügung:
1. Open with GitHub Desktop:
Öffnet das Repository direkt in GitHub Desktop, um es bequem zu klonen.
2. Download ZIP:
Lädt das gesamte Repository als ZIP-Datei herunter, die du anschließend manuell entpacken und in deine IDE integrieren kannst.

3. Prüfe, ob der Projektordner korrekt heruntergeladen wurde.


Projekt in die IDE integrieren
➔ Wenn es ein Maven-Projekt ist:
1. Import starten:
o In Eclipse: File > Import > Maven > Existing Maven Projects.
2. Projekt auswählen:
o Navigiere zum geklonten Ordner.
o Wähle die pom.xml aus.
3. Import abschließen.
o Eclipse lädt die Abhängigkeiten automatisch herunter.
➔ Wenn es ein normales Java-Projekt ist:
1. Import starten:
o In Eclipse: File > Import > General > Existing Projects into Workspace.
2. Projektordner auswählen.
3. Projekt importieren und ggf. Build-Path prüfen:
o Rechtsklick auf das Projekt > Properties > Java Build Path.
o Prüfen, ob Temurin 17 JDK verwendet wird.

