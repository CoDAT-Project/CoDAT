package t7;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Pattern;

import eshmun.Eshmun;
import eshmun.gui.kripke.StructureType;
import t7.expressions.BooleanExpression;
import t7.expressions.Expression;
import t7.statements.DelStatement;
import t7.statements.ExportStatement;
import t7.statements.ExtractStatement;
import t7.statements.ForStatement;
import t7.statements.IfStatement;
import t7.statements.InstanceStatement;
import t7.statements.LoadStatement;
import t7.statements.SearchStatement;
import t7.statements.Statement;
import t7.statements.WhileStatement;
import t7.statements.Statement.Types;
import t7.statements.StructureDefinitionStatement;
import t7.statements.VariableDefinitionStatement;
import t7.types.Structure;
import t7.types.StructureTemplate;
import t7.types.T7Integer;
import t7.types.T7Range;
import t7.types.VariableType;

/**
 * This class offers methods to handle executing statements (one at a time).
 * @author kinan
 */
public class StatementHandler {
	/**
	 * A list of structures that were already instantiated.
	 */
	private TreeSet<Structure> definitions;
	
	/**
	 * Mapping of variable names to variables.
	 */
	private HashMap<String, VariableType> table;
	
	/**
	 * The statement interpreter, contains useful methods
	 * for dealing with files and strings.
	 */
	private StatementInterpreter interpreter;
	
	/**
	 * The stream to print result through.
	 */
	private PrintStream resultStream;
	
	/**
	 * Creates a new Statement Handler.
	 * @param interpreter the interpreter that calls the appropriate method in the handler based on the parser.
	 * @param resultStream the stream to print results through.
	 */
	public StatementHandler(StatementInterpreter interpreter, PrintStream resultStream) {
		table = new HashMap<String, VariableType>();
		definitions = new TreeSet<Structure>();
		this.interpreter = interpreter;
		this.resultStream = resultStream;
	}
	
