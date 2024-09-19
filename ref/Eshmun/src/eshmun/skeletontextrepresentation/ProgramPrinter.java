package eshmun.skeletontextrepresentation;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;



public class ProgramPrinter {

	public static final String initials = "initial";
	public static final String sharedvariables = "sharedvariables";
	public static final String program = "program";
	public static final String process = "process";
	public static final String action = "action";
	public static final String specifications = "specifications";
	public static final String l_grd = "l_grd";
	public static final String g_grd = "g_grd";
	public static final String l_eff = "l_eff";
	public static final String g_eff = "g_eff";

	public Program prog = new Program();

	public void AddTransition(String processNumber, String start, String end, String assignmentA, String guard, String actionName) {

		Action action = new Action(processNumber, start, end, assignmentA, guard, actionName);

		if (prog.processes.keySet().contains(processNumber)) {
			// Process already created
			Process process = prog.processes.get(processNumber);
			process.actions.add(action);
			// Add it to set
			prog.processes.put(processNumber, process);

		} else {

			// Create Process
			Process process = new Process();
			process.name = processNumber;
			process.actions.add(action);
			// Add it to set
			prog.processes.put(processNumber, process);

		}

	}
	
	public void AddProcess(String processNumber) {

		 

		if (prog.processes.keySet().contains(processNumber)) {		
		} else {

			// Create Process
			Process process = new Process();
			process.name = processNumber;
			prog.processes.put(processNumber, process);

		}

	}

	public class Program {

		public String intial;
		
		public String spec;
		
		public HashMap<String, Process> processes = new HashMap<String, Process>();
	 
		public HashSet<SharedVariable> programSharedVariables = new  HashSet<>();
		
		/**
		 * Received as a set of assignments in the form x:=2,x:=1,y:=3
		 * @param allAssignments
		 */
		public void addSharedVariables(HashSet<String> allAssignments) {
			
			HashMap<String, HashSet<String>> variableValuesDictionary = new HashMap<String, HashSet<String>>();
			
			for (String string : allAssignments) {
				if (string.contains(":=") && !string.contains("null")) {
					String varname = string.split(":=")[0];
					String valVal =  string.split(":=")[1];
					
					if(variableValuesDictionary.containsKey(varname)) {
						HashSet<String> values = variableValuesDictionary.get(varname);
						values.add(valVal);
						variableValuesDictionary.put(varname, values);
								
					}else {
						HashSet<String> values = new HashSet<String>();
						values.add(valVal);
						variableValuesDictionary.put(varname, values);
					}
				}
			}
			
			for (Entry<String, HashSet<String>> entry : variableValuesDictionary.entrySet()) {
				
				SharedVariable sharedVariable = new SharedVariable();
				sharedVariable.name = entry.getKey();
				sharedVariable.values = entry.getValue();
				programSharedVariables.add(sharedVariable);
			}
			
		}

		public String print() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(String.format("%s  {" + System.lineSeparator(), program));
			sb.append(String.format("\t%s   : %s" + System.lineSeparator(), initials, intial == null ? "" : intial));
			
			if(programSharedVariables.size() >0 ) {
				sb.append(String.format("\t%s   : " , sharedvariables));
				
				int count=1;
				for (SharedVariable v : programSharedVariables) {
					sb.append(String.format("%s" + (count == programSharedVariables.size() ? "":" , "), v.print()));
					count++;
				}
				
				sb.append(String.format( System.lineSeparator()));
					
			}
			
			
			
			for (Process proc : processes.values()) {
				sb.append(proc.print());
			}
			
