package com.lcwd.electronic.store.helper;

public class ApiConstants {

    //User Api
    public static final String USER_BASE_URL = "/api/users/";
    public static final String USER_ID = "/{userId}";
    public static final String USER_EMAIL = "/email";
    public static final String USER_KEYWORD = "/keyword/{keyword}";
    public static final String USER_IMAGE = "/image/{userId}";

    //Category Api
    public static final String CATE_BASE_URL = "/api/categories/";
    public static final String CATE_ID = "/{categoryId}";
    public static final String CATE_SEARCH = "/search";
    public static final String CATE_IMG = "/image/{categoryId}";

    //Product details
    public static final String PROD_BASE_URL = "/api/products/";
    public static final String PROD_ID = "/{productId}";
    public static final String PROD_SEARCH = "/search/{subTitle}";
    public static final String PROD_IS_LIVE = "/isLive";
    public static final String PROD_IMG = "/image/{productId}";
    public static final String PROD_WITH_CAT = "/category/{categoryId}/products";
    public static final String ASSIGN_CAT = "/category/{categoryId}/product/{productId}";

}
