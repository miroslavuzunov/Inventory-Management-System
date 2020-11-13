package ims.services;

import ims.daos.DepreciationDegreeDao;
import ims.daos.ProductDao;
import ims.daos.ProductDetailsDao;
import ims.entities.DepreciationDegree;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.enums.PriceCurrency;
import ims.enums.ProductType;
import ims.enums.State;
import ims.supporting.CustomField;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    public List<CustomField> initializeDepreciationDegree() {
        List<CustomField> controllerDegrees = new ArrayList<>();
        List<DepreciationDegree> dbDegrees = depreciationDegreeDao.getAll();

        dbDegrees.forEach(degree -> {
            controllerDegrees.add(new CustomField(degree.getCategory() + "(" + degree.getProductGroup() + ")"));
        });

        return controllerDegrees;
    }

    public Integer getDepreciationDegreeId(DepreciationDegree depreciationDegree) {
        DepreciationDegree tempDepreciationDegree = depreciationDegreeDao.getRecordByCategory(depreciationDegree.getCategory());

        if (tempDepreciationDegree == null)
            return null;

        return tempDepreciationDegree.getId();
    }

    public boolean validateData(Map<String, CustomField> customFieldsByName) {
        boolean handlingResult = true;
        String inputBrandAndModel =
                customFieldsByName.get(BRAND_FIELD_NAME).getFieldValue() + " " +
                        customFieldsByName.get(MODEL_FIELD_NAME).getFieldValue();

        ProductDetails productDetails = productDetailsDao.getProductDetailsByBrandAndModel(inputBrandAndModel);

        if (inputBrandAndModel.equalsIgnoreCase(productDetails.getBrandModel())) {
            customFieldsByName.get(BRAND_FIELD_NAME).setState(State.INVALID);
            customFieldsByName.get(MODEL_FIELD_NAME).setState(State.INVALID);
            customFieldsByName.get(MODEL_FIELD_NAME).setMessage(BRAND_MODEL_EXISTS);
            handlingResult = false;
        }

        return handlingResult;
    }

    public void createProduct(Map<String, CustomField> customFieldsByName) {
        ProductDetails productDetails = new ProductDetails();
        setProductDetails(customFieldsByName, productDetails);
        generateProductsByDetails(customFieldsByName, productDetails);
    }

    private void generateProductsByDetails(Map<String, CustomField> customFieldsByName, ProductDetails productDetails) {
        int count = productDetails.getQuantity();
        int lastProductID = getIDFromInventoryNumber(productDao.getLastRecord().getInventoryNumber());

        while (count != 0) {
            Product product = new Product();
            product.setProductDetails(productDetails);
            product.setRegisteredOn(LocalDate.now());
            product.setAvailable(true);
            product.setInventoryNumber(generateUniqueInventoryNumber(customFieldsByName,
                    count,
                    productDetails.getDepreciationDegree().getCategory(),
                    lastProductID)
            );
            productDao.saveRecord(product);
            count--;
        }

    }

    int getIDFromInventoryNumber(String inventoryNumber) {
        if (inventoryNumber != null) {
            String[] separatedInventoryNumber = inventoryNumber.split("-");
            if (separatedInventoryNumber.length != 0)
                return Integer.parseInt(separatedInventoryNumber[3]) + 1; //ID is at fourth position in the inv number
        }
        return 1;
    }

    private String generateUniqueInventoryNumber(Map<String, CustomField> customFieldsByName, int currentNum, String category, int lastProductID) {
        String invNumber = "";

        invNumber += customFieldsByName.get(BRAND_FIELD_NAME).getFieldValue().substring(0, 1).toUpperCase();
        invNumber += customFieldsByName.get(MODEL_FIELD_NAME).getFieldValue().substring(0, 1).toUpperCase();
        invNumber += "-";
        invNumber += category;
        invNumber += "-";
        invNumber += System.nanoTime();
        invNumber += "-";
        invNumber += lastProductID;
        invNumber += "-";
        invNumber += String.valueOf(currentNum);

        return invNumber;
    }

    private void setProductDetails(Map<String, CustomField> customFieldsByName, ProductDetails productDetails) {
        DepreciationDegree depreciationDegree = new DepreciationDegree();

        setProductDepreciationDegree(customFieldsByName, depreciationDegree);
        productDetails.setDepreciationDegree(productDetailsDao.getDepreciationDegreeReference(getDepreciationDegreeId(depreciationDegree)));
        setProductType(customFieldsByName, productDetails);
        productDetails.setBrandModel(
                customFieldsByName.get(BRAND_FIELD_NAME).getFieldValue() + " " +
                        customFieldsByName.get(MODEL_FIELD_NAME).getFieldValue()
        );
        setProductPriceCurrency(customFieldsByName, productDetails);
        productDetails.setDescription(customFieldsByName.get(DESCRIPTION_FIELD_NAME).getFieldValue());
        productDetails.setInitialPrice(new BigDecimal(customFieldsByName.get(UNIT_PRICE_FIELD_NAME).getFieldValue()));
        productDetails.setCurrentPrice(new BigDecimal(customFieldsByName.get(UNIT_PRICE_FIELD_NAME).getFieldValue()));
        productDetails.setQuantity(Integer.parseInt(customFieldsByName.get(QUANTITY_FIELD_NAME).getFieldValue()));
    }

    private void setProductDepreciationDegree(Map<String, CustomField> customFieldsByName, DepreciationDegree depreciationDegree) {
        if (!customFieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).isNullable()) {
            String categoryAndGroupTogether = customFieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).getFieldValue();
            String[] categoryAndGroup = Pattern.compile("[\\(\\)]").split(categoryAndGroupTogether);
            depreciationDegree.setCategory(categoryAndGroup[0]);
            depreciationDegree.setProductGroup(categoryAndGroup[1]);
        }
    }

    private void setProductType(Map<String, CustomField> customFieldsByName, ProductDetails productDetails) {
        if (customFieldsByName.get(PRODUCT_TYPE_FIELD_NAME).getFieldValue().equals("LTTA"))
            productDetails.setProductType(ProductType.LTTA);
        else
            productDetails.setProductType(ProductType.TA);
    }

    private void setProductPriceCurrency(Map<String, CustomField> customFieldsByName, ProductDetails productDetails) {
        if (customFieldsByName.get(UNIT_PRICE_COMBO_NAME).getFieldValue().equals("BGN"))
            productDetails.setPriceCurrency(PriceCurrency.BGN);
        if (customFieldsByName.get(UNIT_PRICE_COMBO_NAME).getFieldValue().equals("USD"))
            productDetails.setPriceCurrency(PriceCurrency.USD);
        if (customFieldsByName.get(UNIT_PRICE_COMBO_NAME).getFieldValue().equals("EUR"))
            productDetails.setPriceCurrency(PriceCurrency.EUR);
    }

}
