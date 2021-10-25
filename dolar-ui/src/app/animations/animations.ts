import { animate, style, transition, trigger } from '@angular/animations';

export const slideDownAndFadeIn = trigger('slideDownAndFadeIn', [
  transition('void => *', [
    style({ opacity: 0, transform: 'translateY(-25px)' }),
    animate('0.5s')
  ])
]);