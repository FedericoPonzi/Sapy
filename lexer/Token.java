package it.uniroma1.sapy.lexer;

public class Token 
{
	TipoToken tipoToken;
	String nomeToken;
	Object contenuto;
	
	public Token(TipoToken tipo, Object contenuto) 
	{
		this.tipoToken = tipo;
		this.contenuto = contenuto;
		
	}
	/**
	 *  Ritorna il tipo di token:
	 * @return String
	 */
	public String getTipo()
	{
		return tipoToken.toString();
	}
	/**
	 * Ritorna il contenuto.
	 * @return String
	 */
	public String getContenuto()
	{
		return contenuto.toString();
	}
		/**
		 * <h2>TipoToken</h2>
		 * Viene utilizzato solo da questa classe, quindi la utilizzo come classe interna.
		 * @author Federico
		 *
		 */
		public enum TipoToken 
		{
			INTERO ("-1"),
			STRINGA ("-1"),
			BOOLEANO ("-1"),
			UGUALE ("="),
			MAGGIORE(">"),
			MINORE ("<"),
			DIVERSO ("<>"),
			MINOREUGUALE ("<="),
			MAGGIOREUGUALE (">="),
			PIU ("+"),
			MENO ("-"),
			PER ("*"),
			DIVISO ("/"), 
			MODULO ("%"),
			LEFT_PAR ("("),
			RIGHT_PAR (")"),
			DUEPUNTI(":"),
			OR ("OR"),
			AND ("AND"),
			NOT ("NOT"),
			REM ("REM"),
			IF ("IF"),
			FOR ("FOR"),
			WHILE ("WHILE"),
			DO ("DO"),
			THEN ("THEN"),
			ELSE ("ELSE"),
			ENDIF ("ENDIF"),
			END ("END"),
			TO ("TO"),
			NEXT ("NEXT"),
			STEP ("STEP"),
			VARIABILE ("VARIABILE"),
			FUNZIONE ("FUNZIONE"),
			GOSUB ("GOSUB"),
			GOTO ("GOTO"),
			PRINT ("PRINT"),
			INPUT ("INPUT"),
			RETURN ("RETURN"),
			EOL ("\n");
			String s;
			private TipoToken(String s)
			{
				this.s= s;
			}
			public String getName()
			{
				return s;
			}
			
			/**
			 * Ritorna true se il valore in input � contenuto in uno dei valori dell' enum. Altrimenti false.
			 * @param p La Stringa da confrontare
			 * @return ret Booleano
			 */
			public static boolean contains(String test) 
			{
		
			    for (TipoToken t : TipoToken.values()) 
			    {
			        if (t.getName().equals(test)) 
			        {
			            return true;
			        }
			    }
			    return false;
			}
			/**
			 * Un metodo che, in base al valore dell'enum, ne ritorna il tipo. Viene utilizzato dal Lexer in modo da assegnare il giusto <br>
			 * enum ad ogni elemento.
			 * @param value Una stringa da analizzare
			 * @return t TipoToken
			 */
			public static TipoToken getTipoByValue(String value)
			{
				for (TipoToken t : TipoToken.values()) 
				{
			        if (value.equals(t.getName()))
			        {
			            return t;
			        }
			    }
				return null;
			}
		}	
}
