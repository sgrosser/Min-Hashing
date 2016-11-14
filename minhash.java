import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

/*
 * Author: Stefan Grosser
 * Date: Nov 14, 2016
 * Description: This program approximates the Jaccard similarity between two numerical files
 * by use MinHashing.  
 * By calculation
 */

public class minhash {
	public static final int numHashFunctions = 2280; //Adjust this value only
	public static int[][] hashFunctions = new int[numHashFunctions][2];
	public static final int P = 100003; //Prime greater than max number in the two files
	
	
	public static void main(String[] args) throws IOException {
		FileReader fr1 = new FileReader("numbers1.txt");
		BufferedReader br1 = new BufferedReader(fr1);
		FileReader fr2 = new FileReader("numbers2.txt");
		BufferedReader br2 = new BufferedReader(fr2);
		
		generateHashFunctions();
		
		//Get shingles for each of the two files
		//Use sets to avoid repetitions
		
		TreeSet<Integer> n1 = new TreeSet<Integer>();
		TreeSet<Integer> n2 = new TreeSet<Integer>();
		
		String st;
		while((st= (br1.readLine())) != null){
			String[] n = st.split(" ");
			for(String s : n){
				int nmber = Integer.parseInt(s);
				n1.add(nmber);
			}
		}
		
		while((st= (br2.readLine())) != null){
			String[] n = st.split(" ");
			for(String s : n){
				int nmber = Integer.parseInt(s);
				n2.add(nmber);
			}
		}
		
		//Now we generate the fingerprints by getting the min hashes for each has function
		ArrayList<Integer> fingerprint1 = new ArrayList<Integer>();
		ArrayList<Integer> fingerprint2 = new ArrayList<Integer>();
		
		for(int i = 0; i < numHashFunctions; i++){
			int minHash = P;
			
			for(int num : n1){
				int h = hash(num,i);
				if(h < minHash)
					minHash = h;
			}
			fingerprint1.add(minHash);
		}
		
		for(int i = 0; i < numHashFunctions; i++){
			int minHash = P;
			
			for(int num : n2){
				int h = hash(num,i);
				if(h < minHash)
					minHash = h;
			}
			fingerprint2.add(minHash);
		}
		
		//System.out.println(fingerprint1.toString() +"\n" + fingerprint2.toString());
		
		//Compute the Jaccard Similarity between the two fingerprints
		double jaccSim = calculateJaccard(fingerprint1, fingerprint2);
		System.out.println("We have a similarity: " + jaccSim);
		
		//Calculate the actual Jaccard similarity
		System.out.println("The actual Jaccard Similarity is: " + calculateJaccard(n1, n2));
		
	}
	
	public static double calculateJaccard(ArrayList<Integer> a, ArrayList<Integer> b){
		int match = 0;
		
		for(int i = 0; i< a.size(); i++){
			if(b.get(i) == a.get(i))
				match++;
			
		}
		
		return (double) match / Math.max(a.size(), b.size());
	}
	
	public static double calculateJaccard(TreeSet<Integer> a, TreeSet<Integer> b){
		int intersection = 0;
		
		for(int i : a){
			if(b.contains(i) )
				intersection++;
			
		}
		return (double) intersection/(a.size()+b.size()-intersection);
	}
	
	public static void generateHashFunctions(){
		ArrayList<Integer> hashVals = new ArrayList<Integer>();
		
		Random rand = new Random();
		for(int i = 0; i< numHashFunctions; i++){
			hashFunctions[i][0] = rand.nextInt(P);
			if(!hashVals.contains(hashFunctions[i][0]))
				hashVals.add(hashFunctions[i][0]);
			else{
				i--;
			}
		}
		hashVals.clear();
		for(int i = 0; i< numHashFunctions; i++){
			hashFunctions[i][1] = rand.nextInt(P)+1;
			if(!hashVals.contains(hashFunctions[i][1]))
				hashVals.add(hashFunctions[i][1]);
			else{
				i--;
			}
		}
	}

	public static int hash(int n, int i){
		int code = n;
		if(code < 0) code *= -1;

		BigInteger x = BigInteger.valueOf(hashFunctions[i][1]);
		BigInteger y = BigInteger.valueOf(code);
		x = x.multiply(y).add(BigInteger.valueOf(hashFunctions[i][0])).mod(BigInteger.valueOf(P));
		int mult = x.intValue();
		


		return mult;
	}
}
