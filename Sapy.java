package it.uniroma1.sapy;

import it.uniroma1.sapy.lexer.Lexer;
import it.uniroma1.sapy.parsing.Parser;
import it.uniroma1.sapy.runtime.ProgrammaEseguibile;
import it.uniroma1.sapy.runtime.istruzioni.InstructionSyntaxErrorException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sapy 
{
	public static int programCounter = 0;
	public static void setProgramCounter(int s)
	{
		programCounter = s;
	}
	public static int getProgramCounter()
	{
		return programCounter;
	}
	public static void main(String[] args)
	{
		File f = new File (args[0]);
		try
		{
			String s = "";
			@SuppressWarnings("resource")
			Scanner in = new Scanner(f);
			while(in.hasNextLine())
			{
				s += in.nextLine();
				s += "\n";
			}
			Lexer lex = new Lexer(s);
			/*lex.printListaString();
			for ( int i= 0; i< lex.getListaTokens().size(); i++){
				System.out.println(lex.getListaTokens().get(i).getContenuto());
			}*/
			@SuppressWarnings("unused")
			Parser parser = new Parser(lex);
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
			/*			
			for(int z = 0; z <= ProgrammaEseguibile.listaVariabili.size()-1; z++){
				System.out.println(ProgrammaEseguibile.listaVariabili.get(z).getName() + ProgrammaEseguibile.listaVariabili.get(z).getValore());
			}*/
		}
		catch(FileNotFoundException e)
		{
			System.err.println("File Not Found: ");
			 e.printStackTrace();
		}
		System.exit(0);
	}
	
	
}
