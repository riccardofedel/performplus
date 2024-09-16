import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../_models';
import { Subject, Observable } from "rxjs";

export class PaginationService {
  constructor() { }

  pagination = {
    page: 1,
    numberOfPages: 10,
    totalPages: 0
  };

  paginationSize = [
    10,
    25,
    100
  ];

  itemCount: number = 0;

  setPages(tendina: number) {
    this.pagination.numberOfPages = tendina;
    this.updatePage(1);
  }

  getPaginationSize() {
    return this.paginationSize;
  }

  setPaginationSize(sizes=[]) {
    this.paginationSize = [...sizes];
    //return this.paginationSize;
  }

  getPages() {
    let pages = [];
    let prev = 0;
    for (let i = 1; i <= this.pagination.totalPages; i++) {
      if (i < 5 || (Math.abs(i - this.pagination.page) < 5) || (Math.abs(i - this.pagination.totalPages) < 5)) {
        if (prev != i - 1) {
          pages.push(-1);
        }
        pages.push(i);
        prev = i;
      }
    }
    return pages;
  }

  private subject = new Subject<any>();

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }


  updatePage(page: number) {
    this.pagination.page = page;
    this.subject.next({ page: page });
  }

  reset(sendMessage = true) {
    this.pagination.totalPages = 0;
    if (sendMessage) {
      this.updatePage(1);
    } else {
      this.pagination.page = 1;
    }
  }

  updateCount(itemCount:number) {
    this.itemCount = itemCount;
    this.pagination.totalPages = Math.ceil(itemCount / this.pagination.numberOfPages);
  }

  getPagination() {
    return this.pagination;
  }

  isFirstPage() {
    return this.pagination.page == 1;
  }
  isLastPage() {
    return this.pagination.page >= this.pagination.totalPages;
  }

  getTotalPage() {
    return this.pagination.totalPages;
  }

  getNumber() {
    return this.pagination.numberOfPages;
  }

  getPage() {
    return this.pagination.page;
  }

  getFirst() {
    return (this.pagination.page - 1) * this.pagination.numberOfPages + 1;
  }

  getLast() {
    let last = (this.pagination.page) * this.pagination.numberOfPages;
    if (this.itemCount < last) return this.itemCount;
    return last;
  }

  getItemCount() {
    return this.itemCount;
  }

}

