import java.util.LinkedList;
import JSHOP2.*;

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

	private static void createState0(State s)	{
		s.add(new Predicate(5, 0, new TermList(TermConstant.getConstant(6), new TermList(TermConstant.getConstant(7), TermList.NIL))));
		s.add(new Predicate(5, 0, new TermList(TermConstant.getConstant(8), new TermList(TermConstant.getConstant(9), TermList.NIL))));
		s.add(new Predicate(5, 0, new TermList(TermConstant.getConstant(10), new TermList(TermConstant.getConstant(11), TermList.NIL))));
		s.add(new Predicate(5, 0, new TermList(TermConstant.getConstant(12), new TermList(TermConstant.getConstant(13), TermList.NIL))));
		s.add(new Predicate(3, 0, new TermList(TermConstant.getConstant(8), TermList.NIL)));
	}

	public static LinkedList<Plan> getPlans()
	{
		LinkedList<Plan> returnedPlans = new LinkedList<Plan>();
		TermConstant.initialize(14);

		Domain d = new ahtn();

		d.setProblemConstants(defineConstants());

		State s = new State(6, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		createState0(s);

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
		return getPlans().getFirst().getOps();
	}
}