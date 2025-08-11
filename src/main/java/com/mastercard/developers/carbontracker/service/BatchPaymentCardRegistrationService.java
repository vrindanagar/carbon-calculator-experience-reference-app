package com.mastercard.developers.carbontracker.service;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import org.openapitools.client.model.PaymentCard;
import org.openapitools.client.model.PaymentCardEnrolment;

import java.util.List;

public interface BatchPaymentCardRegistrationService {

     List<PaymentCardEnrolment> batchRegisterPaymentCards(String userId, List<PaymentCard> paymentCard) throws ServiceException;

}
