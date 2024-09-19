automaton("P", "i", "j", "k", "l", "m") {
	//j -> P I want to talk to
	//k -> P I want to recieve from
	//l -> C I want to talk to
	//m -> C I want to recieve from
	states {
		prev_state = null
		state = 'START'
		phase = 1
		sendSubmit = false
		sendAckSubmit = false
		sendAbort = false 
		sendAckAbort = false
		sendCommit = false
		sendAckCommit = false
		sendAbortCommit = false
		sendAckAbortCommit = false
	}
	actions {
		
		//output to nxt process in line
		internal("p_submit_i") {
			pre {
				prev_state == "SUBMIT" && state =='START' && phase == 1
			}
			effect { state = "SUBMIT" 
						sendSubmit = true
						sendAckSubmit = true 
				 }
		}
		
		output("p_submit_i_j") {
			pre {sendSubmit == true }
			effect {
				sendSubmit = false
			}
		}
		internal("p_abort_i") {
			pre {
				state == 'START' && phase == 1
			}
			effect {  state = "ABORT"
					   sendAbort = true
					   sendAckAbort = true
				  }
		}
		output("p_abort_i_j") {
			pre {sendAbort == true}
			effect {
				sendAbort = false
			}
		}
		
		internal("p_commit_i")  {
			pre {
				prev_state=="COMMIT" && phase == 2 && state  !="COMMIT"
		   }
		   effect { state = "COMMIT"
			   		sendCommit = true
					sendAckCommit = true
			    }
		}
		
		output("p_commit_i_j") {
			pre {
				sendCommit == true	
			}
			effect { 
			}
		}
	
		
		internal("p_abort_commit_i") {
			pre {
				 prev_state=="ABORTCOMMIT" && phase == 2 & state !="ABORTCOMMIT" 
			}
			effect { state = "ABORTCOMMIT"
					sendAbortCommit = true
					sendAckAbortCommit = true
				 }
		}
		
		output("p_abort_commit_i_j") {
			pre {
				 sendAbortCommit ==true
			}
			effect { 
			}
		}
		
		//recieve from P_k
		input("p_submit_k_i") {
			effect {
				prev_state = "SUBMIT"
				phase = 1
			}
		}
		
		input("p_abort_k_i") {
			effect {
				prev_state = "ABORT"
				phase = 1
			}
		}
		
		input("p_commit_k_i") {
			effect {
				prev_state = "COMMIT"
				phase = 2
			}
		}
		
		input("p_abort_commit_k_i") {
			effect {
				prev_state = "ABORTCOMMIT"
				phase = 2
			}
		}
		
		//outout to C_l
		output("c_ack_submit_i_l") {
			pre {
				sendAckSubmit == true
			}
			effect { sendAckSubmit = false }
		}
		
		output("c_ack_abort_i_l") {
			pre {
				sendAckAbort == true
			}
			effect {sendAckAbort = false }
		}
		
		output("c_ack_commit_i_l") {
			pre {
				 sendAckCommit == true
			}
			effect { 
			}
		}
		
		output("c_ack_abort_commit_i_l") {
			pre {
				sendAckAbortCommit == true
			}
			effect {
			}
		}
		
		//recieve from C _M
		
		//inputs from the connecttor
		input("c_submit_m_i") {
			effect {
				prev_state = "SUBMIT"
				phase = 1
			}
		}

		input("c_commit_m_i") {
			effect {
				prev_state = "COMMIT"
				phase = 2
			}
		}
		
		
		input("c_abort_commit_m_i") {
			effect {
				prev_state = "ABORTCOMMIT"
				phase = 2
			}
		}
		
	}
	tasks {
		task("P_Internal_i_", "W") {
			action("p_submit_i");
			action("p_abort_i");
			action("p_commit_i");
			action("p_abort_commit_i");
		}
		task("P_channel_i_j_", "W") {
			action("p_submit_i_j");
			action("p_abort_i_j");
			action("p_commit_i_j");
			action("p_abort_commit_i_j");
		}
		task("P_channel_i_l_", "W") {
			action("c_ack_submit_i_l");
			action("c_ack_submit_i_l");
			action("c_ack_abort_i_l");
			action("c_ack_commit_i_l");
			action("c_ack_abort_commit_i_l");
		}
	}
}


automaton("C", "i" , "j", "k") {
	states {
		phase = 1
		state = 'START'
		prev_state = null
		sendSubmit = false
		sendCommit = false
		sendAbortCommit = false
	}
	actions {
		
		//output to first process
		internal("c_submit_i") {
			pre { phase == 1 && state == 'START' }
			effect {  state = "SUBMIT" 
					sendSubmit = true
				}
		}
		
		output("c_submit_i_j") {
			pre { sendSubmit == true }
			effect {  sendSubmit = false }
		}
		
		internal("c_commit_i") {
			pre { prev_state == "SUBMIT" && phase == 2  && state != "COMMIT" }
			effect { state = "COMMIT" 
					sendCommit = true
				}
		}
		
		
		output("c_commit_i_j") {
			pre { sendCommit == true}
			effect { 
				}
		}
		
		internal("c_abort_commit_i") {
			pre {  prev_state == "ABORT" && phase == 2 && state != "ABORTCOMMIT" }
			effect { 
				state = "ABORTCOMMIT"
				sendAbortCommit = true
				 }
		}
		
		output("c_abort_commit_i_j") {
			pre { sendAbortCommit == true }
			effect { 
			}
		}
		
		
		input("c_ack_submit_k_i") {
			effect {
				prev_state = "SUBMIT"
				phase = 2
			}
		}
		
		input("c_ack_abort_k_i") {
			effect {
				prev_state = "ABORT"
				phase = 2
			}
		}
		
		input("c_ack_commit_k_i") {
			effect {
			}
		}
		
		input("c_ack_abort_commit_k_i") {
			effect {
			}
		}
	}
	tasks {
		task("C_Internal_i_", "W") {
			action("c_submit_i");
			action("c_commit_i");
			action("c_abort_commit_i");
		}
		task("C_channel_i_j_", "W") {
			action("c_submit_i_j");
			action("c_commit_i_j");
			action("c_abort_commit_i_j");
		}
	}
}