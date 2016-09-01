package ai.abstraction;

import ai.core.AI;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.PathFinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.*;

import JSHOP2.*;

public class myAHTN extends AbstractionLayerAI {
	
	/**
	 *
	 * @author santi and Matheus Redecker
	 */
	
	Random r = new Random();
    UnitTypeTable utt;
    UnitType workerType;
	UnitType baseType;
	UnitType barracksType;
	UnitType rangedType;
	UnitType heavyType;
    UnitType lightType;

	public myAHTN(UnitTypeTable a_utt, PathFinding a_pf) {
		super(a_pf);
	    utt = a_utt;
	    workerType = utt.getUnitType("Worker");
	    baseType = utt.getUnitType("Base");
	    barracksType = utt.getUnitType("Barracks");
	    rangedType = utt.getUnitType("Ranged");
	    heavyType = utt.getUnitType("Heavy");
        lightType = utt.getUnitType("Light");
	}

	public void reset() {
	}

	public AI clone() {
	        return new myAHTN(utt, null);
	}
	
	public PlayerAction getAction(int player, GameState gs) throws IOException {
		
		PhysicalGameState pgs = gs.getPhysicalGameState();
		Player p = gs.getPlayer(player);
	    
		//test jshop
		//problem.getPlans();
		//Runtime rt = Runtime.getRuntime();Process pr1 = rt.exec("java JSHOP2.InternalDomain basic");Process pr2 = rt.exec("java JSHOP2.InternalDomain -r problem");
		//Runtime.getRuntime().exec("java JSHOP2.InternalDomain JSHOP/basic");
		
		//Runtime.getRuntime().exec("./test.sh");
		
		System.out.println(problem.getPlans());
		//new JSHOP2GUI();

		
		Unit worker = null;
        Unit barrack = null;
        ArrayList<Unit> unidadesAtaque = new ArrayList<>();
		for (Unit u : pgs.getUnits()) {
			//if(u.getPlayer() == p.getID()) idle(u);
			
            if (u.getType() == workerType && u.getPlayer() == p.getID()) {            	
            	worker = u ;
            }
            if (u.getType() == barracksType && u.getPlayer() == p.getID()) {            	
            	barrack = u;
            }
            if (u.getType().canAttack && !(u.getType() == workerType)){
            	unidadesAtaque.add(u);
            }
        }    
		
		if(barrack == null){
			construirBarrack(worker, p, pgs);
		}else{
			workersRecolhemRecursos(worker, p, pgs);
		}
		
		if (barrack != null){
			r.nextInt(3);
			switch(r.nextInt(3)){
				case 0:
					treinarUnidade(barrack, p, pgs, heavyType);
					break;
				case 1:
					treinarUnidade(barrack, p, pgs, lightType);
					break;
				case 2:
					treinarUnidade(barrack, p, pgs, rangedType);
					break;
			}
		}
		
		for(Unit u : unidadesAtaque){
			unidadeAtaca(u, p, pgs);
		}
		
        //workersRecolhemRecursos(worker, p, pgs);
        //construirBarrack(worker, p, pgs);
        
        return translateActions(player, gs);
	}     
	
