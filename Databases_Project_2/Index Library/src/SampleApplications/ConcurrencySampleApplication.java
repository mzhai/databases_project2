package SampleApplications;

import java.util.ArrayList;


import BTree.BTree;
import BTree.KeyValueMap;
import Datastore.*;
import Keys.Key;
import Keys.KeyFactory;
import BTree.BTreeHandler;
/**
 * Sample application illustrating the use of concurrency in the program.
 * @author Bill Ge, May Zhai
 *
 */
public class ConcurrencySampleApplication {
	public static void main(String [] args) throws Exception{
		//Assume you are reading the file records.dat
		Datastore.readThisFile(Datastore.inputDataFileName);
		ArrayList<Record> records = Datastore.readDataStore();
		
		int [] keyvals = {0,1};
		ArrayList<Key> keys = KeyFactory.createKeys(records, keyvals);
		
		
		//Creates the new btree
		BTree btree = new BTree(2);
		for(int i = 0; i <keys.size(); i++){
			btree.insertKey(keys.get(i));
		}
		System.out.println("BTree built. Total of "+btree.countEntries()+" entries.");
		System.out.println("Adding keys. Please wait...");
		
		Runnable run1 =  new ConcurrencyRunnable(btree);
		Runnable run2 =  new ConcurrencyRunnable(btree);
		Runnable run3 =  new ConcurrencyRunnable(btree);
		Runnable run4 =  new ConcurrencyRunnable(btree);
		
		Thread thread1 = new Thread(run1);
		Thread thread2 = new Thread(run2);
		Thread thread3 = new Thread(run3);
		Thread thread4 = new Thread(run4);

		thread3.start();
		thread1.start();
		thread2.start();
		thread4.start();
		
		thread3.join();
		thread1.join();
		thread2.join();
		thread4.join();
		
		Thread.sleep(10000);

		System.out.println("Total of "+btree.countEntries()+" entries");
	}	
}