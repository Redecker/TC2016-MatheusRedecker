package ai.abstraction;

import ai.core.AI;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.PathFinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import AHTN.*;
import AHTN.HighLevel1.HighLevel1Problem;
import AHTN.HighLevel2.HighLevel2Problem;
import JSHOP2.Predicate;
import JSHOP2.State;
import JSHOP2.TaskAtom;
import JSHOP2.TaskList;
import JSHOP2.TermConstant;
import JSHOP2.TermList;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.*;

public class myAHTN extends AbstractionLayerAI {
	
	/**
	 *
	 * @author santi and Matheus Redecker
	 */
	
	Random r = new Random();
	static UnitTypeTable utt;
    static UnitType workerType;
    static UnitType baseType;
    static UnitType barracksType;
    static UnitType rangedType;
    static UnitType heavyType;
    static UnitType lightType;
    
    static GameState gamestate;
    static PhysicalGameState physicalgamestate;
    static Player meuPlayer;
    static Player outroPlayer;

    private int rounds = 0;
    private static int roundsToAction = 50;
    
    private boolean print = true;
    
    private static String strategy = "HighLevel2";
    
    /*
     * Inicialização da classe
     */

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
    
    /*
     * Métodos para utilização do algoritmo de AHTN
     */
    
    //Faz com que dado um plano e um ponteiro, seja encontrado a proxima tarefa primitiva desse plano
    //retorna o ponteiro para a tarefa primitiva, se não houver retorna -1
    public int nextAction(Plano plan, int pointer){
    	if(plan.getOperacaoPonteiro(pointer+1) != null){
    		//muda alguma coisa do ambiente
    		plan.getEstadoJogo().mudaEstado(plan.getOperacaoPonteiro(pointer));
    		return pointer+1;
    	}else{
    		return -1;
    	}
    }    

	public Node AHTNMax(ArrayList<Plano> planMax, Plano planMin, int deph){
    	Node evaluation = new Node(null,null, -999999);
    	for(Plano p : planMax){
    		Node aux = AHTNMax(p, 0, planMin, 0, deph);
    		if(evaluation.getAvaliacao() < aux.getAvaliacao()){
    			evaluation = aux;
    		}
    	}
    	return evaluation;
    }
	
	public Node AHTNMax(Plano planMax, int ponteiroMax, Plano planMin,  int ponteiroMin, int deph){
		//se o estado é terminal retorna os planos de max e min, e a utilidade do estado s
		if(planMax.getEstadoJogo().evaluation() < 0 || planMin.getEstadoJogo().evaluation() < 0 || deph == 0){
			return new Node(planMax, planMin, planMax.getEstadoJogo().evaluation());
		}
		
		//se houver uma ação primitiva para ser executada
		//altera o ponteiro e muda para perspectiva de Min
		int next = nextAction(planMax, ponteiroMax); 
		if(next != -1){
			print("NodeMaxEvaluation: " + planMax.getEstadoJogo().evaluation());
			print("PlanoMax: " + planMax.getEstadoJogo().toString());
			return AHTNMin(planMax, next, planMin, ponteiroMin, deph-1); 
		}//se não tiver acabou esse plano
		else{
			return new Node(planMax, planMin, planMax.getEstadoJogo().evaluation());
		}
	}
    
	public Node AHTNMin(Plano planMax, int ponteiroMax, Plano planMin,  int ponteiroMin, int deph){
		//se o estado é terminal retorna os planos de max e min, e a utilidade do estado s
		if(planMax.getEstadoJogo().evaluation() < 0 || planMin.getEstadoJogo().evaluation() < 0 || deph == 0){
			return new Node(planMax, planMin, planMin.getEstadoJogo().evaluation());
		}
		
		//se houver uma ação primitiva para ser executada
		//altera o ponteiro e muda para perspectiva de Min
		int next = nextAction(planMin, ponteiroMin); 
		if(next != -1){
			print("NodeMinEvaluation: " + planMin.getEstadoJogo().evaluation());
			print("PlanoMin: " + planMin.getEstadoJogo().toString());
			return AHTNMax(planMax, ponteiroMax, planMin, next, deph -1); 
		}//se não tiver -> acabou esse plano
		else{
			return new Node(planMax, planMin, planMin.getEstadoJogo().evaluation());
		}
	}
	
	private void setEstadoJogo(Plano plano, Player p, PhysicalGameState pgs) {
		EstadoDoJogo edj = new EstadoDoJogo(p, pgs, utt);
		plano.setEstadoJogo(edj);
	}
	
	
	/*
	 * Métodos para utilização do JSHOP 	
	 */
	
