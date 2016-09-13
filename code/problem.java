import java.util.LinkedList;
import JSHOP2.*;

public class problem
{
	private static String[] defineConstants()
	{
		String[] problemConstants = new String[8];

		problemConstants[0] = "base";
		problemConstants[1] = "worker";
		problemConstants[2] = "quartel";
		problemConstants[3] = "ranged";
		problemConstants[4] = "recursoworker";
		problemConstants[5] = "recursoquartel";
		problemConstants[6] = "recursobase";
		problemConstants[7] = "recursoranged";

		return problemConstants;
	}

	private static void createState0(State s)	{
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(1), TermList.NIL)));
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(2), TermList.NIL)));
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(3), TermList.NIL)));
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(4), TermList.NIL)));
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(4), TermList.NIL)));
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(4), TermList.NIL)));
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(5), TermList.NIL)));
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(6), TermList.NIL)));
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(7), TermList.NIL)));
		s.add(new Predicate(0, 0, new TermList(TermConstant.getConstant(8), TermList.NIL)));
	}

	public static LinkedList<Plan> getPlans()
	{
		LinkedList<Plan> returnedPlans = new LinkedList<Plan>();
		TermConstant.initialize(9);

		Domain d = new ahtn();

		d.setProblemConstants(defineConstants());

		State s = new State(1, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		createState0(s);

		tl = new TaskList(1, true);
		tl.subtasks[0] = new TaskList(new TaskAtom(new Predicate(4, 0, new TermList(TermConstant.getConstant(4), TermList.NIL)), false, false));

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
		return getPlans().getFirst().getOps();
	}
}