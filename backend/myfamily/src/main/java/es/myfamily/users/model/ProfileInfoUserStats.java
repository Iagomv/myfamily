package es.myfamily.users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfoUserStats {
  private Integer familiesCount;
  private Integer totalEventsCreated;
  private Integer totalShoppingItemsCreated;
  private Integer totalPurchasedItems;
}
