package eshmun;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.swing.JOptionPane;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.Transition;

public class DrawingTool {

	public DrawingTool() {
		
	}
	
	private final String windowsCommand = "cmd /c dot -Tpng dottool\\kripke.dot -o dottool\\output";//PLEASE ADD <refresh>.png to the path at the end
	private final String linuxCommand = "dot -Tpng dottool/kripke.dot -o dottool/output";
	private final String macCommand = "/usr/local/bin/dot -Tpng dottool/kripke.dot -o dottool/output"; //ALI DONE	
	private String getCommand() {
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("windows")) {
			return windowsCommand;
		} else if (os.contains("mac") || os.contains("apple") || os.contains("os x") || os.contains("osx")) {
			return macCommand;
		} else if (os.contains("linux") || os.contains("unix")) {
			return linuxCommand;
		}
		
		return null;
	}
	
	
public String DoDrawDiagram(Kripke kripke, Collection<Transition> transitions) {
		File f = new File("."+ System.getProperty("file.separator") +"dottool");
		if(!f.exists()) {
			f.mkdir();
		}
		
		CreateKripkeFile(kripke, transitions);
		long refresh = System.currentTimeMillis();

		 // create new filename filter
		f = null;
        FilenameFilter fileNameFilter = new FilenameFilter() {
  
           @Override
           public boolean accept(File dir, String name) {
              if(name.lastIndexOf('.')>0)
              {
                 // get last index for '.' char
                 int lastIndex = name.lastIndexOf('.');
                 
                 // get extension
                 String str = name.substring(lastIndex);
                 
                 // match path name extension
                 if(str.equals(".png") && name.startsWith("output"))
                 {
                    return true;
                 }
              }
              return false;
           }
        };
        // returns pathnames for files and directory
        f = new File("."+ System.getProperty("file.separator") +"dottool");
        File[] paths = f.listFiles(fileNameFilter);
        //for(File path:paths)
        //{
        //   path.delete();
        //}
        
        String command = getCommand() + refresh + ".png";
        System.out.println(command);
        //String command = "dottool/dot.exe -Tpng dottool\\kripke.dot -o dottool\\output" + refresh + ".png";
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			if (p.exitValue() == 0) {
				return "dottool" + System.getProperty("file.separator") +"output" + refresh + ".png";	
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return "";
	}
	
	private void CreateKripkeFile(Kripke kripke, Collection<Transition> transitions)
	{
		try {
			
			File dotFile = new File("dottool" + System.getProperty("file.separator") + "kripke.dot");
			if (dotFile.exists())
				dotFile.delete();
			dotFile.createNewFile();
			FileWriter fw = new FileWriter(dotFile);
			PrintWriter pw = new PrintWriter(fw);
			String[] stmts;
			if(transitions != null)
				stmts = kripke.GenerateDotText(transitions);
			else
				stmts = kripke.GenerateDotText();
			for (String str : stmts) {
				pw.println(str);
			}
			pw.flush();
			fw.flush();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


}
