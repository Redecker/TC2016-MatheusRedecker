package AHTN.HighLevel1;
import java.util.LinkedList;
import JSHOP2.*;
import ai.abstraction.*;
import rts.*;

public class HighLevel1Problem
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

	public static LinkedList<Plan> getPlans(Player p)
	{
		LinkedList<Plan> returnedPlans = new LinkedList<Plan>();
		TermConstant.initialize(14);

		Domain d = new HighLevel1Domain();

		d.setProblemConstants(defineConstants());

		State s = new State(6, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		//aqui chama um método para gerar o problema
		myAHTN.setProblemJSHOP(s, p);
		
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
		return getPlans(null).getFirst().getOps();
	}
}