package SeleniumTestPackage;

import java.util.ArrayList;
import java.util.regex.*;

public class AssetIDParser {
	
	public static String[] Parse(String row) {
		Pattern pattern = Pattern.compile("\\d+");
        Matcher idList = pattern.matcher(row);
        ArrayList<String> itemLinkList = new ArrayList<String>();
        String[] URLList;
        
        while(idList.find()) {
        	String newLink = ConvertToLink(idList.group());
        	if (newLink != "") {
        		itemLinkList.add(newLink);
        	}
        }
        
        URLList = new String[itemLinkList.size()];
        
        for (int i=0; i<itemLinkList.size(); i++) {
        	System.out.println("\"" + itemLinkList.get(i) + "\",");
        	URLList[i] = itemLinkList.get(i);
        }
        
        return URLList;
	}

	public static String ConvertToLink(String id) {
		String link = "";
		int idLength = GetLength(id);
		if (idLength > 5) { // Typical asset id length is 5
			link = "https://www.roblox.com/library/" + id;
		}
		return link;
	}
	
	public static int GetLength(String id) {
		int length = 0;
		if (id.length() > 0) {
			length = id.length();
		}
		return length;
	}
	
	public static int GetLength(int id) {
		int length = 0;
		if (id > 0) {
			length = (int)(Math.log10(id) + 1);
		}
		return length;
	}
	
	public static void main(String[] args) {
		// Parse();
		// Parse(""); // Insert row here
	}
}
