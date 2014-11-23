package Keys;

public class GenericKey extends Key{
	Object [] fields;
	GenericKey(){}
	public GenericKey(Object [] fields){
		this.fields = fields;
	}
	/**
	 * For the generic key, the below method simply concatenates the values
	 * from the fields into a string and compares the outputted strings
	 */
	public int compareTo(Object obj) {
		GenericKey temp = (GenericKey)obj;
		String concat = "";
		String tempcat = "";
		for(int i = 0; i <fields.length;i ++){
			concat = concat + fields[i];
			tempcat = tempcat + temp.fields[i];
		}
		return concat.compareTo(tempcat);
	}

	public boolean equals(Object obj){
		if(obj==null){
			return false;
		}
		GenericKey temp = (GenericKey)obj;
		for (int i = 0; i<fields.length; i++){
			if (fields[i] != temp.fields[i])
				return false;
		}
		return true;
	}
	
	public int hashCode(){
		String code = "";
		for (int i = 0; i < fields.length; i++){
			code = code + fields[i];
		}
		return code.hashCode();
	}
	

}
