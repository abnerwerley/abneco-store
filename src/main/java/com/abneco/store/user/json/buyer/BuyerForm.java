package com.abneco.store.user.json.buyer;

import com.abneco.store.user.entity.Buyer;
import com.abneco.store.user.json.user.UserForm;
import com.abneco.store.utils.DateFormatter;
import com.abneco.store.utils.UpperCaseFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BuyerForm extends UserForm {

    private String cpf;

    public BuyerForm(String name, String email, String password, Long phoneNumber, String cpf) {
        setName(name);
        setEmail(email);
        setPassword(password);
        setPhoneNumber(phoneNumber);
        setCpf(cpf);
    }

    public Buyer toEntity() {
        Buyer buyer = new Buyer();

        buyer.setName(UpperCaseFormatter.formatToCapitalLetter(getName()));
        buyer.setEmail(getEmail());
        buyer.setPassword(getPassword());
        buyer.setPhoneNumber(getPhoneNumber());
        buyer.setCpf(cpf);
        buyer.setEmailVerified(false);
        buyer.setUpdatedAt(DateFormatter.formatNow());
        buyer.setCreatedAt(DateFormatter.formatNow());

        return buyer;
    }
}
