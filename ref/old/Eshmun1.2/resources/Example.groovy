automaton ("A", "i", "j"){ 
	states {
		a1 = 0
		
	}
	actions {
		input ('m1') {
			effect { 
				a1 = 1
				println 'Input action m1 called in A'
			}
		}
		
		output ('m2') {
			pre {a1 == 4}
			effect { 
				a1 = 2
				println 'Output action m2 called in A'
				} 
		}
		
		internal('m3') {
			pre{a1 == 2}
			effect {
				a1 = 3
				println 'Internal action m3 called in A'
			}
		}
		
	}	
} 

automaton ("B"){ 
	states {
		b1 = 0
	}
	actions {
		input ('m1') {
			effect { 
				b1 = 6
				println 'Input action m1 called in B'
			} 
		}
		
		input ('m2') {
			effect { 
				b1 = 2
				println 'Input action m2 called in B'
			}
		}
		
		internal ('m4') {
			effect { 
				b1 = 3
				println 'Internal action m4 called in B'
			} 
		}
		
		internal ('m5') {
			effect { 
				b1 = 1
				println 'Internal action m5 called in B'
			} 
		}
		
	}	
} 