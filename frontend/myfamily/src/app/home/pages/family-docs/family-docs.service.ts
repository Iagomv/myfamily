import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, forkJoin, throwError } from 'rxjs';
import { catchError, finalize, tap } from 'rxjs/operators';
import { ApiCallService } from 'src/app/shared/services/api-call.service';
import { FamilyService } from 'src/app/shared/services/family.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import {
  DocumentCategoryDto,
  DocumentCategoryRequestDto,
  DocumentDtoResponse,
  DocumentRequest,
} from 'src/app/shared/interfaces/documents-interface';

@Injectable({
  providedIn: 'root',
})
export class FamilyDocsService {
  private readonly documentsSubject = new BehaviorSubject<
    DocumentDtoResponse[]
  >([]);
  private readonly categoriesSubject = new BehaviorSubject<
    DocumentCategoryDto[]
  >([]);
  private readonly loadingSubject = new BehaviorSubject<boolean>(false);

  documents$ = this.documentsSubject.asObservable();
  categories$ = this.categoriesSubject.asObservable();
  loading$ = this.loadingSubject.asObservable();

  constructor(
    private apiService: ApiCallService,
    private familyService: FamilyService,
    private toastService: ToastService
  ) {}

  fetchAll(): void {
    const familyId = this.familyService.getFamilyId();
    if (!familyId) {
      this.toastService.showError('Error loading family data');
      return;
    }

    this.loadingSubject.next(true);
    forkJoin({
      categories: this.apiService.getDocumentCategories(familyId),
      documents: this.apiService.getFamilyDocuments(familyId),
    })
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: ({ categories, documents }) => {
          this.categoriesSubject.next(
            Array.isArray(categories) ? categories : []
          );
          this.documentsSubject.next(Array.isArray(documents) ? documents : []);
        },
        error: (err: any) => {
          console.error('Error fetching documents:', err);
          this.toastService.showError('Error loading documents');
        },
      });
  }

  createCategory(
    request: DocumentCategoryRequestDto
  ): Observable<DocumentCategoryDto> {
    const familyId = this.familyService.getFamilyId();
    if (!familyId) {
      this.toastService.showError('Error loading family data');
      return throwError(() => new Error('No family selected'));
    }

    return this.apiService.createDocumentCategory(familyId, request).pipe(
      tap(() => {
        this.toastService.showSuccess('Carpeta creada correctamente');
        this.fetchAll();
      }),
      catchError((error) => {
        console.error('Error creating category:', error);
        this.toastService.showError('Error al crear la carpeta');
        return throwError(() => error);
      })
    );
  }

  deleteCategory(categoryId: number): Observable<void> {
    const familyId = this.familyService.getFamilyId();
    if (!familyId) {
      this.toastService.showError('Error loading family data');
      return throwError(() => new Error('No family selected'));
    }

    return this.apiService.deleteDocumentCategory(familyId, categoryId).pipe(
      tap(() => {
        this.toastService.showSuccess('Carpeta eliminada correctamente');
        this.fetchAll();
      }),
      catchError((error) => {
        console.error('Error deleting category:', error);
        this.toastService.showError('Error al eliminar la carpeta');
        return throwError(() => error);
      })
    );
  }

  uploadDocument(request: DocumentRequest): Observable<void> {
    const familyId = this.familyService.getFamilyId();
    if (!familyId) {
      this.toastService.showError('Error loading family data');
      return throwError(() => new Error('No family selected'));
    }

    this.loadingSubject.next(true);
    return this.apiService.uploadDocument(familyId, request).pipe(
      finalize(() => this.loadingSubject.next(false)),
      tap(() => {
        this.toastService.showSuccess('Documento subido correctamente');
        this.fetchAll();
      }),
      catchError((error) => {
        console.error('Error uploading document:', error);
        this.toastService.showError('Error al subir el documento');
        return throwError(() => error);
      })
    );
  }
}
