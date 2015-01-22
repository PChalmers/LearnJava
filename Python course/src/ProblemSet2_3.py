'''
Created on Jan 14, 2015

@author: ca0d0340
'''

def main():

    Balance = 999999
    annualInterestRate = 0.18

    MonthlyInterestRate= annualInterestRate / 12.0
    
    MonthlyPaymentLowerBound = Balance / 12
    currentLowerBound = MonthlyPaymentLowerBound
    
    MonthlyPaymentUpperBound = (Balance * (1 + MonthlyInterestRate)**12) / 12.0
    currentUpperBound = MonthlyPaymentUpperBound
    
    currentGuess = MonthlyPaymentLowerBound + (MonthlyPaymentUpperBound - MonthlyPaymentLowerBound) / 2
    tryNumber = 0
#    print "MonthlyInterestRate =", MonthlyInterestRate

    while True:
        currentBalance = Balance
#         print "\nThis is try number:", tryNumber
#         print "currentGuess:", round(currentGuess), "currentLowerBound:", currentLowerBound, "currentUpperBound:", currentUpperBound
        tryNumber += 1
        if(tryNumber > 100): 
            break
        
        for index in range(1, 13, 1):
            currentBalance -= currentGuess
            currentBalance = currentBalance + currentBalance * MonthlyInterestRate
#            print "currentGuess:", currentGuess, "currentBalance:", currentBalance
            
        if round(currentBalance) == 0:
            break
        
        elif round(currentBalance) < 0:
            # Current guess needs to increase
            currentUpperBound = currentGuess
            currentGuess = currentLowerBound + (currentUpperBound - currentLowerBound) / 2
            
#             print round(currentBalance), " is Greater than 0 - new currentGuess:", currentGuess
            
        elif round(currentBalance) > 0:
            currentLowerBound = currentGuess
            currentGuess = currentLowerBound + (currentUpperBound - currentLowerBound) / 2
            
#             print round(currentBalance), " is Less than 0 - new currentGuess:", currentGuess
        
    print "Lowest Payment:", round(currentGuess, 2)

if __name__ == '__main__':
    main()
    
    
    