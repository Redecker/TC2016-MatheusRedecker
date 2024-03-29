package AHTN;

import java.util.ArrayList;

public class Plano{
	
	private ArrayList<String> plano;
	private double custo;
	private EstadoDoJogo meuEstado;
	private EstadoDoJogo inimigoEstado;
	
	public Plano(){
		plano = new ArrayList<>();
	}
	
	public void addOperacao(String o){
		plano.add(o);
	}
	
	public void setCusto(Double c){
		custo = c;
	}
	
	public double getCusto(){
		return custo;
	}
	
	public String getOperacaoPonteiro(int p){
		if(p < 0 || p > plano.size()-1) return null;
		return plano.get(p);
	}
	
	public void setMeuEstadoJogo(EstadoDoJogo edj){
		meuEstado = edj;
	}
	
	public EstadoDoJogo getMeuEstadoJogo(){
		return meuEstado;
	}
	
	public void setInimigoEstadoJogo(EstadoDoJogo edj){
		inimigoEstado = edj;
	}
	
	public EstadoDoJogo getInimigoEstadoJogo(){
		return inimigoEstado;
	}
	public String toString(){
		String toReturn = "";
		for(String s : plano){
			toReturn += s + " \n"; 
		}
		return toReturn;
	}
}