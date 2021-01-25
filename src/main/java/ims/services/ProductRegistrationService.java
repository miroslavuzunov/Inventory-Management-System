package ims.services;

import ims.daos.DepreciationDegreeDao;
import ims.daos.ProductDao;
import ims.daos.ProductDetailsDao;
import ims.entities.DepreciationDegree;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.enums.PriceCurrency;
import ims.enums.ProductType;
import ims.enums.RecordStatus;
import ims.enums.State;
import ims.supporting.CustomField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ims.controllers.resources.RegisterProductControllerResources.*;


public class ProductRegistrationService {
    private final DepreciationDegreeDao depreciationDegreeDao;
    private final ProductDetailsDao productDetailsDao;
    private final ProductDao productDao;

    public ProductRegistrationService() {
        this.depreciationDegreeDao = new DepreciationDegreeDao();
        this.productDetailsDao = new ProductDetailsDao();
        this.productDao = new ProductDao();
    }

    public List<CustomField> getDepreciationDegrees() {
        List<CustomField> controllerDegrees = new ArrayList<>();
        List<DepreciationDegree> dbDegrees = depreciationDegreeDao.getAll();

        dbDegrees.forEach(degree -> {
            controllerDegrees.add(new CustomField(degree.getCategory() + "(" + degree.getProductGroup() + ")"));
        });

        return controllerDegrees;
    }

    public boolean isDataBusy(Map<String, CustomField> customFieldsByName) throws NoSuchFieldException {
        String inputBrandAndModel = extractInputBrandAndModel(customFieldsByName);
        ProductDetails productDetails = productDetailsDao.getProductDetailsByBrandAndModel(inputBrandAndModel);

        if (productDetails == null) {
            return false;
        }
        if (inputBrandAndModel.equalsIgnoreCase(productDetails.getBrandAndModel())) {
            customFieldsByName.get(BRAND_FIELD_NAME).setState(State.INVALID);
            customFieldsByName.get(MODEL_FIELD_NAME).setState(State.INVALID);
            customFieldsByName.get(MODEL_FIELD_NAME).setMessage(BRAND_MODEL_EXISTS);

            return true;
        }

        return false;
    }

    private String extractInputBrandAndModel(Map<String, CustomField> customFieldsByName) {
        String inputBrandAndModel =
                customFieldsByName.get(BRAND_FIELD_NAME).getFieldValue() +
                        customFieldsByName.get(MODEL_FIELD_NAME).getFieldValue();

        return inputBrandAndModel.replaceAll("\\s", "");
    }

    public void registerProduct(Map<String, CustomField> customFieldsByName) throws NoSuchFieldException {
        DepreciationDegree depreciationDegree = new DepreciationDegree();
        ProductDetails productDetails = new ProductDetails();

        setProductDepreciationDegree(customFieldsByName, depreciationDegree);
        setProductDetails(customFieldsByName, productDetails, depreciationDegree);
        generateProductsByDetails(customFieldsByName, productDetails, productDetails.getQuantity()); // Separating quantity because of reusability
    }

