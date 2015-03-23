package caup.dataloader.util;

import java.io.Serializable;

/**
 * Created by Richard on 14-9-18.
 */
public class DataWrapper<T> implements Serializable {


    /**
     *
     */

    private static final long serialVersionUID = 2197712541811550519L;

    private String username;

    private T data;
    private String sessionKey;


    // 用于分页结果
    private int numberPerPage;
    private int pageNumber;
    private int totalItemNumber;
    private int totalPageNumber;

    public DataWrapper() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }



    public int getNumberPerPage() {
        return numberPerPage;
    }

    public void setNumberPerPage(int numberPerPage) {
        this.numberPerPage = numberPerPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalItemNumber() {
        return totalItemNumber;
    }

    public void setTotalItemNumber(int totalItemNumber) {
        this.totalItemNumber = totalItemNumber;
    }

    public int getTotalPageNumber() {
        return totalPageNumber;
    }

    public void setTotalPageNumber(int totalPageNumber) {
        this.totalPageNumber = totalPageNumber;
    }

    @Override
    public String toString() {
        return
                "Page Num:" + this.pageNumber + "\n" +
                "Total Page Num:" + this.totalPageNumber + "\n" +
                "Item Num per Page:" + this.numberPerPage + "\n" +
                "Total Item Num:" + this.totalItemNumber + "\n" ;
    }


}
