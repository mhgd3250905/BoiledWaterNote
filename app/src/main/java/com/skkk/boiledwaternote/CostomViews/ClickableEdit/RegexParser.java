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
public class RegexParser {
    public static final String PHONE_PATTERN = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})))";
    public static final String EMAIL_PATTERN = "([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})";
    public static final String URL_PATTERN = "(|[\\s.:;?\\-\\]<\\(])" +
             "((https?://|www\\.|pic\\.)[-\\w;/?:@&=+$\\|\\_.!~*\\|'()\\[\\]%#,]+[\\w/#](\\(\\))?)" +
             "(?=$|[\\s',\\|\\(\\).:;?\\-\\[\\]>\\)])";

    public static final int phoneType=1;
    public static final int emailType=2;
    public static final int urlType=3;

}