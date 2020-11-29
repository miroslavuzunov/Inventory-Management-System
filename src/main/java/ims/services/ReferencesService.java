package ims.services;

import ims.daos.ProductDao;
import ims.daos.ProductDetailsDao;
import ims.daos.ScrappedProductsDao;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.entities.ScrappedProducts;
import ims.enums.FilterChoice;
import ims.enums.ProductType;
import ims.enums.RecordStatus;
import ims.supporting.TableProduct;

import java.util.*;

public class ReferencesService {
    private final ProductDao productDao;
    private final ProductDetailsDao productDetailsDao;
    private final ScrappedProductsDao scrappedProductsDao;
    private List<ProductDetails> allProductDetails;
    private List<Product> allProducts;
    private Map<FilterChoice, Boolean> filterChoices;

    public ReferencesService() {
        productDao = new ProductDao();
        productDetailsDao = new ProductDetailsDao();
        scrappedProductsDao = new ScrappedProductsDao();
        allProducts = productDao.getAll();
        allProductDetails = productDetailsDao.getAll();
    }


    public List<TableProduct> loadAll() {
        Set<TableProduct> tableProducts = new LinkedHashSet<>(); //Keeps insertion order

        for (Product product : allProducts) {
            tableProducts.add(generateTableProduct(product));
        }

        return new ArrayList<>(tableProducts);
    }

    public List<TableProduct> loadChecked(Map<FilterChoice, Boolean> filterChoices) {
        this.filterChoices = filterChoices;
        Set<TableProduct> tableProducts = new LinkedHashSet<>();
        List<Product> filteredProducts = new ArrayList<>();

        if (!getTypeBasedProducts().isEmpty() && !getStatusBasedProducts().isEmpty())
            filteredProducts = intersection(getTypeBasedProducts(), getStatusBasedProducts());
        else {
            if (!getTypeBasedProducts().isEmpty())
                filteredProducts = union(filteredProducts, getTypeBasedProducts());
            if (!getStatusBasedProducts().isEmpty())
                filteredProducts = union(filteredProducts, getStatusBasedProducts());
        }

        for (Product product : filteredProducts) {
            tableProducts.add(generateTableProduct(product));
        }

        return new ArrayList<>(tableProducts);
    }

    private List<Product> getTypeBasedProducts() {
        List<Product> typeBasedProducts = new ArrayList<>();

        if (filterChoices.get(FilterChoice.TA))
            typeBasedProducts = union(typeBasedProducts, getTaProducts());
        if (filterChoices.get(FilterChoice.LTTA))
            typeBasedProducts = union(typeBasedProducts, getLttaProducts());

        return typeBasedProducts;
    }

    private List<Product> getTaProducts() {
        List<Product> taProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.getProductDetails().getProductType().equals(ProductType.TA))
                taProducts.add(product);
        }

        return taProducts;
    }

    private List<Product> getLttaProducts() {
        List<Product> lttaProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.getProductDetails().getProductType().equals(ProductType.LTTA))
                lttaProducts.add(product);
        }

        return lttaProducts;
    }

    private List<Product> getStatusBasedProducts() {
        List<Product> statusBasedProducts = new ArrayList<>();

        if (filterChoices.get(FilterChoice.BUSY))
            statusBasedProducts = union(statusBasedProducts, getBusyProducts());
        if (filterChoices.get(FilterChoice.AVAILABLE))
            statusBasedProducts = union(statusBasedProducts, getAvailableProducts());
        if (filterChoices.get(FilterChoice.MISSING))
            statusBasedProducts = union(statusBasedProducts, getMissingProducts());
        if (filterChoices.get(FilterChoice.SCRAPPED))
            statusBasedProducts = union(statusBasedProducts, getScrappedProducts());

        return statusBasedProducts;
    }

    private List<Product> getBusyProducts() {
        List<Product> busyProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (!product.isAvailable() && product.isExisting())
                busyProducts.add(product);
        }

        return busyProducts;
    }

    private List<Product> getAvailableProducts() {
        List<Product> availableProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.isAvailable())
                availableProducts.add(product);
        }

        return availableProducts;
    }

    private List<Product> getMissingProducts() {
        List<Product> missingProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (!product.isExisting())
                missingProducts.add(product);
        }

        return missingProducts;
    }

    private List<Product> getScrappedProducts() {
        List<Product> scrappedProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (!product.isAvailable() && product.isExisting()
                    && product.getStatus().equals(RecordStatus.DISABLED)
                    && product.getProductDetails().getProductType().equals(ProductType.LTTA))
                scrappedProducts.add(product);
        }

        return scrappedProducts;
    }

    private List<Product> union(List<Product> list1, List<Product> list2) {
        Set<Product> set = new LinkedHashSet<>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<>(set);
    }

    private List<Product> intersection(List<Product> list1, List<Product> list2) {
        List<Product> list = new ArrayList<>();

        for (Product product : list1) {
            if (list2.contains(product)) {
                list.add(product);
            }
        }

        return list;
    }

    private TableProduct generateTableProduct(Product product) {
        TableProduct tableProduct = new TableProduct();
        ProductDetails productDetails = product.getProductDetails();

        tableProduct.setBrand(productDetails.getBrandAndModel());
        tableProduct.setInvNum(product.getInventoryNumber());
        tableProduct.setProductType(productDetails.getProductType().toString());
        tableProduct.setInitialPrice(productDetails.getInitialPrice().toString() + " " + productDetails.getPriceCurrency().toString());
        tableProduct.setCurrentPrice(productDetails.getCurrentPrice().toString() + " " + productDetails.getPriceCurrency().toString());
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
