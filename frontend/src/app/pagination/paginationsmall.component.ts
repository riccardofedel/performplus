import { Component, OnInit, Input } from '@angular/core';
import { first } from 'rxjs/operators';

import { User } from '../_models';
import { UserService, AuthenticationService, PaginationService } from '../_services';
import { ActivatedRoute, Router } from "@angular/router";
import { UtilsService } from "../_services";
import { Subject } from "rxjs";

@Component({  selector:    'paginationsmall',
    templateUrl: 'paginationsmall.component.html' })
export class PaginationSmallComponent implements OnInit {

    @Input() paginationService: PaginationService;
    
    constructor(
        private authenticationService: AuthenticationService,
        private userService: UserService,
        public utilsService: UtilsService,
        private route: ActivatedRoute,
        private router: Router
    ) {

    }

    tendina=25;
    
    onChangeTendina(event) {
        this.paginationService.setPages(this.tendina);
    }
    
    ngOnInit() {
    }

    goToPage(page) {
        this.paginationService.updatePage(page);
        //qui chiamiamo this.updateSamples();
    }
    
    prev() {
        this.goToPage(this.paginationService.getPage()-1);
    }
    
    next() {
        this.goToPage(this.paginationService.getPage()+1);
    }
    

    
}
