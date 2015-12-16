# Introduction #

For each primitive, and for two compound term, we show some example of use.


# List of example #

<b>Introduction to the CABAC library: Split Example with Plug application (in Italian)</b>

L'esempio che proponiamo qui di seguito fa riferimento al seguente scenario.
Supponiamo di avere due applicazioni che hanno a che fare con concetti come carrello (cart) e articolo (item) inseribili nel carrello; una delle due applicazioni riesce a manipolare solo i carrelli mentre l'altra è in grado di gestire unicamente gli oggetti inseribili nel carrello.
Supponiamo che in un determinato istante le due applicazioni vogliano scambiarsi delle informazioni: com'è possiamo permettere la comunicazione se entrambi gli applicativi gestiscono modelli di dati non uniformi? Non possiamo avere una comunicazione diretta.
Ricordiamo che i due concetti, carrello e item, presentano una relazione: un carrello contiene un insieme di item.
La soluzione suggerita dalla teoria dei connettori  è di interporre un connettore tra le due applicazioni: in questo modo le incorrispondenze vengono eliminate dal nuovo componente, senza modificare le applicazioni esistenti.
Il connettore è un semplice componente che coincide o ad una semplice primitiva, che permette di risolvere delle incorrispondenze di base, o a dalla composizione di più primitive: in questo ultimo modo possiamo risolvere delle incompatibilità anche più complesse, con il rispetto della semantica degli operatori proposti.
La teoria dei connettori dispone di una semplice primitiva che fa proprio al nostro caso: split message mismatch. Questa primitiva permette di dividere un messaggio, in arrivo dall'applicazione mittente, in un insieme di 'sottomessaggi', compatibili con l'applicazione destinataria.

Vediamo ora come procedere con l'implementazione pratica di un semplice connettore tramite l'utilizzo della libreria creata. L'esempio che stiamo per vedere fa riferimento alla classe SplitterExample presente nel package newTest della libreria

