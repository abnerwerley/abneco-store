package com.abneco.delivery.address.mock;

import com.abneco.delivery.address.dto.AddressForm;
import com.abneco.delivery.address.entity.Address;
import com.abneco.delivery.address.repository.AddressRepository;
import com.abneco.delivery.user.entity.JuridicalPerson;
import com.abneco.delivery.user.entity.Seller;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class AddressMockRepository implements AddressRepository {

    @Override
    public void deleteByCep(String cep) {

    }

    @Override
    public List<Address> findAll() {
        JuridicalPerson juridicalPerson = new JuridicalPerson("email.@gmail.com", "12345678123456", "Abneco Delivery", "12345678", 11908765132L, false);
        Seller user = new Seller("kasdjlfkajsçdlkfjalçkdjfalkdjf", juridicalPerson, "", "");
        AddressForm form = new AddressForm("kasdjlfkajsçdlkfjalçkdjfalkdjf", "12345678", "", 24);
        return List.of(new Address(user, form, "rua tal", "jardim do meu endereço", "cidade exemplo", "RJ"));
    }

    @Override
    public List<Address> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Address> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Address> findAllById(Iterable<String> iterable) {
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
    public void delete(Address address) {

    }

    @Override
    public void deleteAll(Iterable<? extends Address> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Address> S save(S s) {
        return null;
    }

    @Override
    public <S extends Address> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Address> findById(String s) {
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
    public <S extends Address> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Address> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Address getOne(String s) {
        return null;
    }

    @Override
    public <S extends Address> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Address> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Address> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Address> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Address> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Address> boolean exists(Example<S> example) {
        return false;
    }
}
