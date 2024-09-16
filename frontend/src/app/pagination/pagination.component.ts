import { Component, OnInit, Input } from '@angular/core';
import { first } from 'rxjs/operators';

import { User } from '../_models';
import { ActivatedRoute, Router } from "@angular/router";
import { Subject } from "rxjs";
import { PaginationService } from '../_services/pagination.service';

@Component({
  selector: 'pagination',
  templateUrl: 'pagination.component.html'
})
export class PaginationComponent implements OnInit {

  @Input() paginationService: PaginationService | undefined;
  values: any;
  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {

  }


  tendina = 10;

  onChangeTendina(event: any) {
    if (this.paginationService) {
      this.paginationService.setPages(this.tendina);
    }
  }

  ngOnInit() {
    if (this.paginationService) {
      this.values = this.paginationService.getPaginationSize();
      this.tendina = this.values[0];
    }
  }

  goToPage(page: number) {
    if (this.paginationService) {
      this.paginationService.updatePage(page);
    }
      //qui chiamiamo this.updateSamples();
  }

  prev() {
    if (this.paginationService) {

      this.goToPage(this.paginationService.getPage() - 1);
    }
  }

  next() {
    if (this.paginationService) {
      this.goToPage(this.paginationService.getPage() + 1);
    }
  }



}
