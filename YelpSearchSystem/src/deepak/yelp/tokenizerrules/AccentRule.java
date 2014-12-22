/**
 * 
 */
package deepak.yelp.tokenizerrules;

import java.text.Normalizer;
import java.util.ArrayList;

/**
 * @author Deepak
 *
 */
public class AccentRule {
	private ArrayList<String> tokensToBeReturned;

	public AccentRule(){
		tokensToBeReturned=new ArrayList<String>();
	}

	public ArrayList<String> applyAccentRule(ArrayList<String> tokens){
		for(int i=0;i<tokens.size();i++){
			String token=tokens.get(i);
			token=Normalizer.normalize(token, Normalizer.Form.NFD);
			token=token.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
			tokensToBeReturned.add(token);
		}
		return tokensToBeReturned;
	}
}
