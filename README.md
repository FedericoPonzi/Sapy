Sapy
====

Sapy è il nome del progetto per il corso di metodologie di programmazione dell' anno 2012/2013. Realizzato in java ed inspirato al linguaggio Basic.

Relazione
==========
Il progetto Sapy è strutturato come un compilatore. Il progetto dovrà essere fondato su 4 diversi strati: a quello iniziale, troviamo il Lexer (Lexical Analyzer). Il suo compito sarà quello di prendere in input del plain text e controllarne la grammatica ovvero dovrà controllare se l’utente ha inserito termini presenti nell’ alfabeto di Sapy.
Dopo di che, dovrà trasformare ogni termine base in un oggetto di tipo Token: un Token è una stringa formata da uno o più caratteri che presi insieme ha un significato ben preciso. Il processo che si fà per creare questi Token viene chiamato tokenizzazione.

Tramite l’aggiunta del token “Funzione” tutto ciò che non viene riconosciuto come parte del linguaggio verrà salvato come questo tipo.

Tokenizzazione
=============
La tokenizzazione può essere di tue tipi: 
“Sofisticata”, tramite l’utilizzo delle regex (regular expressions); 
“Semplice”, ovvero utilizzando dei delimitatori (nel caso di Sapy, gli spazi).
Nel mio progetto, ho scelto il primo tipo di tokenizzazione.
Il Lexer di Sapy
Il lexer di Sapy prevede di base:
Un metodo per tokenizzare,
Un metodo per restituite la lista di oggetti tokens,
Un metodo richiamato dal main per stampare a video i nomi dei tokens.
Inoltre come attributi:
Una stringa per contenere le istruzioni prese come input;
Un (Array)List contenente la lista di tokens.

tokenizzare()
Dopo aver costruite un oggetto Lexer, che prende in input una lista di istruzioni sottoforma di stringa, il metodo tokenizzare si occupa appunto di dividere la stringa in lessemi.
Scorrendo carattere per carattere, tokenizzare prende in considerazione diversi casi:
E’ uno spazio: risparmiando i controlli successivi, se stà considerando uno spazio lo salta.
Comincia con $: Se comincia col simbolo del dollaro, tutto ciò che lo segue fino allo spazio sarà il nome di una variabile. Per definizione di Sapy, le variabili possono contere solamente caratteri alfanumerici e simbolo di underscore: è effettivamente questo utilizzato come input della regex, che cerca il primo carattere diverso da questo insieme.
Comincia con doppio apice: Se comincia con doppio apice, allora stiamo considerando una stringa. Il lexer la prenderà finchè non troverà un’altro doppio apice seguito da un fine riga (duepunti, o backslash n).
E’ un intero: Se è un intero, bisogna salvarne il valore in un token.
E’ un booleano: Come sopra.
E’ un simbolo: Nel caso sia un simbolo vede se è < o >: in questo caso controlla il carattere successivo (diverso da uno spazio) per vedere se è  > o =.
E’ una funzione conosciuta: Se è una funzione compresa fra quelle di base di Sapy, allora viene costruito l’oggetto Token apposito.
E’ una funzione: L’ultimo caso, stiamo considerando qualcosa di non presente in Sapy e la cataloghiamo quindi come funzione.

getListaTokens()
Questo metodo è l’interfaccia con lo strato successivo, il parser: questo si occupa di restituire la lista dei tokens generata dal metodo tokenizzare.
getListaTokensStringa()
Metodo utilizzato esclusivamente dal main: serve a stampare la lista dei tokens presi come input da file dal Lexer quando viene eseguito direttamente.


Il Parser
=================
Nel secondo strato troviamo il parser: questo deve controllare se la sintassi seguita dalle istruzioni è giusta.
La sintassi è la branca della linguistica che studia i diversi modi in cui le parole si uniscono tra loro per formare una proposizione ed i vari modi in cui le proposizioni si collegano per formare un periodo (Wikipedia).
Il parser quindi dovrà vedere se ogni elemento è stato inserito nel giusto ordine e in una posizione possibile.
Prenderà in input quindi una (Array)List di Tokens dallo strato superiore, il Lexer, e farà l’analisi sintattica aggiungendo all’ campo statico ListaIstruzioni dell’oggetto Programma Eseguibile (creato dalla classe principale del programma, Sapy.java) i corretti oggetti Istruzione.

A tempo di costruzione, ogni oggetto Istruzione controlla se l’input fornito dal parser sottoforma di una ArrayList di Tokens è esatto: se si si salva i dati di cui ha bisogno per una successiva esecuzione.

Casi particolari sono quelli in cui abbiamo un’istruzione FOR o una IF: per questi due, visto che accettano una lista di istruzioni come input, ho deciso di fare una mutua ricorsione.
Il metodo parse() parsa le istruzioni una alla volta, e ritornando un’oggetto istruzione per volta. Nel caso in cui incontra un IF, chiama un metodo parseIF creato apposta per parsare gli IF che poi richiamerà il parse() ricorsivamente. 
In questo modo, è possibile incapsulare IF dentro a IF. Un lavoro simile è svolto per il metodo parseFOR().

Per quanto riguarda il parsing delle operazioni matematiche, ho ovviamente scelto il parser ricorsivo discendente che viene richiamato dalle istruzioni per risolvere operazioni matematiche.

Interprete
============
Al terzo livello troviamo l’ interprete: questo è in pratica una consolle che permette di eseguire istruzioni Sapy una alla volta.
La sua realizzazione è basata su quella del file principale, sapy.java, ed è un ciclo while infinito (che terminerà con l’input di un’istruzione END) che prende in input una istruzione per volta.
Richimerà il lexer, poi il parser, eseguirà le istruzioni e le rimuoverà da ProgrammaEseguibile (lasciando invariate le variabili).

Sapy
===========
Il quarto ed ultimo livello è quello del compilatore: il file Sapy è il file principale del progetto. Prende in input la path di un file di testo contenente istruzioni Sapy e le esegue una per uno chiamando prima il Lexer, poi il Parser, ed infine eseguendo gli oggetti istruzioni da ProgrammaEseguibile.
E’ fornito anche di un campo ProgramCounter che funge da puntatore per l’utilizzo dell’istruzione GOTO.
