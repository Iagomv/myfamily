export interface DocumentRequest {
  title: string;
  categoryId: number;
  fileData: File;
}

export interface DocumentDtoResponse {
  id: number;
  title: string;
  filePath: string;
  fileType: string;
  fileSize: number;
  categoryName: string;
  familyMemberName: string;
  familyId: number;
  uploadedDate: string;
}

//* Document Category

export interface DocumentCategoryRequestDto {
  name: string;
  description: string;
}

export interface DocumentCategoryDto {
  id: number;
  name: string;
  description: string;
}
