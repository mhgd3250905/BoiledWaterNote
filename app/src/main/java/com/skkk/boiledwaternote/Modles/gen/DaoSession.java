package com.skkk.boiledwaternote.Modles.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteImage;

import com.skkk.boiledwaternote.Modles.gen.NoteDao;
import com.skkk.boiledwaternote.Modles.gen.NoteImageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteDaoConfig;
    private final DaoConfig noteImageDaoConfig;

    private final NoteDao noteDao;
    private final NoteImageDao noteImageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        noteImageDaoConfig = daoConfigMap.get(NoteImageDao.class).clone();
        noteImageDaoConfig.initIdentityScope(type);

        noteDao = new NoteDao(noteDaoConfig, this);
        noteImageDao = new NoteImageDao(noteImageDaoConfig, this);

        registerDao(Note.class, noteDao);
        registerDao(NoteImage.class, noteImageDao);
    }
    
    public void clear() {
        noteDaoConfig.getIdentityScope().clear();
        noteImageDaoConfig.getIdentityScope().clear();
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public NoteImageDao getNoteImageDao() {
        return noteImageDao;
    }

}
