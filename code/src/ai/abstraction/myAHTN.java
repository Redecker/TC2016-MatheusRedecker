package ai.abstraction;

import ai.core.AI;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.PathFinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    
    private static String headProblem = "(defproblem problem ahtn ( ";
    private static int rounds = 0;
    private static int roundsToAction = 100;
    
    boolean print = true;
    
    public class node{
    	
    	private ArrayList<String> planoMax;
    	private ArrayList<String> planoMin;
    	private int pointerMax;
    	private int pointerMin;
    	
    }
    
    //Faz com que dado um plano e um ponteiro, seja encontrado a proxima tarefa primitiva desse plano
    //retorna o ponteiro para a tarefa primitiva, se não houver retorna -1
    public int nextAction(ArrayList<String> plan, int pointer){
    	
    	return -1;
    }    
    
    //adiciona um método novo método no plano de Max
    public ArrayList<ArrayList<String>> decompositionsMax(/*estado s */ArrayList<String> planMax, ArrayList<String> planMin, int pointerMax, int pointerMin){
    	return null;
    }
	
	public void AHTNMax(/*estado s */ArrayList<String> planMax, ArrayList<String> planMin, int pointerMax, int pointerMin, int deph){
		//se o estado é terminal retorna os planos de max e min, e a utilidade do estado s
		
		//se houver uma ação primitiva para ser executada
		//altera o ponteiro e muda para perspectiva de Min
		if(nextAction(planMax, pointerMax) != -1){
			int t = nextAction(planMax, pointerMax);
			//return AHTNMin(d -1); 
		}
		
		ArrayList<String> planMaxAux = new ArrayList<String>();
		ArrayList<String> planMinAux = new ArrayList<String>();
		int v = -999999999;
		
		//adiciona apenas um método no plano
		//N = decompositions();
		
		//para cada plano possivel chama o AHTNMax novamente
		//se o v < v achado acima
		//atualiza os planos achados acima e o v
		//retorna os itens acima
		
	}
    
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
	
	public ArrayList<String> getPlano(String problema){

		//salva o problema no arquivo
		try (BufferedWriter buffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("problem"), "UTF-8"))) {
	        buffWriter.write(problema);    
	        buffWriter.close();
		} catch (Exception e) {
		   	e.printStackTrace();
		}
		
		//executa o script para rodar o JSHOP2		
		try {
			//Runtime.getRuntime().exec();
			Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("./criaPlano.sh");
			InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            if (print) System.out.println("<Problem ERROR>");
            while ( (line = br.readLine()) != null)
            	if (print) System.out.println(line);
            if (print) System.out.println("</Problem ERROR>");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//lê o arquivo com o plano e retorna
		//ajeita o plano e retorna
		ArrayList<String> plano = new ArrayList<String>();
		try (BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream("plano.txt"), "UTF-8"))) {
			String linha = "";			
			while(buffRead.ready()) { 
				linha = buffRead.readLine();
				if(!linha.trim().isEmpty()) {
					plano.add(linha);
				}
			} 
			
			buffRead.close();				
		} catch (Exception e) {
		   	e.printStackTrace();
		}
		
		return plano;
	}
	
	private static int x = 0;
	public String getProblem(Player player, PhysicalGameState pgs){
		String problem = "";
		
		//Verifica quais as unidades e construções que o jogador tem
		for (Unit u : pgs.getUnits()) {
			 if (u.getType() == workerType && u.getPlayer() == player.getID()) {            	
	            	problem += "(have worker)";
	         }
			 if (u.getType() == barracksType && u.getPlayer() == player.getID()) {            	
	            	problem += "(have quartel)";
	         }
			 if (u.getType() == baseType && u.getPlayer() == player.getID()) {            	
	            	problem += "(have base)";
	         }
			 if (u.getType() == heavyType && u.getPlayer() == player.getID()) {            	
	            	problem += "(have heavy)";
	         }
			 if (u.getType() == lightType && u.getPlayer() == player.getID()) {            	
	            	problem += "(have light)";
	         }
			 if (u.getType() == rangedType && u.getPlayer() == player.getID()) {            	
	            	problem += "(have ranged)";
	         }
		}		
		if(player.getResources() >= 10){
			problem += "(have recursoWorker)" + "(have recursoQuartel)" + "(have recursoBase)" + "(have recursoRanged)"; 
		}else if(player.getResources() >= 5){
			problem += "(have recursoWorker)" + "(have recursoQuartel)" + "(have recursoRanged)"; 
		}else if(player.getResources() >= 2){
			problem += "(have recursoWorker)" + "(have recursoRanged)"; 
		}else if(player.getResources() >= 1){
			problem += "(have recursoWorker)"; 
		}		
		problem += ")";
		
		//aqui tem que adicionar o que o plano quer alcançar
		problem += "(";
		problem += getPlanObjective(x);
		x++;
		problem += "))";
		
		return problem;
	}
	
	public String getPlanObjective(int x){
		if(x == 0){
			return "(construir-quartel worker quartel recursoQuartel)";
		}else if(x > 0){
			return "(treinar-unidade ranged quartel recurso)";
		}
		return "";
	}
	
	//pegar cada plano e transformar em ações	
	public void perfromActions(ArrayList<String> plan, GameState gs, PhysicalGameState pgs, Player p){
		//usado para buscar as referencias das unidades
		Unit worker = null;
        Unit barrack = null;
        ArrayList<Unit> unidadesAtaque = new ArrayList<>();
		for (Unit u : pgs.getUnits()) {			
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
		
		for(String stepPlan : plan){
			if(stepPlan.contains("!base")){
				if (print) System.out.println("base");
				construirBase(worker, p, pgs);
			}else if(stepPlan.contains("!quartel")){
				if (print) System.out.println("quartel");
				construirBarrack(worker, p, pgs);
			}else if(stepPlan.contains("!worker")){
				if (print) System.out.println("worker");
				construirWorkers(p, pgs, gs, 1);
			}else if(stepPlan.contains("!obter-recurso")){
				if (print) System.out.println("recurso");
				workersRecolhemRecursos(worker, p, pgs);
			}else if(stepPlan.contains("!atacar")){
				if (print) System.out.println("atacar");
				for(Unit u : unidadesAtaque) unidadeAtaca(u, p, pgs);
			}else if(stepPlan.contains("!treinar")){
				if (print) System.out.println("treinar");
				treinarUnidade(barrack, p, pgs, rangedType);
			}
		}		
	}
	
	//NÃO UTILIZAR O GETPLANOS AQUI DENTRO QUE TAH TRAVANDO O PC, POIS CHAMA MUITAS VEZES, FAZER ALGUM CONTROLE PARA NÃO CHAMAR TANTO!!!!!!
	public PlayerAction getAction(int player, GameState gs) throws IOException {
		rounds ++;
		if(rounds % roundsToAction == 0){
			PhysicalGameState pgs = gs.getPhysicalGameState();
			Player p = gs.getPlayer(player);
			
			String problem = headProblem + getProblem(p, pgs);
			if (print) print(problem);
			ArrayList<String> plan = getPlano(problem);
			
			perfromActions(plan, gs, pgs, p);
			
		}
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

		
		
//		PhysicalGameState pgs = gs.getPhysicalGameState();
//		Player p = gs.getPlayer(player);
//	    
//		Unit worker = null;
//        Unit barrack = null;
//        ArrayList<Unit> unidadesAtaque = new ArrayList<>();
//		for (Unit u : pgs.getUnits()) {
//			//if(u.getPlayer() == p.getID()) idle(u);
//			
//            if (u.getType() == workerType && u.getPlayer() == p.getID()) {            	
//            	worker = u ;
//            }
//            if (u.getType() == barracksType && u.getPlayer() == p.getID()) {            	
//            	barrack = u;
//            }
//            if (u.getType().canAttack && !(u.getType() == workerType)){
//            	unidadesAtaque.add(u);
//            }
//        }    
//		
//		if(barrack == null){
//			construirBarrack(worker, p, pgs);
//		}else{
//			workersRecolhemRecursos(worker, p, pgs);
//		}
//		
//		if (barrack != null){
//			r.nextInt(3);
//			switch(r.nextInt(3)){
//				case 0:
//					treinarUnidade(barrack, p, pgs, heavyType);
//					break;
//				case 1:
//					treinarUnidade(barrack, p, pgs, lightType);
//					break;
//				case 2:
//					treinarUnidade(barrack, p, pgs, rangedType);
//					break;
//			}
//		}
//		
//		for(Unit u : unidadesAtaque){
//			unidadeAtaca(u, p, pgs);
//		}
//		
//        //workersRecolhemRecursos(worker, p, pgs);
//        //construirBarrack(worker, p, pgs);
//        
//        return translateActions(player, gs);
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
	    	if (print) System.out.println(message);
	    }
	    
}
