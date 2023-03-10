package com.abneco.delivery.user.mock;

import com.abneco.delivery.user.entity.Seller;
import com.abneco.delivery.user.repository.SellerRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

//this class is only used in test
public class MockSellerRepository implements SellerRepository {

    @Override
    public Optional<Seller> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Seller> findByCnpj(String cnpj) {
        return Optional.empty();
    }

    public Optional<Seller> findByEmail() {
        return Optional.of(new Seller());
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }

    @Override
    public List<Seller> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Seller> findAll(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<Seller> findAllById(Iterable<String> iterable) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {
        //method won't be used
    }

    @Override
    public void delete(Seller seller) {
        //method won't be used
    }

    @Override
    public void deleteAll(Iterable<? extends Seller> iterable) {
        //method won't be used
    }

    @Override
    public void deleteAll() {
        //method won't be used
    }

    @Override
    public <S extends Seller> S save(S s) {
        return null;
    }

    @Override
    public <S extends Seller> List<S> saveAll(Iterable<S> iterable) {
        return List.of();
    }

    @Override
    public Optional<Seller> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public void flush() {
        //method won't be used
    }

    @Override
    public <S extends Seller> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Seller> iterable) {
        //method won't be used
    }

    @Override
    public void deleteAllInBatch() {
        //method won't be used
    }

    @Override
    public Seller getOne(String s) {
        return null;
    }

    @Override
    public <S extends Seller> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Seller> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Seller> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Seller> Page<S> findAll(Example<S> example, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public <S extends Seller> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Seller> boolean exists(Example<S> example) {
        return false;
    }
}
