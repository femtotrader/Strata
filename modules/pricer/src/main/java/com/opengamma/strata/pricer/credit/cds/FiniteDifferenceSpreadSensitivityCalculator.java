/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.pricer.credit.cds;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.StandardId;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.collect.array.DoubleArray;
import com.opengamma.strata.collect.tuple.Pair;
import com.opengamma.strata.market.curve.CurveName;
import com.opengamma.strata.market.curve.NodalCurve;
import com.opengamma.strata.product.credit.cds.ResolvedCds;
import com.opengamma.strata.product.credit.cds.ResolvedCdsTrade;

/**
 * Finite difference spread sensitivity calculator. 
 * <p>
 * This computes the present value sensitivity to par spreads of bucketed CDSs by bump-and-reprice, i.e., 
 * finite difference method. 
 */
public class FiniteDifferenceSpreadSensitivityCalculator extends SpreadSensitivityCalculator {

  /**
   * Default implementation.
   * <p>
   * The bump amount is one basis point.
   */
  public static final FiniteDifferenceSpreadSensitivityCalculator DEFAULT =
      new FiniteDifferenceSpreadSensitivityCalculator(AccrualOnDefaultFormulae.ORIGINAL_ISDA, 1.0e-4);
  /**
   * The bump amount for the finite difference method.
   * <p>
   * The magnitude of the bump amount must be greater than 1e-10. 
   * However, this bound does not guarantee that the finite difference calculation produces reliable numbers.
   */
  private final double bumpAmount;

  /**
   * Constructor with accrual-on-default formula and bump amount specified.
   * 
   * @param formula  the formula
   * @param bumpAmount  the bump amount
   */
  public FiniteDifferenceSpreadSensitivityCalculator(AccrualOnDefaultFormulae formula, double bumpAmount) {
    super(formula);
    this.bumpAmount = ArgChecker.notZero(bumpAmount, 1.0e-10, "bumpAmount");
  }

  //-------------------------------------------------------------------------
  @Override
  public CurrencyAmount parallelCs01(
      ResolvedCdsTrade trade,
      List<ResolvedCdsTrade> bucketCds,
      CreditRatesProvider ratesProvider,
      ReferenceData refData) {

    checkCdsBucket(trade, bucketCds);
    ResolvedCds product = trade.getProduct();
    Currency currency = product.getCurrency();
    StandardId legalEntityId = product.getLegalEntityId();
    LocalDate valuationDate = ratesProvider.getValuationDate();

    int nBucket = bucketCds.size();
    double[] impSp = impliedSpread(bucketCds, ratesProvider, refData);
    NodalCurve creditCurveBase = calibrator.calibrate(
        bucketCds.toArray(new ResolvedCdsTrade[nBucket]), impSp, new double[nBucket], CurveName.of("baseImpliedCreditCurve"),
        valuationDate, ratesProvider.discountFactors(currency), ratesProvider.recoveryRates(legalEntityId), refData);
    CreditRatesProvider ratesProviderBase = ratesProvider.toBuilder()
        .creditCurves(ImmutableMap.of(Pair.of(legalEntityId, currency), LegalEntitySurvivalProbabilities.of(
            legalEntityId, IsdaCompliantZeroRateDiscountFactors.of(currency, valuationDate, creditCurveBase))))
        .build();
    CurrencyAmount pvBase = pricer.presentValueOnSettle(trade, ratesProviderBase, PriceType.DIRTY, refData);

    double[] bumpedSp = DoubleArray.of(nBucket, i -> impSp[i] + bumpAmount).toArray();
    NodalCurve creditCurveBump = calibrator.calibrate(
        bucketCds.toArray(new ResolvedCdsTrade[nBucket]), bumpedSp, new double[nBucket], CurveName.of("bumpedImpliedCreditCurve"),
        valuationDate, ratesProvider.discountFactors(currency), ratesProvider.recoveryRates(legalEntityId), refData);
    CreditRatesProvider ratesProviderBump = ratesProvider.toBuilder()
        .creditCurves(ImmutableMap.of(
            Pair.of(legalEntityId, currency),
            LegalEntitySurvivalProbabilities.of(
                legalEntityId, IsdaCompliantZeroRateDiscountFactors.of(currency, valuationDate, creditCurveBump))))
        .build();
    CurrencyAmount pvBumped = pricer.presentValueOnSettle(trade, ratesProviderBump, PriceType.DIRTY, refData);

    return CurrencyAmount.of(currency, (pvBumped.getAmount() - pvBase.getAmount()) / bumpAmount);
  }

  @Override
  DoubleArray computedBucketedCs01(
      ResolvedCdsTrade trade,
      List<ResolvedCdsTrade> bucketCds,
      CreditRatesProvider ratesProvider,
      ReferenceData refData) {

    checkCdsBucket(trade, bucketCds);
    ResolvedCds product = trade.getProduct();
    Currency currency = product.getCurrency();
    StandardId legalEntityId = product.getLegalEntityId();
    LocalDate valuationDate = ratesProvider.getValuationDate();

    int nBucket = bucketCds.size();
    double[] res = new double[nBucket];
    double[] impSp = impliedSpread(bucketCds, ratesProvider, refData);
    NodalCurve creditCurveBase = calibrator.calibrate(
        bucketCds.toArray(new ResolvedCdsTrade[nBucket]), impSp, new double[nBucket], CurveName.of("baseImpliedCreditCurve"),
        valuationDate, ratesProvider.discountFactors(currency), ratesProvider.recoveryRates(legalEntityId), refData);
    CreditRatesProvider ratesProviderBase = ratesProvider.toBuilder()
        .creditCurves(ImmutableMap.of(Pair.of(legalEntityId, currency), LegalEntitySurvivalProbabilities.of(
            legalEntityId, IsdaCompliantZeroRateDiscountFactors.of(currency, valuationDate, creditCurveBase))))
        .build();
    double pvBase = pricer.presentValueOnSettle(trade, ratesProviderBase, PriceType.DIRTY, refData).getAmount();
    for (int i = 0; i < nBucket; ++i) {
      double[] bumpedSp = Arrays.copyOf(impSp, nBucket);
      bumpedSp[i] += bumpAmount;
      NodalCurve creditCurveBump = calibrator.calibrate(
          bucketCds.toArray(new ResolvedCdsTrade[nBucket]), bumpedSp, new double[nBucket],
          CurveName.of("bumpedImpliedCreditCurve"),
          valuationDate, ratesProvider.discountFactors(currency), ratesProvider.recoveryRates(legalEntityId), refData);
      CreditRatesProvider ratesProviderBump = ratesProvider.toBuilder()
          .creditCurves(ImmutableMap.of(
              Pair.of(legalEntityId, currency),
              LegalEntitySurvivalProbabilities.of(
                  legalEntityId, IsdaCompliantZeroRateDiscountFactors.of(currency, valuationDate, creditCurveBump))))
          .build();
      double pvBumped = pricer.presentValueOnSettle(trade, ratesProviderBump, PriceType.DIRTY, refData).getAmount();
      res[i] = (pvBumped - pvBase) / bumpAmount;
    }
    return DoubleArray.ofUnsafe(res);
  }

}
