package it.uniroma1.sapy.parsing;

import it.uniroma1.sapy.lexer.Lexer;
import it.uniroma1.sapy.lexer.Token;
import it.uniroma1.sapy.runtime.ProgrammaEseguibile;
import it.uniroma1.sapy.runtime.istruzioni.AssegnamentoVariabile;
import it.uniroma1.sapy.runtime.istruzioni.FOR;
import it.uniroma1.sapy.runtime.istruzioni.IF;
import it.uniroma1.sapy.runtime.istruzioni.InstructionSyntaxErrorException;
import it.uniroma1.sapy.runtime.istruzioni.Istruzione;
import it.uniroma1.sapy.runtime.istruzioni.Puntatore;
import it.uniroma1.sapy.runtime.istruzioni.REM;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * <h2>Parser</h2>
 * Classe Parser: parsa la lista di tokens con cui � costruito e che sono stati generati dal lexer.
 * @author Federico
 *
 */
public class Parser 
{
	private int indice;
	private ArrayList<Token> listaTokens;
	private ArrayList<String> listaEndTokens = new ArrayList<String>();
	
	/**
	 * Il costruttore: prende in input un oggetto Lexer e comincia a parsare.
	 * @param lex
	 */
	public Parser(Lexer lex)
	{
		//Mi aggiungo gli end token che mi serviranno dopo:
		listaEndTokens.add("EOL");
		listaEndTokens.add(":");
		//Mi salvo la lista di tokens:
		listaTokens = lex.getListaTokens();
		
		for (indice = 0; indice < listaTokens.size(); indice++)
		{
			Istruzione i;
			try 
			{
				if(listaTokens.get(indice).getContenuto().equals("EOL")) continue;
				i = parse();
				ProgrammaEseguibile.listaIstruzioni.add(i);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Ritorna il prossimo token, senza rimuoverlo dalla lista.
	 * @return
	 */
	private Token getNextToken()
	{
		return listaTokens.get(indice);
	}
	
	/**
	 * Ritorna true se il prossimo token NON e' di tipo fine linea. Ottimo per i while.
	 * @return boolean
	 */
	private boolean nextTokenIsNotEnd()
	{
		boolean result = false;
		for (int i = 0; i<listaEndTokens.size(); i++)
		{
			result = result || ( getNextToken().getContenuto().equals(listaEndTokens.get(i)) ? true : false);
		}
		return !result;
	}
	/** addEndToken - Aggiunge un fine linea
	 * 
	 */
	private void addEndToken(String token)
	{
		listaEndTokens.add(token);
	}
	/**
	 * Rimuove un fine linea.
	 * @param token
	 */
	private void removeEndToken(String token)
	{
		listaEndTokens.remove(token);
	}
	
	/**
	 * Prende il prossimo Token, e lo sposta avanti l'indice
	 * @return Token
	 */
	private Token nextToken()
	{
		Token t = listaTokens.get(indice);
		indice++;
		return t;
		
	}
	/**
	 * Parsa un'istruzione per volta, e ritorna un oggetto di tipo istruzione.
	 * @return
	 * @throws InstructionSyntaxErrorException
	 */
	private Istruzione parse() throws InstructionSyntaxErrorException
	{
			Istruzione istruzione = null;
			/*
	 		*Caso 1: E' un puntatore. 
	 		*/
			if(getNextToken().getTipo().equals("INTERO"))
			{
				//Controllo se l'ultima istruzione non sia istanza di puntatore. Se si emette un eccezione.
				if(ProgrammaEseguibile.listaIstruzioni.size()>0)
				{
					if(ProgrammaEseguibile.listaIstruzioni.get(ProgrammaEseguibile.listaIstruzioni.size()-1) instanceof Puntatore)
					{
						//TODO: sytaxError: due interi di seguito?
						throw new InstructionSyntaxErrorException("SyntaxError: two integer in a row. That is nonsense!");
					}
				}
				
				istruzione = new Puntatore(Integer.parseInt(getNextToken().getContenuto()));
				
			}	
			//Ricorda NEO: se non sei uno di noi, sei uno di loro
			/*
			 * Caso 2: è un if.
			 * 
			 */
				
			else if(getNextToken().getTipo().equals("IF"))
			{
					//Butto il token IF che non mi serve più
					nextToken();
					//e richiamo il parser apposito.
					istruzione = parseIf();
			}
			/*
			 * Caso 3: e' un assegnamento di Variabile
			 */
			else if(getNextToken().getTipo().equals("REM"))
			{
				ArrayList<Token> commento = new ArrayList<Token>();
				removeEndToken(":");
				while(nextTokenIsNotEnd())
				{
					commento.add(nextToken());
				}
				addEndToken(":");
				istruzione = new REM(commento);
			}
			else if(getNextToken().getTipo().equals("VARIABILE"))
			{
				Token nomeVariabile = nextToken();
				if(getNextToken().getTipo().equals("UGUALE")){
					nextToken();
				}
				else
				{
					throw new InstructionSyntaxErrorException("SyntaxError: you must use ugual to assign a variable. You provided \" "+ getNextToken().getTipo() +" \"");	
				}
				ArrayList<Token> tokens = new ArrayList<Token>();
				while(nextTokenIsNotEnd())
				{
					tokens.add(nextToken());
				}
				istruzione = new AssegnamentoVariabile(nomeVariabile, tokens);
			}
			/*
			 *Caso 4: e' un for 
			 */
			else if(getNextToken().getTipo().equals("FOR"))
			{
				//Butto il token IF che non mi serve pi�
				nextToken();
				//e richiamo il parser apposito.
				istruzione = parseFor();
			}
			/*
			 * Caso 5: Caso generale.
			 */
			else
			{
				/*	Tolti i casi particolari, rimane quello generale.
				*	Se entro qui' vuol dire che mi ritrovo una istruzione nella forma:
				*	ISTRUZIONE PARAMETRO \n
				*/
				try
				{
					Class<?> classeIstr = Class.forName("it.uniroma1.sapy.runtime.istruzioni." + getNextToken().getContenuto());
					Class<? extends Istruzione> ia = classeIstr.asSubclass(Istruzione.class);
					Constructor<?>[] constr0 = ia.getConstructors();
					ArrayList<Token> argomenti = new ArrayList<Token>();
					nextToken(); //questo non mi serve più.
					while(nextTokenIsNotEnd())
					{
						argomenti.add(nextToken());
					}
					//Devo prendere tutto ciò che segue fino all'end e buttarlo dentro al costruttore.
					try
					{
					istruzione = (Istruzione) constr0[0].newInstance(argomenti);
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						System.err.println("Ti sei ricordato di rendere public il costruttore dell' istruzione? :)");
						e.printStackTrace();
					}
				}
				catch(ClassNotFoundException e)
				{
					System.err.println("Instruction not found.");
					e.printStackTrace();
				}
				catch(SecurityException e)
				{
					e.printStackTrace();
				}
				catch (InstantiationException e) 
				{
					e.printStackTrace();
				} 
				catch (IllegalAccessException e) 
				{
					e.printStackTrace();
				} 
				catch (IllegalArgumentException e) 
				{
					e.printStackTrace();
				} 
				catch (InvocationTargetException e) 
				{
					e.printStackTrace();
				}
			}
		
		return istruzione;
		
	}
	/**
	 * Metodo per parsare il FOR.
	 * Ritorna un'istruzione di tipo FOR.
	 * @throws InstructionSyntaxErrorException 
	 */
	
	private Istruzione parseFor() throws InstructionSyntaxErrorException //throws SyntaxErrorThenNotFoundException
	{
		addEndToken("TO");
		Istruzione assegnamento = parse(); //Il controllo viene fatto dall' oggetto Istruzione For
		removeEndToken("TO");
		
		if(getNextToken().getTipo().equals("TO"))
		{
			nextToken();
		}
		else
		{
			throw new InstructionSyntaxErrorException("FOR Syntax Error: TO after Assignment Excpeted");
		}
		//Mi prendo il valore finale come intero.
		Integer to =0;
		if(getNextToken().getTipo().equals("INTERO"))
		{
			//Mi tiro
			to = Integer.parseInt(nextToken().getContenuto());
		}
		else
		{
			throw new InstructionSyntaxErrorException("Syntax Error: For excpeted an integer.");
		}
		
		
		//Controllo se il prossimo token � do:
		if(getNextToken().getTipo().equals("DO"))
		{
			nextToken();
		}
		else
		{
			throw new InstructionSyntaxErrorException("FOR Syntax Error: For excpeted a DO after the TO integer.");
		}
		
		/*
		 * Mi prendo le istruzioni da eseguire:
		 */
		ArrayList<Istruzione> istruzioniDaEseguire = new ArrayList<Istruzione>();
		
		//Aggiungo l'endToken:
		addEndToken("NEXT");
		
		while(!(getNextToken().getTipo().equals("NEXT")))
		{			
			if(!nextTokenIsNotEnd())  //Se il prossimo token e' finelinea, lo butto.
			{
				nextToken();
			}
			else
			{
				// altrimenti richiamo il parse()
				Istruzione i = parse();
				istruzioniDaEseguire.add(i);
			}
		}
		//Butto NEXT
		nextToken();
		//Rimuovo l'endToken
		removeEndToken("NEXT");
		return new FOR(assegnamento, to, istruzioniDaEseguire);
	}
	/**
	 * Metodo per parsare gli IF
	 * @return Istruzione IF
	 * @throws InstructionSyntaxErrorException 
	 * @throws SyntaxErrorThenNotFoundException
	 */
	private Istruzione parseIf() throws InstructionSyntaxErrorException
	{
		ArrayList<Token> condizione = new ArrayList<Token>(); //ArrayList di token che conterr� la condizione da valutare
		ArrayList<Istruzione> istruzioniDaEseguire = new ArrayList<Istruzione>(); //ArrayList di Istruzione che conterr� le istruzioni da eseguire in caso di verifica della condizione
		try
		{
			while(!getNextToken().getTipo().equals("THEN"))
			{
				//Finch� non trovo il THEN, aggiungo i tokens alla lista per la mia collezione
				condizione.add(nextToken());
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			throw new InstructionSyntaxErrorException("Did you remember THEN after the IF's condition?\n");
			
		}
		//Ora sono arrivato al then quindi lo salto.
		//(Sono sicuro che � THEN perch� condizione di uscita dal while precedente.)
		nextToken();
		
		//E mi prendo la lista di istruzioni in caso la condizione viene verificata.
		//Mi aggiungo il mio endToken per l'if:
		addEndToken("ENDIF");
		
		try
		{
			while(!getNextToken().getTipo().equals("ENDIF"))
			{
				//Purtroppo devo fare il controllo per vedere se non st� guardando un finelinea :S
				if(!nextTokenIsNotEnd()) 
				{
						nextToken();
				}
				else
				{
					//Richiamo il parser parse() - mutua ricorsione
					Istruzione i = parse();
					//e mi salvo l'istruzione.
					istruzioniDaEseguire.add(i);
				}
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			throw new InstructionSyntaxErrorException("Did you remember ENDIF after the IF's list of instructions?\n");
		}
		//Butto l' ENDIF
		nextToken();
		//Rimuovo l'endToken:
		removeEndToken("ENDIF");
		return new IF(condizione, istruzioniDaEseguire);
	}
}
