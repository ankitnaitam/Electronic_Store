package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto, String productId);

    //delete
    void deleteProduct(String productId);

    //get single
    ProductDto getProduct(String productId);

    //get all
    PageableResponse<ProductDto> getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //get all : isActive
    PageableResponse<ProductDto> getIsLiveProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //search
    PageableResponse<ProductDto> searchProduct(String subTitle, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //create product with category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);

    //add category in product
    ProductDto assignCategoryToProduct(String categoryId,String productId);

}
