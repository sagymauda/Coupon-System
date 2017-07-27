
/* get company controller  gets the details of the currently logged in company.
 * 
 * as the html page loads the controller is constructed and requests the company details from the service function  
 * 
 * if the webAPI response is success- the company is loaded onto controller as an attribute and is presented in html.
 *
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */


(function(){
	
	var app=angular.module("myApp");
	app.controller("getCompanyCtrl",getCompanyCtrlCtor);
	
	function getCompanyCtrlCtor(CompSrv,ngConfirmSrv){
		this.company={};
		var self=this;
		
		var promise=CompSrv.getCompany()
		promise.then(
				function(resp){
					self.company=resp.data;
					console.log(self.company+ "  from controller");
				},function(err){
					 self.errorMessage=err.data;
						if((err.status)==401){
							ngConfirmSrv.needLoginAlert();

						}
				})
	}
	
	
	
})();