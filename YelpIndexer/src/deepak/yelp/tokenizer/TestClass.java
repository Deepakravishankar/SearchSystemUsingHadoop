package deepak.yelp.tokenizer;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Deepak
 *
 */
public class TestClass {
	public static boolean ASC = true;
	public static boolean DESC = false;
	private static String INPUTPATH="/Users/Deepak/Documents/MS/PDP/Project/SearchSystem/input.txt";
	private static String SECONDINPUTPATH="/Users/Deepak/Documents/MS/PDP/Project/Data/ParsedCombinedData/";
	private static String OUTPUTPATH="/Users/Deepak/Documents/MS/PDP/Project/SearchSystem/output.txt";

	public static void main(String[] args) throws JSONException {
		/*String line="a^^dsad^^34 | b^^^sdad&&d^^";
		String words[]=splitListOfValues(line,"[\\|].");
		for(int i=0;i<words.length;i++){
			String w[]=splitListOfValues(words[i],"[\\^\\^]+");
			for(int j=0;j<w.length;j++){
				System.out.println(w[j]);
			}
		}*/
		/*ArrayList<String> list=new ArrayList<String>();
		list.add("deepak");
		HashMap<Integer,ArrayList<String>> map=new HashMap<Integer,ArrayList<String>>();
		map.put(1,list);
		map.get(1).add("rohit");
		list.add("vijay");
		map.put(2,"vijay");

		Object[] array=map.get(1).toArray();
		for(int i=0;i<array.length;i++){
			System.out.println(array[i]);
		}

		HashMap<String,Integer> map=new HashMap<String,Integer>();
		map.put("1", 3);
		map.put("2", 10);
		map.put("4", 34);
		map.put("3", 22);
		//printMap(map);
		map = sortByComparator(map, DESC);
		for(String commonKey:map.keySet()){
			System.out.println(map.get(commonKey));
		}*/
		StringBuffer buffer=new StringBuffer();
		String documentNames=readDocumentContent(INPUTPATH).replaceAll("[\\D]+"," ");
		String words[]=documentNames.trim().split(" ");
		ArrayList<String> values=new ArrayList<String>();
		for(int i=0;i<words.length;i++){
			values.clear();
			String path=SECONDINPUTPATH+words[i]+".json";
			String contents=readDocumentContent(path);
			JSONObject tokenizer=new JSONObject(contents);
			writeToIndex(i+1+") Document Id ", words[i]);
			JSONArray keys=tokenizer.names();
			String value=null;
			for(int j=0;j<keys.length();j++){
				value=tokenizer.optString(keys.optString(j));
				writeToIndex(keys.optString(j),value);
			}
			writeToIndex("\n","\n");
		}
	}
	private static String readDocumentContent(String path){
		StringBuffer buffer=new StringBuffer();
		try {
			File file=new File(path.toString());
			FileInputStream fis=new FileInputStream(file);
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String line=null;
			while((line=br.readLine())!=null){
				buffer.append(line);
			}
			br.close();
		} catch ( IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	public static void writeToIndex(String key,String value){
		if(key.length() > 0){
			try {
				String fileName=OUTPUTPATH;
				FileWriter fw = new FileWriter(fileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw);
				out.println(key+"     "+value);
				out.close();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	private static String[] splitListOfValues(String valueList,String splitter){
		String listOfValues[]=valueList.split(splitter);
		return listOfValues;			
	}


	private static HashMap<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
	{

		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Integer>>()
				{
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2)
			{
				if (order)
				{
					return o1.getValue().compareTo(o2.getValue());
				}
				else
				{
					return o2.getValue().compareTo(o1.getValue());

				}
			}
				});

		// Maintaining insertion order with the help of LinkedList
		HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public static void printMap(Map<String, Integer> map)
	{
		for (Entry<String, Integer> entry : map.entrySet())
		{
			System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
		}
	}*/

}
