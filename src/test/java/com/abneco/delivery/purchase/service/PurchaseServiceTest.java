package com.abneco.delivery.purchase.service;

import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.purchase.entity.Purchase;
import com.abneco.delivery.purchase.json.PurchaseForm;
import com.abneco.delivery.purchase.repository.PurchaseRepository;
import com.abneco.delivery.purchase.service.PurchaseService;
import com.abneco.delivery.user.entity.Buyer;
import com.abneco.delivery.user.entity.NaturalPerson;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.repository.BuyerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

    @Mock
    private PurchaseRepository repository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BuyerRepository buyerRepository;

    @Captor
    private ArgumentCaptor<List<Product>> productListCaptor;


    public static final String PRODUCT_NAME = "Playstation 5";
    public static final String PRODUCT_DESCRIPTION = "The PlayStation 5 (PS5) is a home video game console developed by Sony Interactive Entertainment.";
    public static final BigDecimal PRICE = new BigDecimal("5.500");
    public static final Seller SELLER = new Seller();
    public static final String PRODUCT_ID = "";
    public static final Product PRODUCT = new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE, SELLER);
    public static final String BUYER_ID = "";
    public static final String BUYER_NAME = "Name";
    public static final String EMAIL = "string@email.com";
    public static final String PASSWORD = "12345678";
    public static final Long PHONE_NUMBER = 1112345678L;
    public static final String CPF = "12312312312";
    public static final NaturalPerson NATURAL_PERSON = new NaturalPerson(EMAIL, CPF, BUYER_NAME, PASSWORD, PHONE_NUMBER, false);
    public static final Buyer BUYER = new Buyer(BUYER_ID, NATURAL_PERSON, "", "");
    public static final int QUANTITY = 5;
    public static final PurchaseForm PURCHASE_FORM = new PurchaseForm(BUYER.getId(), List.of(PRODUCT.getId()), QUANTITY);


    @Test
    void testRegisterPurchase() {
        List<String> productsId = List.of(PRODUCT.getId());
        when(productRepository.findAllById(productsId)).thenReturn(List.of(PRODUCT));
        when(buyerRepository.findById(BUYER.getId())).thenReturn(Optional.of(BUYER));

        service.registerPurchase(PURCHASE_FORM);

        verify(productRepository).findAllById(productsId);
        verify(buyerRepository).findById(BUYER.getId());

        verify(repository).save(any(Purchase.class));
        verify(buyerRepository).save(any(Buyer.class));
        verify(productRepository).saveAll(productListCaptor.capture());
    }

    @Test
    void testRegisterPurchaseProductNotFound() {
        List<String> productsId = List.of(PRODUCT.getId());
        when(productRepository.findAllById(productsId)).thenReturn(List.of());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.registerPurchase(PURCHASE_FORM));
        assertEquals("One or more products not found.", exception.getMessage());

        verify(productRepository).findAllById(productsId);

        verify(repository, never()).save(any(Purchase.class));
        verify(buyerRepository, never()).save(any(Buyer.class));
        verify(productRepository, never()).saveAll(productListCaptor.capture());
    }

    @Test
    void testRegisterPurchaseBuyerNotFound() {
        List<String> productsId = List.of(PRODUCT.getId());
        when(productRepository.findAllById(productsId)).thenReturn(List.of(PRODUCT));
        when(buyerRepository.findById(BUYER.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.registerPurchase(PURCHASE_FORM));
        assertEquals("Buyer not found.", exception.getMessage());

        verify(productRepository).findAllById(productsId);
        verify(buyerRepository).findById(BUYER.getId());

        verify(repository, never()).save(any(Purchase.class));
        verify(buyerRepository, never()).save(any(Buyer.class));
        verify(productRepository, never()).saveAll(productListCaptor.capture());
    }
}
