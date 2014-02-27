package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.Token;

import java.util.ArrayList;

public class PRINT implements Istruzione
{
	ArrayList<Token> t = new ArrayList<Token>();
	String stringa = "";
	public PRINT(ArrayList<Token> to)
	{
		t = to;
	}
	
	public void esegui() throws InstructionSyntaxErrorException
	{
		//Se stò cercando di stampare una stringa:
		if(t.get(0).getTipo() == "STRINGA") 
		{
			//Deve essere un solo token:
			if(t.size() == 1)
			{
				stringa = t.get(0).getContenuto();
			}else{
				throw new InstructionSyntaxErrorException("PRINT: Syntax Error: C'è qualcosa di troppo");
			}
				
		}else{
			//Altrimenti per interi e booleani utilizzo ExpressionEvaluator.
			//Gli passo la lista di tokens:
			ExpressionEvaluator expr = new ExpressionEvaluator(t);
			//Mi salvo il valore come stringa.
			stringa = expr.getValoreString();
			
		}
		if(stringa.length() >= 1){
			System.out.println(stringa);
		}
	}
}
