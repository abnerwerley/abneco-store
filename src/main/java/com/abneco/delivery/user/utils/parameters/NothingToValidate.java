package com.abneco.delivery.user.utils.parameters;

import com.abneco.delivery.user.json.buyer.BuyerForm;
import com.abneco.delivery.user.json.seller.BaseSellerForm;
import com.abneco.delivery.user.json.seller.SellerForm;
import com.abneco.delivery.user.json.user.BaseUserForm;

public class NothingToValidate extends Validate {

    public NothingToValidate() {
        super(null);
    }

    @Override
    public void validate(BuyerForm form) {
        // nothing more to validate
    }

    @Override
    public void validate(BaseUserForm form) {
        // nothing more to validate
    }

    @Override
    public void validate(BaseSellerForm form) {
        // nothing more to validate
    }

    @Override
    public void validate(SellerForm form) {
        // nothing more to validate
    }
}
