import ollama

#OVERVIEW: Requires the model defined in modelUsed to be installed as a local model
#          on the machine that is running this script. Currently prompts for an input
#          from the user regarding what gets sent to the llm.

#currently using codellama, can change this variable to change what model to use
modelUsed = 'codellama'

#Function for sending messages to the ollama model defined by 'modelUsed'
#RETURNS: json response from ollama, specifically the 'message' and 'content' keys
def ollama_chat(message):
    
    #ollama.chat takes in a model (modelUsed) and a set of messages and outputs a response json 
    response = ollama.chat(model=modelUsed, messages=[
        
        #messages currently consists of a single message in json format, as below
        {
            #role tells the llm where this message is coming from (in this case, a user)
            'role': 'user',

            #content is the message to be sent to the llm
            'content': message,
        },
    ])

    #outputs the response as a json
    return response['message']['content']

#Main function, currently prompts for an input that is sent to the ollama model installed locally
def main():

    #Prompt the user
    print(f"Send a message to {modelUsed}:")

    #Takes input
    messageToSend = input()
    
    #Sends input to llm and prints the response
    print(ollama_chat(messageToSend))

# runs main upon startup of the python file
if __name__ == "__main__":
    main()

