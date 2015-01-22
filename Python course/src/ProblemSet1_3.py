'''
Created on Jan 14, 2015

@author: ca0d0340
'''

def main():
    
    s = 'ygcgfislipsdbqggjzkff'
    resultString = ''
    tempString = ''
    previousLetter = ''
    rIndex = 0

    while rIndex < len(s):
        currentchar = s[rIndex]
#        print previousLetter, currentchar, tempString, resultString
        # Check if the current char is greater
        if currentchar >= previousLetter:
            # Current char is greater
            tempString = tempString + currentchar
            # Current char is not greater
            if len(tempString) > len(resultString):
                # Store the New string
                resultString = tempString
        else:
            # Reset the temp string
            tempString = currentchar
        # Always update the previous char
        previousLetter = currentchar
        rIndex += 1

    print "Longest substring in alphabetical order is:", resultString
    

if __name__ == '__main__':
    main()
    