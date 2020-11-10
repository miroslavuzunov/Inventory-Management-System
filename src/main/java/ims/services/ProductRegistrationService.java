package ims.services;

import ims.daos.DepreciationDegreeDao;
import ims.daos.ProductDetailsDao;
import ims.entities.DepreciationDegree;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.entities.ScrappingCriteria;
import ims.supporting.CustomField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ims.controllers.resources.RegisterProductControllerResources.*;


public class ProductRegistrationService {
    private final DepreciationDegreeDao depreciationDegreeDao;
    private final ProductDetailsDao productDetailsDao;

    public ProductRegistrationService() {
        this.depreciationDegreeDao = new DepreciationDegreeDao();
        this.productDetailsDao = new ProductDetailsDao();
    }

    public List<CustomField> initializeDepreciationDegree() {
        List<CustomField> controllerDegrees = new ArrayList<>();
        List<DepreciationDegree> dbDegrees = depreciationDegreeDao.getAll();

        dbDegrees.forEach(degree -> {
            controllerDegrees.add(new CustomField(degree.getCategory() + " (" + degree.getProductGroup() + ")"));
        });

        return controllerDegrees;
    }

    public Integer getDepreciationDegreeId(DepreciationDegree depreciationDegree){
        DepreciationDegree tempDepreciationDegree = depreciationDegreeDao.getRecordByCategoryAndGroup(depreciationDegree.getCategory(), depreciationDegree.getProductGroup());

        if (tempDepreciationDegree == null)
            return null;

        return tempDepreciationDegree.getId();
    }

    public void addProduct(Map<String, CustomField> customFieldsByName) {
        DepreciationDegree depreciationDegree = new DepreciationDegree();
        ScrappingCriteria scrappingCriteria = new ScrappingCriteria();
        ProductDetails productDetails = new ProductDetails();
        Product product = new Product();

        if (!customFieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).isNullable()) {
            String categoryAndGroupTogether = customFieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).getFieldValue();
            String[] categoryAndGroup = Pattern.compile("[\\(\\)]").split(categoryAndGroupTogether);
            depreciationDegree.setCategory(categoryAndGroup[0]);
            depreciationDegree.setProductGroup(categoryAndGroup[1]);

            productDetails.setDepreciationDegree(productDetailsDao.getDepreciationDegreeReference(getDepreciationDegreeId(depreciationDegree)));
        }

        if(!customFieldsByName.get(SCRAPPING_CRITERIA_FIELD_NAME).isNullable()) {

        }

        //TODO!!!


    }
}
