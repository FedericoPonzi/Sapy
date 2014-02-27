package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.runtime.ProgrammaEseguibile;

import java.util.ArrayList;

public class FOR implements Istruzione {
	Istruzione assegnamento;
	Integer to;
	ArrayList<Istruzione> istruzioniDaEseguire;
	String nomeVariabile;
	
	public FOR(Istruzione assegnamento, Integer intero, ArrayList<Istruzione> istruzioniDaEseguire) throws InstructionSyntaxErrorException
	{
		//Mi assicuro che assegnamento sia di tipo Assegnamento Variabile.
		if(!(assegnamento instanceof AssegnamentoVariabile)) throw new InstructionSyntaxErrorException("Syntax Error: Assegnamento di Variabile Excepeted");//TODO: Syntax Error: Assegnamento di Variabile Excepeted
		//Se lo �, me lo salvo:
		this.assegnamento = assegnamento;
		this.to = intero;
		this.istruzioniDaEseguire = istruzioniDaEseguire;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void esegui() throws InstructionSyntaxErrorException
	{
		//First of all: assegno la variabile.
		try
		{
			assegnamento.esegui();
		}catch(InstructionSyntaxErrorException e){
			e.printStackTrace();
		}
		//Dopo aver assegnato la variabile, mi prendo il nome
		nomeVariabile = ((AssegnamentoVariabile) assegnamento).getNomeVariabile();
		//Me la recupero dalla lista delle variabili
		Variabile<?> s = ProgrammaEseguibile.getVariabile(nomeVariabile);
		Integer from = 0;
		//Eseguo il casting:
		Variabile<Integer> fromVar = null;
		if(s.getTipo().equals("Integer"))
		{
			from = ((Integer) s.getValore());
			fromVar = (Variabile<Integer>) ProgrammaEseguibile.getVariabile(nomeVariabile);
		}
		else
		{
			//Se non e' possibile, lancia un errore.
			throw new InstructionSyntaxErrorException("SyntaxError: Assignment of a Integer Excpeted;");
		}
		//Adesso ho il valore di partenza.
		//Ho dove devo arrivare.
		//Mi creo i due casi in cui l'arrivo sia minore o maggiore della partenza
		//from = 4 to = 7
		if(from < to)
		{
			//Caso 1: from < to
			
			int i = 0;
			for(i = from; i <= to; i++)
			{
				fromVar.setValore(i);
				//Mi prendo tutte le istruzioni e le eseguo.				
				for(int is = 0; is< istruzioniDaEseguire.size(); is++)
				{
					istruzioniDaEseguire.get(is).esegui();
				}
			}
			fromVar.setValore(i);
		}
		else if (from > to)
		{
			//Caso 2: from < to
			int i =from;
			while(i >= to )
			{
				fromVar.setValore(i);
				//Mi prendo tutte le istruzioni e le eseguo.				
				for(int is = 0; is< istruzioniDaEseguire.size(); is++)
				{
					istruzioniDaEseguire.get(is).esegui();
				}
				i--;
			}
			fromVar.setValore(i);

		}
		//Se from == to, il for non deve essere eseguito.
		
	}

}
