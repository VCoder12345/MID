# MID
Die Programmiersprache MID für meine Maturaarbeit

Dieser Code basiert auf:
*Nystrom, R. (2021). Crafting interpreters. Genever Benning.
*Parr, T. (2009). Language Implementation Patterns. Pragmatic Bookshelf.

Der Ordner src enthält den eigentlichen Code.
Im Ordner examples können Beispielprogramme in MID gefunden werden und zwei Python-Programme zur Ausgabe von Primzahlen, die in der Arbeit mit MID verglichen worden sind.
Der Ordner export enthält interpreter.jar und interpreter.exe. Mit diesen beiden Programmen kann MID-Quellcode ausgeführt werden. 
Dafür muss in der Konsole das Programm gestartet werden und der Pfad des Quellcodes als Parameter angegeben werden. 
Z.B. `interpreter.exe ../examples/primes.txt` oder `java -jar interpreter.jar ../examples/primes.txt`
Es gibt auch einige optionale Flags, um verschiedene Dinge für die Interpretierung einzustellen:
*b oder benchmarking: die Kompilierungs- und Ausführungszeit wird ausgegeben
*t oder traceExecution: während der Ausführung werden die Bytecode-Instruktionen ausgegeben
*pb oder printBytecode: vor der Ausführung des Programms werden alle generierten Bytecode-Instruktionen ausgegeben
*r oder runProgram: das Programm wird ausgeführt

Diese Flags können entweder auf 1 (an) oder auf 0 (aus) gesetzt werden.
Z.B. `interpreter.exe -b 1 -t 1 ../examples/twoSteps`
Normalerweise ist nur r an- und die restlichen Flags sind ausgeschalten. 
