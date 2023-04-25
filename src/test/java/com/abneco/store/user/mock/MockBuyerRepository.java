package com.abneco.store.user.mock;

import com.abneco.store.user.entity.Buyer;
import com.abneco.store.user.repository.BuyerRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class MockBuyerRepository implements BuyerRepository {

    @Override
    public Optional<Buyer> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Buyer> findByCpf(String cpf) {
        return Optional.empty();
    }

    @Override
    public List<Buyer> findAll() {
        return null;
    }

    @Override
    public List<Buyer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Buyer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Buyer> findAllById(Iterable<String> iterable) {
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
    public void delete(Buyer buyer) {

    }

    @Override
    public void deleteAll(Iterable<? extends Buyer> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Buyer> S save(S s) {
        return null;
    }

    @Override
    public <S extends Buyer> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Buyer> findById(String s) {
        return Optional.of(new Buyer());
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Buyer> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Buyer> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Buyer getOne(String s) {
        return null;
    }

    @Override
    public <S extends Buyer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Buyer> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Buyer> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Buyer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Buyer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Buyer> boolean exists(Example<S> example) {
        return false;
    }
}
