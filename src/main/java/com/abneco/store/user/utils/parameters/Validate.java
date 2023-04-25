package com.abneco.store.user.utils.parameters;

import com.abneco.store.user.json.buyer.BuyerForm;
import com.abneco.store.user.json.seller.BaseSellerForm;
import com.abneco.store.user.json.seller.SellerForm;
import com.abneco.store.user.json.user.BaseUserForm;

public abstract class Validate {
    protected Validate nextToValidate;

    public abstract void validate(BuyerForm form);

    public abstract void validate(BaseUserForm form);

    public abstract void validate(BaseSellerForm form);

    public abstract void validate(SellerForm form);

    protected Validate(Validate nextToValidate) {
        this.nextToValidate = nextToValidate;
    }
}
