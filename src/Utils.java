
public class Utils{

	public static final String REGEX_SEPARATOR = "\\t";
	public static final String LIST_SEPARATOR = "\t";
	
	
	public static String joinFields(String[] fields, int startIndex, int endIndex){
		StringBuilder sb = new StringBuilder();
		
		sb.append(fields[startIndex]);
		for (int i = startIndex+1; i <= endIndex; i ++){
			sb.append(LIST_SEPARATOR);
			sb.append(fields[i]);
		}
		return sb.toString();
	}
	
	public static String joinFields(String[] fields, int[] indexes){
		StringBuilder sb = new StringBuilder();
		
		
		for (int i : indexes){
			sb.append(fields[i]);
			sb.append(LIST_SEPARATOR);
		}
		
		// trim to remove additional tab in the end
		// might not work with another separator 
		return sb.toString().trim();
	}
}

