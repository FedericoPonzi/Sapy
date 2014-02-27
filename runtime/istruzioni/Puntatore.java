package it.uniroma1.sapy.runtime.istruzioni;

public class Puntatore implements Istruzione
{
	int puntatore;
	
	public Puntatore(int i){
		this.puntatore = i;
	}
	
	public int getPuntatore()
	{
		return puntatore;
	}
	@Override
	public void esegui() 
	{
		
	}

}
