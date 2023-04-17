package com.abneco.delivery.purchase.utils;

import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.purchase.json.ProductQuantity;
import com.abneco.delivery.purchase.json.PurchasePerProduct;
import com.abneco.delivery.user.entity.Seller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConverterTest {

    @InjectMocks
    private ProductPurchaseConverter converter;
    @Mock
    private ProductRepository productRepository;

    public static final String PRODUCT_NAME = "Playstation 5";
    public static final String PRODUCT_DESCRIPTION = "The PlayStation 5 (PS5) is a home video game console developed by Sony Interactive Entertainment.";
    public static final BigDecimal PRICE = new BigDecimal("5500");
    public static final Seller SELLER = new Seller();
    public static final String PRODUCT_ID = "1";
    public static final String PRODUCT_ID2 = "2";
    public static final Product PRODUCT = new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE, SELLER);
    public static final Product PRODUCT2 = new Product(PRODUCT_ID2, PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE, SELLER);
    public static final int QUANTITY5 = 5;
    public static final int QUANTITY4 = 4;
    public static final PurchasePerProduct PURCHASE_PRODUCT1 = new PurchasePerProduct(PRODUCT, QUANTITY5);
    public static final PurchasePerProduct PURCHASE_PRODUCT2 = new PurchasePerProduct(PRODUCT2, QUANTITY4);
    public static final List<PurchasePerProduct> PURCHASE_PER_PRODUCT_LIST = List.of(PURCHASE_PRODUCT1, PURCHASE_PRODUCT2);
    public static final List<String> PRODUCT_ID_LIST = List.of("1", "2");
    public static final ProductQuantity PRODUCT_QUANTITY1 = new ProductQuantity(PRODUCT.getId(), QUANTITY5);
    public static final ProductQuantity PRODUCT_QUANTITY2 = new ProductQuantity(PRODUCT2.getId(), QUANTITY4);
    public static final List<ProductQuantity> PRODUCT_QUANTITY_LIST = List.of(PRODUCT_QUANTITY1, PRODUCT_QUANTITY2);
    public static final String PRODUCT_NOT_FOUND = "Product not found.";

    @Test
    void test_convert_to_product_quantity() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(PRODUCT));
        when(productRepository.findById(PRODUCT_ID2)).thenReturn(Optional.of(PRODUCT2));
        List<ProductQuantity> response = converter.convertToProductQuantity(PURCHASE_PER_PRODUCT_LIST);

        assertEquals(PRODUCT_QUANTITY1.getProductId(), response.get(0).getProductId());
        assertEquals(PRODUCT_QUANTITY1.getQuantity(), response.get(0).getQuantity());

        assertEquals(PRODUCT_QUANTITY2.getProductId(), response.get(1).getProductId());
        assertEquals(PRODUCT_QUANTITY2.getQuantity(), response.get(1).getQuantity());

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).findById(PRODUCT_ID);
    }

    @Test
    void test_convert_to_product_quantity_not_found() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> converter.convertToProductQuantity(PURCHASE_PER_PRODUCT_LIST));
        assertEquals(PRODUCT_NOT_FOUND, exception.getMessage());
        verify(productRepository).findById(PRODUCT_ID);
    }

    @Test
    void test_convert_to_purchase_per_product_list() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(PRODUCT));
        when(productRepository.findById(PRODUCT_ID2)).thenReturn(Optional.of(PRODUCT2));
        List<PurchasePerProduct> response = converter.convertToPurchasePerProductList(PRODUCT_QUANTITY_LIST);

        assertEquals(PURCHASE_PRODUCT1.getProduct().getId(), response.get(0).getProduct().getId());
        assertEquals(PURCHASE_PRODUCT1.getQuantity(), response.get(0).getQuantity());

        assertEquals(PURCHASE_PRODUCT2.getProduct().getId(), response.get(1).getProduct().getId());
        assertEquals(PURCHASE_PRODUCT2.getQuantity(), response.get(1).getQuantity());

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).findById(PRODUCT_ID2);
    }

    @Test
    void test_convert_to_purchase_per_product_list_not_found() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> converter.convertToPurchasePerProductList(PRODUCT_QUANTITY_LIST));
        assertEquals(PRODUCT_NOT_FOUND, exception.getMessage());
        verify(productRepository).findById(PRODUCT_ID);

    }

    @Test
    void test_get_ids_from_product_quantity_list() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(PRODUCT));
        when(productRepository.findById(PRODUCT_ID2)).thenReturn(Optional.of(PRODUCT2));

        List<String> response = converter.getProductIdsFromProductQuantityList(PRODUCT_QUANTITY_LIST);
        assertEquals(2, response.size());
        assertEquals(PRODUCT_ID_LIST, response);

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).findById(PRODUCT_ID);
    }

    @Test
    void test_get_ids_from_purchase_per_product_list() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(PRODUCT));
        when(productRepository.findById(PRODUCT_ID2)).thenReturn(Optional.of(PRODUCT2));

        List<String> response = converter.getProductIdsFromPurchasePerProductList(PURCHASE_PER_PRODUCT_LIST);
        assertEquals(2, response.size());
        assertEquals(PRODUCT_ID_LIST, response);

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).findById(PRODUCT_ID);
    }

}
