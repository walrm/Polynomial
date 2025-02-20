package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {

	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage
	 * format of the polynomial is:
	 * 
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * 
	 * with the guarantee that degrees will be in descending order. For example:
	 * 
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * 
	 * which represents the polynomial:
	 * 
	 * <pre>
	 * 4 * x ^ 5 - 2 * x ^ 3 + 2 * x + 3
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients
	 *         and degrees read from scanner
	 */
	public static Node read(Scanner sc) throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	private static Node addEnd(float coeff, int degree, Node n) {
		//Set to Front of Linked List if empty
		if (n == null) {
			n = new Node(coeff, degree, null);
			
		//Traverse and add new Node to end of linked list
		}else {
			for(Node ptr=n; ptr!=null; ptr = ptr.next) {
				
				//At end of linked list, create new node 
				if(ptr.next == null) {
					ptr.next = new Node(coeff, degree, null);
					break;
					
				//FOR MULTIPLY: if the next term has greater degree, place in between
				}else if(degree<ptr.next.term.degree) {
					ptr.next = new Node(coeff, degree, ptr.next);
					break;
				}
			}
		}
		
		//return new, changed n value
		return n;
	}

	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		
		//New linked list comprised of polynomial sum terms
		Node sum = null; 
		
		//Index pointer to iterate through first polynomial and second polynomial
		Node ptr1 = poly1; 
		Node ptr2 = poly2; 

		//Return neither if both null or one polynomial if the other is null
		if(ptr1 == null && ptr2 == null) { 
			sum = null;
		} 
		else {
			
			//Traverse through both polynomials at the same time
			while (ptr1 != null || ptr2 != null) { 
				
				//Adding the rest of polynomial one when polynomial two is already traversed through
				if (ptr2 == null && ptr1 != null) {
					sum = addEnd(ptr1.term.coeff, ptr1.term.degree, sum);
					ptr1 = ptr1.next;
					
				//Adding the rest of polynomial two when polynomial one is already traversed through
				} else if (ptr2 != null && ptr1 == null) {
					sum = addEnd(ptr2.term.coeff, ptr2.term.degree, sum);
					ptr2 = ptr2.next;
					
				//Adding coefficients when the the degrees are equal
				} else if (ptr1.term.degree == ptr2.term.degree) {
					
					//Do not add to sum Linked List if the addition of sums equals zero 
					if (ptr1.term.coeff + ptr2.term.coeff !=0 ){
						sum = addEnd(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, sum);
					}
					
					//Traversing through the two polynomials at the same time
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
					
				//Add lower degree term to end of sum linked list and iterate to next Node
				//of the added polynomial linked list.
				} else if (ptr1.term.degree > ptr2.term.degree) {
					sum = addEnd(ptr2.term.coeff, ptr2.term.degree, sum);
					ptr2 = ptr2.next;
				} else if (ptr2.term.degree > ptr1.term.degree) {
					sum = addEnd(ptr1.term.coeff, ptr1.term.degree, sum);
					ptr1 = ptr1.next;
				}
			}
		}
		return sum;
	}

	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		//Result linked list that represent product (answer) 
		Node product = null;
		
		//Temporary linked list to run through product to match up equivalent degrees.
		Node temp = null;
		Node prev = null;
		
		//Product of term and degree 
		float tempC;
		int tempD;
		
		//boolean to tell whether to add to linked list or modify existing coeff value
		boolean equal = false;
		
		//Return 0 if either polynomial is null
		if(poly1 == null || poly2 == null) {
			return null;
		}
		
		//Nested for loop to multiply each term with each other
		for(Node ptr1 = poly1; ptr1!=null; ptr1= ptr1.next) {
			for(Node ptr2 = poly2; ptr2!=null; ptr2= ptr2.next) {
				equal = false;
				temp = product;
				prev = null;
				
				//Multiplying each term with each other
				tempC = ptr1.term.coeff*ptr2.term.coeff;
				tempD = ptr1.term.degree+ptr2.term.degree;
				
				//Run through product to add coefficients of same degree
				while(temp!=null) {
					if(tempD == temp.term.degree) {
						temp.term.coeff += tempC;
						if (temp.term.coeff == 0 ) {
							prev.next = temp.next;
						}
						equal = true;
					}
					prev = temp;
					temp = temp.next;
				}
				
				//If no equivalent degree exists, add to the end of product instead
				if (!equal && tempC != 0) {
					product = addEnd(tempC, tempD, product);
				}
			}
		}
		return product;
	}

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x    Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		float sum = 0;
		Node ptr = poly;
		while(ptr != null) {
			sum += ptr.term.coeff * Math.pow(x,ptr.term.degree);
			ptr = ptr.next;
		}
		return sum;
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
		for (Node current = poly.next; current != null; current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}
}
