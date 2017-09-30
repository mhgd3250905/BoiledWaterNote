package com.skkk.boiledwaternote.CostomViews.ClickableEdit;

import java.util.List;

/**
 * 创建于 2017/9/30
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/30$ 19:54$.
*/
public interface IRegularUtils {
    void setRegularRule(RegularRuleBean rule);

    List<RegularBean> getMatchResults(CharSequence s);
}
