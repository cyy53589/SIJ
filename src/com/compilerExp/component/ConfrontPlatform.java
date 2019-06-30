/*
 * @(#) ConfrontPlaform.java 2019/05/28
 */
package com.compilerExp.component;

import com.compilerExp.Module.CodeError;
import com.compilerExp.Module.ConfrontRecord;
import com.compilerExp.Module.CodeLine;
import com.compilerExp.Module.StrategyRankRecord;
import com.compilerExp.Token.*;
import com.compilerExp.TreeRuntimeEnv.DebugTreeRunEnv;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * 对抗平台类
 *
 * @author ChenYuyang
 * @version 1.0
 */
public class ConfrontPlatform {
    /**
     * 基于正则表达式的lexer
     */
    public static Lexer lexer;

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

    /**
     * 构建一个对抗平台
     */
    public ConfrontPlatform() {
        ;
    }

    /**
     * 让对抗策略进场.注意此时不编译、编译策略
     *
     * @param files 策略文件,必须以.cyy结尾
     */
    public void setStrategies(List<File> files) {
        strategies = new ArrayList<>();
        error = new ArrayList<>();
        records = new ArrayList<>();
        rank = new ArrayList<>();
        for (File i : files) {
            strategies.add(new CodeLine(i, lexer));
        }
        for (CodeLine i : strategies) {
            // 删除之前留下的伪代码
            File f = new File(i.getCodeFile().getAbsolutePath().replace(".cyy", ".pesudoCode"));
            if (f.exists()) {
                f.delete();
            }
        }
    }

    /**
     * 编译所有进场的策略，如果编译出错，就把策略踢出局
     */
    public void compileAll() {
        for (int i = 0; i < strategies.size(); ++i) {
            if (!strategies.get(i).compile()) {
                String sname = strategies.get(i).getStrategyName();
                String errorMsg = strategies.get(i).getLastError();
                error.add(new CodeError(sname, errorMsg));
                strategies.remove(i--);
            }
        }
    }

    /**
     * 每两个策略对抗round次
     *
     * @param round      两两之间对抗次数
     * @param debugModel 是否为debug模式
     */
    public void runAll(int round, boolean debugModel) {
        for (int cleft = 0; cleft < strategies.size(); ++cleft) {
            for (int cright = 0; cright < strategies.size(); ++cright) {
                ConfrontRecord record = run(strategies.get(cleft), strategies.get(cright), round, debugModel);
                if (record != null)
                    records.add(record);
            }
        }
        HashMap<String, Integer> scoresResult = new HashMap<>(); // 统计最终结果，把分数都汇总加起来
        for (ConfrontRecord i : records) {
            // if-else: 把strategy1的分数加到榜单上去
            if (scoresResult.containsKey(i.getStrategyName1())) {
                scoresResult.replace(i.getStrategyName1(), scoresResult.get(i.getStrategyName1()) + i.getScore1());
            } else {
                scoresResult.put(i.getStrategyName1(), i.getScore1());
            }
            // if-else: 同上
            if (scoresResult.containsKey(i.getStrategyName2())) {
                scoresResult.replace(i.getStrategyName2(), scoresResult.get(i.getStrategyName2()) + i.getScore2());
            } else {
                scoresResult.put(i.getStrategyName2(), i.getScore2());
            }
        }
        // 把榜单拷到rank,方便排序
        for (HashMap.Entry<String, Integer> i : scoresResult.entrySet()) {
            rank.add(new StrategyRankRecord(0, i.getKey(), i.getValue()));
        }
        rank.sort(new Comparator<StrategyRankRecord>() {
            @Override
            public int compare(StrategyRankRecord o1, StrategyRankRecord o2) {
                return o2.getScore() - o1.getScore();
            }
        });
        // 给名次
        for (int i = 0; i < rank.size(); ++i)
            rank.get(i).setRank(i + 1);
    }

