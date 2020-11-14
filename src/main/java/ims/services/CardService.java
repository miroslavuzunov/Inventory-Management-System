package ims.services;

import ims.daos.*;
import ims.entities.PersonInfo;
import ims.entities.Product;
import ims.entities.ProductClient;
import ims.entities.User;
import ims.enums.Role;
import ims.supporting.TableProduct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CardService {
    private final UserDao userDao;
    private final ProductClientDao productClientDao;
    private final ProductDao productDao;
    private final PersonInfoDao personInfoDao;

    public CardService() {
        userDao = new UserDao();
        productClientDao = new ProductClientDao();
        productDao = new ProductDao();
        personInfoDao = new PersonInfoDao();
    }

    public List<TableProduct> getClientsProductsByEgn(String egn, StringBuilder clientsName) { //StringBuilder used because of passing string by reference
        List<TableProduct> controllerProducts = new ArrayList<>();
        List<ProductClient> transactions = new ArrayList<>();

        PersonInfo personInfo = personInfoDao.getRecordByEGN(egn);
        User user = personInfo.getUser();

        if (user != null && user.getRole().equals(Role.CLIENT)) {
            clientsName.append("client: " + personInfo.getFirstName() + " " + personInfo.getLastName());
            transactions = user.getProductClientTransactions();

            transactions.forEach(productClient -> {
                controllerProducts.add(new TableProduct(
                        productClient.getProduct().getProductDetails().getBrandModel(),
                        productClient.getProduct().getInventoryNumber(),
                        productClient.getMrt().getPersonInfo().getFirstName() + " " + productClient.getMrt().getPersonInfo().getLastName(),
                        productClient.getGivenOn().toString()
                ));
            });
        }else{
                clientsName.append("Client not found");
        }
        return controllerProducts;
    }

}
