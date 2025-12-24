import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { DocumentCategoryDto } from 'src/app/shared/interfaces/documents-interface';

@Component({
  selector: 'app-document-category-card',
  templateUrl: './document-category-card.component.html',
  styleUrls: ['./document-category-card.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class DocumentCategoryCardComponent {
  @Input() category!: DocumentCategoryDto;
  @Input() count: number = 0;
  @Output() clicked = new EventEmitter<DocumentCategoryDto>();

  onClick(): void {
    this.clicked.emit(this.category);
  }

  get iconName(): string {
    const name = (this.category?.name || '').toLowerCase();
    if (name.includes('méd') || name.includes('med')) return 'medkit-outline';
    if (name.includes('escuela') || name.includes('school'))
      return 'school-outline';
    if (name.includes('foto') || name.includes('photo'))
      return 'camera-outline';
    if (name.includes('doc')) return 'documents-outline';
    return 'folder-outline';
  }
}
