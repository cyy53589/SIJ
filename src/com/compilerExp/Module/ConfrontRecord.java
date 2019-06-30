/**
 * @(#) ConfrontRecord.java 2019/05/28
 */
package com.compilerExp.Module;

/**
 * 对抗记录
 * @version 1.0
 * @author ChenYuyang
 */
public class ConfrontRecord {
    /**
     * 构建一个空白对抗记录
     */
    public ConfrontRecord(){
        this.score1=this.score2=0;
    }

    /**
     * 构建一个对抗记录
     * @param strategyName1 左边策略名字
     * @param strategyName2 右边策略名字
     * @param score1 左边策略的初始得分
     * @param score2 右边策略的初始得分
     */
    public ConfrontRecord(String strategyName1,String strategyName2,int score1,int score2){
        this.score1=score1;
        this.score2=score2;
        this.strategyName1=strategyName1;
        this.strategyName2=strategyName2;
    }

    /**
     *
     * @param by 左边加分
     */
    public void addScoreLeft(int by){
        score1+=by;
    }

    /**
     *
     * @param by 右边加分
     */
    public void addScoreRight(int by){
        score2+=by;
    }

    /**
     *
     * @return 得到左边策略名字
     */
    public String getStrategyName1() {
        return strategyName1;
    }

    /**
     *
     * @param strategyName1 设置左边策略名字
     */
    public void setStrategyName1(String strategyName1) {
        this.strategyName1 = strategyName1;
    }

    /**
     *
     * @return 得到右边策略名字
     */
    public String getStrategyName2() {
        return strategyName2;
    }

    /**
     *
     * @param strategyName2 设置右边策略名字
     */
    public void setStrategyName2(String strategyName2) {
        this.strategyName2 = strategyName2;
    }

    /**
     *
     * @return 得到左边策略得分
     */
    public int getScore1() {
        return score1;
    }

    /*
    public void setScore1(int score1) {
        this.score1 = score1;
    }
    */

    /**
     *
     * @return 得到右边策略得分
     */
    public int getScore2() {
        return score2;
    }

    /*
    public void setScore2(int score2) {
        this.score2 = score2;
    }
    */

    public String strategyName1,strategyName2;
    int score1,score2;
}
