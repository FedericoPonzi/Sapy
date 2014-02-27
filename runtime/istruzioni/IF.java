package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.Token;
import java.util.ArrayList;

public class IF implements Istruzione
{
	ArrayList<Token> condizioneDaValutare;
	Boolean condizione;
	ArrayList<Istruzione> istruzioni;
	public IF(ArrayList<Token> condizioneDaValutare, ArrayList<Istruzione> istruzioni) 
	{
		this.condizioneDaValutare = condizioneDaValutare;
		this.istruzioni = istruzioni;
	}

	@Override
	public void esegui() throws InstructionSyntaxErrorException 
	{
		ExpressionEvaluator espressioneBooleana = new ExpressionEvaluator(condizioneDaValutare);
		condizione = espressioneBooleana.getValoreBooleano();
		
		if(condizione){
			for(int i=0; i<istruzioni.size(); i++)
			{
				istruzioni.get(i).esegui();
			}
		}
	}

}
