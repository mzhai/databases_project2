package SampleApplications;

import java.io.File;
import java.util.ArrayList;

import BTree.BTree;
import Datastore.Datastore;
import Datastore.Record;
import Keys.Key;
import Keys.KeyFactory;
import Keys.RankKey;
import BTree.BTreeHandler;

/**
 * Please run ConcurrencySampleApplication to see the application. This is the Runnable class for that application. 
 * This thread inserts 1000000 keys into the btree to test if it can handle concurrency. 
 * @author May
 *
 */
public class ConcurrencyRunnable implements Runnable{
	BTree btree;
	
	public ConcurrencyRunnable(BTree t){
		btree = t;
	}
	
	public void run(){		
		for (int k = 1; k < 1000001; k++){
			btree.insertKey(new RankKey(2011, k));
		}

		System.out.println("Thread "+Thread.currentThread().getId()+" done. Total of "+btree.countEntries()+" entries");
	}
}
