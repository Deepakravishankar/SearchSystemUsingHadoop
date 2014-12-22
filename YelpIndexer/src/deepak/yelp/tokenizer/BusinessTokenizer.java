package deepak.yelp.tokenizer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import deepak.yelp.tokenizerrules.*;

/**
 * @author Deepak
 *
 */
public class BusinessTokenizer extends Tokenizer {
	JSONObject tokenizer;
	String data=null;
	String documentId=null;

	public BusinessTokenizer(String data,String documentId){
		listOfValues=new ArrayList<String>();
		this.data=data;
		this.documentId=documentId;	
		try {
			tokenizer=new JSONObject(data);
		} catch (JSONException e) {
		}
	}
	public String businessNameTokenizer(){
		double stars=0.0;
		listOfValues.clear();
		listOfValues.add(documentId);
		listOfValues.add("b");

		String businessId=tokenizer.optString("business_id");
		if(checkIfKeyIsPresent("stars")){
			stars=tokenizer.optDouble("stars");
		}
		listOfValues.add(businessId);
		listOfValues.add(String.valueOf(stars));

		return makeIndexableValue(listOfValues);
	}

	public String businessCityTokenizer(){
		if(checkIfKeyIsPresent("city")){
			double stars=0.0;
			listOfValues.clear();
			listOfValues.add(documentId);
			listOfValues.add("b");

			String name=tokenizer.optString("name");
			if(checkIfKeyIsPresent("stars")){
				stars=tokenizer.optDouble("stars");
			}
			listOfValues.add(name);
			listOfValues.add(String.valueOf(stars));
			return makeIndexableValue(listOfValues);
		}
		return null;
	}

	public String businessStateTokenizer(){
		if(checkIfKeyIsPresent("state")){
			double stars=0.0;
			listOfValues.clear();
			listOfValues.add(documentId);
			listOfValues.add("b");

			String name=tokenizer.optString("name");
			if(checkIfKeyIsPresent("stars")){
				stars=tokenizer.optDouble("stars");
			}
			listOfValues.add(name);
			listOfValues.add(String.valueOf(stars));
			return makeIndexableValue(listOfValues);
		}
		return null;
	}

	public ArrayList<String> businessCategoriesTokenizer(){
		ArrayList<String> categoryKeys=new ArrayList<String>();
		if(checkIfKeyIsPresent("categories")){
			try {
				JSONArray categories=tokenizer.getJSONArray("categories");
				for(int i=0;i<categories.length();i++){
					categoryKeys.add(categories.optString(i));
				}
			} catch (JSONException e) {
			}
		}
		return categoryKeys;
	}

	public String makeCategoryIndexableValue(){
		StringBuffer valueToIndex=new StringBuffer();
		valueToIndex.append(documentId);
		valueToIndex.append("^^");
		valueToIndex.append("c");
		valueToIndex.append("^^");
		valueToIndex.append(tokenizer.optString("name"));
		valueToIndex.append("^^");
		valueToIndex.append(tokenizer.optString("stars"));
		return valueToIndex.toString();
	}

	private boolean checkIfKeyIsPresent(String keyToBeChecked){
		JSONArray keys=tokenizer.names();
		for(int i=0;i<keys.length();i++){
			if(keys.optString(i).equalsIgnoreCase(keyToBeChecked)){
				return true;
			}
		}
		return false;
	}
	public ArrayList<String> tokenizeKey(String key){
		ArrayList<String> keys=new ArrayList<String>();
		keys.add(key);
		ArrayList<String> tokenizedKey=new NumberRule().applyNumberRule(new AccentRule().applyAccentRule(new CapitalizationRule().applyCapitalizationRule(keys)));
		keys.clear();
		if(tokenizedKey.size() >0){
			String businessKey=tokenizedKey.get(0).replaceAll("[\\&\\#\\@]+","").trim();
			if(businessKey.length()>0){
				String words[]=businessKey.split(" ");
				for(int i=0;i<words.length;i++){
					if(words[i].length()>0)
						keys.add(words[i]);
				}
				return keys;
			}
		}
		return null;
	}
}