	public void handleStatement(Statement statement) throws IOException {
		if(statement == null) return; //comment
		
		if(statement.getType() == Types.ClearStatement) { 
			table.clear();
			definitions.clear();
			
			resultStream.println("Cleared");
		}
		
		if(statement.getType() == Types.List) {
			int counter = 0;
			for(Structure st : definitions) { 
				resultStream.println(st.getName());
				counter++;
			}
			resultStream.println("Done. Total "+counter);
		}
		
		if(statement.getType() == Types.Search) {
			int total = 0;
			
			String query = ((SearchStatement) statement).getQuery();
			for(Structure st : definitions) { 
				if(st.getName().contains(query)) {
					resultStream.println(st.getName());
					total++;
				}
			}
			
			resultStream.println("Done. Total "+total);
		}
		
		if(statement.getType() == Types.Del) {
			int total = 0;
			
			List<Pattern> patterns = new ArrayList<Pattern>();
			List<String> del = ((DelStatement) statement).getStructures();
			for(int i = 0; i < del.size(); i++) {
				try {
					patterns.add(Pattern.compile(del.get(i)));
					del.remove(i); i--;
					continue;
				} catch(Exception ex) { }
				
				String d = del.get(i);
				d = d.replace(",", ", ");
				d = d.replace(",  ", ", ");
				del.set(i, d);
			}
			
			Iterator<Structure> iterator = definitions.iterator();
			while(iterator.hasNext()) {
				Structure st = iterator.next();
				
				boolean contains = del.contains(st.getName());
				for(int i = 0; i < patterns.size() && !contains; i++)
					contains = patterns.get(i).matcher(st.getName()).matches();
				
				if(contains) {
					iterator.remove();
					
					resultStream.println(st.getName());
					total++;
				}
			}
			
			resultStream.println("Done. Total "+total);
		}
		
		if(statement.getType() == Types.Export || statement.getType() == Types.Start) {
			Writer writer;
			
			if(statement.getType() == Types.Export) { //Write to a file.
				ExportStatement exportStatement = (ExportStatement) statement;
				String path = exportStatement.getPath();
				
				File file = new File(path);
				if(file.exists())
					file.delete();
				
				file.createNewFile();
				writer = new FileWriter(file);
			} else { //write to a string.
				writer = new StringWriter();
			}
			
			writer.write("***MULTI***"+System.lineSeparator());
			for(Structure st : definitions) {
				String def = st.getDefinition();
				
				while(def.contains(System.lineSeparator()+System.lineSeparator()))
					def = def.replace(System.lineSeparator()+System.lineSeparator(), System.lineSeparator());
				
				writer.write(def.trim());
				writer.write(System.lineSeparator());
				writer.write("***END***"+System.lineSeparator());
			}
			
			writer.close();
			resultStream.println("Done.");
			
			if(statement.getType() == Types.Start) { //Load Eshmun
				final String definition = writer.toString();
				if(Eshmun.eshmun != null) {
					Eshmun.eshmun.loadDefinition(definition, StructureType.MultiKripke);
				} else {
					java.awt.EventQueue.invokeLater(new Runnable() {
					    public void run() {
					    	Eshmun eshmun = new Eshmun(StructureType.MultiKripke);
					    	eshmun.setVisible(true);
					    	eshmun.loadDefinition(definition, StructureType.MultiKripke);
					    }
					});
				}
			}
		}
		
		
		if(statement.getType() == Types.Load) {
			LoadStatement loadStatement = (LoadStatement) statement;
			String path = loadStatement.getPath();
			
			File file = new File(path);
						
			Scanner scan = new Scanner(file);
			resultStream.println("Executing File \"" + path+"\"");
			resultStream.println("");
			while(scan.hasNext()) {
				String line = interpreter.read(scan);
				
				if(line.isEmpty()) continue;
				interpreter.parse(line);
				resultStream.println("");
			}
			
			scan.close();
			resultStream.println("Done.");
		}
		
		if(statement.getType() == Types.VariableDefinition) {
			VariableDefinitionStatement def = (VariableDefinitionStatement) statement;
			
			Expression exp = def.getExpression();
			table.put(def.getName(), exp.evaluate(this, null, 0));
			
			resultStream.println(def.getName() + " = " + table.get(def.getName()));
		}
		
		if(statement.getType() == Types.StructureDefinition) {
			StructureDefinitionStatement def = (StructureDefinitionStatement) statement;
			table.put(def.getName(), def.getStructure());
			
			resultStream.println(statement.toString().trim());
		}
		
		if(statement.getType() == Types.Extract) {
			ExtractStatement extract = (ExtractStatement) statement;
			if(Eshmun.eshmun == null) { 
				resultStream.println("Cannot Use extract, Eshmun is not currently running.");
			} else {
				Collection<String> defs = Eshmun.eshmun.extractPairSpecs(extract.getStructures().size() == 0 ? null : extract.getStructures());
				
				for(String def : defs) {
					String name = def.substring("define".length(), def.indexOf("<")).trim();
					String[] indices = def.substring(def.indexOf("<") + 1, def.indexOf(">")).split(",");					
					String definition = def.substring(def.indexOf("{") + 1, def.lastIndexOf("}")).trim();
					
					for(int i = 0; i < indices.length; i++) {
						indices[i] = indices[i].trim();
					}
					
					StructureTemplate template = new StructureTemplate(name, indices, definition, extract.isOrdered());
					table.put(name, template);
					
					resultStream.println(def.trim() + System.lineSeparator());
				}
			}
		}
		
		if(statement.getType() == Types.InstanceStatement) {
			InstanceStatement instance = (InstanceStatement) statement;
			
			VariableType tmp = table.get(instance.getName());
			if(tmp == null)
				throw new IllegalArgumentException("Unknown symbol "+instance.getName()+".");
			
			else if(!(tmp instanceof StructureTemplate))
				throw new IllegalArgumentException("Illegal type for symbol "+instance.getName()+", expected structure.");
			
			StructureTemplate template = (StructureTemplate) tmp;
			ArrayList<Expression> vars = instance.getVars();
			ArrayList<BooleanExpression> bools = instance.getBools();
			ArrayList<String> names = instance.getNames();
			int total = instantiate(template, vars, bools, names, 0, new String[0]);
			
			resultStream.println("Done. Total "+total);
		}		
		
		if(statement.getType() == Types.For) {
			ForStatement forStatement = (ForStatement) statement;
			String varName = forStatement.getVarName();
			
			HashSet<String> oldKeys = new HashSet<String>(table.keySet());
			for(int i : forStatement.evaluateRange(this)) {
				table.put(varName, new T7Integer(i));
				for(Statement stmt : forStatement.getBody()) {
					handleStatement(stmt);
				}
				
				//Delete variables that go out of scope.
				Iterator<Map.Entry<String, VariableType>> iterator = table.entrySet().iterator();				
				while(iterator.hasNext()) {
					String key = iterator.next().getKey();
					if(!oldKeys.contains(key)) iterator.remove();
				}
			}
		}
		
		if(statement.getType() == Types.While) {
			WhileStatement whileStatement = (WhileStatement) statement;			
			HashSet<String> oldKeys = new HashSet<String>(table.keySet());
			
			while(whileStatement.evaluateCondition(this).value()) {
				for(Statement stmt : whileStatement.getBody()) {
					handleStatement(stmt);
				}
				
				//Delete variables that go out of scope.
				Iterator<Map.Entry<String, VariableType>> iterator = table.entrySet().iterator();				
				while(iterator.hasNext()) {
					String key = iterator.next().getKey();
					if(!oldKeys.contains(key)) iterator.remove();
				}
			}
		}
		
		if(statement.getType() == Types.If) {
			IfStatement ifStatement = (IfStatement) statement;			
			HashSet<String> oldKeys = new HashSet<String>(table.keySet());
			
			if(ifStatement.evaluateCondition(this).value()) {
				for(Statement stmt : ifStatement.getIfBody()) {
					handleStatement(stmt);
				}
			} else if(ifStatement.hasElse()) {
				for(Statement stmt : ifStatement.getElseStatement().getElseBody()) {
					handleStatement(stmt);
				}
			}
			
			//Delete variables that go out of scope.
			Iterator<Map.Entry<String, VariableType>> iterator = table.entrySet().iterator();				
			while(iterator.hasNext()) {
				String key = iterator.next().getKey();
				if(!oldKeys.contains(key)) iterator.remove();
			}
		}
	}
	
