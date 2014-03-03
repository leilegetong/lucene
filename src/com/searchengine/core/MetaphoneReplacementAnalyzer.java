package com.searchengine.core;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.util.Version;

public class MetaphoneReplacementAnalyzer extends Analyzer{
//	public TokenStream tokenStream(String fieldName,Reader reader){
//
//        return new MetaphoneReplacementFilter{
//
//               new LetterTokenizer(Version.LUCENE_42,reader));
//
//        }
//
//   }

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		Tokenizer source = new WhitespaceTokenizer(Version.LUCENE_42,reader);
	      TokenStream filter = new MetaphoneReplacementFilter(source);
	     return new TokenStreamComponents(source, filter);
	}

}
