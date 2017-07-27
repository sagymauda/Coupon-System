
/* get customer controller contains 1 functions:
 * 
 * 1.getCustomer- receives a customer identifier as a parameter and sends to the relevant service function  to receive a customer matching this ID .
 * 
 * if the webAPI response is success- loads the customer details onto controller- will be shown to user in HTML page.
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
	var app=angular.module("myApp");
	app.controller("getCustCtrl",getCustCtrlCtor);
	
	function getCustCtrlCtor(adminSrv,ngConfirmSrv){
		this.getCustomer=function(id){
			this.found=false;
			this.notFound=false;
			if(id==undefined|| id==""){
				this.found=false;
				this.notFound=true;
				 this.errorMessage="id not provided";
				return;
			}
			var self=this;
			var promise=adminSrv.getCustomer(id);
			promise.then(
			  function (resp){
				  self.customer=resp.data;
				  self.found=true;
				  self.notFound=false;
			  },function(err){
				  self.found=false;
				  self.notFound=true;
				  self.errorMessage=err.data;
					 if((err.status)==401){
						 ngConfirmSrv.needLoginAlert();

					 }	
			})
			
		}
		
	}
	
})();