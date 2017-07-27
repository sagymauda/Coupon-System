/* update company controller contains 2 functions-
 * 
 
 * 1.updateCompany-receives an updated company as parameter and sends it to the service to update in the DB. 
 *  when this request is successful calls for the 'getAllCompanies' function again to receive the newly updated list of companies.

 * 2.setOrder- receives a field name by which to sort table.every time this function is called the Ascending variable
 *  changes its value to the opposite.
 *  
 * when this controller is first constructed a request to service to receive all companies is sent ,
 *  so as soon as the html loads, a list of all companies is presented to user.
 *   
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */
(function(){
	var app=angular.module("myApp");
	app.controller("updateCompanyCtrl",updateCompanyCtrlCtor);
	
	function updateCompanyCtrlCtor(adminSrv,ngConfirmSrv){
		
		var self=this;
		this.success = false;
		this.failure = false;    
		
		this.getAllCompanies=function(){
			var promise=adminSrv.getAllCompanies();
				promise.then(
						function(resp){
							self.companies=resp.data;
							
						},function(err){
								if((err.status)==401){
								ngConfirmSrv.needLoginAlert();
						
							}
						});
		}
				
                    
		this.updateCompany=function(company){
				if(company.password==""||company.password==undefined||company.email==undefined||company.email==""){
				self.success = false;
				self.failure = true;
		        self.errorMessage="password and email cannot be blank fields"
		        return;
			}
			self.success = false;
			self.failure = false;
	        var promise= adminSrv.updateCompany(company);
			promise.then(
					function(resp){
						self.success = true;
						self.failure = false;
						self.getAllCompanies();	
						self.edit=!self.edit;
				
					},function(err){
						self.failure = true;
						if((err.status)==401){
							ngConfirmSrv.needLoginAlert();
			 }	
			});
		}
		this.setOrder = function(orderBy)
		{
			self.order = orderBy;
			self.Ascending = !this.Ascending;
		}
		this.getAllCompanies();
		
	}
	
})();