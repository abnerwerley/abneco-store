package com.abneco.store.purchase.mock;

import com.abneco.store.purchase.entity.Purchase;
import com.abneco.store.purchase.repository.PurchaseRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class MockPurchaseRepository implements PurchaseRepository {

    @Override
    public List<Purchase> findByBuyerId(String buyerId) {
        return null;
    }

    @Override
    public List<Purchase> findAll() {
        return null;
    }

    @Override
    public List<Purchase> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Purchase> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Purchase> findAllById(Iterable<String> iterable) {
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
    public void delete(Purchase purchase) {

    }

    @Override
    public void deleteAll(Iterable<? extends Purchase> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Purchase> S save(S s) {
        return null;
    }

    @Override
    public <S extends Purchase> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Purchase> findById(String s) {
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
    public <S extends Purchase> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Purchase> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Purchase getOne(String s) {
        return null;
    }

    @Override
    public <S extends Purchase> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Purchase> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Purchase> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Purchase> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Purchase> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Purchase> boolean exists(Example<S> example) {
        return false;
    }
}
