package core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * La classe Connector definisce un connettore che permette il dialogo tra applicazioni eterogenee,
 * che possono essere dislocate su macchine differenti. Le applicazioni che partecipano alla
 * comunicazione vengono definite tramite le Uri del framework Apache Camel, che possiamo raggiungere
 * tramite questo link  http://camel.apache.org/uris.html.
 * La definizione del connettore avviene tramite l'aggiunta di componenti, come ed esempio sorgenti,
 * transformer, consumer: ogni volta che un messaggio transita sul connettore, ciascun componente in esso presente esamina se 
 * quel messaggio deve essere elaborato.
 * Ogni messaggio in ingresso e' composto da due componenti fondamentali: l'header e il body. 
 * Il body, come possiamo aspettarci, contiene la maggiorparte delle volte l'informazione (l'oggetto) che vogliamo scambiare. 
 * La decisione di elaborare o meno un messaggio viene presa in base al tipo del corpo del messaggio.
 * Ogni volta che un messaggio transita nel connettore, dovremmo aspettarci che tutte le componenti consumino questo 
 * messaggio dall'endpoint sorgente: purtroppo cio' non accade. Camel non gestisce la possibilita' di avere piu' componenti che
 * consumino un messaggio dallo stesso endpoint. 
 * Per questo motivo il connettore e' stato strutturato in maniera sequenziale,
 * cioe' le componenti sono legate a due due, in modo da formare una vera e propria catena. Il messaggio in ingresso al
 * connettore viene propagato da una componente all'altra: in questo modo se una delle componenti verifica che il messaggio
 * e' compatibile con la propria elaborazione, processa il messaggio e lo invia al destinatario della comunicazione.
 * Se il messaggio in ingresso non e' compatibile con la propria elaborazione, la componente si limita a propagare il messaggio
 * alla componente successiva.
 * L'unico accorgimento da avere nella costruzione del connettore e' relativo al metodo che si occupa dell'elaborazione. Se il
 * metodo inviato alla componente verifica che il messaggio deve essere elobarato, allora puo' restituire qualsiasi tipo di 
 * oggetto; altrimenti se il metodo e' incompatibile con il messaggio in ingresso deve limitarsi a restituire null.
 */

public class Connector_old {
	private CamelContext context;
	private Endpoint source;
	private Collection<Endpoint> receivers;
	/**
	 *vmnum identifica il numero di componenti
	 *di tipo vm (http://camel.apache.org/vm.html) che collegano le route definite 
	 *dall'utente.  
	*/
	private int vmnum = 0;
	/**
	 *L'intero order permette l'esecuzione delle rotte in maniera
	 *sincrona, grazie al metodo startupOrder. In questo modo ogni qual volta l'utente
	 *aggiunge una componente all'interno del nostro connettore, come ad esempio uno
	 *splitter o un receiver, viene aggiunto con il parametro order un'intero che 
	 *identifica il turno di esecuzione di quella particolare componente
	 */
	private int order=0; 
	/**
	 * La classe producerTemplate (http://camel.apache.org/producertemplate.html)
	 * permette di inviare dei messaggi ad un particolare endpoint, indicato
	 * come parametro
	 */
	private ProducerTemplate producer;
	private ArrayList<String> uritosend;
	private ArrayList<Object> messagetosend;
	private ArrayList<PollingConsumer> consumers;
	private String endroute = "vm:endroute";
	private Collection<Object> messageconsumed;
	
	/**
	 * Costruttore della classe Connector. Vengono inizializzate le istanze
	 * sulle queli andremo a lavorare, come ad esempio il contesto di Camel.
	 */
	public Connector_old() {
		context = new DefaultCamelContext();
		receivers = new ArrayList<Endpoint>();
		messagetosend = new ArrayList<Object>();
		uritosend = new ArrayList<String>();
		producer = context.createProducerTemplate();
		consumers = new ArrayList<PollingConsumer>();
		messageconsumed = new ArrayList<Object>();
	}

