'''
Created on Jan 14, 2015

@author: ca0d0340
'''

def main():
    
    balance = 4842
    annualInterestRate = 0.2
    monthlyPaymentRate = 0.04
    
    month = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
    previousBalance = balance
    totalPaid = 0
    MonthlyInterestRate= annualInterestRate / 12.0
#    print "MonthlyInterestRate =", MonthlyInterestRate

    for currentMonth in month:
        print "Month:", currentMonth
        MinimumMonthlyPayment = monthlyPaymentRate * previousBalance
        totalPaid += MinimumMonthlyPayment
        print "Minimum monthly payment:",  round(MinimumMonthlyPayment, 2)
        Monthlyunpaidbalance = previousBalance - MinimumMonthlyPayment
        monthlyInterest = MonthlyInterestRate * Monthlyunpaidbalance
#        print "monthlyInterest:", monthlyInterest
        UpdatedBalanceEachMonth = Monthlyunpaidbalance + monthlyInterest
        print "Remaining balance:", round(UpdatedBalanceEachMonth, 2)
        previousBalance = UpdatedBalanceEachMonth
    
    print "Total paid:", round(totalPaid, 2)
    print "Remaining balance:", round(UpdatedBalanceEachMonth, 2)

if __name__ == '__main__':
    main()
    