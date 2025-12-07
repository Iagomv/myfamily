import { Injectable } from '@angular/core';
import { Family } from '../interfaces/family.interface';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FamilyService {
  private families: Family[] = [];
  private selectedFamilySubject = new BehaviorSubject<Family | null>(null);
  public selectedFamily$: Observable<Family | null> =
    this.selectedFamilySubject.asObservable();

  setFamilies(families: Family[]): void {
    this.families = families;
  }

  getFamilies(): Family[] {
    return this.families;
  }

  setSelectedFamily(family: Family): void {
    this.selectedFamilySubject.next(family);
    localStorage.setItem('selectedFamily', JSON.stringify(family));
  }

  getSelectedFamily(): Family | null {
    let family = this.selectedFamilySubject.getValue();

    // If not in memory, try to retrieve from localStorage
    if (!family) {
      const storedFamily = localStorage.getItem('selectedFamily');
      if (storedFamily) {
        try {
          family = JSON.parse(storedFamily);
          this.selectedFamilySubject.next(family);
        } catch (error) {}
      }
    }
    return family;
  }

  clearSelectedFamily(): void {
    this.selectedFamilySubject.next(null);
    localStorage.removeItem('selectedFamily');
  }
}
