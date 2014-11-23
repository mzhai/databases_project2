package BTree;
import java.util.HashMap;

import Datastore.Record;
import Keys.Key;
import Keys.RankKey;

/**
 * HashMap to map Key to offset in datastore
 * @author Bill Ge
 *
 */
public class KeyValueMap{

	
	HashMap<Key, Integer> keyMap;
	HashMap<Key,Record> recordMap;
	public KeyValueMap(){
		keyMap  = new HashMap<Key, Integer>();
		recordMap  = new HashMap<Key, Record>();
	}
	/**
	 * Method inserts the proper mapping into the map
	 * @param obj the key
	 * @param offset offset of record in datastore
	 */
	public void insert(Key obj, int offset){
		keyMap.put( obj, new Integer(offset));
	}
	public void insertRecord(Key obj, Record record){
		recordMap.put(obj, record);
	}
	
	public Integer getKey(Object obj){
		return keyMap.get(obj);
	}
	public Record getRecord(Key obj){
		return recordMap.get(obj);			
	}
}
