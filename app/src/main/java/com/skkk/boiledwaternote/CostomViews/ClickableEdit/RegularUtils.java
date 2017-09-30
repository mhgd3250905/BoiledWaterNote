package com.skkk.boiledwaternote.CostomViews.ClickableEdit;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class RegularUtils implements IRegularUtils {
    private static final String TAG = "RegularUtils";
    private static final String REG_PHONE = "1[0-9]{10}";
    private RegularRuleBean rule;
    private RegexParser regexParser;

    @Override
    public void setRegularRule(RegularRuleBean rule) {
        this.rule = rule;
    }

    @Override
    public List<RegularBean> getMatchResults(CharSequence s) {
        if (rule == null) {
            return null;
        }

        List<RegularBean> rules = rule.getRules();
        List<RegularBean> results = new ArrayList<>();
        for (int i = 0; i < rules.size(); i++) {
            Pattern pattern = Pattern.compile(rules.get(i).getRegularRule());
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                rules.get(i).setStart(matcher.start());
                rules.get(i).setEnd(matcher.end());
                rules.get(i).setMatchContent(matcher.group());
                results.add(rules.get(i));
                Log.i(TAG, "result: " + matcher.group());
            }
        }
        return results;
    }


}