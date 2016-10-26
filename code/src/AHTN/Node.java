package AHTN;

public class Node{
	
	private Plano planMax;
	private Plano planMin;
	private int avaliacao;
	private String UltimaAcaoQueLevouAoPlano;
	
	public Node(Plano pmax, Plano pmin, int avali){
		planMax = pmax;
		planMin	= pmin;
		avaliacao = avali;
		UltimaAcaoQueLevouAoPlano = "";
	}
	
	public void setAcao(String acao){
		UltimaAcaoQueLevouAoPlano = acao;
	}
	
	public String getAcao(){
		return UltimaAcaoQueLevouAoPlano;
	}
	
	public int getAvaliacao(){
		return avaliacao;
	}
	
	public Plano getPlanoMin(){
		return planMin;
	}
	
	public Plano getPlanoMax(){
		return planMax;
	}
}