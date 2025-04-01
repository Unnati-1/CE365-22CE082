#include <iostream>
#include <string>

using namespace std;

string str;
int idx = 0;
bool flag = true;

void S();
void L();
void LPrime();

void S() {
    if (idx >= str.length()) {
        flag = false;
        return;
    }
    
    if (str[idx] == 'a') {
        idx++;
        return;
    }

    if (str[idx] == '(') {
        idx++;
        L();
        if (idx < str.length() && str[idx] == ')') {
            idx++;
        } else {
            flag = false;
        }
        return;
    }

    flag = false;
}

void L() {
    S();
    if (flag) {
        LPrime();
    }
}

void LPrime() {
    if (idx < str.length() && str[idx] == ',') {
        idx++;
        S();
        if (flag) {
            LPrime();
        }
    }
}

int main() {
    cout << "Enter string: ";
    cin >> str;

    S();
    
    if (flag && idx == str.length()) {
        cout << "Valid string" << endl;
    } else {
        cout << "Invalid string" << endl;
    }

    return 0;
}
