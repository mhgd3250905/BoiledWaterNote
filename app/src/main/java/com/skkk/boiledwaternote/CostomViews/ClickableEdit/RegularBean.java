package com.skkk.boiledwaternote.CostomViews.ClickableEdit;

/**
 * 创建于 2017/9/30
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/30$ 19:57$.
*/
public class RegularBean {
    private String regularRule;
    private String matchContent;
    private int textColor;
    private int start;
    private int end;
    private int type;

    public RegularBean(String regularRule, int type) {
        this.regularRule = regularRule;
        this.type = type;
    }

    public String getRegularRule() {
        return regularRule;
    }

    public void setRegularRule(String regularRule) {
        this.regularRule = regularRule;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getMatchContent() {
        return matchContent;
    }

    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
