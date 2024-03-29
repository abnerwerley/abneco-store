package com.abneco.store.purchase.service;

import com.abneco.store.exception.RequestException;
import com.abneco.store.exception.ResourceNotFoundException;
import com.abneco.store.product.entity.Product;
import com.abneco.store.product.repository.ProductRepository;
import com.abneco.store.purchase.entity.Purchase;
import com.abneco.store.purchase.json.ProductQuantity;
import com.abneco.store.purchase.json.PurchaseForm;
import com.abneco.store.purchase.json.PurchasePerProduct;
import com.abneco.store.purchase.json.PurchaseResponse;
import com.abneco.store.purchase.repository.PurchasePerProductRepository;
import com.abneco.store.purchase.repository.PurchaseRepository;
import com.abneco.store.purchase.utils.ProductPurchaseConverter;
import com.abneco.store.user.entity.Buyer;
import com.abneco.store.user.entity.NaturalPerson;
import com.abneco.store.user.entity.Seller;
import com.abneco.store.user.repository.BuyerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @InjectMocks
    private PurchaseService service;

    @InjectMocks
    private ProductPurchaseConverter converter;

    @Mock
    private PurchaseRepository repository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BuyerRepository buyerRepository;

    @Mock
    private PurchasePerProductRepository purchasePerProductRepository;

    @Captor
    private ArgumentCaptor<List<Product>> productListCaptor;


    public static final String PRODUCT_NAME = "Playstation 5";
    public static final String PRODUCT_DESCRIPTION = "The PlayStation 5 (PS5) is a home video game console developed by Sony Interactive Entertainment.";
    public static final BigDecimal PRICE = new BigDecimal("5500");
    public static final Seller SELLER = new Seller();
    public static final String PRODUCT_ID = "1";
    public static final String PRODUCT_ID2 = "2";
    public static final Product PRODUCT = new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE, SELLER);
    public static final Product PRODUCT2 = new Product(PRODUCT_ID2, PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE, SELLER);
    public static final String BUYER_ID = "";
    public static final String BUYER_NAME = "Name";
    public static final String EMAIL = "string@email.com";
    public static final String PASSWORD = "12345678";
    public static final Long PHONE_NUMBER = 1112345678L;
    public static final String CPF = "12312312312";
    public static final NaturalPerson NATURAL_PERSON = new NaturalPerson(EMAIL, CPF, BUYER_NAME, PASSWORD, PHONE_NUMBER, false);
    public static final Buyer BUYER = new Buyer(BUYER_ID, NATURAL_PERSON, "", "");
    public static final int QUANTITY5 = 5;
    public static final int QUANTITY4 = 4;
    public static final PurchasePerProduct PURCHASE_PRODUCT1 = new PurchasePerProduct("", PRODUCT, QUANTITY5);
    public static final PurchasePerProduct PURCHASE_PRODUCT2 = new PurchasePerProduct("", PRODUCT2, QUANTITY4);
    public static final List<PurchasePerProduct> PURCHASE_PER_PRODUCT_LIST = List.of(PURCHASE_PRODUCT1, PURCHASE_PRODUCT2);
    public static final List<ProductQuantity> PRODUCT_QUANTITY_LIST = List.of(new ProductQuantity(PRODUCT.getId(), QUANTITY5));
    public static final PurchaseForm PURCHASE_FORM = new PurchaseForm(BUYER.getId(), PRODUCT_QUANTITY_LIST);
    public static final BigDecimal FINAL_PRICE = PRODUCT.getPrice();
    public static final Purchase PURCHASE = new Purchase("", BUYER, PURCHASE_PER_PRODUCT_LIST, FINAL_PRICE, LocalDateTime.now().toString());

    @BeforeEach
    public void setUp() {
        converter = new ProductPurchaseConverter(productRepository);
        this.service = new PurchaseService(repository, productRepository, buyerRepository, purchasePerProductRepository, converter);
    }

    @Test
    void testGetPurchaseById() {
        when(repository.findById(PURCHASE.getId())).thenReturn(Optional.of(PURCHASE));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(PRODUCT));
        when(productRepository.findById(PRODUCT_ID2)).thenReturn(Optional.of(PRODUCT2));
        PurchaseResponse response = service.getPurchaseById(PURCHASE.getId());

        assertEquals(PURCHASE.getId(), response.getPurchaseId());
        assertEquals(PURCHASE.getBuyer().getId(), response.getBuyerId());
        assertEquals(PURCHASE.getFinalPrice(), response.getFinalPrice());
