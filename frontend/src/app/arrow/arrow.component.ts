import { Component, Input, OnInit } from "@angular/core";

@Component({
  selector: 'app-arrow',
  templateUrl: './arrow.component.html',
})
export class ArrowComponent implements OnInit {

  @Input() activeSort: boolean | undefined;
  @Input() sortDirection: string | undefined;

  constructor() {
  }

  ngOnInit(): void {

  }
}
