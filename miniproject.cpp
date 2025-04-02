#include <iostream>
#include <sstream>
#include <vector>
#include <regex>
#include <unordered_set>

using namespace std;

// Valid C data types
unordered_set<string> validDataTypes = {"int", "float", "char", "double", "short", "long", "bool", "unsigned", "unsigned int", "unsigned long", "long long", "long double"};

// Function to check if an identifier is valid (matches C variable naming rules)
bool isValidIdentifier(const string& identifier) {
    regex identifierPattern("^[a-zA-Z_][a-zA-Z0-9_]*$");
    return regex_match(identifier, identifierPattern);
}

// Function to check if a given value is valid for a specific datatype
bool isValidValue(const string& value, const string& dataType) {
    if (dataType == "int" || dataType == "short" || dataType == "long" || dataType == "unsigned" || dataType == "unsigned int" || dataType == "unsigned long" || dataType == "long long") {
        return regex_match(value, regex("^-?\\d+$"));  // Integer pattern
    } else if (dataType == "float" || dataType == "double" || dataType == "long double") {
        return regex_match(value, regex("^-?\\d*\\.?\\d+$"));  // Floating-point pattern
    } else if (dataType == "char") {
        return regex_match(value, regex("^'.'$"));  // Single character in quotes
    } else if (dataType == "bool") {
        return value == "true" || value == "false";
    }
    return false;
}

// Function to analyze the syntax of a C variable declaration
bool analyzeSyntax(string statement) {
    // Ensure statement ends with ';'
    if (statement.back() != ';') {
        return false;
    }
    statement.pop_back(); // Remove ';' for proper tokenization
    
    // Manually splitting tokens to handle '=' properly
    vector<string> tokens;
    string token;
    bool assignmentFound = false;
    
    for (char ch : statement) {
        if (ch == ' ' || ch == '=') {
            if (!token.empty()) {
                tokens.push_back(token);
                token.clear();
            }
            if (ch == '=') {
                tokens.push_back("=");
                assignmentFound = true;
            }
        } else {
            token += ch;
        }
    }
    if (!token.empty()) {
        tokens.push_back(token);
    }
    
    if (tokens.empty()) return false;
    
    // Step 1: Check if first token is a valid datatype
    if (validDataTypes.find(tokens[0]) == validDataTypes.end()) {
        return false;
    }
    
    string dataType = tokens[0];
    
    // Step 2: Check if second token is a valid identifier
    if (tokens.size() < 2 || !isValidIdentifier(tokens[1])) {
        return false;
    }
    
    // Step 3: If an assignment exists, validate the assigned value
    if (assignmentFound) {
        if (tokens.size() < 4 || tokens[2] != "=" || !isValidValue(tokens[3], dataType)) {
            return false;
        }
    }
    
    return true;
}

int main() {
    string input;
    cout << "Enter a C variable declaration: ";
    getline(cin, input);
    
    if (analyzeSyntax(input)) {
        cout << "Valid variable declaration!" << endl;
    } else {
        cout << "Syntax Error!" << endl;
    }
    
    return 0;
}