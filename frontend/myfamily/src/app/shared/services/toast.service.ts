import { Injectable } from '@angular/core';
import { ToastController } from '@ionic/angular';

@Injectable({ providedIn: 'root' })
export class ToastService {
  constructor(private toastController: ToastController) {}

  async showInfo(message: string, duration: number = 2000): Promise<any> {
    return this.showToast(message, 'primary', duration);
  }

  async showSuccess(message: string, duration: number = 2000): Promise<any> {
    return this.showToast(message, 'success', duration);
  }

  async showError(message: string, duration: number = 2000): Promise<any> {
    return this.showToast(message, 'danger', duration);
  }

  private async showToast(
    message: string,
    color: string,
    duration: number
  ): Promise<any> {
    const toast = await this.toastController.create({
      message,
      duration,
      color,
      position: 'top',
    });
    await toast.present();
    // return when the toast is dismissed so callers can await behaviour
    return toast.onDidDismiss();
  }
}
