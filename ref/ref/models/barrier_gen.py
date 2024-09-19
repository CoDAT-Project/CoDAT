from Queue import Queue
import sys
import math

status = ['S', 'E']
barrier = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

barrierCount = int(sys.argv[1])

names = []
states = []
edges = []

def Edge(stateFrom, stateTo):   
    edges.append(str(stateFrom) + ":" + str(stateTo) + ";")

class State:
    def __init__(self, ls='S', lb='A', rs='S', rb='A', start='false'):
        self.lstatus = ls
        self.lbarrier = lb
        
        self.rstatus = rs
        self.rbarrier = rb
        
        if not str(self) in names:
            tmp = str(self)+":"+self.labels().upper()
            if start == 'true':
                tmp = tmp +":true"

            tmp = tmp + ";"
            states.append(tmp)
            names.append(str(self))
        
    def legalLeft(self):
        return True

        flag = self.lstatus == 'S' and self.rstatus == 'S'
        if flag:
            return self.lbarrier == self.rbarrier or barrier.index(self.lbarrier) < barrier.index(self.rbarrier) or (barrier.index(self.lbarrier) + 1) % barrierCount == barrier.index(self.rbarrier)
               
        flag = flag or self.lbarrier == self.rbarrier
        
        dif = barrier.index(self.lbarrier) - barrier.index(self.rbarrier)
        
        flag = flag or abs(dif) <= 1 or dif == barrierCount-1
        
        return flag
        
    def legalRight(self):
        return True

        flag = self.lstatus == 'S' and self.rstatus == 'S'
        if flag:
            return self.lbarrier == self.rbarrier or  barrier.index(self.rbarrier) < barrier.index(self.lbarrier) or (barrier.index(self.rbarrier) + 1) % barrierCount == barrier.index(self.lbarrier)
        
        flag = flag or self.rbarrier == self.lbarrier
        
        dif = barrier.index(self.rbarrier) - barrier.index(self.lbarrier)
        
        flag = flag or abs(dif) <= 1 or dif == barrierCount-1
        
        return flag
        
    def applyLeft(self):
        if self.lstatus == 'S':
            next = State('E', self.lbarrier, self.rstatus, self.rbarrier)
            return next
        else:
            i = (barrier.index(self.lbarrier) + 1) % barrierCount
            next = State('S', barrier[i], self.rstatus, self.rbarrier)
            return next
            
    def applyRight(self):
        if self.rstatus == 'S':
            next = State(self.lstatus, self.lbarrier, 'E', self.rbarrier)
            return next
        
        else:
            i = (barrier.index(self.rbarrier) + 1) % barrierCount
            next = State(self.lstatus, self.lbarrier, 'S', barrier[i])              
            return next
            
    def labels(self):
        return self.lstatus + self.lbarrier + "1," + self.rstatus + self.rbarrier + "2"        
        
    def __str__(self):
        return self.lstatus + self.lbarrier + "1" + self.rstatus + self.rbarrier + "2"
        
    def __eq__(self, other):
        return (isinstance(other, self.__class__)
            and str(self).lower() == str(other).lower())

    def __ne__(self, other):
        return not self.__eq__(other)
        
    def isStart(self):
        return self.lbarrier == barrier[0] and self.rbarrier == barrier[0] and self.lstatus == 'S' and self.rstatus == 'S'
            
if __name__ == '__main__':
    current = State(start='true')
    
    q = Queue()
    visited = list()
    visited.append(current)    
    
    q.put(current)
    while not q.empty():
        current = q.get()

        
        if current.legalLeft():
            next = current.applyLeft()

            vis = False
            for v in visited:
                if v.__str__() == next.__str__():
                    vis = True
            if not vis:
                q.put(next)
                visited.append(next)
                Edge(current, next)
            
            Edge(current, next)
                
        
        if current.legalRight():
            next = current.applyRight()
            vis = False
            for v in visited:
                if v == next:
                    vis = True

            if not vis:
                q.put(next)
            
            Edge(current, next)
    
    result = "barrier"+str(barrierCount)+"\n"
    result += "States:\n"
    result += "\n".join(set(states)) + "\n" 
    result += "Transitions:\n"
    result += "\n".join(set(edges))
        
    filename = "Ubarrier"+str(barrierCount)+".txt"
    f = open(filename, 'w')
    f.write(result)
    f.close()
