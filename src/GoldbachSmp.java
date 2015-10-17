//******************************************************************************
//
// File:    GoldbackSmp.java
// Using Package: edu.rit.pj2
//
// This Java source file is copyright (C) 2015 by Utkarsh Bhatia. All rights
// reserved. For further information, contact the author, Utkarsh Bhatia, at
// uxb9472@rit.edu.
//
// This class is extending Task class as given in the PJ2 parallel java library
// made by Professor Alan Kaminsky, the given reference to parallel java library and its sample
// code can be referenced from http://www.cs.rit.edu/~ark/bcbd/#source and http://www.cs.rit.edu/~ark/pj2.shtml
// This class is used for running the parallelfor loop
// which works in multiple cores machine. It also accepts variable cores which can be specified
// by the user. It makes use of the reduction variable which is reduced to the desired variable
// according to the user. It is using dynamic scheduling to schedule the numbers between input 
// range to various cores.
//
// Details for PJ2 library as available on http://www.cs.rit.edu/~ark/pj2.shtml
// The library has been made available to General Public under GPL license by 
// Professor Alan Kaminsky. The copyright (C) 2015 to pj2 library is held by Alan Kaminsky.
// PJ2 is free software; you can redistribute it and/or modify it under the terms of
// the GNU General Public License as published by the Free Software Foundation;
// either version 3 of the License, or (at your option) any later version.
//
// PJ2 is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//
// A copy of the GNU General Public License is provided in the file gpl.txt. You
// may also obtain a copy of the GNU General Public License on the World Wide
// Web at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

import java.math.BigInteger;

import edu.rit.pj2.LongLoop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.LongVbl;

/**
 * Class GoldbachSmp is a multicore parallel program that performs Goldbachs conjecture.
 * It finds the largest prime in all the smallest prime number of even numbers. 
 * 
 * Using: java pj2 edu.rit.pj2 from http://www.cs.rit.edu/~ark/pj2.shtml
 * lowerBound = Lower Bound of the input variable
 * upperBound = Upper Bound of the input variable
 *
 * @author  Utkarsh Bhatia
 * @version 29-Sept-2015
 */
public class GoldbachSmp extends Task{

	//Declaring the variables to be used to set the required values
	//checks and sets the lowerbound of the input argument
	BigInteger lowerBound;
	//checks and sets the upperbound of the input argument
	BigInteger upperBound;
	//calculates the difference between upper and lower bound of the
	//input variable, so that long loop can traverse over it
	long difference;
	
