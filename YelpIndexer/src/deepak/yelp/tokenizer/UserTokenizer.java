package deepak.yelp.tokenizer;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import deepak.yelp.tokenizerrules.*;

/**
 * @author Deepak
 *
 */

public class UserTokenizer extends Tokenizer{

	public UserTokenizer(){
		listOfValues=new ArrayList<String>();
	}

	public String userTokenizer(String data,String documentId){
		int totalRating=0;
		listOfValues.add(documentId);
		listOfValues.add("u");
		try {
			JSONObject tokenizer=new JSONObject(data);
			String userId=tokenizer.optString("user_id");
			int funny=tokenizer.optJSONObject("votes").optInt("funny");
			int useful=tokenizer.optJSONObject("votes").optInt("useful");
			int cool=tokenizer.optJSONObject("votes").optInt("cool");
			totalRating=funny+useful+cool;
			listOfValues.add(userId);
			listOfValues.add(String.valueOf(totalRating));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return makeIndexableValue(listOfValues);
	}

	public ArrayList<String> tokenizeKey(String key){
		ArrayList<String> keys=new ArrayList<String>();
		StringBuffer buffer=new StringBuffer();
		keys.add(key);
		ArrayList<String> tokenizedKey=new SpecialCharacterRule().applySpecialCharacterRule(new NumberRule().applyNumberRule(new AccentRule().applyAccentRule(new CapitalizationRule().applyCapitalizationRule(keys))));
		keys.clear();
		if(tokenizedKey.size() >0){
			String tokenKey=tokenizedKey.get(0);
			if(tokenKey.length()>0){
				String words[]=tokenKey.split(" ");
				for(int i=0;i<words.length;i++){
					if(words[i].length()>1)
						keys.add(words[i]);
				}
				return keys;
			}
		}
		return null;
	}

}