	/**
	 * Attempts to create instances recursively, each call will iterate on all possible values
	 * for the variables at index (that satisfy the relating boolean expression if provided).
	 * @param template the structure template to instantiate from.
	 * @param vars the variables/values each relating to an index.
	 * @param bools the boolean expression on each variable (null means no condition).
	 * @param names the names of the indices.
	 * @param index the current variable to iterate over values.
	 * @param values keeps track of the current set of values assigned to every variable (will be used as a replacement in the structure template).
	 * @throws IllegalArgumentException If the type of some instantiation index was not an Integer or a Range.
	 * @return the count of new instantiated structures.
	 */
	private int instantiate(StructureTemplate template, ArrayList<Expression> vars, 
		ArrayList<BooleanExpression> bools, ArrayList<String> names, int index, String[] values) throws IllegalArgumentException {
		
		if(index == vars.size()) { //base case, indices ready.
			int before = definitions.size();
			
			Structure st = template.instantiate(values);
			if(st == null) return 0;
			
			definitions.add(st);
			
			if(definitions.size() != before) {
				resultStream.println(st.getName());
				return 1;
			}
			
			return 0;
		}
		
		VariableType var = vars.get(index).evaluate(this, null, 0);
		BooleanExpression bool = bools.get(index);
		
		if(var.getType() == VariableType.Types.Integer) { //Arithmetic
			String[] next = Arrays.copyOf(values, index + 1);
			next[index] = ((T7Integer) var).value() + "";
			
			return instantiate(template, vars, bools, names, index + 1, next);
		} else if(var.getType() == VariableType.Types.Range) { //Range
			String[] next = Arrays.copyOf(values, index + 1);
				
			int sum = 0;
			for(Integer i : (T7Range) var) {
				if(bool == null || bool.evaluate(this, names.get(index), i).value()) {
					next[index] = i + "";
					sum += instantiate(template, vars, bools, names, index + 1, next);
				}
			}
			
			return sum;
		} else {
			throw new IllegalArgumentException("Illegal type in structure instantiation, Expected Integer or Range.");
		}
	}
	
	/**
	 * Gets the variable specified with the name.
	 * @param name the name of the variable.
	 * @return the variable with that name.
	 */
	public VariableType getVariable(String name) {
		return table.get(name);
	}
}
