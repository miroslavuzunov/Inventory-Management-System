package ims.services;

import ims.daos.ProductDao;
import ims.daos.ProductDetailsDao;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.enums.FilterChoice;
import ims.supporting.TableProduct;

import java.util.*;

public class ReferencesService {
    private final ProductDao productDao;
    private final ProductDetailsDao productDetailsDao;

    public ReferencesService() {
        productDao = new ProductDao();
        productDetailsDao = new ProductDetailsDao();
    }

    public List<TableProduct> loadChecked(Map<FilterChoice, Boolean> filterChoices) { //TODO Check all choices
        Set<TableProduct> tableProducts = new HashSet<>();

        if (filterChoices.get(FilterChoice.TA)) {
            fillSet(tableProducts, productDetailsDao.getAllTaProducts());
        }

        return new ArrayList<>(tableProducts);
    }

    private void fillSet(Set<TableProduct> tableProducts, List<ProductDetails> productDetailsFromDb){
        productDetailsFromDb.forEach(productDetails -> {
            productDetails.getProducts().forEach(product -> {
                TableProduct tableProduct = new TableProduct();

                tableProduct.setBrand(productDetails.getBrandAndModel());
                tableProduct.setInvNum(product.getInventoryNumber());
                tableProduct.setProductType(productDetails.getProductType().toString());
                tableProduct.setInitialPrice(productDetails.getInitialPrice().toString());
                tableProduct.setCurrentPrice(productDetails.getCurrentPrice().toString());
                if(!product.isExisting())
                    tableProduct.setStatus("Missing");
                else{
                    if(product.isAvailable())
                        tableProduct.setStatus("Available");
                    else
                        tableProduct.setStatus("Busy");
                }

                tableProducts.add(tableProduct);
            });
        });
    }

}
