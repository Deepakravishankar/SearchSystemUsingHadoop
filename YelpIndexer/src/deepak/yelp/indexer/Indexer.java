package deepak.yelp.indexer;

/**
 * @author Deepak
 *
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Indexer {
	private static final String OUTPUTPATH="/Users/Deepak/Documents/MS/PDP/Project/Data/NewIndex/";

	public void writeToIndex(String key,String value){
		if(key.length() > 0){
			try {
				String fileName=OUTPUTPATH+getFileName(key);
				FileWriter fw = new FileWriter(fileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw);
				out.println(key+"----->"+value);
				out.close();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getFileName(String key){
		return key.toUpperCase().charAt(0)+".txt";
	}

}
