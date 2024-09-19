iProgram("twoPhaseCommitIProgram") {
	pair {
		I = [[1,3]]

	    varRegex = "_(.)+"
		apRegex = "[a-z]+"

		//mpctl spec declaration
		spec.commitCordinatorAfterSubmitLast = "ANDij[A[((COMMITi) => (A[F(SUBMITj)])) W (SUBMITj)]]"
		spec.decides = "ANDi[A[F(COMMITi | ABORTi)]]"
		
		pairProgram("coordinatorLast", "i", "j") {

			pairProcess("i") {
				AP = [SUBMITi,ABORTi,COMMITi]

				pairStates {
					state("s0", true) {
						SUBMITi = true				
					}
					state("s1") {
						ABORTi = true
					}
					state("s2") {
						COMMITi = true
					}
				}
				
				pairArcs {
					arc("s0", "s1") {
						guard {
							ABORTj  
						}
						action {
						}	
					}
					arc("s0", "s2") {
						guard {
							SUBMITj || COMMITj  
						}
						action {
						}	
					}
					arc("s2", "s2") {
						guard {
							COMMITj || ABORTj  
						}
						action {
						}	
					}

					arc("s1", "s1") {
						guard {
							COMMITj || ABORTj
						}
						action {
						}	
					}
				}
			}

			pairProcess("j") {
				AP = [STARTj,SUBMITj,ABORTj,COMMITj]
			      
				pairStates {
					state("s0", true) {
						STARTj = true
					}
					state("s1") {
						SUBMITj = true
					}
					state("s2") {
						ABORTj = true
					}
					state("s3") {
						COMMITj = true
					}
				}

				pairArcs {
					arc("s0", "s1") {
						guard {
						}
						action {
						}	
					}
					arc("s0", "s2") {
						guard {
						}
						action {
						}	
					}
					arc("s1", "s2") {
						guard {
						}
						action {
						}	
					}
					arc("s1", "s3") {
						guard {  
						}
						action {
						}	
					}
					arc("s2", "s2") {
						guard {  
							ABORTi ||COMMITi
						}
						action {
						}	
					}
					arc("s3", "s3") {
						guard {
							ABORTi ||COMMITi
						}
						action {
						}	
					}
				}
			}
		}
	}

	pair {

		I = [[1,2]]

	    varRegex = "_(.)+"
		apRegex = "[a-z]+"

		//mpctl spec declaration
		spec.coordinatorEventuallyDecides = "ANDi[A[F(COMMITi | ABORTi)]]"
		spec.FirstCommitAfterCoordinatorCommits = "ANDij[A[(COMMITj => A[F(COMMITi)]) W (COMMITi)]]"
		spec.FirstAbortAfterCoordinatorAborts = "ANDij[A[(ABORTi => A[F(ABORTj)]) W (ABORTj)]]"
		spec.firstSafety = "ANDj[A[G(!(COMMITj) | !(ABORTj))] & A[G((COMMITj) => (A[G(COMMITj)]))] & A[G((ABORTj) => (A[G(ABORTj)]))]]"
		spec.firstAbortUnilateraly = "ANDj[STARTj => E[Xj(ABORTj)]]"
		spec.firstSubmitTillCoordinatorCommitsOrAborts = "ANDij[A[G((SUBMITj) => (A[(SUBMITj) U (SUBMITj & (COMMITi | ABORTi))]))]]"	
		
		pairProgram("coordinatorFirst", "i", "j") {

			pairProcess("i") {
				AP = [SUBMITi,ABORTi,COMMITi]

				pairStates {
					state("s0", true) {
						SUBMITi = true				
					}
					state("s1") {
						ABORTi = true
					}
					state("s2") {
						COMMITi = true
					}
				}
				
				pairArcs {
					arc("s0", "s1") {
						guard {
						}
						action {
						}	
					}
					arc("s0", "s2") {
						guard {
						}
						action {
						}	
					}
					arc("s2", "s2") {
						guard {
							COMMITj || ABORTj  
						}
						action {
						}	
					}

					arc("s1", "s1") {
						guard {
							COMMITj || ABORTj
						}
						action {
						}	
					}
				}
			}

			pairProcess("j") {
				AP = [STARTj,SUBMITj,ABORTj,COMMITj]
			      
				pairStates {
					state("s0", true) {
						STARTj = true
					}
					state("s1") {
						SUBMITj = true
					}
					state("s2") {
						ABORTj = true
					}
					state("s3") {
						COMMITj = true
					}
				}

				pairArcs {
					arc("s0", "s1") {
						guard {
							SUBMITi
						}
						action {
						}	
					}
					arc("s0", "s2") {
						guard {
						}
						action {
						}	
					}
					arc("s1", "s2") {
						guard {
							ABORTi
						}
						action {
						}	
					}
					arc("s1", "s3") {
						guard {  
							COMMITi
						}
						action {
						}	
					}
					arc("s2", "s2") {
						guard {  
							ABORTi ||COMMITi
						}
						action {
						}	
					}
					arc("s3", "s3") {
						guard {
							ABORTi ||COMMITi
						}
						action {
						}	
					}
				}
			}
		}
	}

	pair {

		I = [[2,3]]

	    varRegex = "_(.)+"
		apRegex = "[a-z]+"

		//mpctl spec declaration
		spec.SecondSubmitAfterFirstSubmits = "ANDij[A[(SUBMITj => A[F(SUBMITi)]) W (SUBMITi)]]"
		spec.SecondCommitAfterFirstCommits = "ANDij[A[(COMMITj => A[F(COMMITi)]) W (COMMITi)]]"
		spec.secondCommitAfterSubmitAndFirstCommit = "ANDij[A[G((COMMITi & SUBMITj) => (A[F(COMMITj)]))]]"
		spec.secondAbortsThenFirstAborts = "ANDij[A[(ABORTi => A[F(ABORTj)]) W (ABORTj)]]"	
		spec.SecondSafety = "ANDj[A[G(!(COMMITj) | !(ABORTj))] & A[G((COMMITj) => (A[G(COMMITj)]))] & A[G((ABORTj) => (A[G(ABORTj)]))]]"
		spec.secondDecideAfterFirstDecides = "ANDij[(SUBMITj) => A[(SUBMITj) W (SUBMITj & (COMMITi | ABORTi))]]"
		spec.secondAbortUnilateraly = "ANDj[(STARTj) => (E[Xj(ABORTj)])]"
			
		pairProgram("participants", "i", "j") {
			pairProcess("i") {

				AP = [STARTi,SUBMITi,ABORTi,COMMITi]
			      
				pairStates {
					state("s0", true) {
						STARTi = true
					}
					state("s1") {
						SUBMITi = true
					}
					state("s2") {
						ABORTi = true
					}
					state("s3") {
						COMMITi = true
					}
				}

				pairArcs {
					arc("s0", "s1") {
						guard {
						}
						action {
						}	
					}
					arc("s0", "s2") {
						guard {
						}
						action {
						}	
					}
					arc("s1", "s2") {
						guard {
						}
						action {
						}	
					}
					arc("s1", "s3") {
						guard {  
						}
						action {
						}	
					}
					arc("s2", "s2") {
						guard {  
							ABORTj ||COMMITj
						}
						action {
						}	
					}
					arc("s3", "s3") {
						guard {
							ABORTj ||COMMITj
						}
						action {
						}	
					}
				}
			}

			pairProcess("j") {
				AP = [STARTj,SUBMITj,ABORTj,COMMITj]
			      
				pairStates {
					state("s0", true) {
						STARTj = true
					}
					state("s1") {
						SUBMITj = true
					}
					state("s2") {
						ABORTj = true
					}
					state("s3") {
						COMMITj = true
					}
				}

				pairArcs {
					arc("s0", "s1") {
						guard {
							SUBMITi
						}
						action {
						}	
					}
					arc("s0", "s2") {
						guard {
						}
						action {
						}	
					}
					arc("s1", "s2") {
						guard {
							ABORTi
						}
						action {
						}	
					}
					arc("s1", "s3") {
						guard {  
							COMMITi
						}
						action {
						}	
					}
					arc("s2", "s2") {
						guard {  
							ABORTi ||COMMITi
						}
						action {
						}	
					}
					arc("s3", "s3") {
						guard {
							ABORTi ||COMMITi
						}
						action {
						}	
					}
				}
			}
		}
	}
}