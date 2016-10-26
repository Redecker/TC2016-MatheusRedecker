package AHTN.HighLevel2;
import java.util.LinkedList;

import AHTN.EstadoDoJogo;
import AHTN.HighLevel1.HighLevel1Domain;
import JSHOP2.*;
import ai.abstraction.myAHTN;
import rts.Player;

public class HighLevel2Problem
{
	private static String[] defineConstants()
	{
		String[] problemConstants = new String[8];

		problemConstants[0] = "b";
		problemConstants[1] = "rb";
		problemConstants[2] = "q";
		problemConstants[3] = "rq";
		problemConstants[4] = "w";
		problemConstants[5] = "rw";
		problemConstants[6] = "r";
		problemConstants[7] = "rr";

		return problemConstants;
	}

	public static LinkedList<Plan> getPlans(Player p, EstadoDoJogo edj)
	{
		LinkedList<Plan> returnedPlans = new LinkedList<Plan>();
		TermConstant.initialize(14);

		Domain d = new HighLevel2Domain();

		d.setProblemConstants(defineConstants());

		State s = new State(6, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		//aqui chama um método para gerar o problema
		myAHTN.setProblemJSHOP(s, p, edj);
		
		//aqui que é setado o objetivo
		tl =  myAHTN.setObjetiveJSHOP();
		
		
		//        quantidade de planos a ser procurado
		thread = new SolverThread(tl, 3);
		thread.start();

		try {
			while (thread.isAlive())
				Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		returnedPlans.addAll( thread.getPlans() );

		return returnedPlans;
	}

	public static LinkedList<Predicate> getFirstPlanOps() {
		return getPlans(null, null).getFirst().getOps();
	}
}