package ims.supporting;

import ims.daos.*;
import ims.entities.Notifications;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.entities.ScrappedProduct;
import ims.enums.ProductType;
import ims.enums.RecordStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class ProductStateTracker implements Runnable {
    private ProductDetailsDao productDetailsDao;
    private ProductDao productDao;
    private ScrappedProductsDao scrappedProductsDao;
    private NotificationsDao notificationsDao;
    private List<ProductDetails> productDetailsFromDb;


    public ProductStateTracker() {
        productDetailsDao = new ProductDetailsDao();
        productDao = new ProductDao();
        scrappedProductsDao = new ScrappedProductsDao();
        notificationsDao = new NotificationsDao();
        productDetailsFromDb = new ArrayList<>();
    }

    @Override
    public void run() {
        updateTheProducts();
    }

    private void updateTheProducts() {
        try {
            productDetailsFromDb = productDetailsDao.getAllInitiallyLtta();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        List<Product> allLttaProducts = new ArrayList<>();

        for (ProductDetails productDetails : productDetailsFromDb) {
            allLttaProducts.addAll(productDetails.getProducts());
        }

        Period interval;

        for (Product product : allLttaProducts) {
            interval = Period.between(product.getLastModifiedOn(), LocalDate.now());

            if (interval.getYears() >= 1) {
                if (isCurrentPriceLimitPassed(product) && product.getStatus().equals(RecordStatus.ENABLED)) {
                    scrapTheProduct(product);
                } else {
                    decreaseProductPrice(product);
                    checkForProductTypeChange(product);

                    productDao.updateRecord(product);
                }
            }
        }
    }

    private void scrapTheProduct(Product product) {
        ScrappedProduct scrappedProduct = new ScrappedProduct();

        scrappedProduct.setProduct(product);
        scrappedProduct.setScrapDate(LocalDate.now());
        product.setLastModifiedOn(product.getLastModifiedOn().plusYears(1));
        product.setStatus(RecordStatus.DISABLED);
        product.setAvailable(false);

        scrappedProductsDao.saveRecord(scrappedProduct);
        generateScrapNotification(product);

        //TODO remove the product from client's card
    }

    private void decreaseProductPrice(Product product) {
        ProductDetails productDetails = product.getProductDetails();
        productDetails.setCurrentPrice(getPercentageOfThePrice(productDetails.getCurrentPrice(), productDetails.getDepreciationDegree().getPercentage()));
        product.setProductDetails(productDetails);

        productDetails.getProducts().forEach(currentProduct -> {
            currentProduct.setLastModifiedOn(currentProduct.getLastModifiedOn().plusYears(1));
        });

        generatePriceDecreaseNotification(product);
    }

    private void checkForProductTypeChange(Product product) {
        if (isCurrentPriceLimitPassed(product)) {
            ProductDetails productDetails = product.getProductDetails();
            productDetails.setProductType(ProductType.TA);
            product.setProductDetails(productDetails);

            generateTypeChangeNotification(product);
        }
    }

    private void generateScrapNotification(Product product) {
        String notificationMessage = "The product with inventory number ";
        notificationMessage += product.getInventoryNumber();
        notificationMessage += " was scrapped automatically due to its depreciation degree. \nProduct registered on:";
        notificationMessage += product.getRegisteredOn();
        notificationMessage += " Scrapped on:";
        notificationMessage += product.getLastModifiedOn();

        saveTheNotification(notificationMessage);
    }

    private void generatePriceDecreaseNotification(Product product) {
        String notificationMessage = "The price of product with inventory number ";
        notificationMessage += product.getInventoryNumber();
        notificationMessage += " was decreased automatically with ";
        notificationMessage += product.getProductDetails().getDepreciationDegree().getPercentage();
        notificationMessage += "% depending on its depreciation degree.";
        notificationMessage += "\nProduct registered on:";
        notificationMessage += product.getRegisteredOn();
        notificationMessage += " Price decreased on:";
        notificationMessage += product.getLastModifiedOn();
        notificationMessage += "\nInitial price:";
        notificationMessage += product.getProductDetails().getInitialPrice();
        notificationMessage += product.getProductDetails().getPriceCurrency();
        notificationMessage += " Current price:";
        notificationMessage += product.getProductDetails().getCurrentPrice();
        notificationMessage += product.getProductDetails().getPriceCurrency();

        saveTheNotification(notificationMessage);
    }

    private void generateTypeChangeNotification(Product product) {
        String notificationMessage = "The type of product with inventory number ";
        notificationMessage += product.getInventoryNumber();
        notificationMessage += " was transformed automatically from LTTA to TA due to current price threshold reached. \nProduct registered on:";
        notificationMessage += product.getRegisteredOn();
        notificationMessage += " Transformed on:";
        notificationMessage += product.getLastModifiedOn();

        saveTheNotification(notificationMessage);
    }

    private void saveTheNotification(String notificationMessage){
        Notifications newNotification = new Notifications();
        newNotification.setMessage(notificationMessage);
        newNotification.setDateAndTime(LocalDateTime.now());

        notificationsDao.saveRecord(newNotification);
    }

    boolean isCurrentPriceLimitPassed(Product product) {
        BigDecimal initialPrice = product.getProductDetails().getInitialPrice();
        BigDecimal currentPrice = product.getProductDetails().getCurrentPrice();

        return currentPrice.compareTo(getLttaCurrentPriceLimit(initialPrice)) <= 0;
    }

    private BigDecimal getLttaCurrentPriceLimit(BigDecimal initialPrice) {
        return getPercentageOfThePrice(initialPrice, new BigDecimal(10));
    }

    private BigDecimal getPercentageOfThePrice(BigDecimal initialPrice, BigDecimal percentage) {
        return initialPrice.multiply(percentage.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP);
    }
}