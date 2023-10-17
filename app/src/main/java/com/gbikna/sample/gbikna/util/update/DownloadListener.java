package com.gbikna.sample.gbikna.util.update;

public interface DownloadListener {

    void onProgress(String filePath, int progress, int total);

    void onFailure(String filePath, String errMsg);

    void onCompleted(String filePath);

}