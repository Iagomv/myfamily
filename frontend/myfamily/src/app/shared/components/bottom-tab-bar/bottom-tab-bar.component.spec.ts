import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { BottomTabBarComponent } from './bottom-tab-bar.component';

describe('BottomTabBarComponent', () => {
  let component: BottomTabBarComponent;
  let fixture: ComponentFixture<BottomTabBarComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [BottomTabBarComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BottomTabBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
