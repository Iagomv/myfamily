export interface AddShoppingItemsDto {
  items: AddShoppingItemDto[];
}

export interface AddShoppingItemDto {
  name: string;
  quantity: number;
  categoryId: number;
}

export interface ShoppingItem {
  id: number;
  familyId: number;
  itemName: string;
  quantity: number;
  categoryId: number;
  categoryName: string;
  isPurchased: boolean;
  addedByFamilyMemberId: number;
  addedByFamilyMemberName: string;
  addedDate: Date;
}

export interface ShoppingCategory {
  id: number;
  name: string;
  description?: string;
}
