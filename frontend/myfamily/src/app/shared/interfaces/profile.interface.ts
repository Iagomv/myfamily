export interface ProfileInfo {
  userId: number;
  username: string;
  birthdate: string;
  familyMemberName: string;
  email: string;
  familyMemberIcon: string;
  memberSince: string;
  userStats: ProfileInfoUserStats;
}

export interface ProfileInfoUserStats {
  familiesCount: number;
  totalEventsCreated: number;
  totalShoppingItemsCreated: number;
  totalPurchasedItems: number;
}

export interface UserUpdateRequest {
  username?: string;
  email?: string;
  birthdate?: string;
}

export interface UsersDto {
  id: number;
  username: string;
  email: string;
  birthdate: string;
}
