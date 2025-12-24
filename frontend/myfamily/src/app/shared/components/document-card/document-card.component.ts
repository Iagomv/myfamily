import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { DocumentDtoResponse } from '../../interfaces/documents-interface';

@Component({
  selector: 'app-document-card',
  templateUrl: './document-card.component.html',
  styleUrls: ['./document-card.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class DocumentCardComponent {
  @Input() document!: DocumentDtoResponse;
  @Output() downloadClicked = new EventEmitter<DocumentDtoResponse>();

  onDownload(): void {
    this.downloadClicked.emit(this.document);
  }

  formatBytes(bytes: number | null | undefined): string {
    const value = typeof bytes === 'number' ? bytes : 0;
    if (value < 1024) return `${value} B`;
    const kb = value / 1024;
    if (kb < 1024) return `${kb.toFixed(0)} KB`;
    const mb = kb / 1024;
    if (mb < 1024) return `${mb.toFixed(1)} MB`;
    const gb = mb / 1024;
    return `${gb.toFixed(1)} GB`;
  }
}
