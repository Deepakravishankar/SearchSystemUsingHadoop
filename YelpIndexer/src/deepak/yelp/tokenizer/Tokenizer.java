/**
 * @author Deepak
 *
 */

package deepak.yelp.tokenizer;

import java.io.*;
import java.util.ArrayList;

import org.json.*;

import deepak.yelp.indexer.CreateIndex;
import deepak.yelp.indexer.Indexer;

public class Tokenizer {
	ArrayList<String> listOfValues;
	private static final String USER="user";
	private static final String REVIEW="review";
	private static final String BUSINESS="business";
	private static final int MAXCOUNT=474434;
	private static String INPUTPATH="/Users/Deepak/Documents/MS/PDP/Project/Data/ParsedCombinedData/";

	//Method to read the document content.
	private String readDocumentContent(String documentId){
		StringBuffer path=new StringBuffer();
		path.append(INPUTPATH);
		path.append(documentId);
		path.append(".json");
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

	//Method to tokenize and index the data from each file.
	public void tokenize(String documentId){
		String value=null;
		String key=null;
		String jsoncontents=readDocumentContent(documentId);
		JSONObject tokenizer;
		Indexer indexer=new Indexer();
		try {
			tokenizer = new JSONObject(jsoncontents);
			String type=tokenizer.optString("type");
			if(type.equals(USER)){
				UserTokenizer userObject=new UserTokenizer();

				//Get the username as key.
				ArrayList<String> keysToIndex=userObject.tokenizeKey(tokenizer.optString("name"));
				value=userObject.userTokenizer(jsoncontents,documentId);

				for(int i=0;i<keysToIndex.size();i++){
					key=keysToIndex.get(i).trim();
					if(key.length() > 0)
						indexer.writeToIndex(key, value);
				}
			}
			else if(type.equalsIgnoreCase(REVIEW)){
				ReviewTokenizer reviewObject=new ReviewTokenizer(jsoncontents,documentId);

				//Get the user id as key.
				key=tokenizer.optString("user_id");
				value=reviewObject.userIdTokenizer();
				indexer.writeToIndex(key.trim(), value);

				//Get the business id as key.
				key=tokenizer.optString("business_id");
				value=reviewObject.businessIdTokenizer();
				indexer.writeToIndex(key.trim(), value);

				//Get the review text as key.
				ArrayList<String> keysToIndex=new ArrayList<String>();
				ArrayList<String> removeDuplicates=new ArrayList<String>();
				keysToIndex=reviewObject.textTokenizer();
				value=reviewObject.makeTextIndexableValue();
				for(int i=0;i<keysToIndex.size();i++){
					if(!removeDuplicates.contains(keysToIndex.get(i))){
						removeDuplicates.add(keysToIndex.get(i));
						key=keysToIndex.get(i).replaceAll("[\\_\\^]+","").trim();
						if(key.length() > 0)
							indexer.writeToIndex(key, value);
					}
				}
			}
			else if(type.equalsIgnoreCase(BUSINESS)){
				BusinessTokenizer businessObject=new BusinessTokenizer(jsoncontents,documentId);

				//Get the business name as key.
				ArrayList<String> businesskeysToIndex=businessObject.tokenizeKey(tokenizer.optString("name"));
				value=businessObject.businessNameTokenizer();
				if(businesskeysToIndex != null){
					for(int i=0;i<businesskeysToIndex.size();i++){
						key=businesskeysToIndex.get(i).trim();
						if(key.length() > 0)
							indexer.writeToIndex(key, value);
					}
				}
				//Get the business city as key.
				key=tokenizer.optString("city");
				value=businessObject.businessCityTokenizer();
				if(value != null){
					indexer.writeToIndex(key.trim(), value);
				}

				//Get the business state as key.
				key=tokenizer.optString("state");
				value=businessObject.businessStateTokenizer();
				if(value != null){
					indexer.writeToIndex(key.trim(), value);
				}

				//Get the categories as a list of keys.
				ArrayList<String> keysToIndex=new ArrayList<String>();
				keysToIndex=businessObject.businessCategoriesTokenizer();
				value=businessObject.makeCategoryIndexableValue();
				for(int i=0;i<keysToIndex.size();i++){
					indexer.writeToIndex(keysToIndex.get(i).trim(), value);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	//Creates a list of indexable values.
	public static String makeIndexableValue(ArrayList<String> listOfValues){
		StringBuffer buffer=new StringBuffer();
		int size=listOfValues.size();
		for(int i=0;i<size;i++){
			buffer.append(listOfValues.get(i));
			if(i!=size-1){
				buffer.append("^^");
			}
		}
		return buffer.toString();
	}

	public static void main(String args[]) {
		int count=0;
		Tokenizer t=new Tokenizer();
		while(count <= MAXCOUNT){
			System.out.println(count);
			t.tokenize(String.valueOf(count));
			count++;
		}
		System.out.println("Initial Indexing completed");
		CreateIndex createIndex=new CreateIndex();
		createIndex.indexData();
	}
}
