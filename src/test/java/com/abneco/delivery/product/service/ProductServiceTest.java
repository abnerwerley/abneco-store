package com.abneco.delivery.product.service;

import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.exception.RequestException;
import com.abneco.delivery.exception.ResourceNotFoundException;
import com.abneco.delivery.product.entity.Product;
import com.abneco.delivery.product.json.ProductForm;
import com.abneco.delivery.product.json.ProductResponse;
import com.abneco.delivery.product.json.UpdateProductForm;
import com.abneco.delivery.product.repository.ProductRepository;
import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.repository.SellerRepository;
import org.junit.jupiter.api.Assertions;
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
    public static final String ID = "kjahskdfakjhgdkjfahgdfa";
    public static final Seller SELLER = new Seller();
    public static final Product PRODUCT = new Product(NAME, DESCRIPTION, PRICE, SELLER);

    public static final String NEW_NAME = "New product name";
    public static final String NEW_DESCRIPTION = "New description, that has to be longer than 10 char.";
    public static final BigDecimal NEW_PRICE = new BigDecimal("100.0");

    @Test
    void testGetProductById() {
        when(repository.findById(ID)).thenReturn(Optional.of(PRODUCT));
        ProductResponse response = service.getProductById(ID);
        assertEquals(PRODUCT.getName(), response.getName());
        assertEquals(PRODUCT.getDescription(), response.getDescription());
        assertEquals(PRODUCT.getPrice(), response.getPrice());
        assertEquals(PRODUCT.getId(), response.getProductId());

        verify(repository).findById(ID);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.getProductById(ID));
        assertEquals("Product not found.", exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    void testGetProductByIdException() {
        when(repository.findById(ID)).thenThrow(RuntimeException.class);
        Exception exception = Assertions.assertThrows(RequestException.class, () -> service.getProductById(ID));
        assertEquals("Could not get product by id.", exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    void testGetAllProducts() {
        when(repository.findAll()).thenReturn(List.of(PRODUCT));
        List<ProductResponse> response = service.getAllProducts();
        assertEquals(1, response.size());
        verify(repository).findAll();
    }

    @Test
    void testGetAllProductsException() {
        when(repository.findAll()).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.getAllProducts());
        assertEquals("Could not get all products.", exception.getMessage());
        verify(repository).findAll();
    }

    @Test
    void testGetProductsBySeller() {
        when(repository.findBySellerId(SELLER_ID)).thenReturn(List.of(PRODUCT));
        when(sellerRepository.findById(SELLER_ID)).thenReturn(Optional.of(SELLER));
        List<ProductResponse> response = service.getProductsBySeller(SELLER_ID);
        assertEquals(1, response.size());
        verify(repository).findBySellerId(SELLER_ID);
    }

    @Test
    void testGetProductsBySellerNotFound() {
        when(sellerRepository.findById(SELLER_ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.getProductsBySeller(SELLER_ID));
        assertEquals("Seller not found.", exception.getMessage());
    }

    @Test
    void testGetProductsBySellerException() {
        when(sellerRepository.findById(SELLER_ID)).thenReturn(Optional.of(SELLER));
        when(repository.findBySellerId(SELLER_ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.getProductsBySeller(SELLER_ID));
        assertEquals("Could not get products by seller.", exception.getMessage());
    }

    @Test
    void testRegisterProduct() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findByUserId(form.getSellerId())).thenReturn(Optional.of(new Address()));
        service.registerProduct(form);
        verify(sellerRepository).findById(form.getSellerId());
        verify(repository).save(any(Product.class));
    }

    @Test
    void testRegisterProductSellerNotFound() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.empty());
        when(addressRepository.findByUserId(form.getSellerId())).thenReturn(Optional.of(new Address()));
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.registerProduct(form));
        assertEquals("Seller not found.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductAddressNotFound() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findByUserId(form.getSellerId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.registerProduct(form));
        assertEquals("One cannot register a product if you have no address.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductNullNameValidation() {
        ProductForm form = new ProductForm(null, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findByUserId(form.getSellerId())).thenReturn(Optional.of(new Address()));

        Exception exception = assertThrows(RequestException.class, () -> service.registerProduct(form));
        assertEquals("Product name must not be null.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductDescriptionValidation() {
        ProductForm form = new ProductForm(NAME, SHORT_DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findByUserId(form.getSellerId())).thenReturn(Optional.of(new Address()));

        Exception exception = assertThrows(RequestException.class, () -> service.registerProduct(form));
        assertEquals("Product Description must be at least 10 char long.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductPriceValidation() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE_ZERO, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenReturn(Optional.of(new Seller()));
        when(addressRepository.findByUserId(form.getSellerId())).thenReturn(Optional.of(new Address()));

        Exception exception = assertThrows(RequestException.class, () -> service.registerProduct(form));
        assertEquals("Product price must neither be below 0.0, nor null.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProductException() {
        ProductForm form = new ProductForm(NAME, DESCRIPTION, PRICE, SELLER_ID);
        when(sellerRepository.findById(form.getSellerId())).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.registerProduct(form));
        assertEquals("Could not register product.", exception.getMessage());

        verify(sellerRepository).findById(form.getSellerId());
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        UpdateProductForm form = new UpdateProductForm(ID, NEW_NAME, NEW_DESCRIPTION, NEW_PRICE);
        when(repository.findById(ID)).thenReturn(Optional.of(PRODUCT));
        service.updateProduct(form);
        verify(repository).findById(ID);
        verify(repository).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        UpdateProductForm form = new UpdateProductForm(ID, NEW_NAME, NEW_DESCRIPTION, NEW_PRICE);
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateProduct(form));
        assertEquals("Product not found.", exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProductShortDescription() {
        UpdateProductForm form = new UpdateProductForm(ID, NEW_NAME, NEW_DESCRIPTION, BigDecimal.ZERO);
        when(repository.findById(ID)).thenReturn(Optional.of(PRODUCT));
        Exception exception = assertThrows(RequestException.class, () -> service.updateProduct(form));
        assertEquals("Product price must neither be below 0.0, nor null.", exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProductException() {
        UpdateProductForm form = new UpdateProductForm(ID, NEW_NAME, NEW_DESCRIPTION, NEW_PRICE);
        when(repository.findById(ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.updateProduct(form));
        assertEquals("Could not update product.", exception.getMessage());
        verify(repository).findById(ID);
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProductById() {
        when(repository.findById(ID)).thenReturn(Optional.of(PRODUCT));
        service.deleteProductById(ID);
        verify(repository).findById(ID);
        verify(repository).deleteById(ID);
    }

    @Test
    void testDeleteProductByIdNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteProductById(ID));
        assertEquals("Product not found.", exception.getMessage());
    }

    @Test
    void testDeleteProductByIdException() {
        when(repository.findById(ID)).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(RequestException.class, () -> service.deleteProductById(ID));
        assertEquals("Could not delete product by id.", exception.getMessage());
    }
}
