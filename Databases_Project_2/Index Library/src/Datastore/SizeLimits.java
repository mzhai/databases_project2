package Datastore;
public class SizeLimits {
	//below are listings in bytes
	final static int rankLimit = 4;
	final static int yearLimit = 4;
	final static int songTitleLimit = 25 * 2 + 4;
	final static int nameLimit = 25 * 2 + 4;
	final static int featuresLimit = 1;
	final static int sizeLimit = 4;
	final static int timeLimit = 4;
	final static int groupLimit = 1;
	final static int debutYearLimit = 4;
	final static int stateLimit = 20 * 2 + 4;
	final static int countryLimit = 20 * 2 + 4;
	
	final static int totalSize = rankLimit + yearLimit + songTitleLimit + nameLimit +
									featuresLimit + sizeLimit + timeLimit + groupLimit + debutYearLimit +
									stateLimit + countryLimit;
	

	
	/**
	 * builds an array with the offsets of where each piece of data is stored within the datastore
	 * @return array of offsets
	 */
	public static int [] buildSeekOffset(){
		int [] offsets = new int[11 + 1];
		
		offsets[0] = 0;
		offsets[1] = rankLimit;
		offsets[2] = offsets[1] + yearLimit;
		offsets[3] = offsets[2] + songTitleLimit;
		offsets[4] = offsets[3] + nameLimit;
		offsets[5] = offsets[4] + featuresLimit;
		offsets[6] = offsets[5] + sizeLimit;
		offsets[7] = offsets[6] + timeLimit;
		offsets[8] = offsets[7] + groupLimit;
		offsets[9] = offsets[8] + debutYearLimit;
		offsets[10] = offsets[9] + stateLimit;
		offsets[11] = offsets[10] + countryLimit;
		
		return offsets;
	}
}