package eshmun.choreography;
public class Process{
	String name, port;
	public Process(String name, String port) {
		this.name = name;
		this.port = port;
	}
	public Process(String data) {
		if(data.contains(".")) {
			String[] dataSplit = data.split("\\.");
			this.name = dataSplit[0];
			this.port = dataSplit[1];
		}
		else {
			this.name = data;
			this.port = "NuLL";
		}
		
	}
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Process))
			return false;
		Process p = (Process) o;
		return (this.name.equals(p.name) && this.port.equals(p.port));
	}
}
