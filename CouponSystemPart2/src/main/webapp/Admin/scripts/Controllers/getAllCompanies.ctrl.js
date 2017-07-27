
/* get all companies controller-
 * 
 * when this controller is first constructed a request to service to receive all companies is sent ,
 *  so as soon as the html loads, a list of all companies  is presented to user.
 * 

 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
    var app=angular.module("myApp");
    app.controller("GetAllComp",CompCtor);

    function CompCtor(adminSrv,ngConfirmSrv){
      this.companies = [];
		var self = this;
	 
	        var promise = adminSrv.getAllCompanies();
	        promise.then(
	        		function (resp) {
	        			self.companies = resp.data;

	        	        },
	        	        function (err) {
	        	            self.errorMessage=err.data
	        	            if((err.status)==401){
	        	            	 ngConfirmSrv.needLoginAlert();


	   					 }	
	               
	        	        });
		
	}
	

	
    

   

 
        


})();