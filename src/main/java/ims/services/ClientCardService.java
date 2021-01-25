package ims.services;

import ims.daos.*;
import ims.entities.*;
import ims.enums.ProductType;
import ims.enums.RecordStatus;
import ims.enums.Role;
import ims.supporting.TableProduct;
import ims.supporting.UserSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ClientCardService {
    private final UserDao userDao;
    private final ProductClientDao productClientDao;
    private final ProductDao productDao;
    private final PersonInfoDao personInfoDao;
    private List<ProductClient> clientsAllTransactions;

    public ClientCardService() {
        userDao = new UserDao();
        productClientDao = new ProductClientDao();
        productDao = new ProductDao();
        personInfoDao = new PersonInfoDao();
        clientsAllTransactions = new ArrayList<>();
    }

    public List<TableProduct> getClientsProductsByEgn(String egn, StringBuilder clientName) throws NoSuchFieldException { //StringBuilder used because of passing string by reference
        List<TableProduct> tableProducts = new ArrayList<>();
        User client = null;

        PersonInfo personInfo = personInfoDao.getRecordByEgn(egn);
        if (personInfo != null)
         client = personInfo.getUser();

        if (client != null && client.getRole().equals(Role.CLIENT)) {
            modifyClientName(clientName, personInfo, true);
            clientsAllTransactions = productClientDao.getTransactionsByClientAndStatus(client, RecordStatus.ENABLED);
            tableProducts = generateClientsCardTable(clientsAllTransactions, client);
        } else {
            modifyClientName(clientName, personInfo, false);
        }

        return tableProducts;
    }

    private void modifyClientName(StringBuilder clientName, PersonInfo personInfo, boolean isFound) {
        if(isFound) {
            clientName.delete(0, clientName.length());
            clientName.append("client: " + personInfo.getFirstName() + " " + personInfo.getLastName());
        }else {
            clientName.append("Client not found");
        }
    }

    private List<TableProduct> generateClientsCardTable(List<ProductClient> clientsAllTransactions, User client) {
        String productStatus;
        List<TableProduct> tableProducts = new ArrayList<>();
        List<Product> allClientsProducts = productClientDao.getClientsAllCurrentProducts(client); //For select optimization purposes

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

        return tableProducts;
    }

    public User getClientByEgn(String egn) throws NoSuchFieldException {
        PersonInfo personInfo = personInfoDao.getRecordByEgn(egn);

        if (personInfo != null)
            return personInfo.getUser();

        return null;
    }

    public void addAnotherProductToCard(TableProduct selectedProduct, String egn) throws NoSuchFieldException {
        if (selectedProduct != null) {
            List<Product> allProducts = new ArrayList<>();
            Product firstAvailableProduct = new Product();
            User client;

            allProducts = productDao.getAll();
            firstAvailableProduct = getFirstAvailable(allProducts, selectedProduct);
            client = getClientByEgn(egn);

            if (firstAvailableProduct != null)
                addFirstAvailableToClientCard(firstAvailableProduct, client);
        }
    }

    private Product getFirstAvailable(List<Product> allProducts, TableProduct selectedProduct) {
        for (Product product : allProducts) {
            if (isProductFromDbEqualToSelected(product, selectedProduct))
                if (isProductAvailable(product))
                    return product;
        }
        return null;
    }

    private boolean isProductFromDbEqualToSelected(Product product, TableProduct selectedProduct) {
        return product.getProductDetails().getBrandAndModel().equals(selectedProduct.getBrand()); //Checking by brand and model because the field is always unique
    }

    private boolean isProductAvailable(Product product) {
        return product.isAvailable() &&
                product.isExisting() &&
                product.getStatus().equals(RecordStatus.ENABLED);
    }

    private void addFirstAvailableToClientCard(Product firstAvailable, User client) {
        ProductClient productClient = new ProductClient();
        User mrt = UserSession.getLoggedUser();

        if (areClientAndMrtValid(client, mrt)) {
            initializeProductClientTransaction(productClient, mrt, client, firstAvailable);
            saveChangesToDatabase(productClient, client, firstAvailable);
            updateClientChangesForController(client, productClient);
        }
    }

    private boolean areClientAndMrtValid(User client, User loggedUser) {
        return (loggedUser.getRole().equals(Role.MRT) || loggedUser.getRole().equals(Role.ADMIN)) &&
                (client.getRole().equals(Role.CLIENT));
    }

    private void initializeProductClientTransaction(ProductClient productClient, User mrt, User client, Product firstAvailable) {
        firstAvailable.setAvailable(false);
        productClient.setProduct(firstAvailable);
        if (areClientAndMrtValid(client, mrt)) {
            productClient.setMrt(mrt);
            productClient.setClient(client);
        }
        productClient.setGivenOn(LocalDate.now());
        productClient.setStatus(RecordStatus.ENABLED);
    }

    private void saveChangesToDatabase(ProductClient productClient, User client, Product firstAvailable) {
        productDao.updateRecord(firstAvailable);
        productClientDao.saveRecord(productClient);
        userDao.updateRecord(client);
    }

    private void updateClientChangesForController(User client, ProductClient productClient) {
        client.getProductClientTransactions().add(productClient);
    }

    public void removeSelectedProduct(Product product, String egn) throws NoSuchFieldException {
        User client = getClientByEgn(egn);

        removeTransactionFromDb(client, product);
        removeTransactionFromClient(client, product);
        updateProduct(product);

        productDao.updateRecord(product);
        userDao.updateRecord(client);
    }


    private void removeTransactionFromDb(User client, Product product) {
        client.getProductClientTransactions().forEach(transaction -> {
            if (transaction.getProduct().getInventoryNumber().equals(product.getInventoryNumber())) {
                transaction.setStatus(RecordStatus.DISABLED);
                productClientDao.updateRecord(transaction);
            }
        });
    }

    private void removeTransactionFromClient(User client, Product product) {
        client.getProductClientTransactions().removeIf(new Predicate<ProductClient>() {
            @Override
            public boolean test(ProductClient productClient) {
                return productClient.getProduct().getInventoryNumber().equals(product.getInventoryNumber());
            }
        });
    }

    private void updateProduct(Product product) {
        if (!product.getProductDetails().getProductType().equals(ProductType.TA)) {
            product.setStatus(RecordStatus.ENABLED);
            product.setAvailable(true);
        } else
            product.setStatus(RecordStatus.DISABLED); //TA should not be given anymore
    }

    public void changeProductStatus(Product product, boolean status) {
        product.setExisting(status);

        if (status)
            product.setStatus(RecordStatus.ENABLED);
        else
            product.setStatus(RecordStatus.DISABLED);

        productDao.updateRecord(product);
    }


}
