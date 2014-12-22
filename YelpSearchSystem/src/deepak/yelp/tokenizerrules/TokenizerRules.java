package deepak.yelp.tokenizerrules;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Deepak
 *
 */

//Class to apply the tokenization rules on the given input.
public class TokenizerRules {
	private ArrayList<String> tokens;
	private StringTokenizer st;
	private StopWordsRule stopWords;
	private CapitalizationRule capitalization;
	private PunctuationRule punctuation;
	private HyphenRule hyphen;
	private NumberRule number;
	private SpecialCharacterRule specialCharacter;
	private StateRule state;

	public TokenizerRules(){
		tokens=new ArrayList<String>();
		stopWords=new StopWordsRule();
		capitalization=new CapitalizationRule();
		punctuation=new PunctuationRule();
		hyphen=new HyphenRule();
		number=new NumberRule();
		specialCharacter=new SpecialCharacterRule();
		state=new StateRule();
	}

	private void tokenSplitter(String inputData){
		st=new StringTokenizer(inputData);
		while(st.hasMoreTokens()){
			tokens.add(st.nextToken());
		}
	}

	public ArrayList<String> applyTokenizationRules(String inputData){
		//Split the given input into tokens.
		tokenSplitter(inputData);

		//Apply the following tokenization rules.
		return state.applyStateRule(stopWords.applyStopWordsRule(specialCharacter.applySpecialCharacterRule(punctuation.applyPunctuationRule(number.applyNumberRule(hyphen.applyHyphenRule(capitalization.applyCapitalizationRule(tokens)))))));
	}

}