//        assertEquals(PRODUCT_QUANTITY_LIST, response.getProductIdList());
        verify(repository).findById(PURCHASE.getId());
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findById(PURCHASE.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.getPurchaseById(""));
        assertEquals("Purchase not found.", exception.getMessage());
        verify(repository).findById(PURCHASE.getId());
    }

    @Test
    void testGetByIdException() {
        when(repository.findById(PURCHASE.getId())).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.getPurchaseById(""));
        assertEquals("Could not get purchase by id.", exception.getMessage());
        verify(repository).findById(PURCHASE.getId());
    }

    @Test
    void testGetAllPurchases() {
        when(repository.findAll(Sort.by(Sort.Direction.DESC, "purchasedAt"))).thenReturn(List.of(PURCHASE));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(PRODUCT));
        when(productRepository.findById(PRODUCT_ID2)).thenReturn(Optional.of(PRODUCT2));

        List<PurchaseResponse> purchases = service.getAllPurchases();
        assertEquals(1, purchases.size());
        verify(repository).findAll(Sort.by(Sort.Direction.DESC, "purchasedAt"));
    }

    @Test
    void testGetAllException() {
        when(repository.findAll(Sort.by(Sort.Direction.DESC, "purchasedAt"))).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.getAllPurchases());
        assertEquals("Could not get all purchases.", exception.getMessage());
        verify(repository).findAll(Sort.by(Sort.Direction.DESC, "purchasedAt"));
    }


    @Test
    void testGetByBuyer() {
        when(buyerRepository.findById(BUYER.getId())).thenReturn(Optional.of(BUYER));
        when(repository.findByBuyerId(BUYER.getId())).thenReturn(List.of(PURCHASE));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(PRODUCT));
        when(productRepository.findById(PRODUCT_ID2)).thenReturn(Optional.of(PRODUCT2));

        List<PurchaseResponse> purchases = service.getPurchasesByBuyerId(BUYER_ID);
        assertEquals(1, purchases.size());
        verify(repository).findByBuyerId(BUYER.getId());
    }

    @Test
    void testGetByBuyerNotFound() {
        when(buyerRepository.findById(BUYER.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.getPurchasesByBuyerId(BUYER_ID));
        assertEquals("Buyer not found.", exception.getMessage());
        verify(buyerRepository).findById(BUYER_ID);
    }

    @Test
    void testGetByBuyerException() {
        when(buyerRepository.findById(BUYER.getId())).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.getPurchasesByBuyerId(BUYER_ID));
        assertEquals("Could not get purchases by buyer: " + BUYER_ID, exception.getMessage());
    }


    @Test
    void testRegisterPurchase() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));
        when(buyerRepository.findById(BUYER.getId())).thenReturn(Optional.of(BUYER));
        converter.convertToPurchasePerProductList(PRODUCT_QUANTITY_LIST);

        service.registerPurchase(PURCHASE_FORM);

        verify(buyerRepository).findById(BUYER.getId());
        verify(repository).save(any(Purchase.class));
        verify(buyerRepository).save(any(Buyer.class));
    }

    @Test
    void testRegisterPurchaseBuyerNotFound() {
        when(buyerRepository.findById(BUYER.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.registerPurchase(PURCHASE_FORM));
        assertEquals("Buyer not found.", exception.getMessage());

        verify(buyerRepository).findById(BUYER.getId());

        verify(repository, never()).save(any(Purchase.class));
        verify(buyerRepository, never()).save(any(Buyer.class));
        verify(productRepository, never()).saveAll(productListCaptor.capture());
    }

    @Test
    void testRegisterPurchaseException() {
        when(buyerRepository.findById(BUYER.getId())).thenThrow(RuntimeException.class);

        Exception exception = assertThrows(RequestException.class, () -> service.registerPurchase(PURCHASE_FORM));
        assertEquals("Could not purchase.", exception.getMessage());

        verify(buyerRepository).findById(BUYER.getId());

        verify(repository, never()).save(any(Purchase.class));
        verify(buyerRepository, never()).save(any(Buyer.class));
        verify(productRepository, never()).saveAll(productListCaptor.capture());
    }

    @Test
    void deletePurchase() {
        when(repository.findById(PURCHASE.getId())).thenReturn(Optional.of(PURCHASE));
        service.deletePurchase(PURCHASE.getId());

        verify(repository).findById(PURCHASE.getId());
        verify(repository).deleteById(PURCHASE.getId());
    }

    @Test
    void deletePurchaseNotFound() {
        when(repository.findById(PURCHASE.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deletePurchase(""));
        assertEquals("Purchase not found.", exception.getMessage());

        verify(repository).findById(PURCHASE.getId());
        verify(repository, never()).deleteById(PURCHASE.getId());
    }

    @Test
    void deletePurchaseException() {
        when(repository.findById(PURCHASE.getId())).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.deletePurchase(""));
        assertEquals("Could not delete purchase.", exception.getMessage());

        verify(repository).findById(PURCHASE.getId());
        verify(repository, never()).deleteById(PURCHASE.getId());
    }

    @Test
    void testpurchasePerProduct() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));
        BigDecimal price = service.purchasePerProductPrice((PURCHASE_PRODUCT1));
        assertEquals(new BigDecimal("27500"), price);
    }

    @Test
    void testpurchasePerProductNotFound() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.purchasePerProductPrice((PURCHASE_PRODUCT1)));
        assertEquals("Product not found.", exception.getMessage());
    }

    @Test
    void testCalculateFinalPrice() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));
        when(productRepository.findById(PRODUCT2.getId())).thenReturn(Optional.of(PRODUCT2));
        BigDecimal price = service.calculateFinalPrice(PURCHASE_PER_PRODUCT_LIST);
        assertEquals(new BigDecimal("49500"), price);
    }
}
