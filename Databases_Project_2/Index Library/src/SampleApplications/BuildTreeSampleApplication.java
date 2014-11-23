package SampleApplications;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import Keys.*;
import BTree.BTree;
import BTree.KeyValueMap;
import Datastore.*;


/**
 * This is a sample application that demonstrates that the user can enter in any combination of valid primary keys, 
 * and we can build a tree from the key.
 * @author May
 *
 */
public class BuildTreeSampleApplication {
	enum commands{EXIT};
	
	/**
	 * This main method drives the program in command line Mode
	 * @param args
	 */
	public static void main(String [] args){
		String filename = "";
		System.out.println("Welcome to our program!");
		System.out.println("Please enter the path to your .csv file, (Press <Enter> for default path)");
		String input = readLine();
		int [] keyvals = null;
		
		
		//Enter the path to the .csv file
		while(true){
			if(input.compareTo("\n")==0 || input.compareTo("")==0){
				filename = Datastore.inputDataFileName;
				System.out.println("Default path of: "+ filename + " was used");
				if(fileExists(filename)){
					break;
				}
			}else{
				filename = input;
				if(fileExists(filename)){
					break;
				}
			}
			System.out.println("Invalid Path! Please enter a valid path to your .csv file");
			input = readLine();
		}
		
		//Assume the csv file is properly formatted
		
		//create datastore from input csv
		boolean successful = Datastore.createDataStore(filename);
		if(!successful){
			System.out.println("Error with creating datastore from csv file");
			System.exit(0);
		}else{
			System.out.println("Datastore was successfully created");
		}

		
		
		//1st line of csv file should list available fields to choose from
		String [] fields = Datastore.getFields(filename);
		for(int i = 0; i <fields.length; i++){
			System.out.println(i + ": " + fields[i]);
		}

		System.out.println("Please enter a comma delimited list of numbers to be used as the primary index");
		System.out.println("Example: 1,2,3");
        
        while(true){
            input = readLine();
            if(input.compareTo("\n")==0 || input.compareTo("")==0){
            	//some default value for ease of testing
            	
            	break;
            }else{                                
            	String [] keys = input.split(",");
            	
            	keyvals = new int[keys.length];
            	try{
            		for(int i = 0; i <keys.length; i++){
            			keyvals[i] = Integer.parseInt(keys[i]);
            		}
            		for(int i = 0; i <keys.length; i++){
            			System.out.print(fields[keyvals[i]] + " ");
                    }
            		System.out.println();
            	} catch (NumberFormatException e){
            		System.out.println("Invalid list was entered");
            		continue;
            	}
                    
            	if(Datastore.primaryFields(filename, keyvals)){
            		break;
            	}
            	System.out.println("Not a valid primary key combination");
            }
        }

        System.out.println("A valid primary key was entered");

		//Read all the files from the datastore
		ArrayList<Record> records = Datastore.readDataStore();

		//time to build the tree
		//first build the keys from the records
		ArrayList<Key> keys = null;
		try {
			keys = KeyFactory.createKeys(records, keyvals);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//add all the keys into the Btree

		//hardcode an order of 2 temporarily
		BTree btree = new BTree(2);
		KeyValueMap keyValMap = new KeyValueMap();
		
		//determine the order of the btree?
		for(int i = 0; i <keys.size(); i++){
			btree.insertKey(keys.get(i));
			//then build the key->offset map
			keyValMap.insert(keys.get(i), records.get(i).getOffset());
		}
		System.out.println("Successfully created tree");
		System.out.println();
		System.out.println("List of leaf nodes in order (all keys in order)");
		btree.printList();
		System.out.println();
		System.out.println("Breadth-first traversal of tree");
		btree.printTree();
		
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