	private ArrayList<Plano> getPlanosInit(Player p){
		switch(strategy){
			case "HighLevel1":
				return getPlanos(p, HighLevel1Problem.getPlans(p).toString());
			case "HighLevel2":
				return getPlanos(p, HighLevel2Problem.getPlans(p).toString());
		}
		
		return null;
		
	}	
	
	private ArrayList<Plano> getPlanos(Player p, String jshop){
		String[] pla = jshop.split("\n");
		Plano plan = null;
		String[] aux;
		ArrayList<Plano> planos = new ArrayList<>();
		
		for(int i = 0; i < pla.length ;i++){
			//começa um novo plano
			if(pla[i].contains("Plan cost")){
				plan = new Plano();
				aux = pla[i].split(" ");
				plan.setCusto(Double.parseDouble(aux[aux.length-1]));
				planos.add(plan);
			}//cada operação salva
			else if(pla[i].contains("(!")){
				plan.addOperacao(pla[i]);
			}
		}
		
		//se planos for vazio, ou seja, não tem nenhum plano para executar, coloca um plano com avaliação -1, pois não tem o que o jogador fazer
		if(planos.size() < 1){
			Plano pl = new Plano();
			pl.setCusto(-1.0);
			planos.add(pl);
		}
		
		return planos;
	}
	
	public static void setProblemJSHOP(State s, Player p){
		switch(strategy){
			case "HighLevel1":
				getProblemHighLevel12(p, physicalgamestate, s);		
				break;
			case "HighLevel2":
				getProblemHighLevel12(p, physicalgamestate, s);
				break;
		}
		
	}
	
