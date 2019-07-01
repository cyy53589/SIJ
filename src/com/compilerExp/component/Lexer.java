/*
 * @(#) Lexer.java 2019/03/29
 */
package com.compilerExp.component;

import com.compilerExp.Token.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 词法分析器
 *
 * @author ChenYuyang
 * @version 1.0
 */
public class Lexer {
    RegWithLabel[] rules;

    /**
     * 初始化Lexer
     *
     * @param rules 规则，规则由RegWithLabel定义详细看RegWithLabel
     */
    public Lexer(RegWithLabel[] rules) {
        assert rules != null;
        this.rules = new RegWithLabel[rules.length];
        for (int i = 0; i < rules.length; ++i) {
            this.rules[i] = rules[i];
        }
    }

    /**
     * 分析content内容
     *
     * @param line 需要被分析的一行内容
     * @param lineNumber 行数
     * @return 返回Token流
     * @throws Exception 其他IO错误
     */
    public ArrayList<Token> analyse(String line, int lineNumber) throws Exception {
        ArrayList<Token> forRet = new ArrayList<>();
        int indexBased = 0;
        boolean hasRegMatch = true;
        while (hasRegMatch && line.length() > 0) {
            hasRegMatch = false;
            int minIndex = line.length();
            int minIndexEnd = line.length();
            Class<?> label = null;
            for (int i = 0; i < rules.length; ++i) {
                Matcher m = rules[i].getPattern().matcher(line);
                if (m.find()) {
                    hasRegMatch = true;
                    if (minIndex > m.start(1)) {
                        minIndex = m.start(1);
                        minIndexEnd = m.end(1);
                        label = rules[i].label;
                    } else if (minIndex == m.start((1)) && minIndexEnd < m.end(1)) {
                        minIndexEnd = m.end(1);
                        label = rules[i].label;
                    }
                }
            }
            if (hasRegMatch) {
                String ctn = line.substring(minIndex, minIndexEnd);
                //System.out.printf("Value=\"%s\", TokenType=%s\n",ctn,label.getName());
                forRet.add((Token) (label.getConstructors()[0].newInstance(ctn, lineNumber, indexBased + minIndex)));
                line = line.substring(minIndexEnd);
                indexBased += minIndexEnd;
            }
        }
        return forRet;
    }

    /**
     * 带有标签的正则表达式
     *
     * @author ChenYuyang
     * @version 1.0
     */
    static public class RegWithLabel {
        Pattern pattern;
        Class<?> label;

        /**
         * 初始化
         *
         * @param rule  规则正则表达式
         * @param label 标记，是com.compilerExp.Token包下的所有类,应该继承自Token
         */
        public RegWithLabel(String rule, Class<?> label) {
            pattern = Pattern.compile(rule);
            this.label = label;
        }

        /**
         * 得到Label
         *
         * @return 得到Label
         */
        public Class<?> getLabel() {
            return label;
        }

        /**
         * 得到正则表达式
         *
         * @return 正则表达式
         */
        public Pattern getPattern() {
            return pattern;
        }
    }
    private static Lexer lexer;

    static {
        Lexer.RegWithLabel[] rules = new Lexer.RegWithLabel[]{
                new Lexer.RegWithLabel(".*?([a-zA-Z_]+).*?", IdentifierToken.class),
                new Lexer.RegWithLabel(".*?(=).*?", AssignOpToken.class),
                new Lexer.RegWithLabel(".*?(;).*?", SplitOpToken.class),
                new Lexer.RegWithLabel(".*?([\\(\\)\\[\\]\\{\\}]).*?", ParanToken.class),
                new Lexer.RegWithLabel(".*?(>=|<=|==|>|<|!=|\\|\\||&&|\\!).*?", LogOpToken.class),
                new Lexer.RegWithLabel(".*?([+\\-\\*/]).*?", ArOpToken.class),
                new Lexer.RegWithLabel(".*?(\\d+).*?", IntegerToken.class),
                new Lexer.RegWithLabel(".*?(\\d+(\\.\\d*)?([eE][+-]?\\d*)?).*?", DoubleToken.class)
        };
        lexer = new Lexer(rules);
    }
    static public Lexer getRegLexer(){
        return lexer;
    }
}
