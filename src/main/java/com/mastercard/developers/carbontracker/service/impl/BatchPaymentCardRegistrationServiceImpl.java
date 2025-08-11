package com.mastercard.developers.carbontracker.service.impl;

import com.mastercard.developer.interceptors.OkHttpFieldLevelEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developers.carbontracker.configuration.ApiConfiguration;
import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.BatchPaymentCardRegistrationService;
import com.mastercard.developers.carbontracker.util.EncryptionHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IssuerApi;
import org.openapitools.client.model.PaymentCard;
import org.openapitools.client.model.PaymentCardEnrolment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mastercard.developers.carbontracker.util.JSON.deserializeErrors;

@Slf4j
@Service
public class BatchPaymentCardRegistrationServiceImpl implements BatchPaymentCardRegistrationService {

    private final IssuerApi issuerApiForEncryptedPayload;

    @Autowired
    public BatchPaymentCardRegistrationServiceImpl(ApiConfiguration apiConfiguration) throws ServiceException {
        log.info("Initializing User Registration Service");
        issuerApiForEncryptedPayload = new IssuerApi(setupForEncryptedPayload(apiConfiguration));
    }

    private ApiClient setupForEncryptedPayload(ApiConfiguration apiConfiguration) throws ServiceException {
        OkHttpClient client = new OkHttpClient().newBuilder().
                addInterceptor(
                        new OkHttpFieldLevelEncryptionInterceptor(
                                EncryptionHelper.encryptionConfig(apiConfiguration.getEncryptionKeyFile()))).
                addInterceptor(
                        new OkHttpOAuth1Interceptor(apiConfiguration.getConsumerKey(), apiConfiguration.getSigningKey()))
                .build();

        return new ApiClient().setHttpClient(client).setBasePath(apiConfiguration.getBasePath());
    }


    @Override
    public List<PaymentCardEnrolment> batchRegisterPaymentCards(String userId, List<PaymentCard> paymentCard) throws ServiceException {

        List<PaymentCardEnrolment> paymentCardEnrolments;
        try {
            paymentCardEnrolments = issuerApiForEncryptedPayload.batchRegisterPaymentCards(userId, paymentCard);
        } catch (ApiException e) {
            log.error("Exception occurred while registering batch payment cards {}", e.getResponseBody());

            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }

        return paymentCardEnrolments;
    }
}
