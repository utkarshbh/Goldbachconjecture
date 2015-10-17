//******************************************************************************
//
// File:    GoldbachSeq.java
// Using Package: edu.rit.pj2
//
// This Java source file is copyright (C) 2015 by Utkarsh Bhatia. All rights
// reserved. For further information, contact the author, Utkarsh Bhatia, at
// uxb9472@rit.edu.
//
// This class is extending Task class as given in the PJ2 parallel java library
// made by Professor Alan Kaminsky, the given reference to parallel java library and its sample
// code can be referenced from http://www.cs.rit.edu/~ark/bcbd/#source and http://www.cs.rit.edu/~ark/pj2.shtml
// This class is used for running the while loop
// which runs sequentially. It also accepts variable cores which can be specified
// by the user. It runs the complete code in sequential manner and gets the desired
// input according to the user.
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

import edu.rit.pj2.Task;

/**
 * Class GoldbachSmp is a sequential program that performs Goldbachs conjecture.
 * It finds the largest prime in all the smallest prime number of even numbers. 
 * 
 * Using: java pj2 edu.rit.pj2 from http://www.cs.rit.edu/~ark/pj2.shtml
 * lowerBound = Lower Bound of the input variable
 * upperBound = Upper Bound of the input variable
 *
 * @author  Utkarsh Bhatia
 * @version 29-Sept-2015
 */
public class GoldbachSeq extends Task{

	//Declaring the variables to be used to set the required values
	//checks and sets the lowerbound of the input argument
	BigInteger lowerBound;
	//checks and sets the upperbound of the input argument
	BigInteger upperBound;
	
	/* (non-Javadoc)
	 * @see edu.rit.pj2.Task#main(java.lang.String[])
	 * 
	 * main method in accordance with the pj2 library for running the Goldbachs conjecture
	 * and finding the largest prime number of the smallest prime using Goldbachs conjecture in
	 * the given range of numbers
	 * 
	 * @param: accepts 2 parameters. first args[0] is the lowerbound and args[1] is the
	 * 			upperbound of the range in which Goldbachs conjecture is tested
	 * 
	 * @throws Exception 
	 * 				throws Exception when the input is not according to the requirement
	 */
	public void main(String[] args) throws Exception{	
		try{
			//validating if the length of the input argument array is 2. We need 2 inputs, as lower
			//bound and upper bound. If 2 arguments are not received then Exception is thrown
			if(args.length==2){
				lowerBound = new BigInteger(args[0]);
				upperBound = new BigInteger(args[1]);
				//checks for any illegal input in the arguments entered
				if(usage()){
					BigInteger[] returnPrimeVals = new BigInteger[2];
					BigInteger maxPrimeVal = new BigInteger("2");
					String returnThis = "";
					//traverses over the given input range from lower bound to the upper bound
					// to check Goldbachs conjecture over the even numbers
					while(lowerBound.compareTo(upperBound) != 1){
						//calls the method to find desired 2 prime numbers which fulfill Goldbachs conjecture
						returnPrimeVals = getPrimeaddition(lowerBound);
						//checks if the smallest prime receieved is the biggest in all the small prime numbers or not
						if(maxPrimeVal.compareTo(returnPrimeVals[0]) == -1 || returnPrimeVals[0].compareTo(maxPrimeVal) == 0){
							maxPrimeVal = returnPrimeVals[0];
							returnThis = lowerBound + " = " + returnPrimeVals[0] + " + " + returnPrimeVals[1];
						}
						lowerBound = lowerBound.add(new BigInteger("2"));
					}
					System.out.println(returnThis);
				}
				//throws the exception if both the arguments are not entered by the user
			} else{
				System.err.println ("Number of input arguments should exactly be 2.");
			      throw new IllegalArgumentException();
			}
			//catches the exception if the input is undesired
		} catch(Exception e){
			System.err.println ("Usage: java GoldbachSmp running with pj2. Arguments taken: <lowerB> <upperB>");
		    System.err.println ("<lowerB> = lower Bound");
		    System.err.println ("<upperB> = upper Bound");
		    System.err.println ("Remember: lower Bound should be smaller than the upper Bound");
			System.err.print("The input has been entered incorrectly. Please check and enter correct arguments. Exception: " + e);
		}
	}

	/**
	 * The given method takes in the even number and finds 2 prime number whose addition
	 * provides the input number. This satisfies the Goldbachs conjecture.
	 * 
	 * @param lb : taken in the BigInteger over which Goldbachs conjecture has to run
	 * @return : Returns an array with the even number over which Goldbachs conjecture has been run
	 * 				with small prime and big prime in the array indexes 1 and 2
	 */
	private static BigInteger[] getPrimeaddition(BigInteger lb) {
		//Declaring an array to store the number on which Goldbachs conjecture is being run
		// and its respective prime numbers
		BigInteger[] primeVals = new BigInteger[2];
		//finds the next probable prime after 2, since 2 is also a prime number
		//but is even so calculations regarding 2 are excluded
		BigInteger bigPrime1 = (new BigInteger("2")).nextProbablePrime();
		//setting the next prime number value to 0
		BigInteger bigPrime2 = new BigInteger("0");
		//run the loop finding all prime numbers until they are 
		//smaller than the number on which Goldbachs conjecture is being run
		while(bigPrime1.compareTo(lb) == -1){
			//if the first prime number has been found
			if(bigPrime1.isProbablePrime(1)){
				//find the second number with respect to the first prime number
				bigPrime2 = lb.subtract(bigPrime1);
				//checks if the second number found is prime or not
				if(bigPrime2.isProbablePrime(100)){
					primeVals[0] = bigPrime1; primeVals[1] = bigPrime2;
					//if both the prime numbers are found, then break the loop
					break;
				}
			}
			//if the current prime number does not satisfy the Goldbachs conjecture then find the next prime number
			// smaller than the number on which Goldbachs conjecture is being tested
			bigPrime1 = bigPrime1.nextProbablePrime();
		}
		//return the array with the desired values, i.e. the number, the small prime and the large prime number
		return primeVals;
	}
	
	/**
	 * @return returns true if the input satisfies the requirements as given, the lowerbound should be 
	 * smaller than the upper bound and both the lower bound and the upper bound should be divisible by 2,i.e. even.
	 * @throws Exception
	 * 				checks the input arguments and throws Exception if the input is not according to the requirement
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