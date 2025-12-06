package es.myfamily.shopping.model;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddShoppingItemsDto {
  private Long familyId;

  @NotNull(message = "The items list cannot be null.")
  @NotEmpty(message = "The items list cannot be empty.")
  private List<@Valid AddShoppingItemDto> items;
}