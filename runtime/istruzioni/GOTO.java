package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.Sapy;
import it.uniroma1.sapy.lexer.Token;
import it.uniroma1.sapy.runtime.Interprete;
import it.uniroma1.sapy.runtime.ProgrammaEseguibile;

import java.util.ArrayList;

/**
 * Istruzione GOTO
 * @author Federico Ponzi
 * Permette di saltare ad un punto all'altro del codice utilizzando delle etichette numeriche.
 */
public class GOTO implements Istruzione
{
	private int puntatore;
	public GOTO(ArrayList<Token> listaToken)
	{
		//Si aspetta solo un parametro:
		if(listaToken.size() == 1)
		{
			Token to = listaToken.get(0);
			if(to.getTipo() == "INTERO")
			{
				puntatore = Integer.parseInt(to.getContenuto());
			}
			else
			{
			//SyntaxError: Integer Excpeted
			
			}	
		}else{
			//InputError: Too Much Parameters. One Excpeted.
		}
	}
	
	private int cercaPuntatore()
	{
		for(int i = 0; i< ProgrammaEseguibile.listaIstruzioni.size(); i++)
		{
			Istruzione istr = ProgrammaEseguibile.listaIstruzioni.get(i);
			//Se l'istruzione è di tipo Puntatore
			if(istr instanceof Puntatore)
			{
				//La casto
				Puntatore punt = ((Puntatore) istr);
				//E vedo se è il mio uomo.
				if( ((Integer)punt.getPuntatore()).equals(puntatore))
				{
					//Se lo e', mando come output la sua posizione.
					return i;	
				}
			}
		}
		//Altrimenti, -1 :(
		return -1;
	}
	
	@Override
	public void esegui() throws InstructionSyntaxErrorException 
	{

			int s = cercaPuntatore();
			if(s == -1)
			{
				throw new InstructionSyntaxErrorException("Input error: Pointer Not found.");
			}
			else
			{
				Interprete.programCounter = s;
				Sapy.setProgramCounter(s);
			}
	}
	
}
