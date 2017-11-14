package ru.fintech.db.hw1;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

import java.util.List;

/**
 * Created by DB on 12.11.2017.
 */

public class NodeDatabase {
    @Database(entities = {Relations.class}, version = 1)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract RelationsDao relationsDao();
    }

    public static NodeDatabase getInstance() {return ourInstance;}
    private static NodeDatabase ourInstance = new NodeDatabase();

    private RelationsDao dao;
    private SparseArray<Node> data;
    private SparseArray<SparseBooleanArray> parentData;
    private NodeDatabase() {}
    public void init(Context context) {
        if (dao == null) {
            dao = Room
            .databaseBuilder(context, AppDatabase.class, "db-hw7")
            .build().relationsDao();
        }
    }
    public void fetch(){
        if (data == null) {
            data = new SparseArray<>();
            parentData = new SparseArray<>();
            List<Relations> relList = dao.getAll();

            for (Relations rel : relList) {

                Node parent = data.get(rel.parent);
                if (parent == null) {
                    parent = new Node(rel.parent);
                    data.put(rel.parent, parent);
                    parentData.put(rel.parent, new SparseBooleanArray());
                }
                if (rel.parent == rel.child) continue;
                Node child = data.get(rel.child);
                if (child == null) {
                    child = new Node(rel.child);
                    data.put(rel.child, child);
                    parentData.put(rel.child, new SparseBooleanArray());
                }
                parent.children.add(child);
                parentData.get(rel.child).append(rel.parent, true);
            }
        }
    }

    public void createNode (int value) {
        Node node = new Node(value);
        data.put(value, node);
        parentData.put(value, new SparseBooleanArray());
        Relations relations = new Relations(value, value);
        dao.insertAll(relations);
    }

    public void changeRelation(int parentValue, int childValue) {
        SparseBooleanArray list = parentData.get(childValue);
        Relations relations = new Relations(parentValue,childValue);
        if (list.get(parentValue)){
            list.delete(parentValue);
            data.get(parentValue).children.remove(data.get(childValue));
            dao.delete(relations);
        }
        else {
            list.put(parentValue,true);
            data.get(parentValue).children.add(data.get(childValue));
            dao.insertAll(relations);
        }
    }

    public SparseIntArray getNodes () {
        SparseIntArray arr = new SparseIntArray();
        for (int i=0; i<data.size(); i++) {
            int c=0;
            if(data.valueAt(i).children.size()>0) c++;
            if(parentData.get(data.keyAt(i)).size()>0) c+=2;
            arr.put(data.keyAt(i),c);
        }
        return arr;
    }
    public SparseBooleanArray getNodesRelationsForNode (int position, boolean isItParent) {
        SparseBooleanArray arr = new SparseBooleanArray();
        for (int i=0; i<data.size(); i++) {
            if(data.keyAt(i)!=position) arr.put(data.keyAt(i),false);
        }
        //if(isItParent) {
            for (int i=0; i<parentData.get(position).size(); i++) {
                arr.put(parentData.get(position).keyAt(i),parentData.get(position).valueAt(i));
            }
//        }
//        else{
//            for (int i=0; i<parentData.size(); i++) {
//                arr.delete(parentData.keyAt(i));
//                arr.append(parentData.keyAt(i), true));
//            }
//        }
        return arr;
    }

    public SparseIntArray getNodeRelations(int value) {
        SparseIntArray arr = new SparseIntArray();
        for (int i = 0; i < data.size(); i++) {
            final int idx = data.keyAt(i);
            if (idx == value) continue;
            int c = 0;
            if (parentData.get(value).get(idx)) c++;
            if (parentData.get(idx).get(value)) c+=2;
            arr.put(idx, c);
        }
        return arr;
    }

}
