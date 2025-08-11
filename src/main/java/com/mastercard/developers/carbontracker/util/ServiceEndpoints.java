package com.mastercard.developers.carbontracker.util;

public class ServiceEndpoints {
  private ServiceEndpoints() {
  }

  public static final String ADD_USER = "/cts/issuers/users";
  public static final String AGGREGATE_CARBON_SCORE = "/cts/issuers/users/{userid}/aggregate-carbon-scores";
  public static final String DASHBOARDS = "/cts/issuers/users/{userid}/dashboards";
  public static final String DELETE_USER = "/cts/issuers/user-deletions";
  public static final String UPDATE_ISSUER = "/cts/issuers";
  public static final String GET_ISSUER = "/cts/issuers";
  public static final String UPDATE_USER = "/cts/issuers/users/{userid}";

  public static final String BATCH_PAYMENT_CARDS_REGISTRATION = "/cts/issuers/users/{userid}/payment-cards";

  public static final String DELETE_USER_MULTI_CARD = "/cts/issuers/users/{userid}";

  public static final String DELETE_PAYMENT_CARD = "/issuers/payment-cards/{payment_card_id}";



}