Come primo passo creiamo un nuovo progetto. Entriamo nella configurazione del build path e aggiungiamo le librerie necessarie per il funzionamento: quattro di queste sono richieste per il funzionamento di base del framework apache camel (http://camel.apache.org/what-are-the-dependencies.html), mentre una di queste è proprio la nostra libreria (che possiamo utilizzare sincronizzandoci via SVN). Inoltre, considerando lo scenario precedentemente mostrato, disponiamo di due classi, Cart e Item, che modellano i concetti rispettivamente di Carrello e di Articolo (entrambe presenti nell'archivio allegato).
Siamo ora pronti per scrivere il nostro primo connettore:  chiameremo questa classe First.

Abbiamo già detto che il nostro connettore sarà unicamente composto da una primitiva chiamata split message mismatch: nella nostra libreria questa prende il nome di Split (Splitter).
La definizione di uno splitter richiede una serie di passi ben articolati.
Inizialmente definiamo le informazioni di base del nostro oggetto splitter:
```

Split s = new Split("vm:source", Cart.class, "vm:receiver",Department.class);
```
Il primo parametro indica l'uri associato all'endpoint sorgente: in altri termini la stringa racchusa tra gli apici (URI) identifica un'applicazione; permette al connettore di ricevere i messaggi proveniente da quell'applicazione.
Il secondo parametro indica la classe dell'oggetto in ingresso. L'indicazione della classe permette al connettore di discriminare diverse situazioni.
Il terzo parametro contiene un insieme di URI, separati da una virgola se sono più di uno, che indicano alla primitiva Split quali saranno i potenziali destinatari della comunicazione. Nel nostro caso comunichiamo l'intenzione di inviare il risultato dello splitting ad una sola applicazione.
L'ultimo parametro, infine, indica la classe dell'insieme di oggetti in uscita dalla componente.

Una volta definite le informazioni di base del connettore, possiamo procedere con la definizione dei due concetti restanti: la logica di splitting e la logica di routing.

Con logica di splitting intendiamo quale procedimento seguire per dividere il messaggio in ingresso in un insieme di messaggi compatibili con l'applicazione destinataria. Nel nostro caso  la logica di splitting fa riferimento ad una procedura che, preso in ingresso un oggetto carrello, trasmette gli item in esso presenti. L'implementazione è la seguente
```

public List of Item splitItem(Cart cart) {
return cart.getItems();
}
```
Dalla definizione del metodo risciamo a capire che da un singolo oggetto in ingresso, riusciamo ad ottenere un'insieme di oggetti ad esso associati.

Per effettuare una sorta di binding tra la nostra logica e la primitiva split che stiamo definendo, aggiungiamo nella classe First la seguente riga di codice:
```

s.setSplittingLogic(First.class, "splitItem");
```
Con questa istruzione indichiamo che il metodo che si occupa di dividere i messaggi in ingresso è presente nella classe First (la stessa del connettore), e si chiama “splitItem”.

Allo stesso modo dobbiamo definire una logica di routing, questa volta però dobbiamo passare allo splitter l'oggetto che contiene il metodo che usiamo per il routing:
```

First f = new First();

s.setRoutingLogic(f, "routing");```
L'implementazione del metodo è la seguente
```

public Collection of String routing(Item i){
Collectionof String receivers = new ArrayList of String();
receivers.add("vm:receiver");
return receivers;
}
```
Questo metodo viene invocato per ogni Item presente nel carrello (che viene inviato al metodo): nel nostro caso abbiamo deciso di inoltrare ciascun item ottenuto all'applicazione destinataria; nulla impedisce di poter effettuare delle verifiche sull'item ricevuto, in modo tale da scegliere l'applicazione o le applicazioni destinatarie.

La struttura dello logica di routing, quindi la struttura del metodo routing non deve essere alterata. Questa deve sempre avere un parametro che indichi l'oggetto in transito alla componente, e deve sempre restituire una collezione di uri che rappresentano i receivers.

A questo punto abbiamo terminato la costruzione del nostro connettore; non ci rimane altro che avviarlo (e lasciarlo in esecuzione per un po' di secondi).
```

s.start();
try {
Thread.sleep(5000);
} catch (InterruptedException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}```

Proviamo ad avviare la nostra applicazione. Cosa accade?
Dato che non abbiamo nessun applicazione che produce dei messaggi (per quella source uri) , sarà un po' difficile vede in azione il connettore.

Per vedere in azione il connettore, simuliamo le due applicazioni  all'interno del nostro connettore. La simulazione può avvenire tramite due primitive introdotte nell'algebra dei connettori, missing send e extra send: nell'implementazione queste due primitive vengono chiamate Prod e Cons, perchè modellano il comportamento rispettivamente di un produttore e di un consumatore.
In questo modo nel connettore compaiono altri due termini: il primo invia un oggetto cart e l'altro riceve un insieme di item.

Cogliamo questa occasione per introdurre l'operatore di composizione Plug, che ci permette, insieme ad altri operatori, di poter creare connettori composti.

Naturalmente tutto funzione anche avviando singolarmente le componenti create.

Procediamo con la creazione del producer:
```

Prod p = new Prod("vm:source", Cart.class, cart);```

Il primo parametro indica l'uri associato all'endpoint destinatario del messaggio. Il secondo parametro invece fa riferimento alla classe dell'oggetto inviato a quell'uri, mentre l'ultimo parametro è un'istanza della Cart.

Definiamo adesso il consumer:
```

Cons c = new Cons("vm:receiver", Item.class);
```
Il primo parametro permette al consumatore definito di prelevare i messaggi destinati a quell'uri; il secondo parametro indica il tipo dell'oggetto in ingresso.

Applichiamo adesso l'operatore di composizione plug: i risultato dell'applicazione sarà un nuovo termine, non più primitivo, ma composto:
```

CompoundTerm ct = new Plug(p, new Plug(s,c));
```
Avviamo il nuovo connettore, lasciandolo in esecuzione per 5 secondi:
```

ct.start();
try {
Thread.sleep(5000);
} catch (InterruptedException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
```
Adesso è possibile vedere in esecuzione il connettore. Man mano vengono restituiti messaggi che mostrano il comportamento atteso. Ciò puà essere constato da questi due ultimi messaggi sulla console, scritti dal consumer

```
consumed Exchange[Message: Item@XXXXXX] by core.compoundterm.primitiveterm.Cons$1$1@XXXXX

consumed Exchange[Message: Item@XXXXXX] by core.compoundterm.primitiveterm.Cons$1$1@XXXXX```

<br><br><b>Studio delle altre primitive</b><br><br>
Dopo aver appreso il funzionamento generale della libreria, i concetti visti si applicano anche per le altre primitive. L'unica cosa che cambia tra una primitiva e l'altra è la definizione della sua logica. Come abbiamo appena visto, uno splitter ha una logica di splitting e una logica di routing; per il merge, ad esempio, possiamo definire una nostra logica di aggregazione, do sostituire a quella di default. La definizione della logica deve seguire però determinate regole. Quello che faremo qui di seguito sarà capire come interagire con le varie primitive<br>
<br>
<b>Transformer</b>
<br>
Facciamo riferimento all'esempio TranslatorExample presente nel package newTest della libreria.<br>
<br>
La logica di traduzione è un semplice metodo che prende in ingresso<br>
l'oggetto in arrivo al translator e provvede alla sua modifica.<br>
In questo caso sappiamo che i messaggi in arrivo al nostro connettore<br>
sono delle stringhe: infatti nel metodo abbiamo un parametro di<br>
tipo stringa. Ogni volta che abbiamo un messaggio in ingresso verrà<br>
chiamato il seguente metodo che provvederà alle opportune modifiche<br>
per adeguare la comunicazione. Naturalmente il tipo di ritorno del<br>
metodo dovrà essere coerento con quanto desiderato dal ricevente<br>
<pre><code><br>
public String setContent(String body){<br>
//nel nostro caso aggiungiamo la stringa camel a quella ricevuta<br>
return body+" Cameled";<br>
}</code></pre>
<br>
<b>Splitter</b>
<br>
L'esempio presente nella libreria, SplitterExample, è lo stesso visto in precedenza:Split Example with Plug application<br>
<br>
<br>
<b>Merge</b><br>

Nel caso del Merge possiamo definire una nostra logica di aggregazioe, che nel caso di camel prende il nome di strategia di aggregazione. L'approccio seguito fa riferimento alla seguente pagina, <a href='http://camel.apache.org/aggregator.html:'>http://camel.apache.org/aggregator.html:</a> nella sezione <i>Using Custom Strategy</i> possiamo trovare tutti i dettagli.<br>
La definizione di una propria logica di aggregazione comporta la definizione di una nuova classe che implementi AggregationStrategy, presente nella libreria camel-coreX.X, e che contenga la definizione del metodo aggregate. Riceve in ingresso due parametri tipo exchange e restituisce un'oggetto di tipo Exchange. Di seguito abbiamo un esempio, nel quale aggreghiamo tutti gli oggetti che stiamo ricevendo. Infatti il secondo parametro fa riferimento ad un messaggio appena ricevuto, mentre il primo rappresenta l'aggregazione che stiamo conservando. Questa rappresenta la strategia di aggregazione di default:<br>
<pre><code><br>
public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {<br>
Object newBody = newExchange.getIn().getBody();<br>
ArrayList of Object list = null;<br>
//first time we do aggregation<br>
if (oldExchange == null) {<br>
list = new ArrayList&lt;Object&gt;();<br>
list.add(newBody);<br>
newExchange.getIn().setBody(list);<br>
return newExchange;<br>
}<br>
else{<br>
list = oldExchange.getIn().getBody(ArrayList.class);<br>
list.add(newBody);<br>
return oldExchange;<br>
}<br>
}</code></pre>

Inoltre è obbligatorio impostare la taglia dei elementi che si vogliono aggregare. Se i prossimi 5 messaggi in ingresso formano la nostra aggregazione, possiamo procedere con<br>
<pre><code><br>
//Assuming m an instance of Merge<br>
m.setCompletitionSize(5);<br>
</code></pre>
<br>
<b>Order</b><br>
L'utilizzo della primitiva order può includere la definizione di una propria permutazione.Cerchiamo di capire come possiamo definire la nostra funzione di permutazione.  Facendo riferimento all'esempio OrderExample, possiamo vedere che abbiamo un endpoint sorgente che invia tre Stringhe: "Ciao", "come", "Stai". Le sorgenti sono impostate in modo tale che questi messaggi non arrivano in ordine: cioè il connettore potrebbe ricevere ad esempio le tre stringhe in questa sequenza "stai?" "ciao" e "Come". La primitiva order permette di procedere con il riordinamento di questi messaggi, tramite la funzione di permutazione.<br>
Questa funzione prende in ingresso il messaggio in arrivo alla componente e restituisce un Integer che ne indica la sua posizione nella sequenza. Ad esempio, nel nostro caso, la sequenza è la seguente: "Ciao"->1, "come"->2, "stai?"->3. La funzione viene così definita:<br>
<pre><code><br>
public Integer myPermutation(String s){<br>
if(s.equals(" stai ?")) return new Integer(2);<br>
if(s.equals(" come ")) return new Integer(1);<br>
if(s.equals(" ciao ")) return new Integer(0);<br>
return -1;<br>
}<br>
</code></pre>
Inoltre, come per il merge, dobbiamo definire il numero di messaggi che dobbiamo permutare:<br>
<pre><code><br>
//Assuming o an instance of Order<br>
o.setSequenceSize(3);<br>
</code></pre>
Una volta ricevuti questi tre messaggi, la primitiva provvede all'ordinamento e all'inoltro dei messaggi nella corretta sequenza.