package com.abneco.delivery.product.service;

import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.json.ProductForm;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private AddressRepository addressRepository;

    public static final String NAME = "Playstation 5";
    public static final String DESCRIPTION = "The PlayStation 5 (PS5) is a home video game console developed by Sony Interactive Entertainment.";
    public static final String SHORT_DESCRIPTION = "The PS5.";
    public static final BigDecimal PRICE = new BigDecimal("5.500");
    public static final BigDecimal PRICE_ZERO = new BigDecimal("0.0");
    public static final String SELLER_ID = "qiwoeiruyaosjdhkajdhfasdf";

    @Test
    void testRegisterProduct() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findBySellerId(form.getSellerId())).thenReturn(Optional.of(new Address()));
        service.registerProduct(form);
        verify(sellerRepository).findById(form.getSellerId());
        verify(repository).save(any(Product.class));
    }

    @Test
    void testRegisterProductSellerNotFound() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.empty());
        when(addressRepository.findBySellerId(form.getSellerId())).thenReturn(Optional.of(new Address()));
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.registerProduct(form));
        assertNotNull(exception);
        assertEquals("Seller not found.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductAddressNotFound() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findBySellerId(form.getSellerId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.registerProduct(form));
        assertNotNull(exception);
        assertEquals("One cannot register a product if you have no address.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductNullNameValidation() {
        ProductForm form = new ProductForm(null, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findBySellerId(form.getSellerId())).thenReturn(Optional.of(new Address()));

        Exception exception = assertThrows(RequestException.class, () -> service.registerProduct(form));
        assertNotNull(exception);
        assertEquals("Product name must not be null.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductDescriptionValidation() {
        ProductForm form = new ProductForm(NAME, SHORT_DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findBySellerId(form.getSellerId())).thenReturn(Optional.of(new Address()));

        Exception exception = assertThrows(RequestException.class, () -> service.registerProduct(form));
        assertNotNull(exception);
        assertEquals("Product Description must be at least 10 char long.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductPriceValidation() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE_ZERO, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findBySellerId(form.getSellerId())).thenReturn(Optional.of(new Address()));

        Exception exception = assertThrows(RequestException.class, () -> service.registerProduct(form));
        assertNotNull(exception);
        assertEquals("Product price must neither be below 0.0, nor null.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductException() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.registerProduct(form));
        assertNotNull(exception);
        assertEquals("Could not register product.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }
}
