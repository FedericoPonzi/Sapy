package it.uniroma1.sapy.runtime;

import it.uniroma1.sapy.lexer.Lexer;
import it.uniroma1.sapy.parsing.Parser;
import it.uniroma1.sapy.runtime.istruzioni.InstructionSyntaxErrorException;

import java.util.Scanner;

/**
 * <h2>Interprete</h2>
 * Programma che permette di eseguire tutte le istruzioni implementate in sapy da consolle
 * @author Federico
 * */
public class Interprete 
{
	//Mi serve per il GOTO
	public static int programCounter = 0;
	
	public static void main(String[] args)
	{
		Lexer lex;
		@SuppressWarnings("unused")
		Parser parse;
		//Posso fare un ciclo infinito in quanto si aspetta end per terminare il programma.
		while(true)
		{
			@SuppressWarnings("resource")
			String istruzione = new Scanner(System.in).nextLine(); //Chiedo una stringa
			//Tokenizzo col lexer
			lex = new Lexer(istruzione);
			//Parso col parser
			parse = new Parser(lex);
			//Spudoratamente copiato da Sapy:
			while(programCounter < ProgrammaEseguibile.listaIstruzioni.size())
			{
				try
				{
					ProgrammaEseguibile.listaIstruzioni.get(programCounter).esegui();	
				}
				catch(InstructionSyntaxErrorException e)
				{
					e.printStackTrace();
				}
				programCounter++;
			}
		}
		
	}
}
