package ims.daos;

import ims.entities.Product;

import javax.persistence.Query;
import java.util.List;


public class ProductDao extends AbstractDao<Product>{
    public ProductDao() {
        super(Product.class);
    }

    public Product getLastRecord(){
        Query query = manager.createQuery("select row from Product row order by row.id desc");

        List<Product> products = query.getResultList();

        if(!products.isEmpty())
            return products.get(0);
        return new Product();
    }
}
