
/* create customer controller contains 1 function:
 * 
 * 1.createCustomer- receives customer details as entered by user and sends the customer to the relevant service function 
 * to be added to the system.
 *
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * if webAPI response is success-a request is sent to webAPI to receive the newly updated list of customers.
 * */
(function(){
	var app=angular.module("myApp")
	app.controller("createCustCtrl",createCustCtr);
		
	function createCustCtr(adminSrv,ngConfirmSrv){
		this.success=false;
		this.failure=false;
	
	this.createCustomer=function(){
		if(this.newCustomer== undefined ||
				this.newCustomer.cust_name== undefined ||this.newCustomer.password== undefined){
            	this.success = false;
            	this.failure = true;
            	return;
					}
			this.success=false;
			this.failure=false;
			var self = this;
			var promise=adminSrv.addCustomer(this.newCustomer);
			promise.then(
					function(resp){
						
						self.newCustomer={};
						self.success=true;
						self.failure=false;
						var promise2=adminSrv.getAllCustomers();
						promise2.then(
								function(resp){
									self.customers=resp.data;
								},
								function(err){
									
									
								}
								
															
						)
					},
					function(err){
						 self.success = false;
		                 self.failure = true;
		                 self.errorMessage=err.data;
		                 if((err.status)==401){
		                	 ngConfirmSrv.needLoginAlert();

						 }	
					});
		}
	}
	
})();