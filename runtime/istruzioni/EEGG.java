package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.Token;

import java.util.ArrayList;

public class EEGG implements Istruzione
{
	public EEGG(ArrayList<Token> t)
	{
		
	}

	@Override
	public void esegui() 
	{
		System.out.println("Progetto Sapy - Metodologie di programmazione 2012/2013.");
	}

}
