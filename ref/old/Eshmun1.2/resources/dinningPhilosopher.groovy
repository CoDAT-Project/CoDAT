iProgram("dinningPhiloIProgram") {
	pair {
		I = [[1,2],[2,3]]

	    varRegex = "_(.)+"
		apRegex = "[a-z]+"
		spec.initial = "ANDi[Ni]"
		spec.Ni_To_Ti = "ANDi[A[G((Ni)=>(E[Xi(Ti)]))]]"
		spec.Ti_To_Ci = "ANDi[A[G((Ti)=>(A[Xi(Ci)]))]]"
		spec.Ci_To_Ni = "ANDi[A[G((Ci)=>(A[Xi(Ni)]))]]"
		spec.safety = "ANDij[A[G(!(Ci & Cj))]]"
		spec.starvationAbsence = "ANDi[A[G((Ti) => (A[F(Ci)]))]]"
		spec.P_In_One_State_Ni = "ANDi[A[G((Ni) <=> (!(Ti | Ci)))]]"	
		spec.P_In_One_State_Ti = "ANDi[A[G((Ti) <=> (!(Ni | Ci)))]]"
		spec.P_In_One_State_Ci = "ANDi[A[G((Ci) <=> (!(Ti | Ni)))]]"
		spec.Atomic_transitions_N = "ANDij[A[G((Ni) => (A[Xj(Ni)]))] & A[G((Nj) => (A[Xi(Nj)]))]]"
		spec.Atomic_transitions_T = "ANDij[A[G((Ti) => (A[Xj(Ti)]))] & A[G((Tj) => (A[Xi(Tj)]))]]"
		spec.Atomic_transitions_C = "ANDij[A[G((Ci) => (A[Xj(Ci)]))] & A[G((Cj) => (A[Xi(Cj)]))]]"
				
	     
		pairProgram("dinningPhilo", "i", "j") {
			x_i_j = i

			pairProcess("i") {
				AP = [Ni,Ti,Ci]
				
				pairStates {
					state("s0", true) {
						Ni = true				
					}
					state("s1") {
						Ti = true
					}
					state("s2") {
						Ci = true
					}
				}
				
				pairArcs {
					arc("s0", "s1") {
						guard {
							Tj  
						}
						action {
							x_i_j= j
						}	
					}
					arc("s0", "s1") {
						guard {
							Nj || Cj  
						}
						action {
						}	
					}
					arc("s1", "s2") {
						guard {
							Nj || (Tj && x_i_j==i)  
						}
						action {
						}	
					}

					arc("s2", "s0") {
						guard {  
						}
						action {
						}	
					}
				}
			}

			pairProcess("j") {
				AP = [Nj,Tj,Cj]
			      
				pairStates {
					state("s0", true) {
						Nj = true
					}
					state("s1") {
						Tj = true
					}
					state("s2") {
						Cj = true
					}
				}

				pairArcs {
					arc("s0", "s1") {
						guard {
							Ti  
						}
						action {
							x_i_j=i
						}	
					}
					arc("s0", "s1") {
						guard {
							Ni || Ci  
						}
						action {
						}	
					}
					arc("s1", "s2") {
						guard {
							Ni || (Ti && x_i_j==j)  
						}
						action {
						}	
					}

					arc("s2", "s0") {
						guard {  
						}
						action {
						}	
					}
				}
			}
		}
	}
}