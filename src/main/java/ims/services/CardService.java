package ims.services;

import ims.controllers.primary.LoginController;
import ims.daos.*;
import ims.entities.*;
import ims.enums.ProductType;
import ims.enums.RecordStatus;
import ims.enums.Role;
import ims.supporting.TableProduct;

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
            String productStatus = "";

            if (transactions.isEmpty())
                transactions = client.getProductClientTransactions();


            for (ProductClient productClient : transactions) {
                if (isDateInPeriod(startDate, endDate, productClient.getGivenOn()) &&
                        productClient.getStatus().equals(RecordStatus.ENABLED)) {

                    if (productClient.getProduct().isExisting())
                        productStatus = "Existing";
                    else
                        productStatus = "Missing";

                    tableProducts.add(new TableProduct(
                            productClient.getProduct().getProductDetails().getBrandAndModel(),
                            productClient.getProduct().getInventoryNumber(),
                            productClient.getMrt().getPersonInfo().getFirstName() + " " + productClient.getMrt().getPersonInfo().getLastName(),
                            productClient.getGivenOn().toString(),
                            productStatus,
                            productClient.getProduct()
                    ));
                }
            }
        } else {
            clientsName.append("Client not found");
        }

        return tableProducts;
    }

    public boolean isDateInPeriod(LocalDate startDate, LocalDate endDate, LocalDate givenOn){
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList()).contains(givenOn); // Checks if the transaction date is in the specified period (inclusive)
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

        if (!product.getProductDetails().getProductType().equals(ProductType.TA)) {
            product.setStatus(RecordStatus.ENABLED);
            product.setAvailable(true);
        }
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

    public void addAnotherProductToCard(TableProduct selectedProduct) {
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
