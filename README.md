# Goldbachconjecture
Goldbachs Conjecture states that every even integer greater than 2 is the sum of two prime numbers. 
even number = prime1 + prime2
The following programs assumes Goldbachs Conjecture to be true. 
The aim is to have an input of a range of numbers and run Goldbachs Conjecture on all the even numbers in the given range and find a number n, such that
n = prime1 + prime2,
where prime1 is the lowest in all the even numbers on which Goldbachs Conjecture has been run.
The following following source files contains GoldbachSeq to run the file on single core using pj2 java library made by Professor Alan Kaminsky available under GPU General Public license.
It runs Goldbachs conjecture on a given range of Integers, entered as command line arguments, and finds the number with the smallest first prime number from the given input range.
The second file GoldbachSmp runs the same problem on multiple cores in the machine by using the pj2 java library made by Professor Alan Kaminsy available under GPU General Public license.
For further references regarding parallel java library refer to: https://www.cs.rit.edu/~ark/pj2.shtml
Utkarsh Bhatia
