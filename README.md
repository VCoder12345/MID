# MID
Die Programmiersprache MID f�r meine Maturaarbeit

Dieser Code basiert auf:
*Nystrom, R. (2021). Crafting interpreters. Genever Benning.
*Parr, T. (2009). Language Implementation Patterns. Pragmatic Bookshelf.

Der Ordner src enth�lt den eigentlichen Code.
Im Ordner examples k�nnen Beispielprogramme in MID gefunden werden und zwei Python-Programme zur Ausgabe von Primzahlen, die in der Arbeit mit MID verglichen worden sind.
Der Ordner export enth�lt interpreter.jar und interpreter.exe. Mit diesen beiden Programmen kann MID-Quellcode ausgef�hrt werden. 
Daf�r muss in der Konsole das Programm gestartet werden und der Pfad des Quellcodes als Parameter angegeben werden. 
Z.B. `interpreter.exe ../examples/primes.txt` oder `java -jar interpreter.jar ../examples/primes.txt`
Es gibt auch einige optionale Flags, um verschiedene Dinge f�r die Interpretierung einzustellen:
*b oder benchmarking: die Kompilierungs- und Ausf�hrungszeit wird ausgegeben
*t oder traceExecution: w�hrend der Ausf�hrung werden die Bytecode-Instruktionen ausgegeben
*pb oder printBytecode: vor der Ausf�hrung des Programms werden alle generierten Bytecode-Instruktionen ausgegeben
*r oder runProgram: das Programm wird ausgef�hrt

Diese Flags k�nnen entweder auf 1 (an) oder auf 0 (aus) gesetzt werden.
Z.B. `interpreter.exe -b 1 -t 1 ../examples/twoSteps`
Normalerweise ist nur r an- und die restlichen Flags sind ausgeschalten. 
