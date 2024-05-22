package com.realestate.service;

import com.realestate.dto.OfferDto;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.OfferMapper;
import com.realestate.model.Property.Property;
import com.realestate.model.client.Client;
import com.realestate.model.offer.Offer;
import com.realestate.model.user.UserEmployee;
import com.realestate.repository.ClientRepository;
import com.realestate.repository.OffersRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OfferServiceTest {

    @Mock
    private OffersRepository offersRepository;

    @Mock
    private OfferMapper offerMapper;
    @Mock
    private ValidationService validationService;
    @Mock private UserRepository userRepository;
    @Mock private PropertyRepository propertyRepository;
    @Mock private ClientRepository clientRepository;

    @InjectMocks
    private OfferService offerService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOffer_ValidDto_ShouldSaveOfferAndReturnDto() {

        OfferDto inputDto = new OfferDto();
        Offer offer = new Offer();
        Offer savedOffer = new Offer();
        OfferDto expectedDto = new OfferDto();

        when(offerMapper.map(inputDto)).thenReturn(offer);
        when(offersRepository.save(offer)).thenReturn(savedOffer);
        when(offerMapper.map(savedOffer)).thenReturn(expectedDto);
        doNothing().when(validationService).validateData(any());

        OfferDto resultDto = offerService.saveOffer(inputDto);

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        verify(offerMapper, times(1)).map(inputDto);
        verify(offersRepository, times(1)).save(offer);
        verify(offerMapper, times(1)).map(savedOffer);
        verify(validationService, times(1)).validateData(any());
    }

    @Test
    void saveOffer_InvalidDto_ShouldThrowException() {
        OfferDto invalidDto = new OfferDto();
        invalidDto.setUserId(null);

        when(offerMapper.map(invalidDto)).thenReturn(new Offer());
        doThrow(DataIntegrityViolationException.class).when(offersRepository).save(any());

        assertThrows(DataIntegrityViolationException.class, () -> offerService.saveOffer(invalidDto));

        verify(offerMapper, times(1)).map(invalidDto);
        verify(offersRepository, times(1)).save(any());
    }

    @Test
    public void getOfferById_ShouldFindOffer() {
        OfferDto dto = new OfferDto();
        Offer offer = new Offer();
        Long offerId = 1L;

        when(offersRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offerMapper.map(offer)).thenReturn(dto);

        OfferDto result = offerService.getOfferById(offerId);

        assertEquals(dto, result);
    }

    @Test
    public void getOffersByClient_ShouldFindOffers() {

        Offer offer = new Offer();
        OfferDto dto = new OfferDto();
        Long clientId = 1L;

        when(offersRepository.findAllByClient_Id(clientId)).thenReturn(Collections.singletonList(offer));
        when(offerMapper.map(offer)).thenReturn(dto);

        List<OfferDto> result = offerService.getOffersByClient(clientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getAllOffers_ShouldFindAllOffers() {

        int page = 0;
        int size = 10;
        List<Offer> offerList = new ArrayList<>();
        offerList.add(new Offer());
        offerList.add(new Offer());
        Page<Offer> offerPage = new PageImpl<>(offerList);
        when(offersRepository.findAll(any(Pageable.class))).thenReturn(offerPage);

        List<OfferDto> offerDtoList = new ArrayList<>();
        offerDtoList.add(new OfferDto());
        offerDtoList.add(new OfferDto());
        when(offerMapper.map(any(Offer.class))).thenReturn(new OfferDto());

        List<OfferDto> result = offerService.getAllOffers(page, size);

        assertNotNull(result);

        verify(offersRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void markOfferAsSold_ShouldMarkOfferAsSold() {
        Offer offer = new Offer();
        Long offerId = 1L;
        offer.setIsAvailable(true);

        when(offersRepository.findById(offerId)).thenReturn(Optional.of(offer));

        offerService.markOfferAsSold(offerId);

        verify(offersRepository, times(1)).findById(offerId);
        assertFalse(offer.getIsAvailable());
    }

    @Test
    public void findAvailableOffers_ShouldFindAvailableOffers() {
        List<Offer> offers = createOfferList();
        List<OfferDto> dtoOffers = createDtoOfferList();

        when(offersRepository.findAll()).thenReturn(offers);
        when(offerMapper.map(any(Offer.class))).thenReturn(dtoOffers.get(0), dtoOffers.get(1));

        List<OfferDto> result = offerService.findAvailableOffers();

        assertEquals(1, result.size());
        assertEquals(dtoOffers.get(0), result.get(0));
    }

    @Test
    public void findSoldOffers_ShouldFindSoldOffers() {
        List<Offer> offers = createOfferList();
        List<OfferDto> dtoOffers = createDtoOfferList();

        when(offersRepository.findAll()).thenReturn(offers);
        when(offerMapper.map(any(Offer.class))).thenReturn(dtoOffers.get(0), dtoOffers.get(1));

        List<OfferDto> result = offerService.findSoldOffers();

        assertEquals(1, result.size());
        assertEquals(dtoOffers.get(0), result.get(0));
    }

    @Test
    void updateOffer_OfferFound_ShouldUpdateOffer() {
        Long id = 1L;
        OfferDto updateDto = new OfferDto();
        updateDto.setUserId(1L);
        updateDto.setClientId(1L);
        updateDto.setPropertyId(1L);
        Offer offer = new Offer();

        when(offersRepository.findById(id)).thenReturn(Optional.of(offer));
        doNothing().when(validationService).validateData(offer);
        when(userRepository.findById(updateDto.getUserId())).thenReturn(Optional.of(new UserEmployee()));
        when(clientRepository.findById(updateDto.getClientId())).thenReturn(Optional.of(new Client()));
        when(propertyRepository.findById(updateDto.getPropertyId())).thenReturn(Optional.of(new Property()));
        when(offersRepository.save(offer)).thenReturn(offer);

        assertDoesNotThrow(() -> offerService.updateOffer(id, updateDto));

        verify(offersRepository, times(1)).findById(id);
        verify(validationService, times(1)).validateData(offer);
        verify(offersRepository, times(1)).save(offer);
    }

    @Test
    void updateOffer_OfferNotFound_ShouldThrowResourceNotFoundException() {
        Long id = 1L;

        when(offersRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> offerService.updateOffer(id, new OfferDto()));

        verify(offersRepository, times(1)).findById(id);
        verifyNoInteractions(validationService);
        verifyNoMoreInteractions(offersRepository);
    }

    @Test
    public void deleteOffer_ShouldDeleteOffer() {
        Long offerId = 1L;

        offerService.deleteOffer(offerId);

        verify(offersRepository, times(1)).deleteById(offerId);
    }

    private List<OfferDto> createDtoOfferList() {
        List<OfferDto> dtos = new ArrayList<>();
        OfferDto dto1 = new OfferDto();
        OfferDto dto2 = new OfferDto();
        dto1.setIsAvailable(true);
        dto2.setIsAvailable(false);
        dtos.add(dto1);
        dtos.add(dto2);
        return dtos;
    }

    private List<Offer> createOfferList() {
        List<Offer> offers = new ArrayList<>();
        Offer offer1 = new Offer();
        Offer offer2 = new Offer();
        offer1.setIsAvailable(true);
        offer2.setIsAvailable(false);
        offers.add(offer1);
        offers.add(offer2);
        return offers;
    }
}