	/* (non-Javadoc)
	 * @see edu.rit.pj2.Task#main(java.lang.String[])
	 * 
	 * main method in accordance with the pj2 library for running the Goldbachs conjecture
	 * and finding the largest prime number of the smallest prime using Goldbachs conjecture in
	 * the given range of numbers. The Goldbachs conjecture is run in multiple cores using the
	 * pj2 library's parallelfor. The pj2 library is developer by Professor Alan Kaminsky and
	 * is available under the GNU General Public License.
	 * 
	 * @param: accepts 2 parameters. first arg0[0] is the lowerbound and arg0[1] is the
	 * 			upperbound of the range in which Goldbachs conjecture is tested
	 * 
	 *  @throws Exception 
	 * 				throws Exception when the input is not according to the requirement
	 */
	@Override
	public void main(String[] arg0) throws Exception {
		try{
			//validating if the length of the input argument array is 2. We need 2 inputs, as lower
			//bound and upper bound. If 2 arguments are not received then Exception is thrown
			if(arg0.length==2){
				lowerBound = new BigInteger(arg0[0]);
				upperBound = new BigInteger(arg0[1]);
				//checks for any illegal input in the arguments entered
				if(usage()){
					//difference between upper bound and lower bound is calculated and stored in long variable
					long difference = upperBound.subtract(lowerBound).longValue();
					//setting up the global GoldbachSmpVbl for reduction
					GoldbachSmpVbl reduceVbl = new GoldbachSmpVbl(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
					//running the parallel for on given number of cores for the difference iterations(the difference
					// is the numerical difference between the upper bound number and the lower bound number)
					parallelFor(0, difference).schedule(dynamic).exec(new LongLoop() {
						//Thread specific GoldbachSmpVbl variable
						GoldbachSmpVbl addVbl;		
						
						/* (non-Javadoc)
						 * @see edu.rit.pj2.LoopBody#start()
						 * Overriding the threads start method
						 */
						@Override
						public void start()
			            {
							addVbl = threadLocal(reduceVbl);
			            }
						
						/* (non-Javadoc)
						 * @see edu.rit.pj2.LongLoop#run(long)
						 * Overriding the run method with arg0 as the index
						 * @throws Exception
						 */
						@Override
						public void run(long arg0) throws Exception {
							//finds the next number if the given range by adding the current index of the 
							//loop to the lowerbound value
							BigInteger bigNum = lowerBound.add(new BigInteger(String.valueOf(arg0)));
							//finds the next probable prime after 2, since 2 is also a prime number
							//but is even so calculations regarding 2 are excluded
							BigInteger bigPrime1 = (new BigInteger("2")).nextProbablePrime();
							//setting the next prime number value to 0
							BigInteger bigPrime2 = new BigInteger("0");
							//if the next number found is prime, then go inside the loop
							//to run the goldbachs conjecture on the found number
							if(bigNum.mod(new BigInteger("2")).intValue()==0){
								//run the loop finding all prime numbers until they are 
								//smaller than the number on which Goldbachs conjecture is being run
								while(bigPrime1.compareTo(bigNum) == -1){
									//if the first prime number has been found
									if(bigPrime1.isProbablePrime(1)){
										//find the second number with respect to the first prime number
										bigPrime2 = bigNum.subtract(bigPrime1);
										//checks if the second number found is prime or not
										if(bigPrime2.isProbablePrime(100)){
											//if both the prime numbers are found, then break the loop
											break;
										}
									}
									//if the current prime number does not satisfy the Goldbachs conjecture then find the next prime number
									// smaller than the number on which Goldbachs conjecture is being tested
									bigPrime1 = bigPrime1.nextProbablePrime();
								}
								//assigns the values of the largest first prime number to the object of Vbl
								addVbl.reduceToPrime(bigNum, bigPrime1, bigPrime2);
							}
						}
					});
					//show the required equation found by the parallel reduction
					System.out.println(reduceVbl.getNumber() + " = " + reduceVbl.getPrime1() + " + " + reduceVbl.getPrime2());
				}
			}
			//if the input arguments are not valid, throw an exception
			else{
				System.err.println ("Number of input arguments should exactly be 2.");
			      throw new IllegalArgumentException();
			}
			//if the input arguments are not valid, throw an exception
		} catch(Exception e){
			System.err.println ("Usage: java GoldbachSmp running with pj2. Arguments taken: <lowerB> <upperB>");
		    System.err.println ("<lowerB> = lower Bound");
		    System.err.println ("<upperB> = upper Bound");
		    System.err.println ("Remember: lower Bound should be smaller than the upper Bound");
			System.err.print("The input has been entered incorrectly. Please check and enter correct arguments. Exception: " + e);
		}
	}
	
	/**
	 * @return returns true if the input satisfies the requirements as given, the lowerbound should be 
	 * smaller than the upper bound and both the lower bound and the upper bound should be divisible by 2,i.e. even.
	 * If any of the given conditions is not satisfied, throw an exception
	 * @throws Exception
	 * 					checks the input arguments and throws Exception if the input is not according to the requirement
	 */
	private boolean usage() throws Exception{
		//checks if the lower bound is smaller than the upper bound
		if(upperBound.compareTo(lowerBound) == -1){
			System.err.println ("Lower bound can not be greater than upper bound");
			throw new IllegalArgumentException();
		}
		//checks if the lower bound number is even or not
		if(lowerBound.mod(new BigInteger("2")).intValue() != 0){
			System.err.println("Lower bound should be an even number");
			throw new IllegalArgumentException();
		}
		//checks if the upper bound is even or not
		if(upperBound.mod(new BigInteger("2")).intValue() != 0){
			System.err.println("Upper bound should be an even number");
			throw new IllegalArgumentException();
		}
		//returns true if all the conditions are fulfilled
		return true;
	}
}
