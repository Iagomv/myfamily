import {
  Component,
  OnInit,
  CUSTOM_ELEMENTS_SCHEMA,
  ElementRef,
  ViewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { HeaderComponent } from '../../../shared/components/header/header.component';
import { FloatingButtonComponent } from '../../../shared/components/floating-button/floating-button.component';
import { DocumentCardComponent } from '../../../shared/components/document-card/document-card.component';
import { DocumentCategoryCardComponent } from './document-category-card/document-category-card.component';
import { FamilyDocsService } from './family-docs.service';
import {
  DocumentCategoryDto,
  DocumentDtoResponse,
  DocumentRequest,
} from 'src/app/shared/interfaces/documents-interface';
import { ActionSheetController, AlertController } from '@ionic/angular';
import { Observable, combineLatest, firstValueFrom } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-family-docs',
  templateUrl: './family-docs.component.html',
  styleUrls: ['./family-docs.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    IonicModule,
    HeaderComponent,
    FloatingButtonComponent,
    DocumentCategoryCardComponent,
    DocumentCardComponent,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FamilyDocsComponent implements OnInit {
  @ViewChild('fileInput', { static: false })
  fileInput?: ElementRef<HTMLInputElement>;

  loading$: Observable<boolean>;
  categoriesWithCount$!: Observable<
    Array<{ category: DocumentCategoryDto; count: number }>
  >;
  recentDocuments$!: Observable<DocumentDtoResponse[]>;

  private pendingUploadCategoryId: number | null = null;

  constructor(
    private docsService: FamilyDocsService,
    private actionSheetController: ActionSheetController,
    private alertController: AlertController
  ) {
    this.loading$ = this.docsService.loading$;

    const documentsSorted$ = this.docsService.documents$.pipe(
      map((docs) =>
        [...(docs ?? [])].sort(
          (a, b) =>
            new Date(b.uploadedDate).getTime() -
            new Date(a.uploadedDate).getTime()
        )
      ),
      shareReplay(1)
    );

    this.recentDocuments$ = documentsSorted$;

    this.categoriesWithCount$ = combineLatest([
      this.docsService.categories$,
      documentsSorted$,
    ]).pipe(
      map(([categories, docs]) => {
        const safeCategories = Array.isArray(categories) ? categories : [];
        const safeDocs = Array.isArray(docs) ? docs : [];

        return safeCategories.map((category) => ({
          category,
          count: safeDocs.filter((d) => d.categoryName === category.name)
            .length,
        }));
      }),
      shareReplay(1)
    );
  }

  ngOnInit() {
    this.docsService.fetchAll();
  }

  async onUploadClicked(): Promise<void> {
    console.log('[FamilyDocs] onUploadClicked triggered');

    let categories: DocumentCategoryDto[] = [];

    try {
      categories = await firstValueFrom(this.docsService.categories$);
      console.log('[FamilyDocs] Categories fetched:', categories);
      if (!Array.isArray(categories)) {
        categories = [];
      }
    } catch (error) {
      console.error('[FamilyDocs] Error fetching categories:', error);
      categories = [];
    }

    console.log('[FamilyDocs] Safe categories count:', categories.length);

    if (categories.length === 0) {
      // No categories: prompt to create one so the upload flow can continue.
      console.log('[FamilyDocs] No categories, showing create alert');
      const alert = await this.alertController.create({
        header: 'Crear carpeta',
        message: 'No hay carpetas. ¿Deseas crear una ahora?',
        inputs: [
          {
            name: 'name',
            type: 'text',
            placeholder: 'Nombre de la carpeta',
          },
          {
            name: 'description',
            type: 'text',
            placeholder: 'Descripción (opcional)',
          },
        ],
        buttons: [
          {
            text: 'Cancelar',
            role: 'cancel',
          },
          {
            text: 'Crear',
            handler: (value: any): boolean => {
              const name = (value?.name || '').trim();
              const description = (value?.description || '').trim();
              if (!name) {
                // Keep the prompt open so user enters a valid name
                return false;
              }

              this.docsService.createCategory({ name, description }).subscribe({
                next: () => {
                  // After creating, re-open the upload selector
                  setTimeout(() => this.onUploadClicked(), 350);
                },
              });

              // Close the alert and let the setTimeout re-open the upload flow
              return true;
            },
          },
        ],
      });

      await alert.present();

      return;
    }

    console.log('[FamilyDocs] Showing action sheet with categories');
    const actionSheet = await this.actionSheetController.create({
      header: 'Seleccionar carpeta',
      buttons: [
        ...categories.map((c) => ({
          text: c.name,
          handler: () => {
            console.log('[FamilyDocs] Selected category:', c.id);
            this.pendingUploadCategoryId = c.id;
            this.fileInput?.nativeElement.click();
          },
        })),
        {
          text: 'Cancelar',
          role: 'cancel',
        },
      ],
    });

    await actionSheet.present();
  }

  onCategoryClicked(category: DocumentCategoryDto): void {
    // Minimal action aligned with the mock: allow uploading into a folder by tapping it.
    this.pendingUploadCategoryId = category.id;
    this.fileInput?.nativeElement.click();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input?.files?.[0];
    const categoryId = this.pendingUploadCategoryId;

    // Reset input so selecting the same file again triggers change.
    if (input) input.value = '';
    this.pendingUploadCategoryId = null;

    if (!file || !categoryId) return;

    console.log('[FamilyDocs] Preparing upload:', { name: file.name, size: file.size, type: file.type, categoryId });

    const request: DocumentRequest = {
      title: file.name,
      categoryId,
      fileData: file,
    };

    this.docsService.uploadDocument(request).subscribe({
      next: () => console.log('[FamilyDocs] upload success'),
      error: (err) => console.error('[FamilyDocs] upload error', err),
    });
  }

  onDownload(doc: DocumentDtoResponse): void {
    const raw = (doc.filePath || '').trim();
    if (!raw) return;

    const isAbsolute = /^https?:\/\//i.test(raw);
    const url = isAbsolute
      ? raw
      : `${environment.apiUrl}/${raw.replace(/^\//, '')}`;

    window.open(url, '_blank');
  }
}
