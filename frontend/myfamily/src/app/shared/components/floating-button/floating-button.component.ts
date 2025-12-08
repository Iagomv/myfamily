import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonFab, IonFabButton, IonIcon } from '@ionic/angular/standalone';

@Component({
  selector: 'app-floating-button',
  templateUrl: './floating-button.component.html',
  styleUrls: ['./floating-button.component.scss'],
  standalone: true,
  imports: [CommonModule, IonFab, IonFabButton, IonIcon],
})
export class FloatingButtonComponent {
  @Input() icon: string = 'add';
  @Input() tooltip: string = 'Add';
  @Input() color: string = 'success';
  @Output() clicked = new EventEmitter<void>();

  onClick(): void {
    this.clicked.emit();
  }
}
