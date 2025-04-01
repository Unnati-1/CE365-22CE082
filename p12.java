import java.util.*;
import java.util.regex.*;

public class p12 {
    public static String evaluateConstants(String expression) {
        List<String> tokens = tokenize(expression);
        StringBuilder constantExpr = new StringBuilder();
        List<String> newExpr = new ArrayList<>();
        
        for (String token : tokens) {
            if (token.matches("\\d+(\\.\\d+)?")) {
                constantExpr.append(token);
            } else if (token.matches("[-+*/^]") && constantExpr.length() > 0) {
                constantExpr.append(token);
            } else {
                if (constantExpr.length() > 0) {
                    newExpr.add(evaluateExpression(constantExpr.toString()));
                    constantExpr.setLength(0);
                }
                newExpr.add(token);
            }
        }
        
        if (constantExpr.length() > 0) {
            newExpr.add(evaluateExpression(constantExpr.toString()));
        }
        
        return String.join(" ", newExpr);
    }
    
    private static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\b[a-zA-Z_][a-zA-Z_0-9]*\\b|[-+*/^()]|\\d+(\\.\\d+)?").matcher(expression);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }
    
    private static String evaluateExpression(String expr) {
        try {
            return Double.toString(eval(expr));
        } catch (Exception e) {
            return expr;
        }
    }
    
    private static double eval(final String expr) {
        return new Object() {
            int pos = -1, ch;
            
            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }
            
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expr.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                
                double x;
                int startPos = this.pos;
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(startPos, this.pos));
                } else throw new RuntimeException("Unexpected: " + (char)ch);
                
                return x;
            }
        }.parse();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a String: ");
        String expression = scanner.nextLine();
        scanner.close();
        
        String optimizedExpression = evaluateConstants(expression);
        System.out.println("Optimized Expression: " + optimizedExpression);
    }
}
