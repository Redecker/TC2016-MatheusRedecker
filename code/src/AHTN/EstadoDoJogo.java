package AHTN;

import rts.PhysicalGameState;
import rts.Player;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

public class EstadoDoJogo{
	static UnitTypeTable utt;
    static UnitType workerType;
    static UnitType baseType;
    static UnitType barracksType;
    static UnitType rangedType;
    static UnitType heavyType;
    static UnitType lightType;
	
	private int base;
	private int quartel;
	private int worker;
	private int ranged;
	private int heavy;
	private int light;
	private int recurso;
	
	private int baseInimigo;
	private int quartelInimigo;
	private int workerInimigo;
	private int rangedInimigo;
	private int heavyInimigo;
	private int lightInimigo;
	private int recursoInimigo;
	
	private Player player;
	private PhysicalGameState pgs;
	
	private static String evaluationType = "Inimigo"; 
	public EstadoDoJogo(Player p, PhysicalGameState pgs, UnitTypeTable a_utt){
	    utt = a_utt;
	    workerType = utt.getUnitType("Worker");
	    baseType = utt.getUnitType("Base");
	    barracksType = utt.getUnitType("Barracks");
	    rangedType = utt.getUnitType("Ranged");
	    heavyType = utt.getUnitType("Heavy");
        lightType = utt.getUnitType("Light");
		
		
		player = p;
		this.pgs = pgs;
		base = 0;
		quartel = 0;
		worker = 0;
		ranged = 0;
		heavy = 0;
		light = 0;
		
		baseInimigo = 0;
		quartelInimigo = 0;
		workerInimigo = 0;
		rangedInimigo = 0;
		heavyInimigo = 0;
		lightInimigo = 0;
		
		recurso = p.getResources();
		
		if(p.getID() == 0){
			recursoInimigo = pgs.getPlayer(1).getResources();
		}else if(p.getID() == 1){
			recursoInimigo = pgs.getPlayer(0).getResources();
		}else{
			recursoInimigo = 0;
		}
		
		iniciaParametros();
	}
	
	private EstadoDoJogo(){
		
	}
	
	public EstadoDoJogo clone(EstadoDoJogo edj){
		EstadoDoJogo toReturn = new EstadoDoJogo();
		toReturn.base = edj.getBase();
		toReturn.quartel = edj.getQuartel();
		toReturn.worker = edj.getWorker();
		toReturn.ranged = edj.getInimigoRanged();
		toReturn.heavy = 0;
		toReturn.light = 0;
		
		toReturn.baseInimigo = edj.getInimigoBase();
		toReturn.quartelInimigo = edj.getInimigoQuartel();
		toReturn.workerInimigo = edj.getInimigoWorker();
		toReturn.rangedInimigo = edj.getInimigoRanged();
		toReturn.heavyInimigo = 0;
		toReturn.lightInimigo = 0;
		
		toReturn.recurso = edj.getResources();
		toReturn.recursoInimigo = edj.getInimigoResources();
		
		return toReturn;
	}
	
	public int getBase(){
		return base;
	}
	
	public int getQuartel(){
		return quartel;
	}
	
	public int getWorker(){
		return worker;
	}
	
	public int getRanged(){
		return ranged;
	}
	
	public int getResources(){
		return recurso;
	}
	
	public int getInimigoBase(){
		return baseInimigo;
	}
	
	public int getInimigoQuartel(){
		return quartelInimigo;
	}
	
	public int getInimigoWorker(){
		return workerInimigo;
	}
	
	public int getInimigoRanged(){
		return rangedInimigo;
	}
	
	public int getInimigoResources(){
		return recursoInimigo;
	}

	private void iniciaParametros(){
		for (Unit u : pgs.getUnits()) {
   			if (u.getType() == workerType){
   				if(u.getPlayer() == player.getID()) {
   					worker++;
   				}else{
   					workerInimigo++;
   				}
   			}else if (u.getType() == barracksType){
   				if(u.getPlayer() == player.getID()) {
   	   	           	quartel++;
   				}else{
   	   	           	quartelInimigo++;
   				}
   	        }else if (u.getType() == baseType){
   	        	if(u.getPlayer() == player.getID()) {
   	        		base++;
   	        	}else{
   	        		baseInimigo++;
   	        	}  	
   	        }else if (u.getType() == heavyType){
   	        	if(u.getPlayer() == player.getID()) {            	
   	        		heavy++;
   	        	}else{
   	        		heavyInimigo++;
   	        	}
   	        }else if (u.getType() == lightType){
   	        	if(u.getPlayer() == player.getID()) {
   	        		light++;
   	        	}else{
   	        		lightInimigo++;
   	        	}
   	        }else if (u.getType() == rangedType){
   	        	if(u.getPlayer() == player.getID()) {
   	   	           	ranged++;
   	        	}else{
   	   	           	rangedInimigo++;
   	        	}
   	        }
		}
	}
	
	public void mudaEstado(String action){
		if(action.contains("!base")){
			base++; recurso -= 10;
		}else if(action.contains("!quartel")){
			quartel++; recurso -= 5;
		}else if(action.contains("!worker")){
			worker++; recurso -=1;
		}else if(action.contains("!ranged")){
			ranged++; recurso -=3;
		}else if(action.contains("!obter")){
			recurso += 5;
		}
	}
	
	public int evaluation(){
		//se não tiver base e não tiver recurso ou worker, o jogo está perdido
		if(player == null || (base < 1 && (player.getResources() < 10 || worker < 1))){return -1;}
		
		switch(evaluationType){
			//leva em consideração só as suas unidades
			case "Recurso":
				//worker(1) + quartel(5) + base(10) + heavy(3) + light(3) + ranged(3) + recurso(1)
				return (worker) + (quartel * 5) + (base * 10) + (heavy * 3) + (light * 3) + (ranged * 3) + (recurso);
			//unidades que o jogador possui menos as unidades que o outro jogador possui
			case "Inimigo":
				return ((worker) + (quartel * 5) + (base * 10) + (heavy * 3) + (light * 3) + (ranged * 3)) -
						(workerInimigo) + (quartelInimigo * 5) + (baseInimigo * 10) + (heavyInimigo * 3) + (lightInimigo * 3) + (rangedInimigo * 3);
		}		
		return -1;
		
	}
	
	public String toString(){
		String toReturn = "base: " + base + " quartel: " + quartel + " worker: " + worker +
				          " ranged: " + ranged + " heavy: " + heavy + " light: " + light +
				          " recurso: " + recurso;
		
		return toReturn;
	}
	
}

