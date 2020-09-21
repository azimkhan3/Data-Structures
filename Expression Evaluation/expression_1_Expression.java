package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;
public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
    	

    	StringTokenizer token = new StringTokenizer(noSpace(expr),delims,true);

        while (token.hasMoreTokens()) {
        	String ex = token.nextToken();
        	Array pt = new Array(ex);
        	//creates new array for storing
			Variable letter = new Variable(ex);
			//creates new variable which will store values
			if(!(arrays.contains(pt)) && !(vars.contains(letter))) {
	        	//checks if there are any duplicates
				int c = ex.charAt(0);      
	            if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
	            	if(token.hasMoreTokens() && token.nextToken().equals("[")) {
	            		//brackets after a character is an array
	            		arrays.add(new Array(ex));
	            	}
	            	else {
	            		vars.add(new Variable(ex));
	            	}
	            }
			}
        }

    }
    	
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    
	
    
		 
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	StringTokenizer token = new StringTokenizer(noSpace(expr),delims,true);

    	Stack<Float> nums = new Stack<Float>();
    	Stack<String> math = new Stack<String>();

    	while(token.hasMoreTokens()) { 		
    		String ptr = token.nextToken();
    		
    		int c = ptr.charAt(0);
    		
    		if(delims.indexOf(c) < 0 || c == '(') {
    			
    			if(delims.indexOf(c) < 0) {
	    			
    				if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {//variable or array
	    				
    					Array pt = new Array(ptr);
	    				
    					Variable letter = new Variable(ptr);
	    				
    					if(vars.contains(letter)) {
	    					nums.push((float) (vars.get(vars.indexOf(letter)).value));
	    				}
	    				
    					else if(arrays.contains(pt)) {
	    					
    						int x = (int) Math.floor(evaluate(getExpRec(token, token.nextToken()),vars,arrays));
	    					
    						nums.push((float) arrays.get(arrays.indexOf(pt)).values[x]);	
	    				}
	    			}
	    			
    				else {
	    				nums.push(Float.parseFloat(ptr));
	    			}
    			}
    			
    			else {
    				
    				float x = evaluate(getExpRec(token, "("),vars,arrays);
    				
    				nums.push(x);
    			}
    			
    			if(!(math.isEmpty())) {
    				
    				String op = math.pop();
    				
    				switch (op) {
    					case "*": nums.push(nums.pop()*nums.pop());
    						break;
    					case "/": nums.push(1/(nums.pop()/nums.pop())); 
    						break;
    					case "-": nums.push(-1*nums.pop()); math.push("+"); 
    						break;
    					default: math.push(op); 
    				}
    			}
    		}
    		
    		else if(delims.indexOf(c) <= 5) {
    			math.push(ptr);
    		}

    	}
    	
    	while(nums.size() > 1) {
    		nums.push(nums.pop()+nums.pop());
    	}
    	
    	return nums.pop();  
    }
    
    
    // this helper method gets the recursive expression 
    private static String getExpRec(StringTokenizer st, String in) { 	
    	
    	Stack<String> parends = new Stack<String>();
    	parends.push(in);
    	String out = "";
		
    	while(!(parends.isEmpty()) && st.hasMoreTokens()) {
			String next = st.nextToken();
	    	if(next.equals("(") || next.equals("[")) {
	    		parends.push(next);
	    	}
	    	else if(next.equals(")") || next.equals("]")) {
	    		parends.pop();
	    	}
			if(parends.isEmpty()) {
				break;
			}
			out += next;
		}
		return out;
	} 	
    
    // helper method that removes any spaces in between
    private static String noSpace(String x) {
    	if(x.length() == 0) {
    		return "";
    	}
    	else if(x.substring(0,1).equals(" ")) {
    		return noSpace(x.substring(1));
    	}
    	else {
    		return x.substring(0,1) + noSpace(x.substring(1));
    	}
    }
}

    