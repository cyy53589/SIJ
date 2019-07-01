/*
 * @(#) RecursiveDescent.java 2019/03/29
 */
package com.compilerExp.component;

import com.compilerExp.SyntaxTree.*;
import com.compilerExp.Token.*;
import com.compilerExp.util.RecursiveDescentException;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * 递归下降方法类
 *
 * @author ChenYuyang
 * @version 1.0
 */
public class RecursiveDescent {
    private static ArrayList<Token> tokens;
    private static int currTokens = 0;
    private static Token lastMatchToken;

    /**
     * 运行梯度下降方法
     *
     * @param tokensToAnalyse token流
     * @return 返回树根
     * @throws RecursiveDescentException
     */
    public static Tree run(ArrayList<Token> tokensToAnalyse) throws RecursiveDescentException {
        tokens = tokensToAnalyse;
        currTokens = 0;
        ArrayList<Tree> treeArrayList = new ArrayList<>();
        while (!(getToken() instanceof EndToken)) {
            Tree toInsert = Statement();
            treeArrayList.add(toInsert);
        }
        return new StatementsTree(treeArrayList);
    }

    static Tree Statement() throws RecursiveDescentException {
        Token currToken = getToken();
        String tokenValue = getToken().getValue();
        if (tokenValue.contentEquals("if")) {
            return IfStatement();
        } else if (tokenValue.contentEquals("while")) {
            return WhileStatement();
        } else if (tokenValue.contentEquals("for")) {
            return ForStatement();
        } else if (tokenValue.contentEquals("return")) {
            return ReturnStatement();
        } else if (currToken instanceof ParanToken && tokenValue.equals("[")) {
            return CreateArray();
        } else {
            ExpressionTree forRet = Expression();
            if (!match(SplitOpToken.class, ";")) {
                throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处需要分隔符;");
            }
            return forRet;
        }
        // throw new RecursiveDescentException(getToken().getLineNumber(),
        // getToken().getRowNumber(), "无法识别该语句:非法开头");
    }

