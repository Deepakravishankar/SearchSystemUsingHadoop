package deepak.yelp.searchsystem;

import java.io.IOException;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @author Deepak
 *
 */
public class Reduce extends Reducer<Text,Text,Text,Text>{

	private final String PIPESPLITTER="[\\|].";
	private final String CAPSPLITTER="[\\^^]+";
	private static String query;

	private final int ONE=1;
	private final int THREE=1;
	private final int TOP_K=100;
	private int numberOfQueries=0;
	private static int iteration=0;

	public final boolean ASC = true;
	public final boolean DESC = false;

	private HashMap<String,String> userResults;
	private HashMap<String,String> businessResults;
	private HashMap<String,Integer> commonDocuments;

	private TreeMap<Integer,ArrayList<String>> userDocuments;
	private TreeMap<Double,ArrayList<String>> businessDocuments;

	private Text keys=new Text();
	private Text value=new Text();

	long start;
	long end;

	//Initialize variables.
	public void setup(Context context){
		query=context.getConfiguration().get("query",null);
		numberOfQueries=context.getConfiguration().getInt("querysize",0);
		commonDocuments=new HashMap<String,Integer>();
		userDocuments=new TreeMap<Integer,ArrayList<String>>();
		businessDocuments=new TreeMap<Double,ArrayList<String>>();
		userResults=new HashMap<String,String>();
		businessResults=new HashMap<String,String>();
		start=new Date().getTime();
	}


	public void reduce(Text key, Iterable<Text> values,Context context) {

		try {
			if(numberOfQueries > 0){
				iteration++;
				String documentID,type,votes,stars;
				for(Text val:values){
					//Split the list of values to extract each value.
					String listOfValues[]=splitListOfValues(val.toString(),PIPESPLITTER);

					for(int i=0;i<listOfValues.length;i++){	

						String[] splitsOfEachValue=splitListOfValues(listOfValues[i],CAPSPLITTER);
						if(splitsOfEachValue.length > 1){
							documentID=splitsOfEachValue[0];
							type=splitsOfEachValue[1].trim();

							//Rank Values based on document type.
							if(type.equalsIgnoreCase("u")){
								votes=splitsOfEachValue[3].trim();
								rankUserDocumets(documentID,votes);
							}
							else if(type.equalsIgnoreCase("b") || type.equalsIgnoreCase("c")){
								stars=splitsOfEachValue[3].trim();
								rankBusinessDocuments(documentID,stars,type);
							}
						}
					}			
				}

				if(iteration == numberOfQueries){
					end=new Date().getTime();
					ArrayList<String> listOfDocuments=new ArrayList<String>();
					keys.set("Search Results for \" "+query+" \" : ");
					value.set("");
					context.write(keys,value);
					int topK=TOP_K;

					//Write ranked common results to output file.
					if(topK > 0){
						value.set("");
						commonDocuments= sortByComparator(commonDocuments, DESC);
						for(String commonKey:commonDocuments.keySet()){
							keys.set(commonKey);
							context.write(keys,value);
							topK--;
							if(topK==0)
								break;
						}
					}

					//Write ranked business results to output file.
					if(topK > 0){
						value.set("");
						for(Double businessKey:businessDocuments.descendingKeySet()){
							listOfDocuments.clear();
							listOfDocuments=businessDocuments.get(businessKey);
							for(int i=0;i<listOfDocuments.size();i++){
								keys.set(listOfDocuments.get(i));
								context.write(keys,value);
								topK--;
								if(topK==0)
									break;
							}
							if(topK==0)
								break;
						}		
					}

					//Write ranked user results to the output file.
					if(topK > 0){
						value.set("");
						for(Integer userKey:userDocuments.descendingKeySet()){
							listOfDocuments.clear();
							listOfDocuments=userDocuments.get(userKey);
							for(int i=0;i<listOfDocuments.size();i++){
								keys.set(listOfDocuments.get(i));
								context.write(keys,value);
								topK--;
								if(topK==0)
									break;
							}
							if(topK==0)
								break;
						}
					}
					keys.set("The total time for search is");
					value.set(String.valueOf(end-start)+" ms.");
					context.write(keys,value);
					if(topK==TOP_K){
						keys.set("No search results were found for the query \" "+query+" \" ");
						value.set("");
						context.write(keys,value);
					}
				}
			}
			else{
				keys.set("No search results were found for the given query.");
				value.set("");
				context.write(keys,value);
			}

		}catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//Rank user data.
	private void rankUserDocumets(String documentID,String votes){
		if(userResults.containsKey(documentID)){
			if(commonDocuments.containsKey(documentID)){
				int currentValue=commonDocuments.get(documentID);
				commonDocuments.put(documentID, currentValue+1);
			}
			else{
				commonDocuments.put(documentID,THREE);
			}
		}
		else{
			userResults.put(documentID,votes);
			//Get list of documents with same number of votes
			ArrayList<String> list=new ArrayList<String>();
			int key=Integer.valueOf(votes);
			if(userDocuments.containsKey(key)){
				list=userDocuments.get(key);
			}
			list.add(documentID);
			userDocuments.put(key,list);
		}
	}

	//Rank business documents.
	private void rankBusinessDocuments(String documentID,String stars,String type){
		if(businessResults.containsKey(documentID)){
			if(commonDocuments.containsKey(documentID)){
				int currentValue=commonDocuments.get(documentID);
				if(type.equalsIgnoreCase("c"))
					commonDocuments.put(documentID, currentValue+1);
				else
					commonDocuments.put(documentID, currentValue+3);
			}
			else{
				if(type.equalsIgnoreCase("c"))
					commonDocuments.put(documentID,ONE);
				else
					commonDocuments.put(documentID,THREE);
			}
		}
		else{
			businessResults.put(documentID,stars);
			//Get list of documents with same number of votes
			ArrayList<String> list=new ArrayList<String>();
			double key=Double.valueOf(stars);
			if(businessDocuments.containsKey(key)){
				list=businessDocuments.get(key);
			}
			list.add(documentID);
			businessDocuments.put(key,list);
		}
	}

	//Sorting the list based on values.
	private static HashMap<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order){
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>(){
			public int compare(Entry<String, Integer> o1,Entry<String, Integer> o2)	{
				if (order)
					return o1.getValue().compareTo(o2.getValue());
				else
					return o2.getValue().compareTo(o1.getValue());
			}
		});
		HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list){
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}


	//Method to split list of values on the given splitter.
	private String[] splitListOfValues(String valueList,String splitter){
		String listOfValues[]=valueList.split(splitter);
		return listOfValues;			
	}
}
