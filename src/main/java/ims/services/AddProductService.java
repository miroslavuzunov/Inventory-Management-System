package ims.services;

import ims.daos.AbstractDao;
import ims.daos.ProductDao;
import ims.daos.ProductDetailsDao;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.supporting.TableProduct;

import java.util.ArrayList;
import java.util.List;

public class AddProductService {
    private ProductDetailsDao productDetailsDao;
    private ProductDao productDao;

    public AddProductService() {
        productDetailsDao = new ProductDetailsDao();
        productDao = new ProductDao();
    }

    public List<TableProduct> getAllProducts() {
        List<TableProduct> tableProducts = new ArrayList<>();
        List<Product> allProducts = productDao.getAll();

        productDetailsDao.getAll().forEach(productDetails -> {
            TableProduct tableProduct = new TableProduct();

            tableProduct.setBrand(productDetails.getBrandAndModel());
            tableProduct.setProductType(productDetails.getProductType().toString());
            tableProduct.setTotalQuantity(productDetails.getQuantity().toString());
            tableProduct.setAvailableQuantity(getProductAvailableQuantity(allProducts, productDetails).toString());

            tableProducts.add(tableProduct);
        });

        return tableProducts;
    }

    private Integer getProductAvailableQuantity(List<Product> allProducts, ProductDetails productDetails) {
        int availableCount = 0;
        List<Product> toBeRemoved = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.getProductDetails().equals(productDetails) && product.isAvailable()) {
                availableCount++;
                toBeRemoved.add(product);
            }
        }
        allProducts.removeAll(toBeRemoved);

        return availableCount;
    }
}
