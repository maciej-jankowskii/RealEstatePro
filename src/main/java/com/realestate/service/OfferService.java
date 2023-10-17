package com.realestate.service;

import com.realestate.dto.OfferDto;
import com.realestate.mapper.OfferMapper;
import com.realestate.model.offer.Offer;
import com.realestate.repository.OffersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OffersRepository offersRepository;
    private final OfferMapper offerMapper;

    public OfferService(OffersRepository offersRepository, OfferMapper offerMapper) {
        this.offersRepository = offersRepository;
        this.offerMapper = offerMapper;
    }

    @Transactional
    public OfferDto saveOffer(OfferDto offerDto){
        Offer offer = offerMapper.map(offerDto);
        Offer saved = offersRepository.save(offer);
        return offerMapper.map(saved);
    }

    public Optional<OfferDto> getOfferById(Long id){
        return offersRepository.findById(id).map(offerMapper::map);
    }

    public List<OfferDto> getOffersByClient(Long clientId){
        List<Offer> offers = offersRepository.findAllByClient_Id(clientId);
        List<OfferDto> dtos = offers.stream().map(offerMapper::map).collect(Collectors.toList());
        return dtos;
    }

    public List<OfferDto> getAllOffers(){
        List<Offer> offers = (List<Offer>) offersRepository.findAll();
        List<OfferDto> dtos = offers.stream().map(offerMapper::map).collect(Collectors.toList());
        return dtos;
    }

    public void markOfferAsSold(Long offerId){
        Offer offer = offersRepository.findById(offerId).orElseThrow();
        offer.setIsAvailable(false);
        System.out.println(offer.getId());
        offersRepository.save(offer);
    }

    public List<OfferDto> findAvailableOffers(){
        List<Offer> offers = (List<Offer>) offersRepository.findAll();
        List<OfferDto> availableOffers = offers.stream().filter(Offer::getIsAvailable).map(offerMapper::map).collect(Collectors.toList());
        return availableOffers;
    }

    public List<OfferDto> findSoldOffers(){
        List<Offer> offers = (List<Offer>) offersRepository.findAll();
        List<OfferDto> soldOffers = offers.stream().filter(offer -> !offer.getIsAvailable()).map(offerMapper::map).collect(Collectors.toList());
        return soldOffers;
    }



    @Transactional
    public void updateOffer(OfferDto offerDto){
        Offer offer = offerMapper.map(offerDto);
        offersRepository.save(offer);
    }

    public void deleteOffer(Long id){
        offersRepository.deleteById(id);
    }

}
