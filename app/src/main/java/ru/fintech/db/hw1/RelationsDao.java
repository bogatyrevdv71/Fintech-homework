package ru.fintech.db.hw1;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by DB on 12.11.2017.
 */
@Dao
public interface RelationsDao {
    @Query("SELECT parent FROM relations WHERE child=:child")
    List<Integer> getParents(int child);

    @Query("SELECT child FROM relations WHERE parent=:parent")
    List<Integer> getChilds(int parent);

    @Query("SELECT * from relations")
    List<Relations> getAll();

    @Insert
    void insertAll (Relations... relations);

    @Delete
    void delete(Relations relation);

}
