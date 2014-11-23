package SampleApplications;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import Keys.*;
import BTree.BTree;
import BTree.BTreeHandler;
import BTree.KeyValueMap;
import Datastore.*;
/**
 * This is a sample application that demonstrates insert, delete, search, etc. functionalities. This sample application 
 * uses a RankKey primary key and allows the user to choose to insert/delete/search.
 * @author May Bill
 *
 */
public class FunctionalitySampleApplication {
	/**
	 * This main method drives the program in command line Mode
	 * @param args
	 */
	public static void main(String [] args){
		System.out.println("Welcome to our program!");
		
		//Enter the path to the .csv file
		System.out.println(Datastore.inputDataFileName + " will be used");
		if(!fileExists(Datastore.inputDataFileName)){
			System.out.println("File could not be found");
			System.exit(0);
		}
		System.out.println("Creating datastore...");
		//create datastore from input csv
		boolean successful = Datastore.createDataStore(Datastore.inputDataFileName);
		if(!successful){
			System.out.println("Error with creating datastore from csv file");
			System.exit(0);
		}else{
			System.out.println("Datastore was successfully created");
		}

		System.out.println("An instance of the RankKey will be used as the primary key");
		System.out.println("RankKey consists of a primary key built from the rank and year of each song");

		//BTree is created correctly
		BTree btree = new BTree(2);
		ArrayList<Record> records = Datastore.readDataStore();
		KeyValueMap mapToData = new KeyValueMap();
		int [] keyvals = {0,1};
		try {
			ArrayList<Key> keys = KeyFactory.createKeys(records, keyvals);
			for(int i = 0; i <keys.size(); i++){
				btree.insertKey(keys.get(i));
				mapToData.insert(keys.get(i), records.get(i).getOffset());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		System.out.println("Please enter a number to execute one of the following commands");
		System.out.println("Note: For ease of use, random data will be generated for some commands");
		System.out.println("0: Insert");
		System.out.println("1: Search");
		System.out.println("2: Delete");
		System.out.println("3: SearchBetween");
		System.out.println("4: Exit");

		
		String input = readLine();
		
		while(true){
			int command = 0;
			boolean proper = true;
			try{
				command = Integer.parseInt(input);
				if(command <0 || command >4){
					System.out.println("Invalid number entered");
					proper = false;
				}
			}catch(Exception e){
				System.out.println("Invalid input");
				proper = false;
			}
			if(proper==false){
				input = readLine();
				continue;
			}
			
			switch(command){
				case 0:
					System.out.println("Insert was called");
					//not interactive
					Record temp = createRandomRecord();
					RankKey tempkey = new RankKey(temp.year, temp.rank);
					boolean inTree = btree.contains(tempkey);
					
					btree.insertKey(tempkey);
					System.out.println("The following record was just inserted");
					System.out.println(temp);
					
					//write to the datastore if the file doesn't contain the tempkey
					if(!inTree){
						int offset = Datastore.insertEntrytoEnd(temp);
						mapToData.insert(tempkey, offset);
					}
					break;
				case 1:
					System.out.println("Search was called");
					//interactive
					System.out.println("Please enter a key to search for within the tree");
					
					int [] inputVals;
					inputVals = getValidInput();
					
					tempkey = new RankKey(inputVals[0],inputVals[1]);
					boolean withinTree = btree.contains(tempkey);
					if(withinTree){
						System.out.println("year: " + inputVals[0] + ", rank: " + inputVals[1] +" is within the tree");
						Record record = mapToData.getRecord(tempkey);
						if(record==null){
							int offset = mapToData.getKey(tempkey);
							record =Datastore.getRecord(offset);
							mapToData.insertRecord(tempkey, record);
						}
						System.out.println(record);
					}
					else{
						System.out.println("year: " + inputVals[0] + ", rank: " + inputVals[1] +" is not within the tree");
					}
							
					break;
				case 2:
					System.out.println("Delete was called");
					//interactive
					System.out.println("Please enter a key to delete from the tree");

					inputVals = getValidInput();
					
					withinTree = btree.deleteKey(new RankKey(inputVals[0],inputVals[1]));
					
					if(withinTree)
						System.out.println("year: " + inputVals[0] + ", rank: " + inputVals[1] +" was deleted from the tree");
					else{
						System.out.println("year: " + inputVals[0] + ", rank: " + inputVals[1] +" was not deleted from the tree");
					}
					break;
				case 3:
					System.out.println("Range Search was called");
					//interactive
					System.out.println("Please enter the lower bound key to be deleted");
					int [] lowerbound = getValidInput();
					System.out.println("Please enter the upper bound key to be deleted");
					int [] upperbound = getValidInput();
					
					RankKey lower = new RankKey(lowerbound[0], lowerbound[1]);
					RankKey upper = new RankKey(upperbound[0], upperbound[1]);
					
					ArrayList<Key> results = btree.searchInBetween(lower, upper,  true);
					if(results==null){
						System.out.println("No results were found");
					}else{
						for(int i = 0; i < results.size(); i++){
							System.out.println(results.get(i).toString());
							
							Record record =mapToData.getRecord(results.get(i)); 
							if(record==null){
								int offset = mapToData.getKey(results.get(i));
								record = Datastore.getRecord(offset);
								mapToData.insertRecord(results.get(i), record);								
							}
							System.out.println(record);
						}						
					}
					break;
				case 4:
					System.out.println("Exiting program");
					System.exit(0);	
			}
			//write the btree changes to disk in case of our program ever crashing
			BTreeHandler.writeTree(btree, "Btreebackup.mb");
			
			
			
			System.out.println("Please execute another command");
			System.out.println("0: Insert");
			System.out.println("1: Search");
			System.out.println("2: Delete");
			System.out.println("3: SearchBetween");
			System.out.println("4: Exit");

			input = readLine();
		}
	}
	public static int [] getValidInput(){
		String input;
		int [] inputVals;
		System.out.println("Please enter it as: 'year,rank'");
		input = readLine();
		input = input.trim();
		input = input.replaceAll(" ","");
		while((inputVals = validateInput(input))==null){
			System.out.println("Invalid input");
			input = readLine();
		}
		System.out.println(input + " was valid");
		return inputVals;
	}
	public static int [] validateInput(String input){
		String [] vals = input.split(",");
		int [] intvals = new int[vals.length];
		try{
			for(int i = 0; i <vals.length; i++){
				intvals[i] = Integer.parseInt(vals[i]);
			}
		}catch(Exception e){
			return null;
		}
		if(intvals.length!=2){
			return null;
		}
		return intvals;
	}
	public static Record createRandomRecord(){
		Record temp = new Record();
		temp.create(randomRank(), randomYear(), "May", "IS", false, 1, 2, true, 1234, "MD", "USA");
		return temp;
	}
	public static int randomYear(){
		Random r = new Random();
		return r.nextInt(9999) + 1;
	}
	public static int randomRank(){
		Random r = new Random();
		return r.nextInt(100) + 1;
	}
	public static boolean fileExists(String filename){
		File file=new File(filename);
		return file.exists();
	}
	public static String readLine(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String input= null;
		try {
			input = br.readLine();
		} catch (IOException ioe) {
		   System.out.println("IO error trying to read input line");
		   System.exit(1);
		}
		return input;
	}
}
