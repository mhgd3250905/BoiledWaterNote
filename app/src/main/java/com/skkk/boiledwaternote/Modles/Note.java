package com.skkk.boiledwaternote.Modles;

import android.support.annotation.NonNull;

import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteDao;
import com.skkk.boiledwaternote.Modles.gen.NoteImageDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/5/28.
 */
/*
*
* 描    述：
* 作    者：ksheng
* 时    间：2017/5/28$ 15:36$.
*/

@Entity(
        active = true,
        nameInDb = "NOTE"
)
public class Note implements Serializable {
    @Id(autoincrement = true)
    private Long id;                        //id

    @Unique
    @NotNull
    @Property(nameInDb = "nid")
    private Long nid;                       //文章独一无二的ID（时间戳+随机数）

    @Property(nameInDb = "title")
    private String title;                   //文章标题

    @NonNull
    private String content;                 //文章内容

    @OrderBy
    private Date createTime;                //文章创建时间

    @OrderBy
    private Date updateTime;                //文章最近修改时间

    @ToMany(joinProperties = {@JoinProperty(name = "nid", referencedName = "noteId")})
    private List<NoteImage> noteImages;    //文章中的图片

    @Transient
    private boolean isMenuOpen;

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1931499421)
    public synchronized void resetNoteImages() {
        noteImages = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 19574834)
    public List<NoteImage> getNoteImages() {
        if (noteImages == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NoteImageDao targetDao = daoSession.getNoteImageDao();
            List<NoteImage> noteImagesNew = targetDao._queryNote_NoteImages(nid);
            synchronized (this) {
                if (noteImages == null) {
                    noteImages = noteImagesNew;
                }
            }
        }
        return noteImages;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 799086675)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNoteDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 363862535)
    private transient NoteDao myDao;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getNid() {
        return this.nid;
    }

    public void setNid(Long nid) {
        this.nid = nid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    public void setMenuOpen(boolean menuOpen) {
        isMenuOpen = menuOpen;
    }


    @Generated(hash = 1328469578)
    public Note(Long id, @NotNull Long nid, String title, @NotNull String content,
                Date createTime, Date updateTime) {
        this.id = id;
        this.nid = nid;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Generated(hash = 1272611929)
    public Note() {
    }


}
