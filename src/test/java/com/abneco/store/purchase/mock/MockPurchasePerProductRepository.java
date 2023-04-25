package com.abneco.store.purchase.mock;

import com.abneco.store.purchase.json.PurchasePerProduct;
import com.abneco.store.purchase.repository.PurchasePerProductRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class MockPurchasePerProductRepository implements PurchasePerProductRepository {

    @Override
    public Optional<PurchasePerProduct> findByProductId(String productId) {
        return Optional.empty();
    }

    @Override
    public List<PurchasePerProduct> findAll() {
        return null;
    }

    @Override
    public List<PurchasePerProduct> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PurchasePerProduct> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<PurchasePerProduct> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(PurchasePerProduct purchasePerProduct) {

    }

    @Override
    public void deleteAll(Iterable<? extends PurchasePerProduct> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends PurchasePerProduct> S save(S s) {
        return null;
    }

    @Override
    public <S extends PurchasePerProduct> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<PurchasePerProduct> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends PurchasePerProduct> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<PurchasePerProduct> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PurchasePerProduct getOne(String s) {
        return null;
    }

    @Override
    public <S extends PurchasePerProduct> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PurchasePerProduct> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PurchasePerProduct> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PurchasePerProduct> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PurchasePerProduct> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PurchasePerProduct> boolean exists(Example<S> example) {
        return false;
    }
}
