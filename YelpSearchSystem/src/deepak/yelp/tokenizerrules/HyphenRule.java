/**
 * 
 */
package deepak.yelp.tokenizerrules;

import java.util.ArrayList;

/**
 * @author Deepak
 *
 */
public class HyphenRule {
	private ArrayList<String> tokensToBeReturned;

	public HyphenRule(){
		tokensToBeReturned=new ArrayList<String>();
	}

	public ArrayList<String> applyHyphenRule(ArrayList<String> tokens){
		String token;
		for(int i=0;i<tokens.size();i++){
			token=tokens.get(i);
			token=token.replaceAll("[\\-.]+"," ").trim();
			token=token.replaceAll("[\\^.]+","").trim();
			if(token!=null && !token.equals(""))
				tokensToBeReturned.add(token);
		}
		return tokensToBeReturned;

	}
}
