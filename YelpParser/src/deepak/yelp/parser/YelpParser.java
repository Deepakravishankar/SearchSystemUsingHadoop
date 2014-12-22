
package deepak.yelp.parser;

import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.util.Stack;

/**
 * @author Deepak
 *
 */
public class YelpParser {
	private static String INPUTPATH="/Users/Deepak/Documents/MS/PDP/Project/Data/sampleCombinedData.json";
	private static String OUTPUTPATH="/Users/Deepak/Documents/MS/PDP/Project/Data/ParsedCombinedData/";
	private static int count=0;

	public static void parseToDifferentFiles(){
		try {
			File file=new File(INPUTPATH);
			FileInputStream fis=new FileInputStream(file);
			FileOutputStream fos;
			StringBuffer buffer=new StringBuffer();
			int c;
			String fileName=null;
			while((c=fis.read()) > 0 ){
				char d=(char)c;
				if(c != 10)
					buffer.append(d);

				else{
					fileName=getFilePath();
					fos=new FileOutputStream(OUTPUTPATH+fileName);
					fos.write(buffer.toString().getBytes());
					buffer.setLength(0);
					fos.close();
				}
			}
			System.out.println(fileName);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getFilePath(){
		count=count+1;
		return String.valueOf(count)+".json";
	}

	public static void main(String args[]){
		parseToDifferentFiles();
	}
}
