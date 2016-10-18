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
	
	private Player player;
	private PhysicalGameState pgs;
	
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
		recurso = p.getResources();
		setParametros();
	}
	
	public void setParametros(){
		for (Unit u : pgs.getUnits()) {
   			if (u.getType() == workerType && u.getPlayer() == player.getID()) {            	
   	           	worker++;
   	        }else if (u.getType() == barracksType && u.getPlayer() == player.getID()) {            	
   	           	quartel++;
   	        }else if (u.getType() == baseType && u.getPlayer() == player.getID()) {       
   	           	base++;
   	        }else if (u.getType() == heavyType && u.getPlayer() == player.getID()) {            	
   	           	heavy++;
   	        }else if (u.getType() == lightType && u.getPlayer() == player.getID()) {            	
   	           	light++;
   	        }else if (u.getType() == rangedType && u.getPlayer() == player.getID()) {            	
   	           	ranged++;
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
		if(base < 1 && (player.getResources() < 10 || worker < 1)){return -1;}
		//se não função de avaliação do jogo
		//worker(1) + quartel(5) + base(10) + heavy(3) + light(3) + ranged(3) + recurso(1)
		return (worker) + (quartel * 5) + (base * 10) + (heavy * 3) + (light * 3) + (ranged * 3) + (recurso);
	}
	
	public String toString(){
		String toReturn = "base: " + base + " quartel: " + quartel + " worker: " + worker +
				          " ranged: " + ranged + " heavy: " + heavy + " light: " + light +
				          " recurso: " + recurso;
		
		return toReturn;
	}
	
}

