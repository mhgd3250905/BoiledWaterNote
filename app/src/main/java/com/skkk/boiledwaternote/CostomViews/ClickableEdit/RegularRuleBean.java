package com.skkk.boiledwaternote.CostomViews.ClickableEdit;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建于 2017/9/30
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/30$ 19:58$.
*/
public class RegularRuleBean {
    private List<RegularBean> list;

    public RegularRuleBean() {
        list = new ArrayList<>();
    }

    public RegularRuleBean addNewRule(String regularStr, int type) {
        if (list == null) {
            return this;
        }
        list.add(new RegularBean(regularStr, type));
        return this;
    }

    public List<RegularBean> resetRelus() {
        for (RegularBean regularBean : list) {
            regularBean.setStart(0);
            regularBean.setEnd(0);
        }
        return list;
    }

    public List<RegularBean> getRules() {
        return resetRelus();
    }
}