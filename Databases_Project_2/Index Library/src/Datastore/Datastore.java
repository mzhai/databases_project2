package Datastore;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Keys.Key;

/**
 * Handles all I/O operations involved when dealing with the Datastore
 * @author Bill Ge, May Zhai
 *
 */
public class Datastore {
	public static String FILENAME = "records.dat";
	public static String inputDataFileName = "input.csv";

	
	/**
	 * Fetches one record from the datastore
	 * @param offset specifies the offset of the data to look into
	 * @return an instance of the class Record
	 */
	public static Record getRecord(int offset){
		int currentPos = offset;
		int [] offsets = SizeLimits.buildSeekOffset();
		int wordLength = 0;
		Record record = null;
		try{
			RandomAccessFile rf = new RandomAccessFile(FILENAME,"r");
			rf.seek(offset);
			record = new Record();
			record.rank = rf.readInt();
			record.year=rf.readInt();				

			wordLength = rf.readInt();
			record.songTitle = readChars(rf,wordLength);

			rf.seek(currentPos + offsets[3]);
			wordLength = rf.readInt();
			record.name = readChars(rf,wordLength);
			rf.seek(currentPos + offsets[4]);
			
	
			record.features = rf.readBoolean();
			record.size=rf.readInt();
			record.time=rf.readInt();
			record.group = rf.readBoolean();
			record.debutYear=rf.readInt();
			
			wordLength = rf.readInt();
			record.state = readChars(rf,wordLength);
			
			
			rf.seek(currentPos + offsets[10]);
			wordLength = rf.readInt();
			record.country = readChars(rf,wordLength);
			rf.seek(currentPos + offsets[11]);
		}catch(Exception e){
			//e.printStackTrace();
			return null;
		}
		
		return record;		
	}
	/**
	 * Helper method for the purpose of generating a String representation
	 * of a primary key given the keyFields
	 * 
	 * @param record the record
	 * @param keyFields given keyfields
	 * @return String representation of a primary key
	 */
	public static String buildKey(Record record, int [] keyFields){
		String key = "";
		for(int i = 0; i <keyFields.length;i++){
			key = key + record.getRecordValue(keyFields[i]);
		}
		return key;
	}
	/**
	 * Determines whether or not a given key is a valid primary key
	 * @param filename the file
	 * @param key the key value
	 * @return whether a given key is a valid primary key (true/false)
	 */
	public static boolean primaryFields(String filename, int [] key){		
		ArrayList<Record> records = readDataStore();
		HashSet<String> temp = new HashSet<String>();
		boolean proper = true;
		for(int j = 0; j <records.size(); j++){
			Record record = records.get(j);
			String keyString = buildKey(record,key);
			
			if(!temp.add(keyString)){
				return false;
			}
		}
		return proper;
	}
	public static ArrayList<Record> readDataStore(){
		return readDataStore(FILENAME);
	}
	/**
	 * Reads data into Records from the datastore
	 * @return an arraylist of instances of Record
	 * @param filename name of file
	 */
	public static ArrayList<Record> readDataStore(String filename){
		ArrayList<Record> records = new ArrayList<Record>();
		
		int currentPos = 0;
		int [] offsets = SizeLimits.buildSeekOffset();
		int wordLength = 0;
		try{
			RandomAccessFile rf = new RandomAccessFile(filename,"r");
			while(rf.getFilePointer()<=rf.length()){		
				Record record = new Record();
				record.offsetValue = (int)rf.getFilePointer();
				record.rank = rf.readInt();
				record.year=rf.readInt();				

				wordLength = rf.readInt();
				record.songTitle = readChars(rf,wordLength);

				rf.seek(currentPos + offsets[3]);
				wordLength = rf.readInt();
				record.name = readChars(rf,wordLength);
				rf.seek(currentPos + offsets[4]);
				
		
				record.features = rf.readBoolean();
				record.size=rf.readInt();
				record.time=rf.readInt();
				record.group = rf.readBoolean();
				record.debutYear=rf.readInt();
				
				wordLength = rf.readInt();
				record.state = readChars(rf,wordLength);
				
				
				rf.seek(currentPos + offsets[10]);
				wordLength = rf.readInt();
				record.country = readChars(rf,wordLength);
				rf.seek(currentPos + offsets[11]);
				records.add(record);

				currentPos = currentPos + offsets[11];
			}
			rf.close();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return records;
	}
	/**
	 * Reads chars from the datastore
	 * @param rf datastore file
	 * @param length number of characters to read
	 * @return String representation
	 */
	public static String readChars(RandomAccessFile rf, int length){
		StringBuilder temp = new StringBuilder();
		try{
			for(int i = 0; i <length; i++){
				char c = rf.readChar();
				temp.append(c);
			}
		}catch(Exception e){
			
		}
		return temp.toString();
	}
	
	public static void deleteFile(String filename){
		File file = new File(filename);
		if(file.exists())
			file.delete();
	}
	/**
	 * Method writes the data for the datastore given records
	 * @param records An ArrayList that contains instances of the class Record
	 * @return whether the datastore was created successfully or not
	 */
	public static boolean createDataStore(ArrayList<Record>records){
		deleteFile(FILENAME);
		try {
			Writer write;
			RandomAccessFile rf = new RandomAccessFile(FILENAME,"rw");
			int currentPos = 0;
			int [] offsets = SizeLimits.buildSeekOffset();
			for(int i = 0; i <records.size(); i++){
				Record record = records.get(i);

				rf.writeInt(record.rank);
				rf.writeInt(record.year);
				rf.writeInt(record.songTitle.length());
				rf.writeChars(record.songTitle);
				rf.seek(currentPos + offsets[3]);
				rf.writeInt(record.name.length());
				rf.writeChars(record.name);
				rf.seek(currentPos + offsets[4]);
				rf.writeBoolean(record.features);
				rf.writeInt(record.size);
				rf.writeInt(record.time);
				rf.writeBoolean(record.group);
				rf.writeInt(record.debutYear);
				rf.writeInt(record.state.length());
				rf.writeChars(record.state);
				rf.seek(currentPos + offsets[10]);
				rf.writeInt(record.country.length());
				rf.writeChars(record.country);				
				rf.seek(currentPos + offsets[11]);
				
				currentPos = currentPos + offsets[11];
			}
	    
	    } catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * Another method for the purpose of reading data from a csv file and creating
	 * the datastore from it
	 * @param filename name of file
	 */
	public static void readThisFile(String filename){
		ArrayList<Record> records = new ArrayList<Record>();
		File file = new File(filename);
		
		String line = null;
		Object [] data;
		
		try{
			BufferedReader buf = new BufferedReader(new FileReader(file));
			buf.readLine();
			while((line = buf.readLine()) != null){
				Record record = new Record();
				data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				
				if((String)data[9] == null || ((String)data[9]).compareTo("")==0){
					data[9] = "n/a";
				}
				
				record.create(Integer.parseInt((String)data[0]), 		//rank
						Integer.parseInt((String)data[1]), 				//year
						(String)data[2], 								//songTitle
						(String)data[3], 								//name
						Boolean.parseBoolean((String)data[4]), 			//features
						Integer.parseInt((String)data[5]), 				//size
						Integer.parseInt((String)data[6]), 				//time
						Boolean.parseBoolean((String)data[7]), 			//group
						Integer.parseInt(((String)data[8])), 			//debutyear
						(String)data[9], 								//state
						(String)data[10]								//country
				);
				records.add(record);
			}
			buf.close();
			
			createDataStore(records);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Gets name of fields of records
	 * @param filename name of file
	 * @return String array containing names of fields
	 */
	public static String [] getFields(String filename){
		String [] split = null;
		try{	
			RandomAccessFile rf = new RandomAccessFile(FILENAME,"rw");
			File file = new File(filename);
			BufferedReader buf = new BufferedReader(new FileReader(file));
			String fields = buf.readLine();
			split = fields.split(",");
			rf.close();
			buf.close();
		}catch(Exception e){
			
		}
		return split;
	}
	/**
	 * Deletes a record given the offset into the record
	 * @param filename
	 * @param offset
	 * @param outfile
	 * @return whether it was successful
	 */
	public static boolean deleteOne(String filename, int offset, String outfile){
		int currentPos = 0;
		int [] offsets = SizeLimits.buildSeekOffset();
		int wordLength = 0;
		try{
			RandomAccessFile rf = new RandomAccessFile(filename,"r");
			RandomAccessFile rf1 = new RandomAccessFile(outfile,"r");
			
			while(rf.getFilePointer()<=rf.length()){
				if(currentPos==offset){
					currentPos += SizeLimits.totalSize;
					continue;
				}
				
				Record record = new Record();
				record.rank = rf.readInt();
				record.year=rf.readInt();				

				wordLength = rf.readInt();
				record.songTitle = readChars(rf,wordLength);

				rf.seek(currentPos + offsets[3]);
				wordLength = rf.readInt();
				record.name = readChars(rf,wordLength);
				rf.seek(currentPos + offsets[4]);
				
		
				record.features = rf.readBoolean();
				record.size=rf.readInt();
				record.time=rf.readInt();
				record.group = rf.readBoolean();
				record.debutYear=rf.readInt();
				
				wordLength = rf.readInt();
				record.state = readChars(rf,wordLength);
				
				
				rf.seek(currentPos + offsets[10]);
				wordLength = rf.readInt();
				record.country = readChars(rf,wordLength);
				rf.seek(currentPos + offsets[11]);
				
				//ugly way to do it
				//write data back out
				rf1.writeInt(record.rank);
				rf1.writeInt(record.year);
				rf1.writeInt(record.songTitle.length());
				rf1.writeChars(record.songTitle);
				rf1.seek(currentPos + offsets[3]);
				rf1.writeInt(record.name.length());
				rf1.writeChars(record.name);
				rf1.seek(currentPos + offsets[4]);
				rf1.writeBoolean(record.features);
				rf1.writeInt(record.size);
				rf1.writeInt(record.time);
				rf1.writeBoolean(record.group);
				rf1.writeInt(record.debutYear);
				rf1.writeInt(record.state.length());
				rf1.writeChars(record.state);
				rf1.seek(currentPos + offsets[10]);
				rf1.writeInt(record.country.length());
				rf1.writeChars(record.country);				
				rf1.seek(currentPos + offsets[11]);
				

				currentPos = currentPos + offsets[11];
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/**
	 * Creates the datastore from reading each record from the csv file
	 * @param filename
	 * @return successful or not
	 */
	public static boolean createDataStore(String filename){
		deleteFile(FILENAME);
		try{
			Writer write;
			RandomAccessFile rf = new RandomAccessFile(FILENAME,"rw");
			int currentPos = 0;
			int [] offsets = SizeLimits.buildSeekOffset();
			
			
			File file = new File(filename);
			
			String line = null;
			Object [] data;

			
			
			BufferedReader buf = new BufferedReader(new FileReader(file));
			buf.readLine();
			while((line = buf.readLine()) != null){
				Record record = new Record();
				data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				
				if((String)data[9] == null || ((String)data[9]).compareTo("")==0){
					data[9] = "n/a";
				}
				
				record.create(Integer.parseInt((String)data[0]), 		//rank
						Integer.parseInt((String)data[1]), 				//year
						(String)data[2], 								//songTitle
						(String)data[3], 								//name
						Boolean.parseBoolean((String)data[4]), 			//features
						Integer.parseInt((String)data[5]), 				//size
						Integer.parseInt((String)data[6]), 				//time
						Boolean.parseBoolean((String)data[7]), 			//group
						Integer.parseInt(((String)data[8])), 			//debutyear
						(String)data[9], 								//state
						(String)data[10]								//country
				);

				rf.writeInt(record.rank);
				rf.writeInt(record.year);
				rf.writeInt(record.songTitle.length());
				rf.writeChars(record.songTitle);
				rf.seek(currentPos + offsets[3]);
				rf.writeInt(record.name.length());
				rf.writeChars(record.name);
				rf.seek(currentPos + offsets[4]);
				rf.writeBoolean(record.features);
				rf.writeInt(record.size);
				rf.writeInt(record.time);
				rf.writeBoolean(record.group);
				rf.writeInt(record.debutYear);
				rf.writeInt(record.state.length());
				rf.writeChars(record.state);
				rf.seek(currentPos + offsets[10]);
				rf.writeInt(record.country.length());
				rf.writeChars(record.country);				
				rf.seek(currentPos + offsets[11]);
				
				currentPos = currentPos + offsets[11];						
			}
			buf.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Method inserts a new record to the end of the datastore
	 * @param record
	 * @return offset, or -1 if failed
	 */
	public static int insertEntrytoEnd(Record record){
		//seek to the end of the file
		//make sure it's a multiple of a Record totalsize
		//then write the new entry into the file
		int offset = -1;
		try{
	
			RandomAccessFile rf = new RandomAccessFile(FILENAME,"rw");
			int currentPos = 0;
			int [] offsets = SizeLimits.buildSeekOffset();
			
			currentPos = (int)rf.length();
			
			int remainder = SizeLimits.totalSize - (currentPos % SizeLimits.totalSize);
			
			rf.seek(currentPos);
			for(int i = 0; i <remainder;i++){
				rf.writeByte(0);
				currentPos ++;
			}
			offset = currentPos;
		
			rf.writeInt(record.rank);
			rf.writeInt(record.year);
			rf.writeInt(record.songTitle.length());
			rf.writeChars(record.songTitle);
			rf.seek(currentPos + offsets[3]);
			rf.writeInt(record.name.length());
			rf.writeChars(record.name);
			rf.seek(currentPos + offsets[4]);
			rf.writeBoolean(record.features);
			rf.writeInt(record.size);
			rf.writeInt(record.time);
			rf.writeBoolean(record.group);
			rf.writeInt(record.debutYear);
			rf.writeInt(record.state.length());
			rf.writeChars(record.state);
			rf.seek(currentPos + offsets[10]);
			rf.writeInt(record.country.length());
			rf.writeChars(record.country);				
			rf.seek(currentPos + offsets[11]);
		}
		catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		return offset;
	}
}