    /**
     * 提供运行环境，两个策略对抗roundMax次数
     *
     * @param leftS    左边的策略
     * @param rightS   右边的策略
     * @param roundMax 对抗次数
     * @return 如果运行的时候没有出错救返回记录, 如果出错就返回null
     */
    ConfrontRecord run(CodeLine leftS, CodeLine rightS, int roundMax, boolean debugModel) {
        ConfrontRecord fot = new ConfrontRecord();
        fot.setStrategyName1(leftS.getStrategyName());
        fot.setStrategyName2(rightS.getStrategyName());

        HashMap<String, ArrayList<Integer>> leftArrayMap = new HashMap<>(), rightArrayMap = new HashMap<>();
        ArrayList<Integer> leftOutputRecord = new ArrayList<>();
        ArrayList<Integer> rightOutputRecord = new ArrayList<>();
        leftArrayMap.put("my", leftOutputRecord);
        leftArrayMap.put("enemy", rightOutputRecord);
        rightArrayMap.put("my", rightOutputRecord);
        rightArrayMap.put("enemy", leftOutputRecord);
        TreeRunEnv envLeft = new TreeRunEnv(leftArrayMap), envRight = new TreeRunEnv(rightArrayMap);
        if (debugModel) {
            envLeft = new DebugTreeRunEnv(leftArrayMap);
            envRight = new DebugTreeRunEnv(rightArrayMap);
        }

        for (int round = 1; round <= roundMax; ++round) {
            envLeft.resetHasReturnTree();
            envRight.resetHasReturnTree();
            envLeft.addRound();
            envRight.addRound();
            if (!leftS.run(envLeft)) {
                extractError(leftS);
                return null;
            }
            if (!rightS.run(envRight)) {
                extractError(rightS);
                return null;
            }
            if (!(envLeft.hasReturnTree() && envRight.hasReturnTree())) {
                return null;
            }
            addScore(fot, envLeft.getFinalResult(), envRight.getFinalResult());
            leftOutputRecord.add(envLeft.getFinalResult());
            rightOutputRecord.add(envRight.getFinalResult());
        }
        return fot;
    }

    /**
     * leftChose,rightChose,scoreFor
     * 例子: (0,0,1)= 左选择0,右选择0,给左的分数是
     */
    static int[][][] scoreMap = new int[][][]{
            {
                    {1, 1}, {5, 0}
            },
            {
                    {0, 5}, {3, 3}
            }
    };

    static void addScore(ConfrontRecord r, int leftResult, int rightResult) {
        if (leftResult > 1) leftResult = 1;
        if (rightResult > 1) rightResult = 1;
        r.addScoreLeft(scoreMap[leftResult][rightResult][0]);
        r.addScoreRight(scoreMap[leftResult][rightResult][1]);
    }

    void extractError(CodeLine s) {
        String sName = s.getStrategyName();
        String msg = s.getLastError();
        error.add(new CodeError(sName, msg));
    }

    /**
     * 把dotCode全部写出到strategyName+".dot",并且画图
     */
    public void drawStrategyTree() {
        for (CodeLine i : strategies) {
            try {
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(i.getCodeFile().getAbsolutePath() + ".dot"));
                out.write(i.getDotCode());
                out.close();
                //new Thread(()-> {
                    try {
                        String cmd = String.format("dot.exe tmp/%s.dot -Tpng -o tmp/%s.png", i.getStrategyName(), i.getStrategyName());
                        System.out.println(cmd);
                        if (Runtime.getRuntime().
                                exec(cmd).
                                waitFor() != 0) {
                            System.out.println(String.format("%s 写出错误", i.getStrategyName()));
                        }
                        Runtime.getRuntime().exec(String.format(
                            "tmp/%s.png",i.getStrategyName())
                            );
                    } catch (Exception e) {
                        System.out.println(String.format("%s:%s", i.getStrategyName(), e.getMessage()));
                        System.out.println(e);
                    }
                //}).start();

            } catch (FileNotFoundException e) {
            } catch (Exception ie) {
                System.out.println(String.format("%s:%s", i.getStrategyName(), ie.getMessage()));
            }
        }
    }


    /**
     * 得到编译运行时候的错误
     *
     * @return (策略名字 ， 错误信息)
     */
    public ArrayList<CodeError> getError() {
        return error;
    }

    /**
     * @return 得到运行记录
     */
    public ArrayList<ConfrontRecord> getRecords() {
        return records;
    }

    /**
     * @return 得到运行之后的分数排名, 分数按照(1, 1), (5, 1), (3, 3)来计算
     */
    public ArrayList<StrategyRankRecord> getRank() {
        return rank;
    }

    ArrayList<ConfrontRecord> records;
    ArrayList<CodeError> error;


    ArrayList<StrategyRankRecord> rank;
    ArrayList<CodeLine> strategies;
}
