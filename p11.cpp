#include <iostream>
#include <vector>
#include <string>
#include <sstream>

using namespace std;

struct Quadruple 
{
    string op;
    string arg1;
    string arg2;
    string result;
};

// Function to remove spaces
string removeSpaces(const string &str) {
    string result = "";
    for (char ch : str) {
        if (!isspace(ch))
            result += ch;
    }
    return result;
}


string firstPass(string expr, vector<Quadruple> &quadruples, int &tempCount) {
    string newExpr = "";
    string current = ""; 
    for (int i = 0; i < expr.length(); i++) {
        if (isdigit(expr[i])) {
            current = expr[i]; 
        } else if (expr[i] == '*' || expr[i] == '/') {
            i++;
            while (i < expr.length() && isspace(expr[i])) i++; 
            string nextDigit = "";
            if (isdigit(expr[i])) {
                nextDigit = expr[i];
            }
           
            stringstream ss;
            ss << "t" << tempCount++;
            string temp = ss.str();
        
            quadruples.push_back({string(1, expr[i - 1]), current, nextDigit, temp});

            current = temp;
        } else if (expr[i] == '+' || expr[i] == '-') {
            newExpr += current;
            newExpr += expr[i];
            current = "";
        }
    }
    
    if (!current.empty()) {
        newExpr += current;
    }
    return newExpr;
}

// Second pass: handle + and -
void secondPass(string expr, vector<Quadruple> &quadruples, int &tempCount) {
    string left = "";
    for (int i = 0; i < expr.length(); i++) {
        if (isdigit(expr[i]) || expr[i] == 't') {
            left += expr[i];
        } else if (expr[i] == '+' || expr[i] == '-') {
            string op(1, expr[i]);
            string right = "";

            // Get next operand
            i++;
            while (i < expr.length() && (isdigit(expr[i]) || expr[i] == 't')) {
                right += expr[i];
                i++;
            }
            i--; // Adjust index

            // Create temporary variable
            stringstream ss;
            ss << "t" << tempCount++;
            string temp = ss.str();

            // Add quadruple
            quadruples.push_back({op, left, right, temp});

            // Update left to temp
            left = temp;
        }
    }
}

int main() {
    string expression;
    cout << "Enter an arithmetic expression (integers only): ";
    getline(cin, expression);

    expression = removeSpaces(expression);

    vector<Quadruple> quadruples;
    int tempCount = 1;

    // First pass: * and /
    string afterFirstPass = firstPass(expression, quadruples, tempCount);

    // Second pass: + and -
    secondPass(afterFirstPass, quadruples, tempCount);

    // Output Quadruple Table
    cout << "\nQuadruple Table:\n";
    cout << "Op\tArg1\tArg2\tResult\n";
    for (auto q : quadruples) {
        cout << q.op << "\t" << q.arg1 << "\t" << q.arg2 << "\t" << q.result << endl;
    }

    return 0;
}