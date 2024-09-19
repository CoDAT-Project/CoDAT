from collections import deque
import sys
import math

status = ['T', 'L', 'R', 'LR', 'E', 'ER', 'EL']

processCount = int(sys.argv[1])

states = []
edges = []

def addState(state):
    states.append(str(state))
    
def addEdge(stateFrom, stateTo):
    sFrom = stateFrom.name()
    sTo = stateTo.name()
    
    edges.append(sFrom + ":" + sTo + ";")

class State:
    def __init__(self, start=False, ps=[status[0] for i in range(processCount)]):
        self.ps = list(ps) # Copy
        self.start = start

    def getLegalSucessors(self):
        sucessors = []
        
        for i in range(processCount):
            next = list(self.ps)
            char = next[i]
            
            if char == 'T':
                next[i] = 'L'
                sucessors.append(State(False, next))
                
                next[i] = 'R'
                sucessors.append(State(False, next))
                
            if char == 'L':
                next[i] = 'LR'
                sucessors.append(State(False, next))
                
                next[i] = 'EL'
                sucessors.append(State(False, next))
                
            if char == 'R':
                next[i] = 'ER'
                sucessors.append(State(False, next))
                
            if char == 'E' or char == 'ER' or char == 'EL':
                next[i] = 'T'
                sucessors.append(State(False, next))
                
            if char == 'LR':
                next[i] = 'E'
                sucessors.append(State(False, next))
                
        return sucessors
        
    def __eq__(self, other):
        for i in range(processCount):
            if not self.ps[i] == other.ps[i]:
                return False
                
        return True 
    
    def __str__(self):
        tmp = []
        
        for i in range(processCount):
            tmp.append(self.ps[i]+str(i+1))
            
        name = "".join(tmp)
        labels = ",".join(tmp)
        
        result = name+":"+labels

        if self.start:
            result = result + ':true'

        return result + ";"

    def name(self):
        tmp = []
        
        for i in range(processCount):
            tmp.append(self.ps[i]+str(i+1))
            
        return "".join(tmp)
            
if __name__ == '__main__':
    current = State(True)
    
    q = deque([ current ])
    visited = list()
    visited.append(current)
    
    while len(q) > 0:
        current = q.popleft()
        addState(current)
        
        for next in current.getLegalSucessors():
            isVisited = False   
            for v in visited:
                if v.name() == next.name():
                    isVisited = True
                    break
                    
            if not isVisited:
                q.append(next)
                visited.append(next)
                
            addEdge(current, next)
                
    result = "drinking\n"
    result += "States:\n"
    result += "\n".join(set(states)) + "\n" 
    result += "Transitions:\n"
    result += "\n".join(set(edges))
    
    filename = "Udrinking"+str(processCount)+".txt"
    f = open(filename, 'w')
    f.write(result)
    f.close()
