package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.Token;
import it.uniroma1.sapy.runtime.ProgrammaEseguibile;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Funzione INPUT
 * Prende in input una stringa e la inserisce in una variabile
 * TODO: Eccezioni.
 */
public class INPUT implements Istruzione
{
	String nomeVariabile;
	public INPUT(ArrayList<Token> to)
	{
		if(to.size() == 1)
		{
			Token t = to.get(0);
			if(t.getTipo() == "VARIABILE")
			{
				nomeVariabile = t.getContenuto();
			}else{
				//TODO: Syntax Error: Variable expeted.
				System.out.println("Syntax Error: Variable expeted.");
			}
		}else{
			System.out.println("SyntaxError: too much Parameters.");
			//TODO: SyntaxError: too much Parameters.
		}
	}
	
	@Override
	public void esegui() 
	{
		
		@SuppressWarnings("resource")
		String input = new Scanner(System.in).next();
		
		for(int i = 0; i<ProgrammaEseguibile.listaVariabili.size(); i++)
		{
			Variabile<?> var = ProgrammaEseguibile.listaVariabili.get(i);
			if( var.getName().equals(nomeVariabile)){
				ProgrammaEseguibile.listaVariabili.remove(i);
			}
		}
		ProgrammaEseguibile.listaVariabili.add(new Variabile<String>(nomeVariabile, input));
		
	}

}
