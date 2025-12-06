package es.myfamily.shopping.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddShoppingItemDto {

  @NotNull(message = "Item name cannot be null.")
  private String name;

  @Min(value = 1, message = "Quantity must be at least 1.")
  private int quantity;

  @NotNull(message = "Category ID cannot be null.")
  private Long categoryId;
}
