package it.uniroma1.sapy.lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer
{
	/**
	 * Lista dei tokens
	 */
	private ArrayList<Token> listaTokens = new ArrayList<Token>();

	public Lexer(String input)
	{
		String[] arrayIstruzioni = input.split("\n");

		for (int i = 0; i < arrayIstruzioni.length; i++)
		{
			tokenizza(arrayIstruzioni[i].trim());
			listaTokens.add(new Token(Token.TipoToken.EOL, "EOL"));
		}
	}

	/**
	 * <h2>Tokenizza</h2> Questa funzione riceve in input una stringa di
	 * istruzioni, e la aggiunge al campo listaTokens
	 * 
	 * @param istruzioni
	 * @return void
	 */

	public void tokenizza(String istruzioni)
	{

		for (int i = 0; i < istruzioni.length(); i++)
		{

			// Senza ripetere la funzione charAt tutte le volte, per
			// semplicitï¿½ e pulizia mi creo una variabile:
			char carattereConsiderato = istruzioni.charAt(i);
			// Caso 1: Lo spazio
			if (carattereConsiderato == ' ')
			{ // se ï¿½ uno spazio non ho nulla da farci e quindi risparmio
				// tempo e continuo.
				continue;
			}
			// Caso 2: Comincia con $
			else if (carattereConsiderato == '$')
			{
				/*
				 * Ho trovato l' inizio di una variabile. Trovo la fine del nome
				 * usando questa regex: [^A-Za-z0-9_] ^ significa diverso dï¿½
				 * A-za-Z sono i caratteri da a fino z sia maiuscoli che
				 * minuscoli 0-9 si intende i numeri L'underscore ï¿½ ammesso
				 * quindi viene incluso nel nome della variabile.
				 */
				int endVar = getPosFromRegex(istruzioni.substring(i + 1),
				        "[^A-Za-z0-9_]");
				// Mi trovo la stringa del nome:
				String nomeVariabile = istruzioni.substring(i, i + endVar + 1);
				// Aggiungo il token alla lista dei tokens.
				listaTokens.add(new Token(Token.TipoToken.VARIABILE,
				        nomeVariabile));
				// Aggiorno il puntatore:
				i += endVar + 1;
			}
			// Caso 3: E' un intero
			else if (Character.isDigit(carattereConsiderato))
			{
				int fineNumero = getPosFromRegex(istruzioni.substring(i),
				        "[^0-9]"); // Forse i +1 ï¿½ da vedere.
				listaTokens.add(new Token(Token.TipoToken.INTERO, Integer
				        .parseInt(istruzioni.substring(i, i + fineNumero))));
				i += fineNumero - 1; // -1 perchÃ¨ la lunghezza Ã¨ diversa dalla
				                     // posizione. Tipo 3+3, trovato il 3
				                     // aggiunge 1 e il prossimo ciclo i viene
				                     // automaticamente incrementato. Quindi
				                     // salta il +

			}
			// Caso 4.1: E' un simbolo =
			else if (carattereConsiderato == '=')
			{
				if (getLastToken().getContenuto() == ">")
				{
					removeLastToken();
					listaTokens.add(new Token(Token.TipoToken.MAGGIOREUGUALE,
					        ">="));
				}
				else if (getLastToken().getContenuto() == "<")
				{
					removeLastToken();
					listaTokens.add(new Token(Token.TipoToken.MINOREUGUALE,
					        "<="));

				}
				else
				{
					listaTokens.add(new Token(Token.TipoToken.UGUALE, "="));
				}
			}
			// Caso 4.2: E' un simbolo >.
			else if (carattereConsiderato == '>')
			{
				if (getLastToken().getContenuto() == "<")
				{
					removeLastToken();
					listaTokens.add(new Token(Token.TipoToken.DIVERSO, "<>"));
				}
				else
				{
					listaTokens.add(new Token(Token.TipoToken.MAGGIORE, ">"));
				}
			}
			// Caso 5: Stò guardando una stringa, compresa fra due doppi apici.
			else if (carattereConsiderato == '"')
			{
				int fineStringa = getPosFromRegex(istruzioni.substring(i + 1),
				        "\"");
				listaTokens.add(new Token(Token.TipoToken.STRINGA, istruzioni
				        .substring(i + 1, i + fineStringa + 1)));
				i += fineStringa + 2;
			}
			// Caso 6: Tutto il resto può essere gestito in questo caso.
			else
			{
				// Cerco se fa parte delle mie istruzioni
				int fineIstruzione = getPosFromRegex(istruzioni.substring(i),
				        "[^A-Za-z_]"); // Trovo la fine dell'istruzione o del
				                       // testo comunque
				// In questo caso, includiamo anche gli operatori aritmetici.
				// fineIstruzione però, si ferma non appena li incontra:
				// prendendo come primo carattere proprio un simbolo
				// Ritornera'  0. Per tokenizzarlo, imposto fineIstruzione = 1.
				if (fineIstruzione == 0)
				{
					fineIstruzione = 1;
				}

				String confrontoTipoToken = istruzioni.substring(i, i
				        + fineIstruzione); // Prendo il testo (es: PRINT o
				                           // INPUT)
				// Vedo se e' un valore booleano.
				// Posso inserire anche in i valori in minuscolo, li
				// controllera' ugualmente.
				if (confrontoTipoToken.equalsIgnoreCase("TRUE")
				        || confrontoTipoToken.equalsIgnoreCase("FALSE"))
				{
					listaTokens.add(new Token(Token.TipoToken.BOOLEANO, Boolean
					        .parseBoolean(confrontoTipoToken)));

				}
				else
				{
					// se non ï¿½ un booleano, allora cerco fra le istruzioni
					// che conosco:
					if (Token.TipoToken.contains(confrontoTipoToken.toString())) // Controllo
					                                                             // se
					                                                             // ï¿½
					                                                             // presente
					                                                             // fra
					                                                             // la
					                                                             // mia
					                                                             // lista
					                                                             // di
					                                                             // Tokens
					{// Se ï¿½ fra le istruzioni che conosco, aggiungo il token.
						Token.TipoToken tipoToken = Token.TipoToken
						        .getTipoByValue(confrontoTipoToken);
						listaTokens.add(new Token(tipoToken, tipoToken
						        .getName()));

					}
					else
					{
						// Se non ï¿½ presente allora sarï¿½ di tipo FUNZIONE.
						listaTokens.add(new Token(Token.TipoToken.FUNZIONE,
						        confrontoTipoToken));
					}

				}
				i = i + fineIstruzione - 1;
			}
		}
	}

	/**
	 * <h2>PrintListaString()</h2> Stampa a video il nome dei tokens.
	 */
	public void printListaString()
	{
		for (int i = 0; i < listaTokens.size(); i++)
		{
			// Avrei voluto usare il foreach
			// Ma cosÃ¬ l'ultimo token non avrÃ  la virgola. OCD.
			Token t = listaTokens.get(i);
			if (i == listaTokens.size() - 1)
			{
				System.out.print(t.getTipo());
			}
			else
			{
				System.out.print(t.getTipo() + ",");
			}
		}
	}

	/**
	 * Rimuove l'ultimo token
	 */
	private void removeLastToken()
	{
		listaTokens.remove(listaTokens.size() - 1);
	}

	/**
	 * Ritorna l'ultimo tokens senza rimuoverlo dalla lista.
	 * 
	 * @return Token UltimoToken
	 */
	private Token getLastToken()
	{
		return listaTokens.get(listaTokens.size() - 1);
	}

	/**
	 * Restituisce la lista dei tokens
	 */
	public ArrayList<Token> getListaTokens()
	{
		return listaTokens;
	}

	/**
	 * getPosFromRegex Ritorna l'indice della prima occorrenza della stringa
	 * data in input, utilizzando la regex data in input.
	 * 
	 * @param stringa
	 * @return
	 */
	private int getPosFromRegex(String stringa, String regex)
	{
		int index = stringa.length();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(stringa);
		if (m.find())
		{
			index = m.start();
		}

		return index;
	}

	/**
	 * Se richiamato da consolle, il lexer prende in input un file .txt e ne
	 * restituisce i tokens.
	 */
	public static void main(String[] args)
	{
		/*
		 * TODO: cambiare file.txt a args[0].
		 */
		File f = new File("C:\\Users\\Federico\\Desktop\\prog.txt");
		try
		{
			String s = "";
			@SuppressWarnings("resource")
			Scanner in = new Scanner(f);

			while (in.hasNextLine())
			{
				s += in.nextLine();
				s += "\n";
			}
			Lexer lex = new Lexer(s);
			/*
			 * for(int z = 0; z < lex.getListaTokens().size(); z++) {
			 * System.out.println(lex.getListaTokens().get(z).getContenuto()); }
			 */
			lex.printListaString();

		}
		catch (FileNotFoundException e)
		{
			System.err.println("File Not Found: ");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			System.err.println("ERRORE");
			e.printStackTrace();
		}
	}
}
