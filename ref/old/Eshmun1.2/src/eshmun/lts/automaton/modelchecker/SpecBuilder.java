package eshmun.lts.automaton.modelchecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.lts.kripke.Kripke;
import eshmun.modelchecker.ModelChecker;
import eshmun.parser.ANTLRParser;
import eshmun.parser.Parser;
import eshmun.regex.IRegexEvaluator;
import eshmun.regex.RegexEvaluator;

public class SpecBuilder {

	List<Specification> specifications;
	
	public SpecBuilder(String fileName) throws Exception {
		specifications = new ArrayList<Specification>();
		Parser specParser = new ANTLRParser();
		
		
		File file = new File(fileName);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
	
		String line = null; 
		while((line = bufferedReader.readLine())!=null) {
			String[] split = line.split("=");
			Specification spec = new Specification(split[0],specParser.parse(split[1]));
			specifications.add(spec);
		}
	}
	

}
