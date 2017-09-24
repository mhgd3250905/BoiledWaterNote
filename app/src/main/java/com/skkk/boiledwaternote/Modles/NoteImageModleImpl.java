package com.skkk.boiledwaternote.Modles;

import java.util.List;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/24$ 15:21$.
*/
public interface NoteImageModleImpl {
    NoteImage queryOne();//查询一个图片
    List<NoteImage> queryAll(int noteType);//查询指定类型的所有图片
    boolean deleteOne(NoteImage noteImage);//删除一个图片
    boolean deleteAll(List<NoteImage> noteImageList);//删除所有图片
}
