import { familyMember } from './family-member.interface';

export interface Family {
  id: number;
  familyName: string;
  invitationCode: string;
  familyMembers: familyMember[]; // List of User objects representing family members
}
