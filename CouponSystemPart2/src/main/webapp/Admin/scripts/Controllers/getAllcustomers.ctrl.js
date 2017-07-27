

/* get all customers controller-
 * 
 * when this controller is first constructed a request to the relevant service function  to receive all customers is sent ,
 *  so as soon as the html loads, a list of all customers  is presented to user.
 * 

 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
	var app= angular.module("myApp");
	app.controller("getAllcustomersCtrl",customerCtrlCtor);
	
	function customerCtrlCtor(adminSrv,ngConfirmSrv) {
		this.customers = [];
		var self = this;
	 
	        var promise = adminSrv.getAllCustomers();
	        promise.then(
	        		function (resp) {
	        			self.customers = resp.data;

	        	        },
	        	        function (err) {
	        	            self.errorMessage=err.data;
		        		    if((err.status)==401){
		        		    	ngConfirmSrv.needLoginAlert();
		   					        		    }

	        	        });
		
	}
	
})();
	
	
		
		
	