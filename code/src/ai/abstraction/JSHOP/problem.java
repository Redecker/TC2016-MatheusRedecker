package ai.abstraction.JSHOP;
import java.util.LinkedList;
import JSHOP2.*;
import ai.abstraction.*;
import rts.*;

public class problem
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

		Domain d = new ahtn();

		d.setProblemConstants(defineConstants());

		State s = new State(6, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		//aqui chamar um método proprio para gerar o problema
		myAHTN.setProblemJSHOP(s, p);
//		for(String string : s.getState()){
//			System.out.println(string);
//		}
		//createState0(s);
		
		//aqui que é setado o objetivo, se for sempre o mesmo beleza
		tl = new TaskList(1, true);
		tl.subtasks[0] = new TaskList(new TaskAtom(new Predicate(4, 0, new TermList(TermConstant.getConstant(12), TermList.NIL)), false, false));

		thread = new SolverThread(tl, 1);
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