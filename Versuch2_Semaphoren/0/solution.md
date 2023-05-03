# Lösungen Aufgabe 0

## busy-wait

Beim busy waiting (dt. aktiven Warten) wird beim programmieren während einem Prozess aktiv auf ein Signal gewartet, welche die unterbrechung aufhebt. <br>
Hiervon gibt es mehrere Anwendungen
- ### Zeitüberbrückung

  Bei der Zeitüberbrückung wird eine while-Schleife verwendet, welche bis zu einem bestimmten Wert zählt. Solange wird der Prozess pausiert.

  Bsp:
  ```java
  int i = 0;
  while (i < n) {
    i = i + 1:
  }
  ```
- ### Synchronisation

  Die Synchronisation wird als aktives Warten eingesetzt um

  #### Zustandsabfrage

Eine Komponente A kann erst dann mit ihren Aktionen fortsetzen, wenn eine Komponente B einen bestimmten Zustand erreicht hat. Dieser Zustand wird auf eine Weise angezeigt, die die Komponente A prüfen kann. Die Komponente A setzt dann aktives warten ein um die Aktion zu pausieren bis der gewünschte Zustand erreicht ist. Das aktive Warten wird dann auch ``polling``` genannt.
  ```java
  solange (Bedingung b nicht erfüllt) {
   warte für einige Zeit;
  }
  ```
  Diese Variante des aktiven Wartens wird auch als *slow busy waiting* oder *lazy polling* bezeichnet.

  #### Abfrage einer Sperre

  Wenn zwischen zwei Prozessen eine gemeinsam genutzte Variable vereinbart wird und durch eine Veränderung des Variablenwerts ein Fortsetzen des Prozesses angezeigt werden kann, so kann der Prozess aktives Warten verwenden um die Veränderung zu erkennen.
  ```java
  Prozess A                     Prozess B
  ...                           ...
  solange ( lock == 0) {        ...
     ;                          lock = 1;
  }                             ...
  Aktion a                      ...                           
  ```
Da die Variable ```lock``` verhindert, dass der Prozess A die Aktion a unkontrolliert durch B fortsetzt, wird die Variable Sperrvariable genannt. Da die Veränderung der Variable mittels wiederholtem (drehendem) Abfragen festgestellt wird, nennt man dieses Verfahren auch **Spinlock**.

## deadlock

Deadlock oder Verklemmung bezeichnet in der Informatik einen Zustand, bei dem eine zyklische Wartesituation zwischen mehreren Prozessen auftritt, wobei jeder beteiligte Prozess auf die Freigabe von Betriebsmitteln wartet, die ein anderer beteiligter Prozess bereits exklusiv belegt hat.

Nach Coffman sind die folgenden vier Bedingungen hinreichend für die Möglichkeit einer Verklemmung:

1. Die Betriebsmittel werden ausschließlich durch die Prozesse freigegeben<br>***(No Preemption)***
2. Die Prozesse fordern Betriebsmittel an, behalten aber zugleich den Zugriff auf andere <br>***(Hold and Wait)***
3. Der Zugriff auf die Betriebsmittel ist exklusiv <br>***(Mutual Exclusion)***
4. Mindestens zwei Prozesse besitzen bezüglich der Betriebsmittel eine zirkuläre Abhängigkeit <br>***(Circular Wait)***


## verhungern

Als Verhungern bezeichnet man in der Informatik den Fall, wenn ein Prozess keine CPU-Zeit zugeteilt bekommt, obwohl er zur Ausführung bereit wäre. Der Scheduler im Betriebssystemkern sollte idealerweise dafür sorgen, dass dies nicht geschieht und die CPU-Zeit „fair“ zugeteilt wird. Im allgemeinen gibt es keine ideale Lösung verhungern zu vermeiden, da ein ideales prioritätengesteuertes Scheduling gerade nicht fair ist.
