package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author auk11 - CS112 
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param link1 First input polynomial (front of polynomial linked list)
	 * @param link2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node link1 = poly1;
		Node link2 = poly2;
		
		Node Sum = null;
		Node Sumlink = null;
		
		
		
		if (link2 == null) {
			return link1; // returns poly1 if poly2 is 0
		}
		if (link1 == null) {
			return link2; // returns poly2 if poly1 is 0
		}
		while (link1 != null && link2 != null) {
			
			// adds the coefficients of same degree
			
			if (link1.term.degree == link2.term.degree) {
				if(link1.term.coeff + link2.term.coeff != 0) {
					if (Sum == null) {
						// if there is nothing in the sum add the sum to the new linked list
						Sum = new Node((link1.term.coeff + link2.term.coeff), link1.term.degree,null);
						Sumlink = Sum;
					}
					else {
						// if there is already values in sum, add this sum to the list
						Sumlink.next = new Node((link1.term.coeff + link2.term.coeff), link1.term.degree,null);
						Sumlink = Sumlink.next;
						
					}
				}
				
				link1 = link1.next; //Moves on to the next values
				link2 = link2.next; //Moves on to the next values
				
			}
			else if (link1.term.degree < link2.term.degree) {
				// this sorts the list depending on the degree
				if (Sum == null) {
					Sum = new Node(link1.term.coeff, link1.term.degree, null);
					Sumlink = Sum;
				}
				else {
					Sumlink.next = new Node(link1.term.coeff, link1.term.degree, null);
					Sumlink = Sumlink.next;
					
				}
				link1 = link1.next;
			}
			else if (link1.term.degree > link2.term.degree) {
				// Sorts the list depending on the degree
				if (Sum == null) {
					Sum = new Node(link2.term.coeff, link2.term.degree, null);
					Sumlink = Sum;
				}
				else {
					Sumlink.next = new Node(link2.term.coeff, link2.term.degree, null);
					Sumlink = Sumlink.next;
					
				}
				link2 = link2.next;
			}
		}
		while (link1 != null) {
			// if link1 is bigger than link 2, the rest of link1 is added to the sum
			if (Sumlink == null) {
				Sum = new Node(link1.term.coeff, link1.term.degree, null);
				Sumlink = Sum;
			}
			else {
			Sumlink.next = new Node(link1.term.coeff,link1.term.degree,null);
			Sumlink = Sumlink.next;
			link1 = link1.next;
		}
		}
		while (link2 != null) {
			// if link2 is bigger than link 1, the rest of link1 is added to the sum
			if (Sumlink == null) {
				Sum = new Node(link2.term.coeff,link2.term.degree,null);
				Sumlink = Sum;
			
			}
			else {
			Sumlink.next = new Node(link2.term.coeff,link2.term.degree,null);
			Sumlink = Sumlink.next;
			link2 = link2.next;
		}
		}
		return Sum;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param link1 First input polynomial (front of polynomial linked list)
	 * @param link2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node link1 = poly1;
		Node link2 = poly2;
		Node Product = null;
		
		while (link1 != null) {
			while (link2 != null) {
				
				//Starts to multiply the first variable of poly1 with all of poly2
				
				Node set = new Node((link1.term.coeff*link2.term.coeff),(link1.term.degree+link2.term.degree),null);
				Product = add(Product,set);
				link2 = link2.next;
				
			}
			
			link1 = link1.next;
			link2 = poly2; //Repeats the process for everyother variable of poly1
		}
		
		return Product;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		double sum = 0;
		float coefficient ;
		int degree;
		Node link = poly;
		while (link != null) {
			coefficient = link.term.coeff;
			degree = link.term.degree;
			
			sum = sum + coefficient * Math.pow(x, degree);
			link = link.next;
		}
		return (float) sum;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
