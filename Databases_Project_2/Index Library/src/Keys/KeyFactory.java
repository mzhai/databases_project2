package Keys;

import java.util.ArrayList;
import java.util.HashSet;

import Datastore.*;
public class KeyFactory {

	public static ArrayList createKeys(ArrayList<Record> records, int [] keys) throws Exception{
		Key keyType = typeOfKey(keys);
		if(keyType instanceof GenericKey){
			return buildGenericKeys(records);
		}else if(keyType instanceof RankKey){
			return buildRankKeys(records);
		}else if(keyType instanceof SongKey){
			return buildSongKeys(records);
		}else{
			throw new Exception("Unrecognizable Key in creatKeys()");
		}
	}	
	public static ArrayList createKeys(String filename, int [] keys) throws Exception{
		ArrayList<Record> records = Datastore.readDataStore(filename);
		return createKeys(records, keys);
	}
	public static ArrayList<SongKey> buildSongKeys(ArrayList<Record> records){
		ArrayList<SongKey> keys = new ArrayList<SongKey>();
		for(int i = 0 ; i <records.size(); i++){
			Record record = records.get(i);
			keys.add(new SongKey(record.name, record.songTitle,record.year));
		}
		return keys;
		
	}
	public static ArrayList<SongKey> buildSongKeys(String filename){
		ArrayList<Record> records = Datastore.readDataStore(filename);
		return buildSongKeys(records);
	}
	public static ArrayList<GenericKey> buildGenericKeys(ArrayList<Record> records){
		ArrayList<GenericKey> keys = new ArrayList<GenericKey>();
		for(int i = 0 ; i <records.size(); i++){
			Record record = records.get(i);
			keys.add(new GenericKey(record.getObjects()));
		}		
		return keys;
	}

	public static ArrayList<GenericKey> buildGenericKeys(String filename){
		ArrayList<Record> records = Datastore.readDataStore(filename);
		return buildGenericKeys(records);
	}
	public static ArrayList<RankKey> buildRankKeys(ArrayList<Record>records){
		ArrayList<RankKey> keys = new ArrayList<RankKey>();
		for(int i = 0 ; i <records.size(); i++){
			Record record = records.get(i);
			keys.add(new RankKey(record.year, record.rank));
		}
		return keys;
	}

	public static ArrayList<RankKey> buildRankKeys(String filename){
		ArrayList<Record> records = Datastore.readDataStore();
		return buildRankKeys(records);
	}
	public static HashSet<Integer> getRankKeyContents(){
		HashSet<Integer> temp = new HashSet<Integer>();
		temp.add(new Integer(0));
		temp.add(new Integer(1));
		
		return temp;
	}
	public static HashSet<Integer> getSongKeyContents(){
		HashSet<Integer> temp = new HashSet<Integer>();
		temp.add(new Integer(1));
		temp.add(new Integer(2));
		temp.add(new Integer(3));
		
		return temp;
	}
	
	public static Key typeOfKey(int []keyvals){
		ArrayList<Integer> keyvalstemp = new ArrayList<Integer>();

		HashSet<Integer> songKeyContents = getSongKeyContents();
		HashSet<Integer> rankKeyContents = getRankKeyContents();

		for(int i = 0; i <keyvals.length;i++){
			keyvalstemp.add(keyvals[i]);
		}
		
		if(keyvalstemp.containsAll(songKeyContents)){
			return new SongKey();
		}else if(keyvalstemp.containsAll(rankKeyContents)){
			return new RankKey();
		}
		
		return new GenericKey();
	}
}
