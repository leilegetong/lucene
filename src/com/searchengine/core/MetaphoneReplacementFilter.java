package com.searchengine.core;

import java.io.IOException;

import org.apache.commons.codec.language.Metaphone;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class MetaphoneReplacementFilter extends TokenFilter{
	public static final String METAPHONE="METAPHONE";
	private CharTermAttribute termAttr;
	private TypeAttribute typeAttr;
	
    private Metaphone metaphoner=new Metaphone();
    public MetaphoneReplacementFilter(TokenStream input){
       super(input);
       termAttr = addAttribute(CharTermAttribute.class);
       typeAttr = addAttribute(TypeAttribute.class);
    }

  

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		if(!input.incrementToken())
			return false;
		String encoded = metaphoner.encode(termAttr.toString());
		System.out.println(termAttr.toString());
		System.out.println(encoded);
//		System.out.println(encoded.length());

		termAttr.copyBuffer(encoded.toCharArray(), 0, encoded.length());
		typeAttr.setType(METAPHONE);
		return true;
	}

}
