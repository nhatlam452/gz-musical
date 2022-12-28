package com.example.duantotnghiep;

import com.example.duantotnghiep.Contract.ProductContract;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Presenter.ProductPresenter;
import com.google.android.gms.analytics.ecommerce.Product;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class ProductTest {
    @Test
    public void testOnFinished() {
        // Create a mock ProductListResponse object with some mock data
        ProductListResponse productListResponse = Mockito.mock(ProductListResponse.class);
        List<Products> data = new ArrayList<>();
        data.add(new Products(1, "Product 1", "Brand 1"));
        data.add(new Products(2, "Product 2", "Brand 2"));
        Mockito.when(productListResponse.getData()).thenReturn(data);

        // Create a mock ProductContract.View object
        ProductContract.View productView = Mockito.mock(ProductContract.View.class);

        // Create a ProductPresenter object and call the onFinished method
        ProductPresenter presenter = new ProductPresenter(productView);
        presenter.onFinished(productListResponse);

        // Verify that the setProductList method of the productView object was called with the correct data
        Mockito.verify(productView).setProductList(data);
    }

}
