package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.Token;

import java.util.ArrayList;

public class END implements Istruzione {

	public END(ArrayList<Token> s) throws InstructionSyntaxErrorException
	{
		if (s.size() > 0) throw new InstructionSyntaxErrorException("What are you doing with that exit instruction?");
	}
	@Override
	public void esegui() throws InstructionSyntaxErrorException 
	{
		System.exit(0);
	}

}