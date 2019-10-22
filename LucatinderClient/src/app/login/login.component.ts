import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProfileService } from '../profile.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {


  constructor(private router: Router, private profileService: ProfileService) {
  }

  login(form){
    this.profileService.login(form.value.name).subscribe(data => this.router.navigate(['/home']));
  }
}