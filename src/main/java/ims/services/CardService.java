package ims.services;

import ims.daos.*;
import ims.entities.*;
import ims.enums.RecordStatus;
import ims.enums.Role;
import ims.supporting.TableProduct;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CardService {
    private final UserDao userDao;
    private final ProductClientDao productClientDao;
    private final ProductDetailsDao productDetailsDao;
    private final ProductDao productDao;
    private final PersonInfoDao personInfoDao;
    private User client;
    List<ProductClient> transactions;

    public CardService() {
        userDao = new UserDao();
        productClientDao = new ProductClientDao();
        productDetailsDao = new ProductDetailsDao();
        productDao = new ProductDao();
        personInfoDao = new PersonInfoDao();
        transactions = new ArrayList<>();
    }

    public List<TableProduct> getClientsProductsByEgnAndPeriod(String egn, StringBuilder clientsName, LocalDate startDate, LocalDate endDate) { //StringBuilder used because of passing string by reference
        List<TableProduct> tableProducts = new ArrayList<>();

        PersonInfo personInfo = personInfoDao.getRecordByEgn(egn);
        client = personInfo.getUser();

        if (client != null && client.getRole().equals(Role.CLIENT)) {
            clientsName.append("client: " + personInfo.getFirstName() + " " + personInfo.getLastName());
            String status = "";

            if (transactions.isEmpty())
                transactions = client.getProductClientTransactions();


            for (ProductClient productClient : transactions) {
                if (startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList()).contains(productClient.getGivenOn()) && // Checks if the transaction date is in the specified period (inclusive)
                        productClient.getStatus().equals(RecordStatus.ENABLED)) {

                    if (productClient.getProduct().isExisting())
                        status = "Existing";
                    else
                        status = "Missing";

                    tableProducts.add(new TableProduct(
                            productClient.getProduct().getProductDetails().getBrandAndModel(),
                            productClient.getProduct().getInventoryNumber(),
                            productClient.getMrt().getPersonInfo().getFirstName() + " " + productClient.getMrt().getPersonInfo().getLastName(),
                            productClient.getGivenOn().toString(), productClient.getProduct(),
                            status
                    ));
                }
            }
        } else {
            clientsName.append("Client not found");
        }

//        ProductClient productClient = new ProductClient();
//        PersonInfo personInfo2 = personInfoDao.getRecordByEgn("9910115768");
//        User mrt = personInfo2.getUser();
//        Product product = productDao.getLastRecord();
//
//        productClient.setProduct(product);
//        productClient.setMrt(mrt);
//        productClient.setClient(client);
//        productClient.setGivenOn(LocalDate.now());
//        productClient.setStatus(RecordStatus.ENABLED);
//
//        productClientDao.saveRecord(productClient);

        return tableProducts;
    }

    public void removeSelectedProduct(Product product) {
        client.getProductClientTransactions().forEach(transaction -> {
            if (transaction.getProduct().getInventoryNumber().equals(product.getInventoryNumber())) {
                transaction.setStatus(RecordStatus.DISABLED);
            }
        });

        client.getProductClientTransactions().removeIf(new Predicate<ProductClient>() {
            @Override
            public boolean test(ProductClient productClient) {
                return productClient.getProduct().getInventoryNumber().equals(product.getInventoryNumber());
            }
        });

        product.setAvailable(true);
        productDao.updateRecord(product);
        userDao.updateRecord(client);
    }

    public List<ProductDetails> getAllProductsDetails() {
        return productDetailsDao.getAll();
    }

    public void changeProductStatus(Product product, boolean status) {
        product.setExisting(status);

        productDao.updateRecord(product);
    }
}
