/**
 * 
 */
package deepak.yelp.tokenizerrules;

import java.util.ArrayList;

/**
 * @author Deepak
 *
 */
public class SpecialCharacterRule {
	private ArrayList<String> tokensToBeReturned;

	public SpecialCharacterRule(){
		tokensToBeReturned=new ArrayList<String>();
	}

	public ArrayList<String> applySpecialCharacterRule(ArrayList<String> tokens){
		String currentToken=null;
		for(int i=0;i<tokens.size();i++){
			currentToken=tokens.get(i);
			currentToken=currentToken.replaceAll("[^\\w^\\s]+","");
			if(currentToken!=null && !currentToken.equals(""))
				tokensToBeReturned.add(currentToken);
		}
		return tokensToBeReturned;
	}
}
