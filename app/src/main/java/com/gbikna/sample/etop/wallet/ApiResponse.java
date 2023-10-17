package com.gbikna.sample.etop.wallet;

public class ApiResponse<T> {
    private boolean loading;
    private boolean error;
    private String msg;
    private T value;

    public ApiResponse(boolean loading, boolean error, String msg, T value) {
        this.loading = loading;
        this.error = error;
        this.msg = msg;
        this.value = value;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getValue() {
        return (T) value;
    }

    public void setValue(T value) { this.value =  value; }
}
