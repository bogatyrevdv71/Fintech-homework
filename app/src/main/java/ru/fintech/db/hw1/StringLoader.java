package ru.fintech.db.hw1;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 * Created by DB on 22.10.2017.
 *
 */

public class StringLoader extends AsyncTask<Void,Void,String> {
    private static int stringIndex =0;

    private WeakReference<StringContainerFragment> stringContainerFragment;

    StringLoader(StringContainerFragment stringContainerFragment) {
        this.stringContainerFragment = new WeakReference<>(stringContainerFragment);
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(isCancelled()){
            return null;
        }
        final String[] testStringArray = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit."
                ,"Donec eget dolor pharetra nisi aliquam maximus at et nibh.",
                "Fusce vehicula at lacus vestibulum interdum.",
                "Suspendisse condimentum commodo mauris, non mattis ipsum imperdiet ut.",
                "Fusce sed ultrices arcu, eget commodo dolor.",
                "Vivamus non lacus sit amet sapien fermentum interdum.",
                "Aliquam vestibulum blandit nisi.",
                "Aliquam rhoncus, nibh gravida blandit convallis, tellus purus mollis nunc, quis " +
                        "interdum magna massa mollis neque.",
                "Donec et vestibulum purus.",
                "Donec vel ex facilisis, varius augue at, imperdiet ante.",
                "Orci varius natoque penatibus et magnis dis parturient montes, nascetur " +
                        "ridiculus mus.",
                "Nulla sagittis eros non tortor tristique placerat.",
                "Nulla eu sem quis orci scelerisque viverra.",
                "Vestibulum congue porttitor elit, vitae porta est.",
                "Cras ultrices euismod ullamcorper.",
                "Donec mollis blandit eros quis vestibulum.",
                "Nam eget arcu in lacus egestas facilisis vitae quis sapien."};
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if(stringIndex<testStringArray.length)
            return testStringArray[stringIndex++];
        else
            return null;
    }
    @Override
    protected void onPostExecute(String list) {
        StringContainerFragment stringContainerFragment = this.stringContainerFragment.get();
        if (stringContainerFragment != null) {
            stringContainerFragment.setString(list);
        }
    }
}
