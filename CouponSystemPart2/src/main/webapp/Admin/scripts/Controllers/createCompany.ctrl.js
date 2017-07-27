
/* create company controller contains 1 function:
 * 
 * 1.createCompany- receives company details as entered by user and sends the company to the relevant service function 
 *  to be added to the system.
 *
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * if webAPI response is success-a request is sent to webAPI to receive the newly updated list of companies.
 * */

(function(){
	 var app = angular.module("myApp");
	 
	    app.controller("CreateCompanyCtrl", CreateCompanyCtrlCtor);
	 
	    
	    function CreateCompanyCtrlCtor(adminSrv,ngConfirmSrv) {
	 
	        this.success = false;
	        this.failure = false;

	 
	        this.createCompany = function () {
	        
	           	            if (this.newCompany == undefined || this.newCompany.compName == undefined || this.newCompany.password== undefined
	            		|| this.newCompany.email== undefined)
	            {
	                this.success = false;
	                this.failure = true;
	                return;
	            }
	            this.success = false;
	            this.failure = false;
	            var self = this;
	            var promisePost = adminSrv.addCompany(this.newCompany);
	            promisePost.then(
	             function (resp) {
	                self.newCompany = {};
  	                 self.success = true;
	                 self.failure = false;
	 
	             },
	                function (err) {
		                self.errorMessage=err.data;
		                self.success = false;
		                self.failure = true;
		                if((err.status)==401){
		                	ngConfirmSrv.needLoginAlert();
		                	
		                }	
	             });
	            
	            var promisePost2 = adminSrv.getAllCompanies();
	            promisePost2.then(
	             function (resp) {
	         	     self.companies = resp.data;
	               
	 
	             },
	                function (err) {
	                   
	                
	                });
	 
	        
	 
	        }
	 
	    }
	
})();