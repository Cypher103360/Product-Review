package com.pr.productreviewadmin.models;

import com.google.gson.annotations.SerializedName;

public class CatModel {

    private String id;
    private String banner;
    private String title;
    @SerializedName("parent_id")
    private String parentId;
    private String subCat;
    private String product;

    public String getId() {
        return id;
    }


    public String getBanner() {
        return banner;
    }

    public String getTitle() {
        return title;
    }

    public String getParentId() {
        return parentId;
    }

    public String getSubCat() {
        return subCat;
    }

    public String getProduct() {
        return product;
    }

    //<!--//Database Connection-->
//<!--$sqlConn =  new mysqli($hostname, $username, $password, $database);-->
//
//<!--//Build SQL String-->
//<!--$sqlString = "SELECT * FROM my_table";-->
//
//<!--//Execute the query and put data into a result-->
//<!--$result = $sqlConn->query($sqlString);-->
//
//<!--//Copy result into a associative array-->
//<!--$resultArray = $result->fetch_all(MYSQLI_ASSOC);-->
//
//<!--//Copy result into a numeric array-->
//<!--$resultArray = $result->fetch_all(MYSQLI_NUM);-->
//
//<!--//Copy result into both a associative and numeric array-->
//<!--$resultArray = $result->fetch_all(MYSQLI_BOTH);-->
}