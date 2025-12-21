export interface FamilyMember {
  familyId: number;
  userId: number;
  username: string;
  email: string;
  familyMemberName: string;
  selectedIcon: string;
  createdAt: string;
}

export interface FamilyMemberIconUpdateRequest {
  familyId: number;
  userId: number;
  newIconName: string;
}
