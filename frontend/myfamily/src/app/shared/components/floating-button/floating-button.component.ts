import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-floating-button',
  templateUrl: './floating-button.component.html',
  styleUrls: ['./floating-button.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
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
