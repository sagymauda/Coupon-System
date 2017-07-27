

/* remove customer controller contains 1 function-'removeCustomer' which receives the id number of the
 * customer as a parameter and sends a request to service to delete the customer matching that id.
 *  when this request is successful calls for the 'getAllCustomers' function again to receive the newly updated list of company.
 * 
 * when this controller is first constructed a request to service to receive all customer is sent ,
 *  so as soon as the html loads, a list of all company is presented to user.
 *   
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
	var app=angular.module("myApp");
	app.controller("removeCustCtrl",removeCustCtrlCtr);
	
	function removeCustCtrlCtr(adminSrv,$ngConfirm,ngConfirmSrv){
		this.removed=false;
		this.notRemoved=false;
		var self=this;
		
		this.getAllCustomers=function(){
			var promise1=adminSrv.getAllCustomers();
			promise1.then(
					function(resp){
						self.customers=resp.data;},
					function(err){
						if((err.status)==401){
							ngConfirmSrv.needLoginAlert();	
				}})
			
		}
		this.confirmDeletion=function(customer){
			$ngConfirm({
				theme : 'dark',
				animation : 'rotateYR',
				closeAnimation : 'scale',
				animationSpeed : 500,
				boxWidth : '30%',
				useBootstrap : false,
			    title: "Confirmation",
			    content:"are you sure you want to remove customer "+customer.cust_name +"?",
			    typeAnimated: true,
			    buttons: {
			        Yes: {
			            text: 'Yes',
			            btnClass: 'btn-green',
			            action: function (){
			            	self.removeCustomer(customer.cust_id);
			                }
			            },
			        Cancel: function () { 
			        	$ngConfirm('canceled');
			        	}}
			});
		}
		
		this.removeCustomer=function(id){
			var promise=adminSrv.removeCustomer(id);
			promise.then(
					function(resp){
						self.removed=true;
						self.notRemoved=false;
						self.getAllCustomers();
						
					},function(err){
						self.errorMessage=err.data;
						if((err.status)==401){
							ngConfirmSrv.needLoginAlert();							
					}})
			
				}
		this.getAllCustomers();
	}
		
	
	
})();