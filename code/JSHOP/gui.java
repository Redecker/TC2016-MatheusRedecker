import JSHOP2.*;
import java.util.*;

public class gui{
	public static void main(String[] args) {
		LinkedList<Plan> llp = problem.getPlans();
		
		LinkedList<Predicate> llpre = llp.peek().getOps();

		//System.out.println(llpre.peek());

		//System.out.println(llp.size());
		System.out.println(llp);
		System.out.println(problem.getPlans());
		//new JSHOP2GUI();
	} 
}
