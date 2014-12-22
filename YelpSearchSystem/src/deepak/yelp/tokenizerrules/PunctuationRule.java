/**
 * 
 */
package deepak.yelp.tokenizerrules;

import java.util.ArrayList;

/**
 * @author Deepak
 *
 */
public class PunctuationRule {
	private ArrayList<String> tokensToBeReturned;

	public PunctuationRule(){
		tokensToBeReturned=new ArrayList<String>();
	}

	public ArrayList<String> applyPunctuationRule(ArrayList<String> tokens){
		String currentToken;
		for(int i=0;i<tokens.size();i++){
			currentToken=tokens.get(i);

			currentToken=currentToken.replaceAll("[^\\w.]+","");
			currentToken=currentToken.replaceAll("(\\.|\\?|!)\\s*"," ").trim();

			if(currentToken!=null && !currentToken.equals(""))
				tokensToBeReturned.add(currentToken);
		}
		return tokensToBeReturned;
	}
}
