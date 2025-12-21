import { FamilyMember } from './family-member.interface';

export interface Family {
  id: number;
  familyName: string;
  invitationCode: string;
  familyMembers: FamilyMember[]; // List of User objects representing family members
}
