package com.skkk.boiledwaternote.Modles;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteImageDao;

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
        nameInDb = "IMAGE"
)
public class NoteImage {
    @Id(autoincrement = true)
    private Long id;             //id

    private Long noteId;         //对应的文章id

    private String path;         //图片路径

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

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1834839630)
public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNoteImageDao() : null;
}

/** Used for active entity operations. */
@Generated(hash = 85985401)
private transient NoteImageDao myDao;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

public String getPath() {
        return this.path;
}

public void setPath(String path) {
        this.path = path;
}

public Long getNoteId() {
        return this.noteId;
}

public void setNoteId(Long noteId) {
        this.noteId = noteId;
}

public Long getId() {
        return this.id;
}

public void setId(Long id) {
        this.id = id;
}

@Generated(hash = 601800733)
public NoteImage(Long id, Long noteId, String path) {
        this.id = id;
        this.noteId = noteId;
        this.path = path;
}

@Generated(hash = 1190870963)
public NoteImage() {
}



}
