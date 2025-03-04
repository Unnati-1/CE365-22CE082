#include <iostream>
#include <fstream>
#include <cctype>
#include <string>
#include <vector>
#include <unordered_set>
using namespace std;

const unordered_set<string> keywords = {"struct", "int", "char", "float", "return", "if", "else", "while", "for", "do", "void"};
const unordered_set<char> punctuation = {'(', ')', '{', '}', ',', ';'};
unordered_set<char> operators = {'+', '-', '*', '/', '=', '<', '>', '!', '%', '.', '&'};

bool isValidIdentifier(const string &token)
{
    if (!isalpha(token[0]) && token[0] != '_')
        return false;
    for (char ch : token)
    {
        if (!isalnum(ch) && ch != '_')
            return false;
    }
    return true;
}

void tokenize(const string &line, vector<string> &tokens, vector<string> &errors)
{
    string token;
    bool inString = false;

    for (size_t i = 0; i < line.length(); i++)
    {
        char ch = line[i];

        // Handle strings
        if (ch == '\'' || ch == '\"')
        {
            if (inString)
            {
                token += ch;
                tokens.push_back("String: " + token);
                token.clear();
                inString = false;
            }
            else
            {
                if (!token.empty())
                {
                    tokens.push_back(token);
                    token.clear();
                }
                token += ch;
                inString = true;
            }
            continue;
        }

        // Handle comments
        if (ch == '/' && i + 1 < line.length() && (line[i + 1] == '/' || line[i + 1] == '*'))
        {
            break; // Ignore the rest of the line or block comment
        }

        // Handle operators and punctuation
        if (operators.count(ch) || punctuation.count(ch))
        {
            if (!token.empty())
            {
                tokens.push_back(token);
                token.clear();
            }
            tokens.push_back(string(1, ch));
            continue;
        }

        // Handle whitespace
        if (isspace(ch))
        {
            if (!token.empty())
            {
                tokens.push_back(token);
                token.clear();
            }
            continue;
        }

        // Append to token
        token += ch;
    }

    // Final token
    if (!token.empty())
    {
        tokens.push_back(token);
    }
}

// Function to process tokens and categorize them
void processTokens(const vector<string> &tokens, vector<string> &categorizedTokens, vector<string> &errors)
{
    for (const string &token : tokens)
    {
        if (keywords.count(token))
        {
            categorizedTokens.push_back("Keyword: " + token);
        }
        else if (isValidIdentifier(token))
        {
            categorizedTokens.push_back("Identifier: " + token);
        }
        else if (isdigit(token[0]))
        {
            bool valid = true;
            for (char ch : token)
            {
                if (!isdigit(ch))
                {
                    valid = false;
                    break;
                }
            }
            if (valid)
            {
                categorizedTokens.push_back("Constant: " + token);
            }
            else
            {
                errors.push_back(token + " invalid lexeme");
            }
        }
        else if (punctuation.count(token[0]))
        {
            categorizedTokens.push_back("Punctuation: " + token);
        }
        else if (operators.count(token[0]))
        {
            categorizedTokens.push_back("Operator: " + token);
        }
        else
        {
            errors.push_back(token + " invalid lexeme");
        }
    }
}

int main()
{
    ifstream inputFile("hello.c");

    string line;
    vector<string> tokens;
    vector<string> categorizedTokens;
    vector<string> errors;

    while (getline(inputFile, line))
    {
        tokenize(line, tokens, errors);
    }

    processTokens(tokens, categorizedTokens, errors);

    cout << "TOKENS" << endl;
    for (const string &token : categorizedTokens)
    {
        cout << token << endl;
    }

    cout << "\nLEXICAL ERRORS" << endl;
    for (const string &error : errors)
    {
        cout << error << endl;
    }

    inputFile.close();
    return 0;
}