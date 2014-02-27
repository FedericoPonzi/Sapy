package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.Token;
import it.uniroma1.sapy.runtime.ProgrammaEseguibile;

import java.util.ArrayList;

public class AssegnamentoVariabile implements Istruzione
{
	String nomeVariabile;
	String assegnamentoString;
	int assegnamentoInt;
	boolean assegnamentoBool;
	int tipo;
	ArrayList<Token> listaToken;
	
	public String getNomeVariabile(){
		return nomeVariabile;
	}
	public AssegnamentoVariabile(Token variabile,ArrayList<Token> listaToken) 
	{
		nomeVariabile = variabile.getContenuto();
		this.listaToken = listaToken;
	}
	
	@Override
	public void esegui() throws InstructionSyntaxErrorException 
	{
		
			//Controllo se cerca di assegnare una stringa.
			if(listaToken.get(0).getTipo() == "STRINGA")
			{
				//Se si, deve esserci solo la stringa dopo l'uguale.
				if(listaToken.size() == 1)
				{
					assegnamentoString = listaToken.get(0).getContenuto();
					tipo = 0;
				}
				else
				{
					throw new InstructionSyntaxErrorException("AssegnamentoVariabile Syntax Error: per assegnare la variabile, deve esserci solo la stringa dopo l'uguale.");	
				}
			}
			else
			{
				//Se non cerca di assegnare una stringa, potrebbe essere un numero o un booleano.
				
				//TODO: remove EOL token
				//Creo l'expression evaluator
				ExpressionEvaluator expr = new ExpressionEvaluator(listaToken);
				
				try
				{
					assegnamentoInt = Integer.parseInt(expr.getValoreString());
					tipo = 1;
				}
					catch(NumberFormatException e)
				{
					if(expr.getValoreString().equalsIgnoreCase("true") || expr.getValoreString().equalsIgnoreCase("false"))
					{
						assegnamentoBool = Boolean.parseBoolean(expr.getValoreString());
						tipo = 2;
					}
					else
					{
						throw new InstructionSyntaxErrorException("QUALCOSA E' ANDATO STORTO.: assegnamento variabile");	

					}
				}
		
		}
		

		for(int i = 0; i< ProgrammaEseguibile.listaVariabili.size(); i++)
		{
			Variabile<?> var = ProgrammaEseguibile.listaVariabili.get(i);
			if( var.getName().equals(nomeVariabile)){
				ProgrammaEseguibile.listaVariabili.remove(i);
			}
		}
		switch(tipo)
		{
			//Stringa:
			case 0: ProgrammaEseguibile.listaVariabili.add(new Variabile<String>(nomeVariabile, assegnamentoString));
				break;
			//Intero:
			case 1:  ProgrammaEseguibile.listaVariabili.add(new Variabile<Integer>(nomeVariabile, assegnamentoInt));;
				break;
			//Booleano:
			case 2:  ProgrammaEseguibile.listaVariabili.add(new Variabile<Boolean>(nomeVariabile, assegnamentoBool));;
				break;
		}

		
	}

}
