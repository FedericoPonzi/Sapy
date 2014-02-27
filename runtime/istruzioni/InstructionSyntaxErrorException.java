package it.uniroma1.sapy.runtime.istruzioni;

@SuppressWarnings("serial")
public class InstructionSyntaxErrorException extends Exception 
{
	public InstructionSyntaxErrorException(String message) 
	{
		System.err.println(message);
	}
}
