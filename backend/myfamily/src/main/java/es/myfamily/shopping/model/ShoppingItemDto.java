package es.myfamily.shopping.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import es.myfamily.users.model.UsersDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingItemDto {

  private Long id;

  private Long familyId;

  private String itemName;

  private Integer quantity;

  private Long categoryId;

  private String categoryName;

  private Boolean isPurchased;

  private Long addedByFamilyMemberId;

  private String addedByFamilyMemberName;

  @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private Date addedDate;
}
