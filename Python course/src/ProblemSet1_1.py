'''
Created on Jan 14, 2015

@author: ca0d0340
'''

def main():
    s = 'aei34kja*(&%#$&\\sdfuiasryopwejrbn u'
    count = 0
    vowels = ['a', 'e', 'i', 'o', 'u']

    for num in vowels:
        count += s.count(num)
    print count

if __name__ == '__main__':
    main()
    
