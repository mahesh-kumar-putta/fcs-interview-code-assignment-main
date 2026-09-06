package com.fulfilment.application.monolith.fulfilment;

public class FulfilmentAssignment {

  public final Long productId;
  public final Long storeId;
  public final Long warehouseId;

  public FulfilmentAssignment(Long productId, Long storeId, Long warehouseId) {
    this.productId = productId;
    this.storeId = storeId;
    this.warehouseId = warehouseId;
  }
}
