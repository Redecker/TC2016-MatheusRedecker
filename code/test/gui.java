import JSHOP2.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class gui{
	public static void main(String[] args) {
		
		try (BufferedWriter buffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("plano.txt"), "UTF-8"))) {
	        buffWriter.write(problem.getPlans().toString());    
	        buffWriter.close();
		} catch (Exception e) {
		   	e.printStackTrace();
		}

		new JSHOP2GUI();
		//LinkedList<Plan> llp = problem.getPlans();
		//LinkedList<Predicate> llpre = llp.peek().getOps();
		//System.out.println(llpre.peek());
		//System.out.println(llp.size());
		//System.out.println(llp);
		//System.out.println(problem.getPlans());
		//new JSHOP2GUI();
	} 
}