    static ArrayCreatementTree CreateArray() throws RecursiveDescentException {
        match(ParanToken.class, "[");
        ExpressionTree arraySize = Expression();
        if (!match(ParanToken.class, "]")) {
            throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处需要符号\"]\"");
        }
        if (!match(LogOpToken.class, ">")) {
            throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处需要符号\">\"");
        }
        IdentifierTree arrayName = Identifier();
        if (!match(SplitOpToken.class)) {
            throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处需要分隔符\";\"");
        }
        return new ArrayCreatementTree(arrayName, arraySize);
    }

    /**
     * 控制块，就是 "(param1;param2;...;paramn){statement1;...}"
     *
     * @throws RecursiveDescentException
     */
    static Pair<ArrayList<ExpressionTree>, StatementsTree> ControlBlock(int paramsNum)
            throws RecursiveDescentException {
        ArrayList<ExpressionTree> expressionTrees = new ArrayList<>();
        ArrayList<Tree> trees = new ArrayList<>();
        if (!match(ParanToken.class, "(")) {
            throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处需要符号\"(\"");
        }
        expressionTrees.add(Expression());
        for (int i = 1; i < paramsNum; ++i) {
            if (!match(SplitOpToken.class, ";")) {
                throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(),
                        "此处需要符号\";\"");
            }
            expressionTrees.add(Expression());
        }
        if (!match(ParanToken.class, ")")) {
            throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处需要符号\")\"");
        }
        if (!match(ParanToken.class, "{")) {
            throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处需要符号\"{\"");
        }
        while (!match(ParanToken.class, "}") && !EndToken.class.isInstance(getToken())) {
            trees.add(Statement());
        }
        if (!(lastMatchToken instanceof ParanToken && lastMatchToken.getValue().equals("}"))) {
            throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处需要符号\"{\"");
        }
        return new Pair<ArrayList<ExpressionTree>, StatementsTree>(expressionTrees, new StatementsTree(trees));
    }

    static IfStatementTree IfStatement() throws RecursiveDescentException {
        match(IdentifierToken.class, "if");
        Pair<ArrayList<ExpressionTree>, StatementsTree> controlBlock = ControlBlock(1);

        if (match(IdentifierToken.class, "else")) {
            if (!match(ParanToken.class, "{")) {
                throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "需要符号\"{\"");
            }
            ArrayList<Tree> trees = new ArrayList<>();
            while (!match(ParanToken.class, "}") && !(getToken() instanceof EndToken)) {
                trees.add(Statement());
            }
            if (lastMatchToken instanceof EndToken) {
                throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "需要符号\"}\"");
            }
            return new IfStatementTree(controlBlock.getKey().get(0), controlBlock.getValue(),
                    new StatementsTree(trees));
        }
        return new IfStatementTree(controlBlock.getKey().get(0), controlBlock.getValue(), null);
    }

    static WhileStatementTree WhileStatement() throws RecursiveDescentException {
        match(IdentifierToken.class, "while");
        Pair<ArrayList<ExpressionTree>, StatementsTree> controlBlock = ControlBlock(1);
        return new WhileStatementTree(controlBlock.getKey().get(0), controlBlock.getValue());
    }

    static ForStatementTree ForStatement() throws RecursiveDescentException {
        match(IdentifierToken.class, "for");
        Pair<ArrayList<ExpressionTree>, StatementsTree> controlBlock = ControlBlock(3);
        ExpressionTree[] conditions = new ExpressionTree[3];
        for (int i = 0; i < 3; ++i)
            conditions[i] = controlBlock.getKey().get(i);
        return new ForStatementTree(conditions, controlBlock.getValue());
    }

    static ReturnTree ReturnStatement() throws RecursiveDescentException {
        Token returnToken = getToken();
        match(IdentifierToken.class, "return");
        ExpressionTree expressionTree = Expression();
        if (!match(SplitOpToken.class, ";")) {
            throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处需要符号\";\"");
        }
        return new ReturnTree((IdentifierToken) returnToken, expressionTree);
    }

    static ExpressionTree Expression() throws RecursiveDescentException {
        if ((LogOpToken.class.isInstance(getToken()) && getToken().getValue().contentEquals("!")
                || IntegerToken.class.isInstance(getToken())
                || (ParanToken.class.isInstance(getToken()) && getToken().getValue().contentEquals("(")
                        || (getToken() instanceof IdentifierToken)))) {
            return Assignment();
        }
        throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(), "此处应该出现表达式");
    }

    static ExpressionTree Assignment() throws RecursiveDescentException {
        ExpressionTree left = LogicComparsion();
        Token opToken = getToken();
        if (opToken instanceof AssignOpToken) {
            ExpressionTree right = AssignmentPlus();
            return new BiTokenOperatorTree(opToken, left, right);
        } else {
            return left;
        }
    }

    static ExpressionTree AssignmentPlus() throws RecursiveDescentException {
        match(AssignOpToken.class);
        ExpressionTree left = Expression();
        Token opToken = getToken();
        if (opToken instanceof AssignOpToken) {
            ExpressionTree right = AssignmentPlus();
            return new BiTokenOperatorTree(opToken, left, right);
        } else {
            return left;
        }
    }

    static ExpressionTree LogicComparsion() throws RecursiveDescentException {
        ExpressionTree left = Addition();
        Token opToken = getToken();
        if (opToken instanceof LogOpToken) {
            ExpressionTree right = LogicComparsionPlus();
            return new BiTokenOperatorTree(opToken, left, right);
        } else {
            return left;
        }
    }

    static ExpressionTree LogicComparsionPlus() throws RecursiveDescentException {
        match(LogOpToken.class);
        ExpressionTree left = Expression();
        Token opToken = getToken();
        if (opToken instanceof LogOpToken) {
            ExpressionTree right = LogicComparsionPlus();
            return new BiTokenOperatorTree(opToken, left, right);
        }
        return left;
    }

    static ExpressionTree Addition() throws RecursiveDescentException {
        ExpressionTree left = Mutiplication();

        Token opToken = getToken();
        while (opToken instanceof ArOpToken && (opToken.getValue().equals("+") || opToken.getValue().equals("-"))) {
            match(ArOpToken.class, opToken.getValue());
            ExpressionTree right = Mutiplication();
            left = new BiTokenOperatorTree(opToken, left, right);
            opToken = getToken();
        }
        return left;
    }

    static ExpressionTree AdditionPlus() throws RecursiveDescentException {
        match(ArOpToken.class, getToken().getValue());
        ExpressionTree left = Expression();
        Token opToken = getToken();
        if (opToken instanceof ArOpToken && (opToken.getValue().equals("+") || opToken.getValue().equals("-"))) {
            ExpressionTree right = AdditionPlus();
            return new BiTokenOperatorTree(opToken, left, right);
        }
        return left;
    }

    static ExpressionTree Mutiplication() throws RecursiveDescentException {
        ExpressionTree left = TokenUnit();
        Token opToken = getToken();
        while (opToken instanceof ArOpToken && (opToken.getValue().equals("*") || opToken.getValue().equals("/"))) {
            match(ArOpToken.class, opToken.getValue());
            ExpressionTree right = TokenUnit();
            left = new BiTokenOperatorTree(opToken, left, right);
            opToken = getToken();
        }
        return left;
    }

    static ExpressionTree MutiplicationPlus() throws RecursiveDescentException {
        match(ArOpToken.class, getToken().getValue());
        ExpressionTree left = Expression();
        Token opToken = getToken();
        if (opToken instanceof ArOpToken && (opToken.getValue().equals("*") || opToken.getValue().equals("/"))) {
            ExpressionTree right = MutiplicationPlus();
            return new BiTokenOperatorTree(opToken, left, right);
        }
        return left;
    }

    static ExpressionTree TokenUnit() throws RecursiveDescentException {
        Token token = getToken();
        if (token.getValue().contentEquals("-")) {
            match(ArOpToken.class, "-");
            token = getToken();
            if (!match(NumberToken.class)) {
                throw new RecursiveDescentException(token.getLineNumber(), token.getRowNumber(), "此处应该出现合法数值");
            }
            return new ConstantTree((IntegerToken) lastMatchToken, true);
        } else if (token.getValue().contentEquals("(")) {
            match(ParanToken.class, "(");
            ExpressionTree forRet = Expression();
            if (!match(ParanToken.class, ")")) {
                throw new RecursiveDescentException(token.getLineNumber(), token.getRowNumber(), "此处应该有右括号')'");
            }
            return forRet;
        } else if (token.getValue().contentEquals("!")) {
            Token opToken = getToken();
            match(LogOpToken.class, "!");
            return new UniTokenOperatorTree(opToken, Expression());
        } else {
            if (match(NumberToken.class)) {
                return new ConstantTree((IntegerToken) lastMatchToken);
            } else if (IdentifierToken.class.isInstance(token)) { // 如果遇到了标识符
                return Identifier();
            } else {
                throw new RecursiveDescentException(token.getLineNumber(), token.getRowNumber(), "非法符号");
            }
        }
    }

    static IdentifierTree Identifier() throws RecursiveDescentException {
        Token identifier = getToken();
        if (match(IdentifierToken.class)) {
            if (match(ParanToken.class, "[")) {
                ExpressionTree index = Expression();
                if (!match(ParanToken.class, "]")) {
                    throw new RecursiveDescentException(getToken().getLineNumber(), getToken().getRowNumber(),
                            "此处应该出现\"]\"");
                }
                return new IdentifierTree((IdentifierToken) identifier, index);
            } else {
                return new IdentifierTree((IdentifierToken) identifier);
            }
        } else {
            throw new RecursiveDescentException(identifier.getLineNumber(), identifier.getRowNumber(), "需要标识符");
        }
    }

    static boolean match(Class<?> tokenClass) {
        if (tokenClass.isInstance(getToken())) {
            lastMatchToken = getToken();
            currTokens++;
            return true;
        }
        return false;
    }

    static boolean match(Class<?> tokenClass, String value) {
        return getToken().getValue().contentEquals(value) && match(tokenClass);
    }

    static Token getToken() {
        Token last = tokens.get(tokens.size() - 1);
        if (currTokens >= tokens.size())
            return new EndToken("EOF", last.getLineNumber(), last.getRowNumber());
        return tokens.get(currTokens);
    }
}