    private void setProductDepreciationDegree(Map<String, CustomField> customFieldsByName, DepreciationDegree depreciationDegree) {
        if (!customFieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).isNullable()) {
            String categoryAndGroupTogether = customFieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).getFieldValue();
            String[] categoryAndGroup = Pattern.compile("[\\(\\)]").split(categoryAndGroupTogether);
            depreciationDegree.setCategory(categoryAndGroup[0]);
            depreciationDegree.setProductGroup(categoryAndGroup[1]);
        }
    }

    private void setProductDetails(Map<String, CustomField> customFieldsByName, ProductDetails productDetails, DepreciationDegree depreciationDegree) throws NoSuchFieldException {
        if (!customFieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).isNullable())
            productDetails.setDepreciationDegree(productDetailsDao.getDepreciationDegreeReference(getDepreciationDegreeId(depreciationDegree)));
        productDetails.setBrandAndModel(extractInputBrandAndModel(customFieldsByName));
        productDetails.setDescription(customFieldsByName.get(DESCRIPTION_FIELD_NAME).getFieldValue());
        productDetails.setInitialPrice(new BigDecimal(customFieldsByName.get(UNIT_PRICE_FIELD_NAME).getFieldValue()));
        productDetails.setCurrentPrice(new BigDecimal(customFieldsByName.get(UNIT_PRICE_FIELD_NAME).getFieldValue()));
        productDetails.setQuantity(Integer.parseInt(customFieldsByName.get(QUANTITY_FIELD_NAME).getFieldValue()));

        setProductType(customFieldsByName, productDetails);
        setProductPriceCurrency(customFieldsByName, productDetails);
    }

    private void setProductType(Map<String, CustomField> customFieldsByName, ProductDetails productDetails) {
        if (customFieldsByName.get(PRODUCT_TYPE_FIELD_NAME).getFieldValue().equals("LTTA")) {
            productDetails.setInitialProductType(ProductType.LTTA);
            productDetails.setProductType(ProductType.LTTA);
        } else {
            productDetails.setInitialProductType(ProductType.TA);
            productDetails.setProductType(ProductType.TA);
        }
    }

    private void setProductPriceCurrency(Map<String, CustomField> customFieldsByName, ProductDetails productDetails) {
        if (customFieldsByName.get(UNIT_PRICE_COMBO_NAME).getFieldValue().equals("BGN"))
            productDetails.setPriceCurrency(PriceCurrency.BGN);
        if (customFieldsByName.get(UNIT_PRICE_COMBO_NAME).getFieldValue().equals("USD"))
            productDetails.setPriceCurrency(PriceCurrency.USD);
        if (customFieldsByName.get(UNIT_PRICE_COMBO_NAME).getFieldValue().equals("EUR"))
            productDetails.setPriceCurrency(PriceCurrency.EUR);
    }

    public void generateProductsByDetails(Map<String, CustomField> customFieldsByName, ProductDetails productDetails, int quantity) {
        int count = quantity;
        String productCategory;
        int lastProductID = extractIDFromInventoryNumber(productDao.getLastRecord().getInventoryNumber());

        if (productDetails.getDepreciationDegree() != null)
            productCategory = productDetails.getDepreciationDegree().getCategory();
        else
            productCategory = "TA";

        while (count != 0) {
            Product product = new Product();
            product.setProductDetails(productDetails);
            product.setRegisteredOn(LocalDate.now());
            product.setLastModifiedOn(LocalDate.now());
            product.setAvailable(true);
            product.setExisting(true);
            product.setInventoryNumber(generateUniqueInventoryNumber(customFieldsByName,
                    productDetails.getQuantity() - count,
                    productCategory
                    ,
                    lastProductID)
            );
            product.setStatus(RecordStatus.ENABLED);

            productDao.saveRecord(product);

            count--;
        }
    }

    int extractIDFromInventoryNumber(String inventoryNumber) {
        if (inventoryNumber != null) {
            String[] separatedInventoryNumber = inventoryNumber.split("-");
            if (separatedInventoryNumber.length != 0)
                return Integer.parseInt(separatedInventoryNumber[3]) + 1; //ID is at fourth position in the inv number
        }

        return 1;
    }

    private String generateUniqueInventoryNumber(Map<String, CustomField> customFieldsByName, int currentNum, String category, int lastProductID) {
        String invNumber = "";
        String halfNano = String.valueOf(System.nanoTime()); //Provides uniqueness
        halfNano = halfNano.substring(halfNano.length() / 2); //Shortening

        invNumber += customFieldsByName.get(BRAND_FIELD_NAME).getFieldValue().substring(0, 1).toUpperCase();
        invNumber += customFieldsByName.get(MODEL_FIELD_NAME).getFieldValue().substring(0, 1).toUpperCase();
        invNumber += "-";
        invNumber += category;
        invNumber += "-";
        invNumber += halfNano;
        invNumber += "-";
        invNumber += lastProductID;
        invNumber += "-";
        invNumber += String.valueOf(currentNum);

        return invNumber;
    }

    public List<ProductDetails> getAllProductsDetails() {
        return productDetailsDao.getAll();
    }

    public ProductDetails getProductDetailsByBrandAndModel(String brandAndModel) throws NoSuchFieldException {
        return productDetailsDao.getProductDetailsByBrandAndModel(brandAndModel);
    }

    public void updateProductQuantity(ProductDetails productDetails, int newQuantity) {
        productDetails.setQuantity(productDetails.getQuantity() + newQuantity);

        productDetailsDao.updateRecord(productDetails);
    }

    public Integer getDepreciationDegreeId(DepreciationDegree depreciationDegree) throws NoSuchFieldException {
        DepreciationDegree tempDepreciationDegree = depreciationDegreeDao.getRecordByCategory(depreciationDegree.getCategory());

        if (tempDepreciationDegree == null)
            return null;

        return tempDepreciationDegree.getId();
    }
}
