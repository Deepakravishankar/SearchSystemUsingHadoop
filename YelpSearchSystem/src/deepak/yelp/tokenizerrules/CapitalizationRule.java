/**
 * 
 */
package deepak.yelp.tokenizerrules;

import java.util.ArrayList;

/**
 * @author Deepak
 *
 */
public class CapitalizationRule {
	private ArrayList<String> tokensToBeReturned;

	public CapitalizationRule(){
		tokensToBeReturned=new ArrayList<String>();
	}

	public ArrayList<String> applyCapitalizationRule(ArrayList<String> tokens){
		for(int i=0;i<tokens.size();i++){
			tokensToBeReturned.add(tokens.get(i).toLowerCase().trim());
		}
		return tokensToBeReturned;
	}
}
