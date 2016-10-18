package AHTN;

public class Node{
	
	private Plano planMax;
	private Plano planMin;
	private int avaliacao;
	
	public Node(Plano pmax, Plano pmin, int avali){
		planMax = pmax;
		planMin	= pmin;
		avaliacao = avali;
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