/* update customer controller contains 2 functions-
 * 
 
 * 1.updateCustomer-receives an updated customer as parameter and sends it to the service to update in the DB. 
 *  when this request is successful calls for the 'getAllCustomers' function again to receive the newly updated list of customers.

 * 2.setOrder- receives a field name by which to sort table. every time this function is called the Ascending variable
 *  changes its value to the opposite.
 *  
 * when this controller is first constructed a request to service to receive all customers is sent ,
 *  so as soon as the html loads, a list of all companies is presented to user.
 *   
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */


(function(){
	var app=angular.module("myApp");
	app.controller("updateCustomerCtrl",updateCustomerCtrlCtor);
	
	function updateCustomerCtrlCtor(adminSrv,ngConfirmSrv){
		this.success = false;
        this.failure = false;
        var self=this;
        
        this.getAllCustomers=function(){
        	var promise1=adminSrv.getAllCustomers();
        	promise1.then(
        			function(resp){
        			self.customers=resp.data;
        			},function(err){
        				if((err.status)==401){
        					ngConfirmSrv.needLoginAlert();
                		}});
        	        }
        
              
		this.updateCustomer=function(customer){
			if(customer.password==""||customer.password==undefined){
				self.success = false;
				self.failure = true;
		        self.errorMessage="password cannot be blank "
		        return;
			}
			self.success = false;
			self.failure = false;
	        var promise= adminSrv.updateCustomer(customer);
			promise.then(
					function(resp){
						self.edit=!self.edit;
						self.success = true;
						self.failure = false;
						self.getAllCustomers();
				      				
					},function(err){
						if((err.status)==401){
							ngConfirmSrv.needLoginAlert();
		   		 }
				
			});
			
		}
		this.setOrder = function(orderBy){
			self.order = orderBy;
			self.Ascending = !this.Ascending;
		}
		  this.getAllCustomers();
		
	}
	
})();