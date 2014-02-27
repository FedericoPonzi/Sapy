package it.uniroma1.sapy.runtime.istruzioni;

public class Variabile <T>
{
	T contenuto;
	String nomeVariabile;
	
	Variabile(String nomeVariabile, T contenuto){
		this.contenuto = contenuto;
		this.nomeVariabile = nomeVariabile;
	}
	
	//Metodi setter e getter:
	public T getValore()
	{
		return contenuto;
	}
	public void setValore(T contenuto)
	{
		this.contenuto = contenuto; 
	}
	
	public String getName()
	{
		return nomeVariabile;
	}
	

	public String getTipo()
	{
		//Se il tipo è Integer, ritorna Integer.
		return contenuto.getClass().getSimpleName();
	}
}
