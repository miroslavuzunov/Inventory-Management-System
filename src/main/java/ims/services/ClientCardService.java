package ims.services;

import ims.controllers.primary.LoginController;
import ims.daos.*;
import ims.entities.*;
import ims.enums.ProductType;
import ims.enums.RecordStatus;
import ims.enums.Role;
import ims.supporting.Cache;
import ims.supporting.TableProduct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ClientCardService {
    private final UserDao userDao;
    private final ProductClientDao productClientDao;
    private final ProductDetailsDao productDetailsDao;
    private final ProductDao productDao;
    private final PersonInfoDao personInfoDao;
    private List<ProductClient> clientsAllTransactions;
    private static Cache cache = new Cache();

    public ClientCardService() {
        userDao = new UserDao();
        productClientDao = new ProductClientDao();
        productDetailsDao = new ProductDetailsDao();
        productDao = new ProductDao();
        personInfoDao = new PersonInfoDao();
        clientsAllTransactions = new ArrayList<>();
    }

    public List<TableProduct> getClientsProductsByEgn(String egn, StringBuilder clientName) throws NoSuchFieldException { //StringBuilder used because of passing string by reference
        List<TableProduct> tableProducts = new ArrayList<>();

        PersonInfo personInfo = personInfoDao.getRecordByEgn(egn);
        User client;

        client = (User) cache.getCachedObject(egn);

        if (client == null) {
            client = getClientByEgn(egn);
            cache.cacheObject(egn, client);
        }

        List<Product> clientsAllProducts = productClientDao.getClientsAllProducts(client); //Requests optimization
        List<ProductDetails> clientsAllProductDetails = productDetailsDao.getAll(); //Requests optimization

        if (client != null && client.getRole().equals(Role.CLIENT)) {
            clientName.delete(0, clientName.length());
            clientName.append("client: " + personInfo.getFirstName() + " " + personInfo.getLastName());
            String productStatus;

            clientsAllTransactions = productClientDao.getTransactionsByClientAndStatus(client, RecordStatus.ENABLED);

            for (ProductClient productClient : clientsAllTransactions) {
                if (productClient.getProduct().isExisting())
                    productStatus = "Existing";
                else
                    productStatus = "Missing";

                TableProduct tableProduct = new TableProduct();

                tableProduct.setBrand(productClient.getProduct().getProductDetails().getBrandAndModel());
                tableProduct.setInvNum(productClient.getProduct().getInventoryNumber());
                tableProduct.setGivenBy(productClient.getMrt().getPersonInfo().getFirstName() + " " + productClient.getMrt().getPersonInfo().getLastName());
                tableProduct.setGivenOn(productClient.getGivenOn().toString());
                tableProduct.setStatus(productStatus);
                tableProduct.setProduct(productClient.getProduct());

                tableProducts.add(tableProduct);
            }
        } else {
            clientName.append("Client not found");
        }

        return tableProducts;
    }

    public User getClientByEgn(String egn) throws NoSuchFieldException {
        PersonInfo personInfo = personInfoDao.getRecordByEgn(egn);

        return personInfo.getUser();
    }

    public void addAnotherProductToCard(TableProduct selectedProduct, String egn) throws NoSuchFieldException {
        if (selectedProduct != null) {
            ProductClient productClient = new ProductClient();
            List<Product> allProducts = new ArrayList<>();
            Product firstAvailable = new Product();
            User loggedUser = LoginController.getLoggedUser();

            allProducts = productDao.getAll();

            for (Product product : allProducts) {
                if (product.getProductDetails().getBrandAndModel().equals(selectedProduct.getBrand()) && //Checking by brand and model because the field is always unique
                        product.isAvailable() &&
                        product.isExisting() &&
                        product.getStatus().equals(RecordStatus.ENABLED)) {
                    firstAvailable = product;

                    if (product.getProductDetails().getProductType().equals(ProductType.TA))
                        product.setStatus(RecordStatus.DISABLED); //TA should not be returned anymore
                    break;
                }
            }

            User client;

            client = (User) cache.getCachedObject(egn);

            if (client == null) {
                client = getClientByEgn(egn);
                cache.cacheObject(egn, client);
            }

            firstAvailable.setAvailable(false);

            productClient.setProduct(firstAvailable);
            if (loggedUser.getRole().equals(Role.MRT) || loggedUser.getRole().equals(Role.ADMIN))
                productClient.setMrt(loggedUser);
            if (client.getRole().equals(Role.CLIENT))
                productClient.setClient(client);
            productClient.setGivenOn(LocalDate.now());
            productClient.setStatus(RecordStatus.ENABLED);

            productDao.updateRecord(firstAvailable);
            productClientDao.saveRecord(productClient);
            client.getProductClientTransactions().add(productClient);
            userDao.updateRecord(client); //Updates product client transactions
        }
    }

    public void removeSelectedProduct(Product product, String egn) throws NoSuchFieldException {
        User client;

        client = (User) cache.getCachedObject(egn);

        if (client == null) {
            client = getClientByEgn(egn);
            cache.cacheObject(egn, client);
        }

        client.getProductClientTransactions().forEach(transaction -> {
            if (transaction.getProduct().getInventoryNumber().equals(product.getInventoryNumber())) {
                transaction.setStatus(RecordStatus.DISABLED);
                productClientDao.updateRecord(transaction);
            }
        });

        client.getProductClientTransactions().removeIf(new Predicate<ProductClient>() {
            @Override
            public boolean test(ProductClient productClient) {
                return productClient.getProduct().getInventoryNumber().equals(product.getInventoryNumber());
            }
        });

        if (!product.getProductDetails().getProductType().equals(ProductType.TA)) {
            product.setStatus(RecordStatus.ENABLED);
            product.setAvailable(true);
        }
        productDao.updateRecord(product);
        userDao.updateRecord(client);
    }

    public void changeProductStatus(Product product, boolean status) {
        product.setExisting(status);

        productDao.updateRecord(product);
    }


}