	    public void unidadeAtaca(Unit u, Player p, PhysicalGameState pgs) {
		    Unit closestEnemy = null;
		    int closestDistance = 0;
		    for (Unit u2 : pgs.getUnits()) {
		        if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID()) {
		            int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
		            if (closestEnemy == null || d < closestDistance) {
		                closestEnemy = u2;
		                closestDistance = d;
		            }
		        }
		    }
		    if (closestEnemy != null) {
		        attack(u, closestEnemy);
		    }
	    }

	    public void treinarUnidade(Unit u, Player p, PhysicalGameState pgs, UnitType ut) {
		    if (p.getResources() >= ut.cost) {
		        train(u, ut);
		    }
		}	
	
		public void construirBase(Unit worker, Player p, PhysicalGameState pgs){
            int nroBases = 0;
            for (Unit u : pgs.getUnits()) {
            	if (u.getType() == baseType && u.getPlayer() == p.getID()) {
            		nroBases++;
            	}
            }
            
            List<Integer> reservedPositions = new LinkedList<Integer>();
            if (nroBases < 1) {
			    if (p.getResources() >= baseType.cost) {
			        int pos = findBuildingPosition(reservedPositions, worker, p, pgs);			        
			        if((pos % pgs.getWidth()) + 2 < pgs.getHeight()) build(worker, baseType, (pos % pgs.getWidth()) + 2, pos / pgs.getWidth());
			        else if((pos / pgs.getWidth()) + 2 < pgs.getWidth()) build(worker, baseType, (pos % pgs.getWidth()), pos / pgs.getWidth()+2);
			        else{
			        	build(worker, baseType, (pos % pgs.getWidth()), pos / pgs.getWidth());
			        }
			    }
            }
		}
		
		public void construirBarrack(Unit worker, Player p, PhysicalGameState pgs){            
			int nroBarracks = 0;
            for (Unit u : pgs.getUnits()) {
            	if (u.getType() == barracksType && u.getPlayer() == p.getID()) {
            		nroBarracks++;
            	}
            }
            
            List<Integer> reservedPositions = new LinkedList<Integer>();
            if (nroBarracks < 1) {
			    if (p.getResources() >= barracksType.cost) {
			        int pos = findBuildingPosition(reservedPositions, worker, p, pgs);
			        if((pos % pgs.getWidth()) + 2 < pgs.getHeight()) build(worker, barracksType, (pos % pgs.getWidth()) + 2, pos / pgs.getWidth());
			        else if((pos / pgs.getWidth()) + 2 < pgs.getWidth()) build(worker, barracksType, (pos % pgs.getWidth()), pos / pgs.getWidth()+2);
			        else{
			        	build(worker, barracksType, (pos % pgs.getWidth()), pos / pgs.getWidth());
			        }
			    }
            }
		}	
		
        // Worker W colhe recurso.
	    public void workersRecolhemRecursos(Unit worker, Player p, PhysicalGameState pgs){
	    	//for (Unit u : workers) {
	        Unit closestBase = null;
	        Unit closestResource = null;
	        int closestDistance = 0;
	        //verifica se a unidade � um recurso e verifica a posi��o dela
	        for (Unit u2 : pgs.getUnits()) {
	            if (u2.getType().isResource) {
	                int d = Math.abs(u2.getX() - worker.getX()) + Math.abs(u2.getY() - worker.getY());
	                if (closestResource == null || d < closestDistance) {
	                    closestResource = u2;
	                    closestDistance = d;
	                }
	            }
	        closestDistance = 0;
	        }
	        //verifica se a unidade � um deposito para largar o recurso
	        for (Unit u2 : pgs.getUnits()) {
	            if (u2.getType().isStockpile && u2.getPlayer()==p.getID()) {
	                int d = Math.abs(u2.getX() - worker.getX()) + Math.abs(u2.getY() - worker.getY());
	                if (closestBase == null || d < closestDistance) {
	                    closestBase = u2;
	                    closestDistance = d;
	                }
	            }
	        }
	            
            // faz com que o worker colha o recurso e coloque na base
            if (closestResource != null && closestBase != null) {
                AbstractAction aa = getAbstractAction(worker);
                if (aa instanceof Harvest) {
                    Harvest h_aa = (Harvest)aa;
                    if (h_aa.target != closestResource || h_aa.base!=closestBase) harvest(worker, closestResource, closestBase);
                } else {
                    harvest(worker, closestResource, closestBase);
                }
	       }
	   }
	    
	    //A base faz at� x workers
	    public void construirWorkers(Player p, PhysicalGameState pgs, GameState gs, int nw) {	    	
	    	Unit base = null;		    
	        int nworkers = 0;
	    	
	        for (Unit u2 : pgs.getUnits()) {
	        	//conta numero de workers		        
	        	if (u2.getType() == workerType && u2.getPlayer() == p.getID()) {
	                nworkers++;
	            }
	            //verifica quem � a base
		    	if (u2.getType() == baseType && u2.getPlayer() == p.getID() && gs.getActionAssignment(u2) == null) {
		    		base = u2;
		    	}
	        }
	        // se tiver menos de NW worker e tiver recurso faz um.
	        if (nworkers < nw && p.getResources() > workerType.cost && base != null) {
	            train(base, workerType);
	        }
	    }

	    public int findBuildingPosition(List<Integer> reserved, Unit u, Player p, PhysicalGameState pgs) {
        int bestPos = -1;
        int bestScore = 0;

        for (int x = 0; x < pgs.getWidth(); x++) {
            for (int y = 0; y < pgs.getHeight(); y++) {
                int pos = x + y * pgs.getWidth();
                if (!reserved.contains(pos) && pgs.getUnitAt(x, y) == null) {
                    int score = 0;

                    score = -(Math.abs(u.getX() - x) + Math.abs(u.getY() - y));

                    if (bestPos == -1 || score > bestScore) {
                        bestPos = pos;
                        bestScore = score;
                    }
                }
            }
        }

        return bestPos;
    }
	    
	    private void print(String message){
	    	System.out.println(message);
	    }
	    
}
