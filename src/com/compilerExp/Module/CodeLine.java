/*
 * @(#) Strategy.java 2019/05/28
 */
package com.compilerExp.Module;

import com.compilerExp.TreeRuntimeEnv.DebugTreeRunEnv;
import com.compilerExp.SyntaxTree.Tree;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.Token.Token;
import com.compilerExp.component.Lexer;
import com.compilerExp.component.RecursiveDescent;
import com.compilerExp.util.CompilerException;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 策略类
 * @version 1.0
 * @author ChenYuyang
 */
public class CodeLine {
    /**
     *
     * @param codeFile 策略代码文件
     * @param lexer 特定的Lexer
     */
    public CodeLine(File codeFile, Lexer lexer){
        this.codeFile=codeFile;
        this.strategyName=codeFile.getName();
        this.lexer=lexer;
    }

    /**
     *
     * @return 得到策略名字
     */
    public String getStrategyName(){return strategyName;}

    /**
     * 编译策略
     * @return 返回编译是否成
     */
    public boolean compile(){
        try {
            content.clear();
            content.add("[代码开始]");
            Scanner sc = new Scanner(new FileInputStream(codeFile));
            ArrayList<Token> tokenArrayList = new ArrayList<>();
            int lineNumber=1;
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                content.add(line);
                if(line.charAt(0)=='#'){
                    lineNumber++; // 注释
                }
                else{
                    tokenArrayList.addAll(lexer.analyse(line,lineNumber++));
                }
            }
            root = RecursiveDescent.run(tokenArrayList);
        }catch (FileNotFoundException fileNot) {
            errorLine="File Not Found";
            errorMsg = fileNot.getMessage();
        }catch (CompilerException e){
            errorLine=content.get(e.getErrorLine());
            errorMsg=getErrorWavyLine(e.getErrorRow()+1)+": "+e.getMessage();
        }
        catch (Exception e){
            errorLine="IO Problem";
            errorMsg=e.getMessage();
        }
        return root!=null;
    }

    /**
     * 运行策略
     * @param env 运行环境
     * @return 返回运行是否成功
     */
    public boolean run(TreeRunEnv env){
        try {
            root.exec(env);
            if(env instanceof DebugTreeRunEnv) {
                writeOutPesudoCode(((DebugTreeRunEnv)env).getPseudocode());
            }
            return true;
        }catch (SynTreeRuntimeException e){
            errorLine=content.get(e.getErrorLine());
            errorMsg=getErrorWavyLine(e.getErrorRow()+1)+": " +e.getMessage();
            return false;
        }
    }

    /**
     * 得到策略的dot代码
     * @return dot代码
     */
    public String getDotCode(){
        GraphDrawer drawer = new GraphDrawer();
        String rootVe = drawer.genVertice();
        drawer.addVertice(rootVe);
        root.putIntoGraphviz(drawer,rootVe);
        return drawer.getDotCode();
    }

    /**
     * 把伪代码写入文件中去
     * @param pesudoCode 伪代码
     */
    void writeOutPesudoCode(String pesudoCode){
        String pesName = codeFile.getAbsolutePath();
        if(codeFile.getName().endsWith(".cyy"))
            pesName = pesName.substring(0,pesName.length()-4);
        pesName=pesName+".pesudoCode";
        try {
            PrintStream os=new PrintStream(new FileOutputStream(pesName,true));
            os.println(pesudoCode);
            os.close();
        }catch (Exception e){
        }
    }

    /**
     * 重写equals.
     * @param to 另外一个strategy
     * @return 如果文件相同就返回true,不然就返回False
     */
    @Override
    public boolean equals(Object to){
        if(!(to instanceof CodeLine)){
            return false;
        }
        CodeLine anStrategy=(CodeLine)to;
        return this.codeFile.equals(anStrategy.codeFile);
    }

    /**
     * 重写hashCode
     * @return 返回codeFile的hashCode()
     */
    @Override
    public int hashCode(){
        return codeFile.hashCode();
    }

    String getErrorWavyLine(int row){
        StringBuilder sb = new StringBuilder();
        for(int i=1;i<row;++i)sb.append('~');
        sb.append('^');
        return sb.toString();
    }

    /**
     *
     * @return 得到上一次的错误
     */
    public String getLastError(){
        return String.format("%s\n%s",errorLine,errorMsg);
    }

    /**
     *
     * @return 本策略是否编译成功过了
     */
    public boolean isCompiled(){return root!=null;}

    /**
     *
     * @return 得到策略代码文件
     */
    public File getCodeFile(){
        return codeFile;
    }

    Tree root=null;
    String strategyName;
    String errorMsg,errorLine;
    File codeFile;
    ArrayList<String> content = new ArrayList<>();
    Lexer lexer;
}
