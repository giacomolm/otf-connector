/**
 * L'esempio che proponiamo qui di seguito fa riferimento al seguente scenario.
Supponiamo di avere due applicazioni che hanno a che fare con concetti come carrello (cart) e articolo (item) inseribili nel carrello; una delle due applicazioni riesce a manipolare solo i carrelli mentre l'altra è in grado di gestire unicamente gli oggetti inseribili nel carrello. 
Supponiamo che in un determinato istante le due applicazioni vogliano scambiarsi delle informazioni: com'è possiamo permettere la comunicazione se entrambi gli applicativi gestiscono modelli di dati non uniformi? Non possiamo avere una comunicazione diretta.
Ricordiamo che i due concetti, carrello e item, presentano una relazione: un carrello contiene un insieme di item. 
La soluzione suggerita dalla teoria dei connettori  è di interporre un connettore tra le due applicazioni: in questo modo le incorrispondenze vengono eliminate dal nuovo componente, senza modificare le applicazioni esistenti.
Il connettore è un semplice componente che coincide o ad una semplice primitiva, che permette di risolvere delle incorrispondenze di base, o a dalla composizione di più primitive: in questo ultimo modo possiamo risolvere delle incompatibilità anche più complesse, con il rispetto della semantica degli operatori proposti.
La teoria dei connettori dispone di una semplice primitiva che fa proprio al nostro caso: split message mismatch. Questa primitiva permette di dividere un messaggio, in arrivo dall'applicazione mittente, in un insieme di 'sottomessaggi', compatibili con l'applicazione destinataria.

Vediamo ora come procedere con l'implementazione pratica di un semplice connettore tramite l'utilizzo della libreria creata.

In allegato al documento trovate tutte le librerie (inclusa quella di camel) utilizzate per rendere operativo questo semplice connettore.

Come primo passo creiamo un nuovo progetto. Entriamo nella configurazione del build path e aggiungiamo le 5 librerie presenti nell'archivio allegato: quattro di queste sono richieste per il funzionamento di base del framework apache camel (http://camel.apache.org/what-are-the-dependencies.html), mentre una di queste è proprio la nostra libreria (CABAC.jar). Inoltre, considerando lo scenario precedentemente mostrato, disponiamo di due classi, Cart e Item, che modellano i concetti rispettivamente di Carrello e di Articolo (entrambe presenti nell'archivio allegato). 
Siamo ora pronti per scrivere il nostro primo connettore:  chiameremo questa classe First.

Abbiamo già detto che il nostro connettore sarà unicamente composto da una primitiva chiamata split message mismatch: nella nostra libreria questa prende il nome di Split (Splitter). 
La definizione di uno splitter richiede una serie di passi ben articolati. 
Inizialmente definiamo le informazioni di base del nostro oggetto splitter:

Split s = new Split("vm:source", Cart.class, "vm:receiver",Department.class);

Il primo parametro indica l'uri associato all'endpoint sorgente: in altri termini la stringa racchusa tra gli apici (URI) identifica un'applicazione; permette al connettore di ricevere i messaggi proveniente da quell'applicazione.
Il secondo parametro indica la classe dell'oggetto in ingresso. L'indicazione della classe permette al connettore di discriminare diverse situazioni.
Il terzo parametro contiene un insieme di URI, separati da una virgola se sono più di uno, che indicano alla primitiva Split quali saranno i potenziali destinatari della comunicazione. Nel nostro caso comunichiamo l'intenzione di inviare il risultato dello splitting ad una sola applicazione.
L'ultimo parametro, infine, indica la classe dell'insieme di oggetti in uscita dalla componente.

Una volta definite le informazioni di base del connettore, possiamo procedere con la definizione dei due concetti restanti: la logica di splitting e la logica di routing.

Con logica di splitting intendiamo quale procedimento seguire per dividere il messaggio in ingresso in un insieme di messaggi compatibili con l'applicazione destinataria. Nel nostro caso  la logica di splitting fa riferimento ad una procedura che, preso in ingresso un oggetto carrello, trasmette gli item in esso presenti. Aggiungiamo nella classe First la seguente riga di codice:

s.setSplittingLogic(First.class, "splitItem");

Con questa istruzione indichiamo che il metodo che si occupa di dividere i messaggi in ingresso è presente nella classe First (la stessa del connettore), e si chiama “splitItem”. Qui di seguito abbiamo l'implementazione del metodo:

			public List<Item> splitItem(Cart cart) { 
				return cart.getItems(); 
			}

Allo stesso modo dobbiamo definire una logica di routing:

s.setRoutingLogic(First.class, "routing");

L'implementazione del metodo è la seguente

		public Collection<String> routing(Item i){ 
			Collection<String> receivers = new ArrayList<String>(); 
			receivers.add("vm:receiver");
			return receivers;
		}

Questo metodo viene invocato per ogni Item presente nel carrello (che viene inviato al metodo): nel nostro caso abbiamo deciso di inoltrare ciascun item ottenuto all'applicazione destinataria; nulla impedisce di poter effettuare delle verifiche sull'item ricevuto, in modo tale da scegliere l'applicazione o le applicazioni destinatarie.

A questo punto abbiamo terminato la costruzione del nostro connettore; non ci rimane altro che avviarlo (e lasciarlo in esecuzione per un po' di secondi). 

		s.start(); 
		try { 
			Thread.sleep(5000); 
		} catch (InterruptedException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		}

Proviamo ad avviare la nostra applicazione. Cosa accade? 
Dato che non abbiamo nessun applicazione che produce dei messaggi (per quella source uri) , sarà un po' difficile vede in azione il connettore.

Per vedere in azione il connettore, simuliamo le due applicazioni  all'interno del nostro connettore. La simulazione può avvenire tramite due primitive introdotte nell'algebra dei connettori, missing send e extra send: nell'implementazione queste due primitive vengono chiamate Prod e Cons, perchè modellano il comportamento rispettivamente di un produttore e di un consumatore. 
In questo modo nel connettore compaiono altri due termini: il primo invia un oggetto cart e l'altro riceve un insieme di item. 

Cogliamo questa occasione per introdurre l'operatore di composizione Plug, che ci permette, insieme ad altri operatori, di poter creare connettori composti.

Naturalmente tutto funzione anche avviando singolarmente le componenti create.

Procediamo con la creazione del producer:

Prod p = new Prod("vm:source", Cart.class, cart);

Il primo parametro indica l'uri associato all'endpoint destinatario del messaggio. Il secondo parametro invece fa riferimento alla classe dell'oggetto inviato a quell'uri, mentre l'ultimo parametro è un'istanza della Cart.

Definiamo adesso il consumer:

Cons c = new Cons("vm:receiver", Item.class);

Il primo parametro permette al consumatore definito di prelevare i messaggi destinati a quell'uri; il secondo parametro indica il tipo dell'oggetto in ingresso.

Applichiamo adesso l'operatore di composizione plug: i risultato dell'applicazione sarà un nuovo termine, non più primitivo, ma composto:

CompoundTerm ct = new Plug(p, new Plug(s,c));

Avviamo il nuovo connettore, lasciandolo in esecuzione per 5 secondi: 		

		ct.start(); 
		try { 
			Thread.sleep(5000); 
		} catch (InterruptedException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		}

Adesso è possibile vedere in esecuzione il connettore. Man mano vengono restituiti messaggi che mostrano il comportamento atteso. Ciò puà essere constato da questi due ultimi messaggi sulla console, scritti dal consumer

consumed Exchange[Message: Item@XXXXXX] by core.compoundterm.primitiveterm.Cons$1$1@XXXXX

consumed Exchange[Message: Item@XXXXXX] by core.compoundterm.primitiveterm.Cons$1$1@XXXXX
 */

