package com.realestate.service;

import com.realestate.dto.OfferAvailableDto;
import com.realestate.dto.OfferDto;
import com.realestate.mapper.OfferAvailableMapper;
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
    private final OfferAvailableMapper offerAvailableMapper;

    public OfferService(OffersRepository offersRepository, OfferMapper offerMapper, OfferAvailableMapper offerAvailableMapper) {
        this.offersRepository = offersRepository;
        this.offerMapper = offerMapper;
        this.offerAvailableMapper = offerAvailableMapper;
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

    public List<OfferAvailableDto> getAvailableProperty(){
        List<Offer> offers = (List<Offer>) offersRepository.findAll();
        List<OfferAvailableDto> availableOffers = offers.stream().map(offerAvailableMapper::map).filter(OfferAvailableDto::getIsAvailable).collect(Collectors.toList());
        return availableOffers;
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
