package Keys;

import java.io.Serializable;

public class SongKey extends Key implements Serializable {
	String artistName;
	String songName;
	int year;
	SongKey(){}
	SongKey(String artistName, String songName, int year){
		this.artistName = artistName;
		this.songName = songName;
		this.year = year;
	}
	
	/**
	 * 0 means the obj are equal
	 * 1 means the current obj is greater than the parameter
	 * -1 means the current obj is less than the parameter
	 */
	public int compareTo(Object obj) {
		SongKey temp = (SongKey) obj;
		String concat = artistName + songName;
		String tempcat = temp.artistName + temp.songName;
		
		if(concat.compareTo(tempcat)==0){
			if(this.year < temp.year){
				return -1;
			}else if(this.year > temp.year){
				return 1;
			}
		}
		return concat.compareTo(tempcat);
	}
	public boolean equals(Object obj){
		if(obj==null){
			return false;
		}
		SongKey temp = (SongKey)obj;
		if(temp.year==this.year && temp.artistName.compareTo(this.artistName)==0 && temp.songName.compareTo(this.songName)==0){
			return true;
		}
		return false;
	}
	public int hashCode(){
		return ((this.year + "" + this.artistName+ this.songName).hashCode()) ;
	}
	public String toString(){
		String output = "";
		output = this.artistName + ":"  + this.songName+ ":"+this.year;
		return output;
	}

}
