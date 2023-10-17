package com.gbikna.sample.gbikna.util.update;

public interface QueryListener {

    void onFailure(String packageName, String errMsg);

    void onResponse(String packageName, String packageInfo);

}