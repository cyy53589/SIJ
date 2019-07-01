package com.compilerExp;

import com.compilerExp.Module.CodeError;
import com.compilerExp.component.ConfrontPlatform;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class CLI  {
    /**
     * GUI入口
     *
     * @param args --debug : debug模式,非常慢
     */
    public static void main(String[] args) {
        if (args.length >= 0) {
            for (String i : args) {
                switch (i) {
                    case "--debug":
                        debugModel = true;
                        System.out.println("输出伪代码模式启动");
                        break;
                    case "--SynTree":
                        writeOutSynTreeModel = true;
                        System.out.println("输出图模式启动");
                        break;
                }
            }
        }
        launch(args);
    }


    /*
    public static void main_2(String[] args) throws Exception {
        /*
        ArrayList<Token> tokens = new ArrayList<>();
        int line = 1;
        Scanner sc = new Scanner(new FileInputStream("in.txt"));
        while (sc.hasNextLine()) {
            tokens.addAll(lexer.analyse(sc.nextLine(), line++));
        }
        System.out.printf("Token size=%d\n", tokens.size());
        for (Token i : tokens) {
            System.out.printf("[%s\t]\tValue=\"%s\"\n", i.getClass().getSimpleName(), i.getValue());
        }
        System.out.println("===");
        Tree root = RecursiveDescent.run(tokens);
        DebugTreeRunEnv env=new DebugTreeRunEnv();
        root.exec(env);
        System.out.println("Result:"+env.getFinalResult());
    }
        */
    /*
    String getDoeCode(Tree root){
        GraphDrawer dg = new GraphDrawer();
        dg.addVertice("root");
        root.putIntoGraphviz(dg, "root");
        return dg.getDotCode();
    }
    */
}