	/**
	 * Il metodo start() avvia il contesto del connettore. Questo si traduce
	 * automaticamente come l'avvio del contesto del framework Camel. 
	 * Con l'avvio del connettore vengono azionati tutti i thread relativi il framework:
	 * la loro esecuzione permette l'avviamento della comunicazione vera e propria. 
	 * Durante l'esecuzione del connettore vengono controllati se esistono dei Producer che vogliono inviare dei messaggi
	 * a determinati endpoint.
	 * Il metodo richiede che venga espressa anche la durata del connettore, espressa
	 * tramite il parametro in ingresso: se  questo e' pari 0 allora il connettore
	 * non interrompe mai la propria esecuzione.
	 * @param exectime esprime il tempo di esecuzione del connettore in millisecondi
	 */
	public void start(long exectime){
		try {
			final Object message = null;
			//Inizialmente colleghiamo le nostre rotte al destinatario
			context.addRoutes(new RouteBuilder() {
				
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(endroute).
					startupOrder(order).
					process(new Processor() {
						@Override
						public void process(Exchange exchange) throws Exception {
							// TODO Auto-generated method stub
							for(Iterator<Endpoint> i = receivers.iterator(); i.hasNext();){
								Endpoint e = i.next();
								Object message = exchange.getIn().getBody();
								//System.out.println(exchange.getIn().getBody());
								producer.sendBody(e,message);
							}
						}
					});
				}
			});
			order++;

			//Avviamo il contesto
			context.start();
			
			/**
			 * Controlliamo se ci sono dei producer che vogliono inviare
			 * messaggi a degli endpoint
			 */
			//Avviamo il servizio offerto dai Consumer
			for(Iterator<PollingConsumer> i = consumers.iterator(); i.hasNext();){
				PollingConsumer consumer = i.next();
				consumer.start();
			}
			
			for(int i=0; i<uritosend.size(); i++){
				//Inviamo il messaggio all'uri indicato
				//System.out.println("Producing "+messagetosend.get(i)+" to "+uritosend.get(i));
				producer.sendBody(uritosend.get(i),messagetosend.get(i));
				uritosend.remove(i);
				messagetosend.remove(i);
			}
			
			//Mettiamo in ascolto i receveir per tutta la durata del connettore
			TimerTask t = new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for(Iterator<PollingConsumer> i = consumers.iterator(); i.hasNext();){
						PollingConsumer consumer = i.next();
						Exchange e = consumer.receiveNoWait();
						if(e!=null)
							System.out.println("Consumed "+e.getIn().getBody());
					}
				}
			};
			Timer timer = new Timer();
			timer.schedule(t, 0,100);
			
			if(exectime!=0){
				Thread.sleep(exectime);
				timer.cancel();
				context.stop();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Ci sono stati problemi con l'avvio del contesto");
			e.printStackTrace();
		}
	}
	
	/**
	 * Il metodo stop() permette di forzare l'arresto del connettore. Prima di arrestarlo, e di 
	 * conseguenza interrompere il contesto di Camel, si attendono un paio di secondi
	 */
	public void stop(){
		try {
			Thread.sleep(10000);
			context.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Ci sono stati problemi con l'interruzione del contesto");
			e.printStackTrace();
		}
	}
	
	/**
	 * Il metodo addSource permette di aggiungere la sorgente al connettore.
	 * Per come viene modellata la comunicazione in Camel, non e' possibile registrare nel conettore piu' di una sorgente per una stessa route. 
	 * @param uri identifica la risorsa che rappresenta il nostro endpoint
	 */
	public void addSource(String uri) {
		
		source = context.getEndpoint(uri);
		try {
			context.addRoutes(new RouteBuilder() {
				
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(source).startupOrder(order).to("vm:localhost0");
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order++;
	}
	
	/**
	 * Il metodo addReceiver aggiunge al connettore i destinari, quindi gli endpoint di destinazione della comunicazione.
	 * I destinatari vegono praticamenti aggiunti al connettore prima dell'avvio del
	 * connettore. 
	 * @param uri identifica la risorsa che rappresenta il nostro endpoint
	 */
	public void addReceiver(String uri){
		receivers.add(context.getEndpoint(uri));
	}
	
	/**
	 * Versione del metodo addReceiver che prende in ingresso una collezione di URI
	 * @param uris collezione di stringhe che fanno riferimento a delle uri
	 */
	public void addReceiver(Collection<String> uris){
		for(Iterator<String> it = uris.iterator(); it.hasNext();){
			String uri = it.next();
			receivers.add(context.getEndpoint(uri));
		}
	}
	
	/**
	 * Il metodo produce permette l'invio di particolari messaggi all'endpoint rappresentato
	 * dall'uri: l'endpoint puo' appartenere o meno al connettore. Questo avviene tramite 
	 * l'utilizzo del ProducerTemplate (http://camel.apache.org/producertemplate.html).
	 * Questo metodo si differenzia da addProducer: infatti, mentre nel metodo addProducer
	 * si inserisce un producer nel connettore, il seguente metodo permette di produrre messaggi
	 * all'esterno del connettore, per esigenze proprie.
	 * L'invio del messaggio puo' essere fatto solo quando viene
	 * avviato il connettore: l'invio dei messaggi viene praticamente effettuato da quel punto
	 * in poi
	 * @param uri identifica la risorsa che rappresenta il nostro endpoint
	 * @param o identifica il messaggio che si vuole inviare
	 */
	public void produce(String uri, Object o){
		uritosend.add(uri);
		messagetosend.add(o);
	}
	
	/**
	 * Il metodo consume permette di consumare un messaggio da un determinato endpoint:
	 * diversamente da quanto accade con il metodo addConsumer, l'endpoint non puo' appartenere al connettore.
	 * Attraverso questo metodo inseriamo un consumer esterno al connettore, ma comunque legato al suo funzionamento.
	 * Inoltre il seguente metodo non deve essere confuso con il metodo addReceiver, in quanto
	 * nel primo definiamo un consumatore che preleva i messaggi dall'endpoint indicato
	 * dall'uri, mentre il secondo si limite a deporre il messaggio nell'endpoint indicato.
	 * @param uri identifica l'endpoint dal quale vogliamo consumare
	 */
	public void consume(String uri){
		try {
			PollingConsumer pc = context.getEndpoint(uri).createPollingConsumer();
			consumers.add(pc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Problema con la definizione del consumer. Controlla se hai inserito l'uri in maniera corretta");
			e.printStackTrace();
		}
	}
	
	/**
	 * Aggiunge al connettore un producer, cioe' una particolare componente che permette di produrre messaggi.
	 * La produzione del messaggio e' subordinata dall'esecuzione del metodo indicato dai parametri.
	 * Ogni volta che viene inviato un messaggio al connettore, viene eseguito il metodo indicato nel producer e, in base 
	 * al comportamento del metodo stesso, il componente produce o meno il messaggio.
	 * Se il messaggio in ingresso alla componente soddisfa il metodo, cioe' soddisfa i criteri di produzione 
	 * del messaggio presenti nel metodo, il messaggio viene recapitato a destinazione
	 * @param intype tipo del messaggio in ingresso che si vuole trattare
	 * @param methodclass classe contenente il metodo che si occupa dell'elaborazione
	 * @param method stringa che in indica il nome del metodo dell'elaborazione
	 */
	public void addProducer(final Class intype, final Class methodclass, final String method){
		try {
			context.addRoutes(new RouteBuilder() {
				
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					//Recuperiamo l'endpoint precedentemente definito
					Endpoint e1 = context.getEndpoint("vm:localhost"+vmnum);
					vmnum++;
					final int actual = vmnum;
					//definiamo un nuovo endpoint all'interno del nostro connettore
					Endpoint e2 = context.getEndpoint("vm:localhost"+vmnum);
						
					//definizione della route
					from(e1).startupOrder(order).
					process(new Processor() {
						@Override
						public void process(Exchange exchange) throws Exception {
							// TODO Auto-generated method stub
							Object message = exchange.getIn().getBody(intype);
							producer.sendBody("vm:localhost"+actual, message);
							Method myMethod = null;
							Method[] m = methodclass.getDeclaredMethods();
							for(int i=0; i<m.length; i++){
								if(m[i].getName().equals(method)){
									myMethod = m[i];
								}
							}
							final Object t = methodclass.newInstance();
							//invocazione del metodo ed insiremento del suo risultato nel coropo del messaggio
							if(message!=null){
								for(Iterator<Object> i = messageconsumed.iterator(); i.hasNext();){
									Object o = i.next();
									exchange.getIn().setBody(myMethod.invoke(t,o));
								}
							}
						}
					}).choice().when(body().isNotNull()).to(endroute)
					.end();
				}
			});
	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order++;
	}
	
	/**
	 * Il metodo addConsumer aggiunge un consumer all'interno del connettore.
	 * Permette di consumare, cioe' assorbire, un messaggio in transito nel connettore che rispetta
	 * determinati vincoli, verificati dal metodo indicato dai parametri.
	 * @param intype tipo del messaggio in ingresso che si vuole trattare
	 * @param methodclass classe contenente il metodo, di confronto, che si occupa dell'elaborazione
	 * @param equalmethod stringa che in indica il nome del metodo dell'elaborazione
	 */
	public void addConsumer(final Class intype, final Class methodclass, final String equalmethod){
		try {
			context.addRoutes(new RouteBuilder() {
				
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					//Recuperiamo l'endpoint precedentemente definito
					Endpoint e1 = context.getEndpoint("vm:localhost"+vmnum);
					vmnum++;
					final int actual = vmnum;
					//definiamo un nuovo endpoint all'interno del nostro connettore
					Endpoint e2 = context.getEndpoint("vm:localhost"+vmnum);
						
					//definizione della route
					from(e1).startupOrder(order).
					process(new Processor() {
						@Override
						public void process(Exchange exchange) throws Exception {
							// TODO Auto-generated method stub
							//System.out.println("Questo e' il corpo del messaggio "+exchange.getIn().getHeader("Prova"));
							//Recuperiamo il metodo per l'elaborazione
							Object message = exchange.getIn().getBody(intype);
							producer.sendBody("vm:localhost"+actual, message);
							Method myMethod = null;
							Method[] m = methodclass.getDeclaredMethods();
							for(int i=0; i<m.length; i++){
								if(m[i].getName().equals(equalmethod)){
									myMethod = m[i];
								}
							}
							final Object t = methodclass.newInstance();
							//invocazione del metodo ed insiremento del suo risultato nel coropo del messaggio
							if(message!=null)
								if(((Boolean)myMethod.invoke(t,message))){
									messageconsumed.add(message);
									//Forzo tutti i consumer del connettore ad essere rieseguiti
									producer.sendBody(source,null);
								}
						}
					});
					//choice().when(body().is).to(source)
					//.end();
					
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order++;
	}
		
	
	/**
	 * Il metodo addTransfomer permette di definire un trans all'interno del nostro connettore.
	 * Al suo interno viene definito un processor: viene creato un processore legato al contesto camel
	 * nel quale avviene l'elaborazione dei dati in ingresso. All'interno di questo processor
	 * recuperiamo ed invochiamo il metodo (definito dall'utente) che viene usato praticamente
	 * per l'elaborazione: le generalita' del metodo sono comunicate tramite i parametri. 
	 * Dobbiamo conoscere il tipo di dato in ingresso al nostro transformer, in 
	 * modo tale da non avere errore a runtime.
	 * Il risultato del metodo viene inserito nel corpo del messaggio in uscita dal transfomer.
	 * Ricordiamo che, se il messaggio in ingresso non puo' essere elaborato, il metodo che si occupa dell'elaborazione dovra' restituire
	 * null.
	 * @param intype tipo del contenuto del messaggio in ingresso al nostro connettore
	 * @param methodclass classe del metodo dai noi definita per l'elaborazione dei dati
	 * @param method stringa indicante il nome del metodo
	 */
	public void addTransformer(final Class intype, final Class methodclass, final String method){

		try {
			context.addRoutes(new RouteBuilder() {
				
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					//Recuperiamo l'endpoint precedentemente definito
					Endpoint e1 = context.getEndpoint("vm:localhost"+vmnum);
					vmnum++;
					final int actual = vmnum;
					//definiamo un nuovo endpoint all'interno del nostro connettore
					Endpoint e2 = context.getEndpoint("vm:localhost"+vmnum);
						
					//definizione della route
					from(e1).startupOrder(order).
					process(new Processor() {
						
						@Override
						public void process(Exchange exchange) throws Exception {
							// TODO Auto-generated method stub
							Object message = exchange.getIn().getBody(intype);
							//Recuperiamo il metodo per l'elaborazione
							producer.sendBody("vm:localhost"+actual, message);
							Method myMethod = null;
							Method[] m = methodclass.getDeclaredMethods();
							for(int i=0; i<m.length; i++){
								if(m[i].getName().equals(method)){
									myMethod = m[i];
								}
							}
							final Object t = methodclass.newInstance();
							//invocazione del metodo ed insiremento del suo risultato nel coropo del messaggio
							if(message!=null)
								//System.out.println(message+" --> "+myMethod.invoke(t,message));
								exchange.getIn().setBody(myMethod.invoke(t,message));
						}
					}).
					choice().when(body().isNotNull()).to(endroute)
					.end();
					
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order++;
	}
	
	/**
	 * Il metodo addSplitter permette di aggiungere uno splitter al nostro connettore.
	 * Questo metodo permette di dividere un messaggio in ingresso, in tanti sottomessaggi, in
	 * base alla struttura del messaggio stesso: una volta diviso il messaggio iniziale, 
	 * i sottomessaggi vengono inviati all'endpoint destinatario. 
	 * Anche in questo caso i parametri passati permettono di recuperare il metodo che si vuole
	 * invocare per l'elaborazione dei dati in ingresso. Il metodo individuato deve permettere la separazione 
	 * del dato in input, cioe' deve includere il criterio di splitting.
	 * Il metodo richiamato dovra' restituire l'insieme dei sottodati ricavati, sotto forma di una collezione.
	 * Il framework si occupa ne passi successivi di recapitare i nuovi dati in maniera corretta
	 * @param methodclass classe contentente il metodo che si occupa dell'elaborazione  
	 * @param method stringa indicante il nome del metodo.
	 */
	public void addSplitter(final Class methodclass,final String method){
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					Endpoint e1 = context.getEndpoint("vm:localhost"+vmnum);
					vmnum++;
					final int actual = vmnum;
					Endpoint e2 = context.getEndpoint("vm:localhost"+vmnum);
						
					from(e1).startupOrder(order).
					process(new Processor() {
						@Override
						public void process(Exchange exchange) throws Exception {
							// TODO Auto-generated method stub
							//System.out.println("ricevo "+exchange.getIn().getBody()+" da vm:localhost"+(actual-1));
							Object message = exchange.getIn().getBody();
							producer.sendBody("vm:localhost"+actual, message);
						}
					}).
					split().method(methodclass, method).
					choice().when(body().isNotNull()).to(endroute)
					.end();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order++;
	}
}
