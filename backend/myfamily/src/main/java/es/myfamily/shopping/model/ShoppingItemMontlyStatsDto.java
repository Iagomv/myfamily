package es.myfamily.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingItemMontlyStatsDto {
  private Integer purchasedItems;
  private Integer pendingItems;
  private ShoppingCategory mostPurchasedCategory;
}
