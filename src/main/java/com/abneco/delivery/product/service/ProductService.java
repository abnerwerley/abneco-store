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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

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

    public void registerProduct(ProductForm form) {
        try {
            Optional<Seller> optionalSeller = sellerRepository.findById(form.getSellerId());
            Optional<Address> optionalAddress = addressRepository.findBySellerId(form.getSellerId());
            if (optionalSeller.isEmpty()) {
                throw new ResourceNotFoundException("Seller not found.");
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

    public void save(Product product) {
        if (product.getPrice().compareTo(new BigDecimal("0.0")) <= 0 || product.getPrice() == null) {
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
