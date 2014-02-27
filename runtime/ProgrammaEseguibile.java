package it.uniroma1.sapy.runtime;

import it.uniroma1.sapy.runtime.istruzioni.InstructionSyntaxErrorException;
import it.uniroma1.sapy.runtime.istruzioni.Istruzione;
import it.uniroma1.sapy.runtime.istruzioni.Variabile;

import java.util.ArrayList;

public class ProgrammaEseguibile 
{

	
	public static ArrayList<Variabile<?>> listaVariabili = new ArrayList<Variabile<?>>();
	public static ArrayList<Istruzione> listaIstruzioni = new ArrayList<Istruzione>();
	
	
	//Metodo per aggiungere un'istruzione
	public void AggiungiIstruzione(Istruzione i)
	{
		listaIstruzioni.add(i);
	}
	/**
	 * Ritorna true se una variabile col nome dato in input è presente nella lista variabili.
	 * @param nomeVariabile
	 * @return booleano
	 */
	
	public static boolean getVariabilePresente(String nome)
	{
		for(int i= 0; i<listaVariabili.size(); i++)
		{
			if(listaVariabili.get(i).getName().equals(nome))
			{
				return true;
			}
		}
		return false;
	}
	public static Variabile<?> getVariabile(String nome)
	{
		if(getVariabilePresente(nome)){
			for(int i= 0; i<listaVariabili.size(); i++)
			{
				if(listaVariabili.get(i).getName().equals(nome))
				{
					return listaVariabili.get(i);
				}
			}
		}
		return null;
	}
	
	
	public void esegui() throws InstructionSyntaxErrorException
	{
		for(Istruzione i: listaIstruzioni)
		{
			i.esegui();
		}
	}
	
}