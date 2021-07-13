package com.card.service;

import com.card.entity.Merchant;
import com.card.repository.MerchantRepository;
import com.card.service.exception.MerchantException;

import javax.inject.Singleton;

@Singleton
public class MerchantService {
    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant findById(Long id) throws MerchantException {
        return merchantRepository.findById(id).orElseThrow(()->new MerchantException(("Merchant does not exist")));
    }
}
