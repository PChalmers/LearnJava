'''
Created on Jan 14, 2015

@author: ca0d0340
'''

def main():
    s = 'azcbobobegghakl'
    searchString = 'bob'
    count = 0
    index = 0

    while index < len(s) - len(searchString)+1:
#        print s[index:index + len(searchString)]
        if s[index:index + len(searchString)] == searchString:
            count += 1
        index += 1
    print "Number of times bob occurs is:", count

if __name__ == '__main__':
    main()
    