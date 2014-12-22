/**
 * 
 */
package deepak.yelp.indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Deepak
 *
 */
public class CreateIndex{

	private final static String INPUTPATH="/Users/Deepak/Documents/MS/PDP/Project/Data/NewIndex/";
	private static final String OUTPUTPATH="/Users/Deepak/Documents/MS/PDP/Project/Data/NewSortedIndex/";
	private Map<String,String> sortedMap;

	public CreateIndex(){
		sortedMap=new TreeMap<String,String>();
	}

	//Method to read the document content.
	private void readAndSortDocumentContent(String documentId){
		StringBuffer path=new StringBuffer();
		StringBuffer values=new StringBuffer();
		String keyAndValue[];
		String key=null;
		String value=null;
		path.append(INPUTPATH);
		path.append(documentId);
		path.append(".txt");
		try {
			File file=new File(path.toString());
			FileInputStream fis=new FileInputStream(file);
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String line=null;
			while((line=br.readLine())!=null){
				keyAndValue=line.split("----->");
				key=keyAndValue[0].toLowerCase();
				value=keyAndValue[1];
				if(sortedMap.containsKey(key)){
					values.append(sortedMap.get(key));
					values.append(" | ");
					values.append(value);
					sortedMap.put(key,values.toString());
					values.setLength(0);
				}
				else{
					sortedMap.put(key,value);
				}
			}
			br.close();
		} catch ( IOException e) {
			e.printStackTrace();
		}
	}

	public void writeIndexToFile(String fileName) {
		Iterator<Entry<String, String>> it = sortedMap.entrySet().iterator();
		try{
			FileWriter fw = new FileWriter(OUTPUTPATH+fileName, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			out.print("{");
			int count=0;
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				if(count !=0 ){
					out.print(",");
				}
				out.print("\""+pairs.getKey()+"\": \""+pairs.getValue()+"\"");
				count++;
			}
			out.print("}");
			out.close();
			bw.close();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		sortedMap.clear();
	}

	public void indexData() {
		char name='A';
		for(int i=0;i<26;i++){
			char docName=(char) (name+i);
			new Thread(){
				public void run(){
					CreateIndex threadObj=new CreateIndex();
					threadObj.readAndSortDocumentContent(String.valueOf(docName));
					threadObj.writeIndexToFile(String.valueOf(docName)+".json");
					System.out.println(docName);
				}
			}.start();
		}
		for(int i=0;i<10;i++){
			int docName=i;
			new Thread(){
				public void run(){
					CreateIndex threadObj=new CreateIndex();
					threadObj.readAndSortDocumentContent(String.valueOf(docName));
					threadObj.writeIndexToFile(String.valueOf(docName)+".json");

				}
			}.start();
		}
		readAndSortDocumentContent(String.valueOf('-'));
		writeIndexToFile(String.valueOf('-')+".json");
	}
}
