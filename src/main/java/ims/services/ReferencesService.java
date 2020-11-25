package ims.services;

import ims.daos.ProductDao;
import ims.daos.ProductDetailsDao;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.enums.FilterChoice;
import ims.enums.ProductType;
import ims.enums.RecordStatus;
import ims.supporting.TableProduct;

import java.util.*;

public class ReferencesService {
    private final ProductDao productDao;
    private final ProductDetailsDao productDetailsDao;
    private List<Product> allProducts;

    public ReferencesService() {
        productDao = new ProductDao();
        productDetailsDao = new ProductDetailsDao();
        allProducts = productDao.getAll();
    }

    public List<TableProduct> loadChecked(Map<FilterChoice, Boolean> filterChoices) {
        Set<TableProduct> tableProducts = new HashSet<>();

        for (Product product : allProducts) {
            tableProducts.add(getTableProduct(product));
        }

        List<TableProduct> toBeDeleted = new ArrayList<>();

        for (TableProduct tableProduct : tableProducts) {
            if (!filterChoices.get(FilterChoice.TA))
                if (tableProduct.getProduct().getProductDetails().getProductType().equals(ProductType.TA))
                    toBeDeleted.add(tableProduct);
            if (!filterChoices.get(FilterChoice.LTTA))
                if (tableProduct.getProduct().getProductDetails().getProductType().equals(ProductType.LTTA))
                    toBeDeleted.add(tableProduct);
            if (!filterChoices.get(FilterChoice.AVAILABLE))
                if (tableProduct.getProduct().isAvailable() && tableProduct.getProduct().isExisting())
                    toBeDeleted.add(tableProduct);
            if (!filterChoices.get(FilterChoice.BUSY))
                if (!tableProduct.getProduct().isAvailable() && tableProduct.getProduct().isExisting())
                    toBeDeleted.add(tableProduct);
            if (!filterChoices.get(FilterChoice.MISSING))
                if (!tableProduct.getProduct().isExisting())
                    toBeDeleted.add(tableProduct);
            //TODO SCRAPPED FILTER
        }
        tableProducts.removeAll(toBeDeleted);

        return new ArrayList<>(tableProducts);
    }

    private TableProduct getTableProduct(Product product) {
        TableProduct tableProduct = new TableProduct();
        ProductDetails productDetails = product.getProductDetails();

        tableProduct.setBrand(productDetails.getBrandAndModel());
        tableProduct.setInvNum(product.getInventoryNumber());
        tableProduct.setProductType(productDetails.getProductType().toString());
        tableProduct.setInitialPrice(productDetails.getInitialPrice().toString());
        tableProduct.setCurrentPrice(productDetails.getCurrentPrice().toString());
        tableProduct.setProduct(product);

        if (!product.isAvailable() && product.isExisting()
                && product.getStatus().equals(RecordStatus.DISABLED)
                && product.getProductDetails().getProductType().equals(ProductType.LTTA))
            tableProduct.setStatus("Scrapped");
        else {
            if (!product.isExisting())
                tableProduct.setStatus("Missing");
            else {
                if (product.isAvailable())
                    tableProduct.setStatus("Available");
                else
                    tableProduct.setStatus("Busy");
            }
        }

        return tableProduct;
    }

}
