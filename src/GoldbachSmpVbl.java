//******************************************************************************
//
// File:    GoldbachSmpVbl.java
// Using Package: edu.rit.pj2.vbl
//
// This Java source file is copyright (C) 2015 by Utkarsh Bhatia. All rights
// reserved. For further information, contact the author, Utkarsh Bhatia, at
// uxb9472@rit.edu.
//
// This class is implementing Vbl interface as given in the PJ2 parallel java library
// made by Professor Alan Kaminsky, the given reference to parallel java library and its sample
// code can be referenced from http://www.cs.rit.edu/~ark/bcbd/#source and http://www.cs.rit.edu/~ark/pj2.shtml
// This class is used for the reduction of variables
// running in parallel cores in GoldbashSmp class. The reduce method implemented in this
// class compares varies variables and reduces the output to the desired output according
// to the biggest number found from all the parallel running cores.
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

import edu.rit.pj2.Vbl;

/**
 * Class GoldbachSmpVbl provides a reduction variable for the Goldbachs conjecture shared by
 * multiple threads executing a parallel for loop. Class GoldbachSmpVbl is using 
 *  variables declared in the given class.
 * 
 * Class GoldbachSmpVbl supports the parallel reduction pattern. Each
 * thread creates a thread-local copy of the shared variable by calling the
 * threadLocal() method of class. Each thread performs operations on
 * its own copy, without needing to synchronize with the other threads. At the
 * end of the parallel statement, the thread-local copies are automatically
 * reduced together, and the result is stored in the original shared
 * variable. The reduction is performed by the shared variable's reduce() method.
 * The reduction method is declared is overriden and the reduction operation
 * is specified in the given class. The reduction is carried out according to 
 * the given requirements of the Goldbachs conjecture.
 *
 * @author  Utkarsh Bhatia
 * @version 29-Sept-2015
 */
/**
 * @author maddoc
 *
 */
public class GoldbachSmpVbl implements Vbl{

	//Declaring the variables to be used to set the required values
	//even number of which Goldbachs Conjecture is being tested
	private BigInteger number;
	//the small prime number got from Goldbachs conjecture
	private BigInteger prime1;
	//the large prime number got from Goldbachs conjecture
	private BigInteger prime2;
	
	/**
	 * @param number : the constructor sets the number in the object
	 * @param prime1 : the constructor sets the small prime in the object
	 * @param prime2 : the constructor sets the large prime in the object
	 */
	public GoldbachSmpVbl(BigInteger number, BigInteger prime1, BigInteger prime2) {
		super();
		this.number = number;
		this.prime1 = prime1;
		this.prime2 = prime2;
	}

	/**
	 * Reduce the given shared variable into this shared variable. The two
	 * variables are combined together, and the result is stored in this shared
	 * variable. The reduce() method does not need to be multiple
	 * thread safe (thread synchronization is handled by the caller).
	 * 
	 * Class GoldbachSmpVbls reduce() method compares the given variables prime1
	 * and the threads prime1 and stores the value which holds the biggest prime1, in
	 * accordance with the given requirements
	 *
	 * @param arg0
	 *            Shared variable.
	 *
	 * returns void
	 * 
	 *   @exception  ClassCastException
	 *     Thrown if the class of arg0 is not
	 *     compatible with the class of this shared variable.
	 */
	@Override
	public void reduce(Vbl arg0) {
		GoldbachSmpVbl compareVbl = (GoldbachSmpVbl)arg0;
		if(this.prime1.compareTo(compareVbl.prime1) == -1){
			this.copy(compareVbl);
		}else if(compareVbl.prime1.compareTo(this.prime2)==0){
			if(this.number.compareTo(compareVbl.number) == -1){
				this.copy(compareVbl);
			}
		}
		else{}
	}

	   /**
	    * Set this shared variable to the given shared variable.
	    *
	    * @param  arg0  Shared variable.
	    */
	@Override
	public void set(Vbl arg0) {
		this.copy((GoldbachSmpVbl)arg0);
	}
	
	/**
	 * Create a clone of this GoldbachSmpVbl.
	 *
	 * @return Clone.
	 * 
	 * @throws RuntimeException
	 */
	public Object clone(){
		try{
			GoldbachSmpVbl add = (GoldbachSmpVbl) super.clone();
			add.copy(this);
			return add;
		}
		catch(Exception e){
			throw new RuntimeException("Shouldn't happen", e);
		}
	}
	
	/**
	 * Make this GoldbachSmpVbl variables be a deep copy 
	 * of the given GoldbachSmpVbl variables.
	 *
	 * @param GoldbachSmpVbl
	 *            GoldbachSmpVbl to copy.
	 *
	 * @return void
	 */
	private void copy(GoldbachSmpVbl addVbl) {
		this.number = addVbl.number;
		this.prime1 = addVbl.prime1;
		this.prime2 = addVbl.prime2;
	}

	/**
	 * @return number
	 * getter of the number on which Goldbachs conjecture has been run
	 */
	public BigInteger getNumber() {
		return number;
	}

	/**
	 * @param number
	 * setter of the number on which Goldbachs conjecture has been run
	 */
	public void setNumber(BigInteger number) {
		this.number = number;
	}

	/**
	 * @return number
	 * getter of the prime1,i.e. smaller prime which is got by 
	 * running the Goldbachs conjecture on the number	 
	 */
	public BigInteger getPrime1() {
		return prime1;
	}
	
	/**
	 * @param prime1
	 * setter of the prime1,i.e. smaller prime which is got by 
	 * running the Goldbachs conjecture on the number
	 */
	public void setPrime1(BigInteger prime1) {
		this.prime1 = prime1;
	}

	/**
	 * @return number
	 * getter of the prime2,i.e. larger prime which is got by 
	 * running the Goldbachs conjecture on the number	 
	 */
	public BigInteger getPrime2() {
		return prime2;
	}
	
	/**
	 * @param prime2
	 * setter of the prime2,i.e. larger prime which is got by 
	 * running the Goldbachs conjecture on the number
	 */
	public void setPrime2(BigInteger prime2) {
		this.prime2 = prime2;
	}
	
	/**
	 * @param bigNum: takes the number on which Goldbachs conjecture is run
	 * @param bigPrime1: the first prime number obtained from Goldbachs conjecture
	 * @param bigPrime2: the second prime number obtained from Goldbachs conjecture
	 * 
	 * @throws
	 * 		throws an Exception if the input does not matches the expected input of BigInteger
	 */
	public void reduceToPrime(BigInteger bigNum, BigInteger bigPrime1, BigInteger bigPrime2){
		//if the current small prime number is bigger than current cores smallest prime
		//number satisfying the Goldbachs conjecture, then replace
		if(this.getPrime1().compareTo(bigPrime1) == -1){
			this.setNumber(bigNum);
			this.setPrime1(bigPrime1);
			this.setPrime2(bigPrime2);
			//if the current small prime number is equal than current cores smallest prime
			//number satisfying the Goldbachs conjecture, then check further
		} else if(this.getPrime1().compareTo(bigPrime1) == 0){
			//find the bigger number which satisfies the Goldbachs conjecture having the same prime
			//as the smaller number and select the bigger number
			if(this.getNumber().compareTo(bigNum) == -1){
				this.setNumber(bigNum);
				this.setPrime1(bigPrime1);
				this.setPrime2(bigPrime2);
			}
			//if the smallest prime is not bigger or equal, do nothing
		} else{	}
	}
	
}
