import {Component, Output, EventEmitter, Input} from '@angular/core';
import {NgbPaginationModule} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-pagination-list',
  standalone: true,
  imports: [NgbPaginationModule],
  templateUrl: './pagination-list.component.html',
  styleUrl: './pagination-list.component.scss'
})
export class PaginationListComponent {
  @Input() nrOfElements = 0;

  @Output() newSelectedPage = new EventEmitter<number>();

  selectionChanged(newPage: number) {
    this.newSelectedPage.emit(newPage);
  }
}
