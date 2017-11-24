package ru.fintech.db.hw1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by DB on 24.11.2017.
 */

public class Presenter implements PresenterModelInterface, PresenterViewInterface {
    Presenter(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }

    private ModelInterface model;
    private ViewInterface view;
    @Override
    public void callback(NewsObject[] d) {
        Arrays.sort(d, new Comparator<NewsObject>() {
            @Override
            public int compare(NewsObject o1, NewsObject o2) {
                return (int) (o1.getDate()-o2.getDate());
            }
        });
        List<String> list = new ArrayList<>();
        for (int i = 0; i < d.length; i++) {
            list.add(d[i].getText());
        }
        view.callback(list);
    }

    @Override
    public void get(ViewInterface i) {
        view = i;
        model.get(this);
    }
}
