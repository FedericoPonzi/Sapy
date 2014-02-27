package it.uniroma1.sapy.runtime.istruzioni;


import it.uniroma1.sapy.lexer.Token;
import it.uniroma1.sapy.runtime.ProgrammaEseguibile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



//Parser Ricorsivo Discendente.
public class ExpressionEvaluator
{
	ArrayList<Token> tokens = new ArrayList<Token>();
	int pointer = 0;
	String result;
	ExpressionEvaluator(ArrayList<Token> s ) throws InstructionSyntaxErrorException
	{
		this.tokens = s;
		this.result = (exp());
	    if(pointer < s.size())
	    {
	    	throw new InstructionSyntaxErrorException("ExpressionEvaluator C'e' qualcosa di troppo.");
	    }
	}

	public boolean getValoreBooleano() throws InstructionSyntaxErrorException
	{
		boolean s = false;
		try
		{
			 s = Boolean.parseBoolean(result);
		}
		catch(Exception e)
		{
			throw new InstructionSyntaxErrorException("Non e' un booleano.");
		}
		return s;
	}
	
	public String getValoreString()
	{
		return result;
	}
	public Integer getValoreIntero() throws InstructionSyntaxErrorException
	{
		int s = 0;
		try
		{
			 s = Integer.parseInt(result);
		}
		catch(Exception e)
		{

			e.printStackTrace();
			throw new InstructionSyntaxErrorException("ExpressionEvaluator: integer Excepted.");
		}
		return s;
	}	
	
	
	
	private Token peek(){
		if(pointer == tokens.size())
		{
			return new Token(Token.TipoToken.FUNZIONE, "FUNZIONE");
		}
		return tokens.get(pointer);
	}
	private void consume()
	{
		pointer +=1;
	}
	
	//Si parte!
	
	public String exp() throws InstructionSyntaxErrorException
	{
		String x = and_exp();
		while (peek().getTipo() == "OR")
		{
			consume();
			String y = and_exp();
			x = OR(x,y);
		}
		return x;
	}
	private String OR(String x, String y)
	{
		return ((Boolean)(Boolean.parseBoolean(x) || Boolean.parseBoolean(y))).toString();
	}
	

	public String and_exp() throws InstructionSyntaxErrorException
	{
		String x = cmp_exp();
		while(peek().getTipo() == "AND")
		{
			consume();
			String y = cmp_exp();
			x = AND (x,y);
		}
		return x;
	}	
	private String AND(String x, String y)
	{
		return ((Boolean)(Boolean.parseBoolean(x) && Boolean.parseBoolean(y))).toString();
	}

	public String cmp_exp() throws InstructionSyntaxErrorException{
		String x = int_exp();
		String[] compare = {"<>","=","<",">","<=",">="};		
		List<String> s =  Arrays.asList(compare);
		while(s.contains(peek().getContenuto()))
		{
			Token simbolo = peek();
			consume();
			String y = int_exp();
			Boolean p = compara(simbolo.getContenuto(), x,y);
			x = p.toString();
		}
		return x;
	}
	public Boolean compara(String simbolo, String x1, String y1) throws InstructionSyntaxErrorException
	{
		//Funzione che esegue la comparazione
		try
		{
			
			Integer x = Integer.parseInt(x1);
			Integer y = Integer.parseInt(y1);
			switch (simbolo)
			{
				case "=": return x==y;
				case "<": return x<y;
				case ">": return x>y;
				case "<=": return x<=y;
				case ">=": return x>=y;
				case "<>": return x!= y;
				default: return false;
			}
		}
		catch(NumberFormatException e)
		{
			try
			{
				boolean x = Boolean.parseBoolean(x1);
				boolean y = Boolean.parseBoolean(y1);
				switch (simbolo)
				{
					case "=": return x==y;
					case "<>": return x != y;
					default: return false;
				}	
			}
			catch( Exception ex)
			{
				throw new InstructionSyntaxErrorException("Something strange happened. ExpressionEvaluator");
			}
		}
	}
	
	public String int_exp() throws InstructionSyntaxErrorException
	{
		String x = mul_term();
		while(peek().getContenuto() == "+" || peek().getContenuto() == "-")
		{
			Token segno = peek();
			consume();
			//String y = int_exp();
			String y = mul_term();
			x = intex(segno.getContenuto(), x, y).toString();
		}
		return x;
	}
	
	//Risolve una espressione di due interi formata da + o -
	public Integer intex(String segno, String x1, String y1)
	{
		Integer x = Integer.parseInt(x1);
		Integer y = Integer.parseInt(y1);
		switch (segno)
		{
			case "+": return x+y;
			case "-": return x-y;
			default: return 1;
		}
	}
	
	
	public String mul_term() throws InstructionSyntaxErrorException{
		String x = simple_term();
		while(peek().getContenuto() == "*" || peek().getContenuto().equals("/") || peek().getContenuto().equals( "%"))
		{
			Token segno = peek();
			consume();
			String y = simple_term();
			x = mul(segno.getContenuto(), x, y).toString();
		}
		return x;
	}
	//Risolve una espressione di due interi formata da * o / o %
	public Integer mul(String segno, String x1, String y1) throws InstructionSyntaxErrorException
	{
		Integer x = Integer.parseInt(x1);
		Integer y = Integer.parseInt(y1);
		switch (segno)
		{
			case "*": return x*y;
			case "/": return x/y;
			case "%": return x%y;
		}
		throw new InstructionSyntaxErrorException("ExpressionEvaluator: mul: something went wrong.");

	}
	
	public String simple_term() throws InstructionSyntaxErrorException
	{
		
		if(peek().getContenuto() == "(")
		{
			consume();
			String result = exp();
			consume();
			return result;
		}		
		else if(is_term(peek().getTipo()))
		{
			Token s = peek();
			consume();
			return iterm(s);
		}
		throw new InstructionSyntaxErrorException("ExpressionEvaluator: \""+ peek().getContenuto() + "\": Is not a term.");		
	}
	
	
	public String iterm(Token termine) throws InstructionSyntaxErrorException
	{
		if(termine.getTipo() == "VARIABILE")
		{		
			if(ProgrammaEseguibile.getVariabilePresente(termine.getContenuto()))
			{
				return ProgrammaEseguibile.getVariabile(termine.getContenuto()).getValore().toString();
			}else{
				
				throw new InstructionSyntaxErrorException("ExpressionEvaluator: \""+ termine.getContenuto()+"\" Variable not Inizialized");
			}
		}
		else if(termine.getTipo() == "INTERO")
		{
			return termine.getContenuto().toString();
		}
		else if(termine.getTipo() == "MENO")
		{
				String res = simple_term();
				Integer result = - Integer.parseInt(res);
				return result.toString();
			
		}
		else if(termine.getTipo() == "BOOLEANO")
		{
			return termine.getContenuto().toString();
		}
		else if(termine.getContenuto() == "NOT")
		{
			String ret = iterm(peek());
			Boolean s = (!Boolean.parseBoolean(ret));
			consume();
			return s.toString();
		}
		throw new InstructionSyntaxErrorException("ExpressionEvaluator Error: \" "+ termine.getContenuto() +" \" Iterm not found.");
	}

	/**
	 * Funzione booleana che dice se un tipo di termine e' o no un termine.
	 * @param t : il tipo del token
	 * @return boolean
	 */
	public boolean is_term(String t)
	{
		switch(t){
		case "INTERO": return true;
		case "BOOLEANO": return true;
		case "NOT": return true;
		case "STRINGA": return true;
		case "VARIABILE": return true;
		case "MENO" : return true;
		case "EOL": return false;
		}
		return false;
	}
}	
