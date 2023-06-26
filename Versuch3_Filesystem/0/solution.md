# Lösungen Aufgabe 0

## Wie findet ein FAT32-Treiber mittels der Verwaltungsdaten die Größe eines Sektors/Clusters?
Ein FAT32-Treiber findet die Größe eines Sektors/Clusters, indem er auf die Verwaltungsdaten des Dateisystems zugreift. Diese Daten enthalten spezifische Parameter, die Informationen über die Größe der Sektoren und Cluster liefern.

## Wie stehen die drei Einheiten Sektornummer, Clusternummer und Dateiadresse im Zusammenhang? Wie lassen sich die Einheiten umrechnen?
Ein Cluster besteht aus einer oder mehreren Sektoren. Die Clusternummer ist also im Grunde eine Art Gruppierung der Sektornummern. Die Sektornummer wiederum ist ein physischer Verweis auf den Ort der Daten auf der Festplatte.

Eine Dateiadresse entspricht einer Clusterkette in einem FAT (File Allocation Table). Um von einer Dateiadresse zur Sektornummer zu kommen, werden die Cluster, die die Datei belegen, in der FAT verfolgt und die entsprechenden Sektoren identifiziert.

Eine Umrechnung zwischen diesen Einheiten ist also eine Berechnung, die auf der Grundlage der Clustergröße und der Position der Datei innerhalb der Clusterkette erfolgt.

## Wie errechnet sich der Beginn der ersten/zweiten FAT?
Der Beginn der ersten FAT folgt üblicherweise auf die optionalen reservierten Sektoren nach dem Bootsektor und dem FS Information Sector (bei FAT32). Der Beginn der zweiten FAT errechnet sich, indem man die Größe der ersten FAT zum Startpunkt der ersten FAT addiert. Beide Positionen sind durch die Struktur des Dateisystems festgelegt und in den entsprechenden Verwaltungsdaten des Dateisystems codiert.

## Wo beginnt der erste Cluster auf dem Medium und was ist seine Nummer (Hinweis: es ist weder 0 noch 1)? Wo der nächste?
Der erste Datencluster auf einem FAT32-Medium ist Cluster 2. Dieser beginnt direkt nach dem letzten Sektor der letzten FAT. Der nächste Cluster, Cluster 3, würde direkt nach dem letzten Sektor von Cluster 2 beginnen.

## Wie kann ein Treiber das Wurzelverzeichnis finden?
Ein Treiber kann das Wurzelverzeichnis eines Dateisystems finden, indem er auf die Verwaltungsdaten des Dateisystems zugreift. Diese Informationen enthalten normalerweise den Ort des Wurzelverzeichnisses im Dateisystem. Bei der FAT32-Struktur ist dies üblicherweise ein definierter Ort nach den File Allocation Tables. Dies ermöglicht es dem Treiber, auf die grundlegende Struktur des Dateisystems und somit auf alle darin enthaltenen Dateien und Unterverzeichnisse zuzugreifen.

## Wie sieht ein Verzeichniseintrag aus?
Ein Verzeichniseintrag in FAT32 ist 32 Byte groß und enthält Informationen wie den Dateinamen, die Dateierweiterung, Attribute, die Erstellungszeit und das Erstellungsdatum, die Startcluster-Nummer und die Dateigröße. Einige Einträge können speziell markiert sein, um sie als gelöscht oder als Teil eines langen Dateinamens zu kennzeichnen.