package ru.fintech.db.hw1;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by DB on 12.11.2017.
 */
@Entity(primaryKeys = {"parent","child"})
public class Relations {
    public int parent;
    public int child;
//    public transient List<Relations> children;
    @Override
    public String toString() {
        return Integer.toString(parent)+" -> "+Integer.toString(child);
    }
    public Relations (int parent, int child) {
        this.parent = parent;
        this.child = child;
    }
}
