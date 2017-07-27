
/* get company controller contains 1 functions:
 * 
 * 1.getCompany- receives a company identifier as a parameter and sends a request to the service to receive a company matching this ID .
 * 
 * if the webAPI response is success- loads the company details onto controller- will be shown to user in HTML page.
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
	var app= angular.module("myApp");
	app.controller("getCompanyCtrl",getCompanyCtrlCtor);
	
	function getCompanyCtrlCtor(adminSrv,ngConfirmSrv){
		this.getCompany=function(id){
			this.found=false;
			this.notFound=false;
			var self=this;
			if(id==undefined|| id==""){
				this.found=false;
				this.notFound=true;
				self.errorMessage="id not provided";
				return;
			}
			var promise=adminSrv.getCompany(id);
			promise.then(
					function(resp){
						self.company=resp.data;
						self.found=true;
						self.notFound=false;
					},function(err){
						self.found=false;
						self.notFound=true;
						self.errorMessage=err.data;
						 if((err.status)==401){
							 ngConfirmSrv.needLoginAlert();
						 }	
					});
			
		}
		
	}
	
	
	
})();