package Keys;

import java.io.Serializable;

public class RankKey extends Key implements Serializable {
	int rank;
	int year;
	RankKey(){}
	public RankKey(int year, int rank){
		this.year = year;
		this.rank = rank;
	}
	
	/**
	 * 0 means the obj are equal
	 * 1 means the current obj is greater than the parameter
	 * -1 means the current obj is less than the parameter
	 */
	public int compareTo(Object obj) {
		RankKey temp = (RankKey) obj;
		if(this.year < temp.year){
			return -1;
		}else if(this.year > temp.year){
			return 1;
		}
		else if(this.year==temp.year){
			if(this.rank <temp.rank){
				return -1;
			}else if(this.rank > temp.rank){
				return 1;
			}
		}
		return 0;
	}
	
	public String toString(){
		String output = "";
		output = this.year + ":"  + this.rank;
		return output;
	}
	public boolean equals(Object obj){
		if(obj==null){
			return false;
		}
		RankKey temp = (RankKey)obj;
		if(temp.year==this.year && temp.rank==this.rank){
			return true;
		}
		return false;
	}
	public int hashCode(){
		return ((this.year + "" + this.rank).hashCode());
	}
}
