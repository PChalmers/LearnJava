'''
Created on Jan 14, 2015

@author: ca0d0340
'''

def main():

    balance = 3926
    annualInterestRate = 0.2

    monthlyPayment = 10    
    MonthlyInterestRate= annualInterestRate / 12.0
#    print "MonthlyInterestRate =", MonthlyInterestRate

    while True:
        currentBalance = balance
        
        for index in range(1, 13, 1):
            currentBalance -= monthlyPayment
            currentBalance = currentBalance + currentBalance * MonthlyInterestRate
#            print monthlyPayment, currentBalance
            
        if currentBalance <= 0:
            break

        monthlyPayment += 10
        
    print "Lowest Payment:", monthlyPayment

if __name__ == '__main__':
    main()
    