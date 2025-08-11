package com.mastercard.developers.carbontracker.controller;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.GetDashboardService;
import com.mastercard.developers.carbontracker.service.IssuerService;
import com.mastercard.developers.carbontracker.service.UpdateUserService;
import com.mastercard.developers.carbontracker.service.UserRegistrationService;
import com.mastercard.developers.carbontracker.service.BatchPaymentCardRegistrationService;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.IssuerConfiguration;
import org.openapitools.client.model.IssuerProfile;
import org.openapitools.client.model.IssuerProfileDetails;
import org.openapitools.client.model.UpdateUserProfile;
import org.openapitools.client.model.UserProfile;
import org.openapitools.client.model.UserReference;
import org.openapitools.client.model.PaymentCardEnrolment;
import org.openapitools.client.model.PaymentCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.ADD_USER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.AGGREGATE_CARBON_SCORE;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DASHBOARDS;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DELETE_USER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.GET_ISSUER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.UPDATE_ISSUER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.UPDATE_USER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.BATCH_PAYMENT_CARDS_REGISTRATION;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DELETE_USER_MULTI_CARD;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DELETE_PAYMENT_CARD;

@RestController
@Slf4j
@Validated
public class IssuerController {

  private final IssuerService issuerService;

  private final UserRegistrationService userRegistrationService;

  private final GetDashboardService getDashboardService;

  private final UpdateUserService updateUserService;

  private final BatchPaymentCardRegistrationService batchPaymentCardRegistrationService;

  @Autowired
  public IssuerController(IssuerService issuerService, UserRegistrationService userRegistrationService, GetDashboardService getDashboardService, UpdateUserService updateUserService, BatchPaymentCardRegistrationService batchPaymentCardRegistrationService) {
    this.issuerService = issuerService;
    this.userRegistrationService = userRegistrationService;
    this.getDashboardService = getDashboardService;
    this.updateUserService = updateUserService;
    this.batchPaymentCardRegistrationService = batchPaymentCardRegistrationService;
  }

  @GetMapping(DASHBOARDS)
  public ResponseEntity<Dashboard> getAuthToken(@Pattern(regexp = "^[0-9A-Fa-f-]{36}") @Size(min = 36, max = 36) @ApiParam(value = "Unique identifier for a cardholder enrolled into Priceless Planet Carbon Tracker Service.", required = true) @PathVariable("userid") String userId, String lang) throws ServiceException {
    return ResponseEntity.ok(getDashboardService.getAuthToken(userId, lang));
  }


  @GetMapping(AGGREGATE_CARBON_SCORE)
  public ResponseEntity<AggregateCarbonScore> getAggregateCarbonScore(@Pattern(regexp = "^[0-9A-Fa-f-]{36}") @Size(min = 36, max = 36) @ApiParam(value = "Unique identifier for a cardholder enrolled into Priceless Planet Carbon Tracker Service.", required = true) @PathVariable("userid") String userId) throws ServiceException {
    return ResponseEntity.ok(issuerService.getAggregateCarbonScore(userId));
  }


  @PostMapping(ADD_USER)
  public ResponseEntity<UserReference> userRegistration(@ApiParam(value = "User's Personal and Card information which needs to be registered onto Priceless Planet Carbon Tracker platform. This endpoint uses Mastercard payload encryption. Please refer to the **[Payload Encryption](https://mstr.cd/2UPfda0)** page for implementation details.", required = true) @Valid @RequestBody UserProfile userProfile) throws ServiceException {
    return ResponseEntity.ok(userRegistrationService.userRegistration(userProfile));
  }

  @PutMapping(UPDATE_ISSUER)
  public ResponseEntity<IssuerProfile> updateIssuer(@ApiParam(value = " issuer configuration", required = true) @Valid @RequestBody IssuerConfiguration issuerConfiguration) throws ServiceException {

    IssuerProfile issuerProfile = issuerService.updateIssuer(issuerConfiguration);
    return ResponseEntity.ok(issuerProfile);
  }

  @PutMapping(UPDATE_USER)
  public ResponseEntity<UserReference> updateUser(@Pattern(regexp = "^[0-9A-Fa-f-]{36}") @Size(min = 36, max = 36) @ApiParam(value = "Unique identifier for a cardholder enrolled into Priceless Planet Carbon Tracker Service.", required = true) @PathVariable("userid") String userId, @ApiParam(value = " User's Personal information which needs to be updated for enrolled user onto Priceless Planet Carbon Tracker platform. This endpoint uses Mastercard payload encryption. Please refer to the **[Payload Encryption](https://mstr.cd/2UPfda0)** page for implementation details.", required = true) @Valid @RequestBody UpdateUserProfile updateUserProfile) throws ServiceException {

    UserReference userReference = updateUserService.updateUser(userId, updateUserProfile);
    return ResponseEntity.ok(userReference);
  }

  @PostMapping(DELETE_USER)
  public ResponseEntity<List<String>> deleteUsers(@ApiParam(value = " User ids", required = true) @Valid @RequestBody List<String> userIds) throws ServiceException {
    return issuerService.deleteUsers(userIds);
  }

  @GetMapping(GET_ISSUER)
  public ResponseEntity<IssuerProfileDetails> getIssuer() throws ServiceException {
    IssuerProfileDetails issuerProfileDetails = issuerService.getIssuer();
    return new ResponseEntity<>(issuerProfileDetails, HttpStatus.OK);
  }

  @PostMapping(BATCH_PAYMENT_CARDS_REGISTRATION)
  public ResponseEntity< List<PaymentCardEnrolment>> batchRegisterPaymentCards(@PathVariable("userid")  String userId, @Valid @RequestBody List<PaymentCard> paymentCard) throws ServiceException {
    List<PaymentCardEnrolment> paymentCardEnrolment = batchPaymentCardRegistrationService.batchRegisterPaymentCards(userId, paymentCard);
    return ResponseEntity.ok(paymentCardEnrolment);
  }

  @DeleteMapping(DELETE_USER_MULTI_CARD)
  public ResponseEntity<Void> deleteUserAndPaymentCards(@PathVariable("userid") String userId) throws ServiceException  {
    issuerService.deleteUserAndPaymentCards(userId);
    return ResponseEntity.accepted().build();
  }

  @DeleteMapping(DELETE_PAYMENT_CARD)
  public ResponseEntity<Void> deletePaymentCard(@PathVariable("payment_card_id") String paymentCardId) throws ServiceException  {

    issuerService.deletePaymentCard(paymentCardId);

    return ResponseEntity.accepted().build();
  }


}