			sb.append(String.format("\t %s : { %s }", specifications , spec));
			sb.append(String.format( System.lineSeparator()));
			sb.append(String.format("}" + System.lineSeparator()));
			
			
			return sb.toString();

		}
	}

	public class Process {

		public String name;
		public ArrayList<Action> actions = new ArrayList<>();

		public String print() {
			StringBuilder sb = new StringBuilder();
			
			sb.append(String.format("\t%s  %s {" + System.lineSeparator(), process, name));
			for (Action action : actions) {
				sb.append(action.print());
			}
			sb.append(String.format("\t }" + System.lineSeparator()));
			
			
			return sb.toString();

		}
	}

	public class Action {

		public String name;
		public String processNumber;
		public String start;
		public String end;
		public String assignmentA;
		public String guard;

		public Action(String processNumber, String start, String end, String assignmentA, String guard, String actionName) {
			this.processNumber = processNumber ;
			this.start = start;
			this.end = end;
			this.assignmentA = assignmentA;
			this.guard = guard;
			this.name = actionName;
		}

		public String print() {

			// T2 C2 [x := null] (T1 & x=2)

			/*
			 * action { 
			 * l_grd: T2 
			 * g_grd: N1 | (T1 & x=2) 
			 * l_eff: T2 ,C2 := ff ,tt 
			 * g_eff: x := null 
			 * }
			 */
			StringBuilder sb = new StringBuilder();

			String[] startLabels = start.split(",");
			String[] endLabels = end.split(",");
			
			
			String localGuardPrint = null;
			
			if(start.trim().length()>0) {
				localGuardPrint =  String.join(" & ",  startLabels) ;
			}else {
				localGuardPrint = "true";
			}
			
			
			sb.append(String.format("\t\t%s   {" + System.lineSeparator(), action + " " + this.name.replace(",", "")));
			sb.append(String.format("\t\t\t%s : %s " + System.lineSeparator(), l_grd, localGuardPrint));
			sb.append(String.format("\t\t\t%s : %s " + System.lineSeparator(), g_grd, guard.equals("()")? "true": guard));
			
		//Dealing with Local Effects
			
			
			TreeMap<String, String> localGuardMap = new TreeMap<String, String>();
			
			for (int i = 0; i < startLabels.length; i++) {
				if(startLabels[i]== null || startLabels[i].isEmpty()) {
					
				}else {
				if( Arrays.asList(endLabels).contains(startLabels[i]) )
					localGuardMap.put(startLabels[i], "tt");
				else
					localGuardMap.put(startLabels[i], "ff");
				}
			}
			
			for (int i = 0; i < endLabels.length; i++) {
				
				if(endLabels[i]== null || endLabels[i].isEmpty()) {
				}else {
					localGuardMap.put(endLabels[i], "tt");
				}
				
					
			}
			
			
			
			
			sb.append(String.format("\t\t\t%s : %s := %s"  + System.lineSeparator(), l_eff, 
					String.join(",",  localGuardMap.keySet()), 
					String.join(",",  localGuardMap.values())
							));
			
			if(assignmentA.contains(",")) {
				
				assignmentA = assignmentA.replace("[", "").replace("]", "");
				String[] assignments = assignmentA.split(",");
				int l = assignments.length;
				String[] variables = new String[l];
				String[] values = new String[l];
				int i = 0 ;
				for (String string : assignments) {
					String[] split = string.split(":=");
					variables[i]= split[0];
					values[i]= split[1];
					
					i++;
				}
				
				sb.append(String.format("\t\t\t%s : %s := %s"  + System.lineSeparator(), g_eff, 
						String.join(",", variables), String.join(",", values) ));  
				
				
			}else {
				sb.append(String.format("\t\t\t%s : %s"  + System.lineSeparator(), g_eff, 
						assignmentA.replace("[", "").replace("]", "")));  
			}
		
			
			
			
			sb.append(String.format("\t\t }" + System.lineSeparator()));

			return sb.toString();

		}

	}

	public class SharedVariable{
		
		public String name;
		
		public HashSet<String>  values= new HashSet<>();
		
		public String print() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(String.format(" %s : { %s } ", name,  String.join(",", values) ));			
			
			return sb.toString();
		
		}
		
	}
}
