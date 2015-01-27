'''
Created on Jan 14, 2015

@author: ca0d0340
'''

def main():

    print isWordGuessed(['e', 'i', 'k', 'p', 'r', 's'])

def isWordGuessed(lettersGuessed):
    '''
    secretWord: string, the word the user is guessing
    lettersGuessed: list, what letters have been guessed so far
    returns: boolean, True if all the letters of secretWord are in lettersGuessed;
      False otherwise
    '''
    # FILL IN YOUR CODE HERE...
    import string
    stringToPrint = string.ascii_lowercase
    print stringToPrint
    
    for letter in lettersGuessed:
        stringToPrint = stringToPrint.translate(None, letter)
    return stringToPrint

if __name__ == '__main__':
    main()
    
