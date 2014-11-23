package Datastore;

import java.util.ArrayList;

/**
 * @author May Zhai
 * @author Bill Ge
 *
 */
public class Record {
	public int rank;				//index of 0	
	public int year;				//index of 1
	public String songTitle = "";	//index of 2
	public String name = "";		//index of 3
	public boolean features;		//index of 4
	public int size;				//index of 5
	public int time;				//index of 6
	public boolean group;			//index of 7
	public int debutYear;			//index of 8
	public String state ="";		//index of 9
	public String country = "";		//index of 10
	
	int offsetValue = -1;
	
	static int recordSize = 11;
	
	public Record(){}
	public static int getRecordSize(){
		return recordSize;
	}
	public Object getRecordValue(int index){
		switch(index){
		case 0:
			return rank;
		case 1:
			return year;
		case 2:
			return songTitle;
		case 3:
			return name;
		case 4:
			return features;
		case 5:
			return size;
		case 6:
			return time;
		case 7:
			return group;
		case 8:
			return debutYear;
		case 9:
			return state;
		case 10:
			return country;
		}
		return null;
	}
	/**
	 * Method checks for the validity of given parameters
	 * If valid, goes ahead and contructs the Record
	 * @param rank
	 * @param year
	 * @param songTitle
	 * @param name
	 * @param features
	 * @param size
	 * @param time
	 * @param group
	 * @param debutYear
	 * @param state
	 * @param country
	 * @return value indicating if the record was successfully created
	 */
	public boolean create(int rank, int year, String songTitle, String name, 
			boolean features, int size, int time, boolean group, 
			int debutYear, String state, String country){
		if(songTitle.length() > SizeLimits.songTitleLimit ||
				name.length() > SizeLimits.nameLimit ||
				state.length() > SizeLimits.stateLimit ||
				country.length() > SizeLimits.countryLimit){
		
			return false;
		}
		this.rank = rank;
		this.year = year;
		this.songTitle = songTitle;
		this.name = name;
		this.features = features;
		this.size = size;
		this.time = time;
		this.group = group;
		this.debutYear = debutYear;
		this.state = state;
		this.country = country;
		
		return true;
	}
	/**
	 * Method allows for the output of the information stored in Record
	 * in a readable format
	 */
	public String toString(){
		String result = "";
		result = result + "rank: " + this.rank + "\n";
		result = result + "year: " + this.year + "\n";
		result = result + "songTitle: " + this.songTitle + "\n";
		result = result + "name: " + this.name + "\n";
		result = result + "features: " + this.features + "\n";
		result = result + "size: " + this.size + "\n";
		result = result + "time: " + this.time + "\n";
		result = result + "group: " + this.group + "\n";
		result = result + "debutYear: " + this.debutYear + "\n";
		result = result + "state: " + this.state + "\n";
		result = result + "country: " + this.country + "\n";
		
		return result;
	}
	public boolean validateString(){
		return true;
	}
	public Object [] getObjects(){
		Object [] arr = new Object[this.getRecordSize()];
		for(int i = 0; i <arr.length; i ++){
			arr[i] = getRecordValue(i);
		}
		return arr;
	}
	public int getOffset(){
		return offsetValue;
	}
	public static String printRecords(ArrayList<Record> records){
		String output = "";
		for(int i = 0; i <records.size();i++){
			output = output + records.get(i).toString();
			output = output + "\n";
		}
		return output;
	}
}
