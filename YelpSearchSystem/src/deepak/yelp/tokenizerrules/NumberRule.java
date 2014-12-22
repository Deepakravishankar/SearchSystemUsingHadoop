package deepak.yelp.tokenizerrules;

import java.util.ArrayList;

/**
 * @author Deepak
 *
 */
public class NumberRule {

	private ArrayList<String> tokensToBeReturned;

	public NumberRule(){
		tokensToBeReturned=new ArrayList<String>();
	}

	public ArrayList<String> applyNumberRule(ArrayList<String> tokens){
		String currentToken;
		for(int i=0;i<tokens.size();i++){
			currentToken=tokens.get(i);
			currentToken=currentToken.replaceAll("\\d+","");
			if(currentToken!=null && !currentToken.equals(""))
				tokensToBeReturned.add(currentToken);
		}
		return tokensToBeReturned;

	}
}