	private static void getProblemHighLevel12(Player player, PhysicalGameState pgs, State s){
		//basicos para definição de recursos
		s.add(new Predicate(5, 0, new TermList(TermConstant.getConstant(6), new TermList(TermConstant.getConstant(7), TermList.NIL))));
		s.add(new Predicate(5, 0, new TermList(TermConstant.getConstant(8), new TermList(TermConstant.getConstant(9), TermList.NIL))));
		s.add(new Predicate(5, 0, new TermList(TermConstant.getConstant(10), new TermList(TermConstant.getConstant(11), TermList.NIL))));
		s.add(new Predicate(5, 0, new TermList(TermConstant.getConstant(12), new TermList(TermConstant.getConstant(13), TermList.NIL))));
		
		//Verifica quais as unidades e construções que o jogador tem
		for (Unit u : pgs.getUnits()) {
			 if (u.getType() == workerType && u.getPlayer() == player.getID()) {     
				 //worker w
				 s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(10), TermList.NIL)));
	         }
			 if (u.getType() == barracksType && u.getPlayer() == player.getID()) {            	
				 //quartel q
				 s.add(new Predicate(3, 0, new TermList(TermConstant.getConstant(8), TermList.NIL)));
	         }
			 if (u.getType() == baseType && u.getPlayer() == player.getID()) {            	
				 //base b
				 s.add(new Predicate(2, 0, new TermList(TermConstant.getConstant(6), TermList.NIL)));
	         }
			 if (u.getType() == heavyType && u.getPlayer() == player.getID()) {            	
	            //heavy h
				//s.add(new Predicate(2, 0, new TermList(TermConstant.getConstant(6), TermList.NIL)));
	         }
			 if (u.getType() == lightType && u.getPlayer() == player.getID()) {            	
		        //light l
				//s.add(new Predicate(2, 0, new TermList(TermConstant.getConstant(6), TermList.NIL)));	         
			 }
			 if (u.getType() == rangedType && u.getPlayer() == player.getID()) {            	
		        //ranged r
				s.add(new Predicate(4, 0, new TermList(TermConstant.getConstant(12), TermList.NIL)));	         
			 }
		}		
		if(player.getResources() >= 10){
			//have rb
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(7), TermList.NIL)));
			//have rq
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(9), TermList.NIL)));
    		//have rr
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(13), TermList.NIL)));
		 	//have rw
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(11), TermList.NIL)));
		}else if(player.getResources() >= 5){
			//have rq
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(9), TermList.NIL)));
    		//have rr
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(13), TermList.NIL)));
		 	//have rw
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(11), TermList.NIL)));
		}else if(player.getResources() >= 2){
			//have rr
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(13), TermList.NIL)));
		 	//have rw
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(11), TermList.NIL)));
		}else if(player.getResources() >= 1){
		 	//have rw
    		s.add(new Predicate(1, 0, new TermList(TermConstant.getConstant(11), TermList.NIL)));
		}				
	}
	
	public static TaskList setObjetiveJSHOP(){
		switch(strategy){
			case "HighLevel1":
				return getPlanObjectiveHighLevel12();
			case "HighLevel2":
				return getPlanObjectiveHighLevel12();
		}		
		
		return null;
	}
	
	private static TaskList getPlanObjectiveHighLevel12(){
		//                  tamanho dos objetivos
		TaskList tl = new TaskList(1, true);		
		tl.subtasks[0] = new TaskList(new TaskAtom(new Predicate(4, 0, new TermList(TermConstant.getConstant(12), TermList.NIL)), false, false));
		
		return tl;
	}
		
	/*
	 * Método onde é gerada a proxima ação a ser executada
	 */
	
	//metodo que é chamado para gerar as ações das unidades
	public PlayerAction getAction(int player, GameState gs) throws IOException {
	
		if(rounds % roundsToAction == 0){					
			//inicia as classes para controle do jogo
			//inicia componentes do jogo global
			gamestate = gs;
			physicalgamestate = gs.getPhysicalGameState();
			if(player == 0){
				meuPlayer = gs.getPlayer(player);
				outroPlayer = gs.getPlayer(player+1);
			}else if(player == 1){
				meuPlayer = gs.getPlayer(player);
				outroPlayer = gs.getPlayer(player-1);
			}
			
			//Gera os planos do Max
			ArrayList<Plano> planosMax = getPlanosInit(meuPlayer);
			for(Plano p : planosMax){
				setEstadoJogo(p, meuPlayer, physicalgamestate);
				p.getEstadoJogo().evaluation();
			}			
			
			//Gera os planos do Min
			ArrayList<Plano> planosMin = getPlanosInit(outroPlayer);			
			for(Plano p : planosMin){
				setEstadoJogo(p, outroPlayer, physicalgamestate);
			}
			
			Node node = AHTNMax(planosMax, planosMin.get(0), 5);
			perfromActions(node.getPlanoMax().getOperacaoPonteiro(0), gs, gs.getPhysicalGameState(), meuPlayer);
		}
		
		rounds++;
		return translateActions(player, gs);
	}     
	
	/*
	 * Métodos para executar as ações no MicroRTS 
	 */
	
	//executa a ação
	public void perfromActions(String action, GameState gs, PhysicalGameState pgs, Player p){
		if(action == null){
			return;
		}
		//usado para buscar as referencias das unidades
		Unit worker = null;
        Unit barrack = null;
        ArrayList<Unit> unidadesAtaque = new ArrayList<>();
		for (Unit u : pgs.getUnits()) {		
            if (u.getType() == workerType && u.getPlayer() == p.getID()) { 
            	worker = u;
            }
            if (u.getType() == barracksType && u.getPlayer() == p.getID()) {            	
            	barrack = u;
            }
            if (u.getType().canAttack && !(u.getType() == workerType)){
            	unidadesAtaque.add(u);
            }
        }
		
		//executa o que o plano manda
		print("Ação: " + action);
		if(action.contains("!base")){
			construirBase(worker, p, pgs);
		}else if(action.contains("!quartel")){
			construirBarrack(worker, p, pgs);
		}else if(action.contains("!worker")){
			construirWorkers(p, pgs, gs, 1);
		}else if(action.contains("!obter-recurso")){
			workersRecolhemRecursos(worker, p, pgs);
		}else if(action.contains("!atacar")){
			for(Unit u : unidadesAtaque) unidadeAtaca(u, p, pgs);
		}else if(action.contains("!ranged")){
			treinarUnidade(barrack, p, pgs, rangedType);
		}else if(action.contains("!treina-ataca")){
			for(Unit u : unidadesAtaque) unidadeAtaca(u, p, pgs);
			treinarUnidade(barrack, p, pgs, rangedType);
		}
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
			        if((pos % pgs.getWidth()) /*+ 2*/ < pgs.getHeight()) build(worker, barracksType, (pos % pgs.getWidth()) /*+ 2*/, pos / pgs.getWidth());
			        else if((pos / pgs.getWidth()) /*+ 2*/ < pgs.getWidth()) build(worker, barracksType, (pos % pgs.getWidth()), pos / pgs.getWidth()/*+2*/);
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
	        }
	        closestDistance = 0;
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
	
	//metodo de print
	private void print(String message){
	    	if (print) System.out.println(message);
	    }
	    
}
