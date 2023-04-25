package com.abneco.store.product.service;

import com.abneco.store.address.entity.Address;
import com.abneco.store.address.repository.AddressRepository;
import com.abneco.store.exception.RequestException;
import com.abneco.store.exception.ResourceNotFoundException;
import com.abneco.store.product.entity.Product;
import com.abneco.store.product.json.ProductForm;
import com.abneco.store.product.json.ProductResponse;
import com.abneco.store.product.json.UpdateProductForm;
import com.abneco.store.product.repository.ProductRepository;
import com.abneco.store.user.entity.Seller;
import com.abneco.store.user.repository.SellerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private AddressRepository addressRepository;

    public static final String SELLER_NOT_FOUND = "Seller not found.";
    public static final String PRODUCT_NOT_FOUND = "Product not found.";

    public ProductResponse getProductById(String productId) {
        try {
            Product product = repository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
            return product.toResponse();

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not get product by id.");
        }
    }

    public List<ProductResponse> getAllProducts() {
        try {
            return repository.findAll().stream()
                    .map(Product::toResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not get all products.");
        }
    }

    public List<ProductResponse> getProductsBySeller(String sellerId) {
        try {
            Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);
            if (optionalSeller.isEmpty()) {
                throw new ResourceNotFoundException(SELLER_NOT_FOUND);
            }

            return repository.findBySellerId(sellerId).stream()
                    .map(Product::toResponse)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not get all products. " + e.getMessage());
            throw new RequestException("Could not get products by seller.");
        }
    }

    public void registerProduct(ProductForm form) {
        try {
            Seller seller = sellerRepository.findById(form.getSellerId()).orElseThrow(() -> new ResourceNotFoundException(SELLER_NOT_FOUND));
            Optional<Address> optionalAddress = addressRepository.findByUserId(form.getSellerId());

            if (optionalAddress.isEmpty()) {
                throw new ResourceNotFoundException("One cannot register a product if you have no address.");
            }
            save(form.toEntity(seller));

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not register product. " + e.getMessage());
            throw new RequestException("Could not register product.");
        }
    }

    public void updateProduct(UpdateProductForm form) {
        try {
            Product product = repository.findById(form.getProductId()).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));

            product.setName(form.getName());
            product.setDescription(form.getDescription());
            product.setPrice(form.getPrice());
            save(product);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());

        } catch (Exception e) {
            log.error("Could not update product. " + e.getMessage());
            throw new RequestException("Could not update product.");
        }

    }

    public void deleteProductById(String productId) {
        try {
            Product product = repository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));

            repository.delete(product);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RequestException("Could not delete product by id.");
        }

    }

    private void save(Product product) {
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RequestException("Product price must neither be below 0.0, nor null.");
        }
        if (product.getDescription().length() < 10) {
            throw new RequestException("Product Description must be at least 10 char long.");
        }
        if (product.getName() == null) {
            throw new RequestException("Product name must not be null.");
        }
        repository.save(product);
    }
}
