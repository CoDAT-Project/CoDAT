package eshmun.skeletontextrepresentation;

import java.util.Objects;

public class Transition {

	private State sourceState;
	
	private State destinationState;
	
	private String effects;
	
	private String actionNumber;
	
	public State getSource() {
		return sourceState;
	}
	
	public State getDestination() {
		return destinationState;
	}
	
	public String getActionNumber() {
		
		if(actionNumber == null) return "";
		return actionNumber;
	}
	
	public String getEffects() {
		return effects;
	}
	
	public Transition(State source, State destination, String effects) {
		this.sourceState = source;
		this.destinationState= destination;
		this.effects = effects;
		 
	}
	
	public Transition(State source, State destination, String effects, String actionNumber) {
		this.sourceState = source;
		this.destinationState= destination;
		this.effects = effects;
		this.actionNumber = actionNumber;
	}
	
	@Override
	public String toString() {

		return   sourceState.toString() + "-->" + destinationState.toString();

	}
	

	 // Overriding equals() to compare two State objects 
   @Override
   public boolean equals(Object o) { 
 
       // If the object is compared with itself then return true   
       if (o == this) { 
           return true; 
       } 
 
       /* Check if o is an instance of State or not  */
       if (!(o instanceof Transition)) { 
           return false; 
       } 
         
 
       Transition s = (Transition) o; 
         
       // Compare the data members and return accordingly  
       
       boolean b= this.toString().equals(s.toString());;
       if(!b) {
    	   //System.out.println(this + "  !=  "+ o);
       }else {
    	  // System.out.println(this + "  ==  "+ o);
       }
       return b;
               
   } 
   
 
   @Override
   public int hashCode() {
      
          return Objects.hash(this.toString());
   }


}
