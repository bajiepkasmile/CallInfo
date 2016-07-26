package com.nodomain.callinfo;

import android.os.AsyncTask;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LoadCallInfoTask extends AsyncTask<String, Void, String> {

    private TextView tvInfo;

    public LoadCallInfoTask(TextView tvInfo) {
        this.tvInfo = tvInfo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String info = "";
        try {
            String phoneNumber = strings[0];
            Document document = Jsoup.connect("https://yandex.ru/search/?text=" + phoneNumber).get();
            Elements elements = document.getElementsByClass("serp-item__title-link");
            info = elements.get(0).text();
        } catch (Exception e) {
            info = "Не удалось получить информацию о номере";
        }

        return info;
    }

    @Override
    protected void onPostExecute(String info) {
        tvInfo.setText(info);
    }

}
