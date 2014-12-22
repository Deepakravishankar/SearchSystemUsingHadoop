package deepak.yelp.tokenizer;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import deepak.yelp.tokenizerrules.TokenizerRules;

/**
 * @author Deepak
 *
 */
public class ReviewTokenizer extends Tokenizer {
	JSONObject tokenizer;
	String data=null;
	String documentId=null;

	public ReviewTokenizer(String data,String documentId){
		listOfValues=new ArrayList<String>();
		this.data=data;
		this.documentId=documentId;	
		try {
			tokenizer=new JSONObject(data);
		} catch (JSONException e) {
		}
	}

	public String userIdTokenizer(){
		listOfValues.clear();
		int totalRating=0;
		listOfValues.add(documentId);
		listOfValues.add("r");

		String businessId=tokenizer.optString("business_id");
		int funny=tokenizer.optJSONObject("votes").optInt("funny");
		int useful=tokenizer.optJSONObject("votes").optInt("useful");
		int cool=tokenizer.optJSONObject("votes").optInt("cool");
		totalRating=funny+useful+cool;

		listOfValues.add(businessId);
		listOfValues.add(String.valueOf(totalRating));

		return makeIndexableValue(listOfValues);
	}

	public String businessIdTokenizer(){
		int totalRating=0;
		listOfValues.clear();

		listOfValues.add(documentId);
		listOfValues.add("r");

		String userId=tokenizer.optString("user_id");
		int funny=tokenizer.optJSONObject("votes").optInt("funny");
		int useful=tokenizer.optJSONObject("votes").optInt("useful");
		int cool=tokenizer.optJSONObject("votes").optInt("cool");
		totalRating=funny+useful+cool;

		listOfValues.add(userId);
		listOfValues.add(String.valueOf(totalRating));

		return makeIndexableValue(listOfValues);
	}

	public ArrayList<String> textTokenizer(){

		ArrayList<String> textKeys=new ArrayList<String>(); 

		TokenizerRules ruleManager=new TokenizerRules();
		textKeys=ruleManager.applyTokenizationRules(tokenizer.optString("text"));

		return textKeys;
	}

	public String makeTextIndexableValue(){
		StringBuffer valueToIndex=new StringBuffer();
		valueToIndex.append(documentId);
		valueToIndex.append("^^");
		valueToIndex.append("t");
		valueToIndex.append("^^");
		valueToIndex.append(tokenizer.optString("business_id"));
		return valueToIndex.toString();
	}
}
