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
            Optional<Product> optionalProduct = repository.findById(productId);
            return optionalProduct.map(Product::toResponse).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));

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
            Optional<Seller> optionalSeller = sellerRepository.findById(form.getSellerId());
            Optional<Address> optionalAddress = addressRepository.findByUserId(form.getSellerId());
            if (optionalSeller.isEmpty()) {
                throw new ResourceNotFoundException(SELLER_NOT_FOUND);
            }
            if (optionalAddress.isEmpty()) {
                throw new ResourceNotFoundException("One cannot register a product if you have no address.");
            }
            save(form.toEntity(optionalSeller.get()));

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
            Optional<Product> optionalProduct = repository.findById(form.getProductId());
            if (optionalProduct.isEmpty()) {
                throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
            }

            Product product = optionalProduct.get();
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
            Optional<Product> optionalProduct = repository.findById(productId);
            if (optionalProduct.isEmpty()) {
                throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
            }

            repository.deleteById(productId);

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
