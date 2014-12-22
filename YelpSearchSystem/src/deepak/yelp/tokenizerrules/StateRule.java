/**
 * 
 */
package deepak.yelp.tokenizerrules;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Deepak
 *
 */
public class StateRule {
	private ArrayList<String> tokensToBeReturned;
	private HashMap<String,String> statemap;

	public StateRule(){
		tokensToBeReturned=new ArrayList<String>();
		statemap=new HashMap<String,String>();
		statemap.put("alabama", "al");
		statemap.put("alaska", "ak");
		statemap.put("arizona", "az");
		statemap.put("arkansas", "ar");
		statemap.put("california", "ca");
		statemap.put("colorado", "co");
		statemap.put("connecticut", "ct");
		statemap.put("delaware", "de");
		statemap.put("florida", "fl");
		statemap.put("georgia", "ga");
		statemap.put("hawaii", "hi");
		statemap.put("idaho", "id");
		statemap.put("illinois", "il");
		statemap.put("indiana", "in");
		statemap.put("iowa", "ia");
		statemap.put("kansas", "ks");
		statemap.put("kentucky", "ky");
		statemap.put("louisiana", "la");
		statemap.put("maine", "me");
		statemap.put("maryland", "md");
		statemap.put("massachusetts", "ma");
		statemap.put("michigan", "mi");
		statemap.put("minnesota", "mn");
		statemap.put("mississippi", "ms");
		statemap.put("missouri", "mo");
		statemap.put("montana", "mt");
		statemap.put("nebraska", "ne");
		statemap.put("nevada", "nv");
		statemap.put("new hampshire", "nh");
		statemap.put("new jersey", "nj");
		statemap.put("new mexico", "nm");
		statemap.put("new york", "ny");
		statemap.put("north carolina", "nc");
		statemap.put("north dakota", "nd");
		statemap.put("ohio", "oh");
		statemap.put("oklahoma", "ok");
		statemap.put("oregon", "or");
		statemap.put("pennsylvania", "pa");
		statemap.put("rhode island", "ri");
		statemap.put("south carolina", "sc");
		statemap.put("south dakota", "sd");
		statemap.put("tennessee", "tn");
		statemap.put("texas", "tx");
		statemap.put("utah", "ut");
		statemap.put("vermont", "vt");
		statemap.put("virginia", "va");
		statemap.put("washington", "wa");
		statemap.put("west virginia", "wv");
		statemap.put("wisconsin", "wi");
		statemap.put("wyoming", "wy");

	}

	public ArrayList<String> applyStateRule(ArrayList<String> tokens){
		String token=null;
		for(int i=0;i<tokens.size();i++){
			token=tokens.get(i);
			if(statemap.containsKey(token.toLowerCase())){
				tokensToBeReturned.add(statemap.get(token));
			}else{
				tokensToBeReturned.add(token);
			}
		}
		return tokensToBeReturned;
	}
}
