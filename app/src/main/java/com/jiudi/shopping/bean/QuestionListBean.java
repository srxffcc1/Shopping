package com.jiudi.shopping.bean;

/**
 * 作者：Constantine on 2018/9/5.
 * 邮箱：2534159288@qq.com
 */
public class QuestionListBean {
    private String mId;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    private String mQuestion;
    private String mAnswer;
}
