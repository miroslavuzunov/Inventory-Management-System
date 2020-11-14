package ims.daos;

import ims.entities.Product;
import ims.entities.ProductClient;

import java.util.List;

public class ProductClientDao extends AbstractDao<ProductClient> {
    public ProductClientDao() {
        super(ProductClient.class);
    }

}
