// src/app/shared/highlight-invalid.directive.ts
import { Directive, ElementRef, Input, OnChanges } from '@angular/core';

/**
 * Simple directive to highlight invalid inputs via red border.
 * Usage: <input [appHighlightInvalid]="invalidBoolean" ... />
 */
@Directive({ selector: '[appHighlightInvalid]' })
export class HighlightInvalidDirective implements OnChanges {
  @Input('appHighlightInvalid') invalid = false;
  constructor(private el: ElementRef<HTMLInputElement>) {}
  ngOnChanges(): void {
    this.el.nativeElement.style.border = this.invalid ? '1px solid #dc2626' : '';
  }
}