package newTest;
import java.util.ArrayList;
import java.util.Collection;

import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Prod;
import core.compoundterm.primitiveterm.Split;


public class Tutorial {

	public static void main(String[] args) {
		Split s = new Split("vm:endpoint1", newTest.Cart.class, "vm:endpoint2",Item.class);
		s.setSplittingLogic(Tutorial.class, "splitItem");
		
		//Not default routing
		//Tutorial t = new Tutorial();
		//s.setRoutingLogic(t, "routing");
		//s.start();
	
		Cons c = new Cons("vm:endpoint2", Item.class);
		//c.start();

		
		Item i1 = new Item(000, "Descrizione del primo item");
		Item i2 = new Item(000, "Descrizione del secondo item");
		Cart cart = new Cart(002);
		cart.addItem(i1);
		cart.addItem(i2);
		Prod p = new Prod("vm:endpoint1", newTest.Cart.class, cart);	
		//p.start();
		
		CompoundTerm ct = new Plug(p, new Plug(s, c));
		ct.start();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Item> splitItem(Cart c) {
		Cart cart =  (Cart) c;
		return cart.getItems();
	}
	
	static Object prev;
	
	public Collection<String> routing(Item i){ 
		Collection<String> receivers = new ArrayList<String>();
		if(!i.equals(prev)){
			prev = i;
			receivers.add("vm:endpoint2");
			return receivers;
		}
		else return receivers;
	}
}