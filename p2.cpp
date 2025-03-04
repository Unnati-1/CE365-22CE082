#include <iostream>
#include <vector>
#include <string>

using namespace std;

int main() {
    bool exit = false;

    cout << "Number of input symbols : ";
    int i;
    cin >> i;
    cin.ignore();
    vector<string> input(i + 1);
    cout << "Input symbols : ";
    for (int j = 0; j < i; j++) {
        getline(cin, input[j]);
    }

    cout << "Enter number of states : ";
    int s;
    cin >> s;
    cout << "Initial state : ";
    int initial;
    cin >> initial;

    cout << "Number of accepting states : ";
    int nAccepting;
    cin >> nAccepting;
    vector<int> accepting(nAccepting);
    cout << "Accepting states : ";
    for (int j = 0; j < nAccepting; j++) {
        cin >> accepting[j];
    }
    cout << "Dead state : ";
    int dead;
    cin >> dead;

    vector<vector<int>> transition(s, vector<int>(i));

    for (int state = 0; state < s; state++) {
        for (int in = 0; in < i; in++) {
            cout << (state + 1) << " to " << (in == 0 ? "a" : "b") << " -> ";
            int nextState;
            cin >> nextState;
            transition[state][in] = nextState;
        }
    }

    cin.ignore();
    while (!exit) {
        cout << "Enter Input String : ";
        string str;
        getline(cin, str);

        if (str == "exit") {
            exit = true;
        } else {
            int curr = initial;
            if (str.empty()) {
                for (int j : accepting) {
                    if (j == curr) {
                        cout << "Valid String" << endl;
                        break;
                    } else {
                        cout << "Invalid String" << endl;
                    }
                }
            }
            for (char ch : str) {
                if (ch != '0' && ch != '1') {
                    curr = dead;
                } else {
                    int from = curr - 1, to = ch - '0';
                    curr = transition[from][to];
                }
            }

            bool valid = false;
            for (int j : accepting) {
                if (j == curr) {
                    valid = true;
                    cout << "Valid String" << endl;
                    break;
                }
            }
            if (!valid) {
                cout << "Invalid String" << endl;
            }
        }
    }
    return 0;
}

