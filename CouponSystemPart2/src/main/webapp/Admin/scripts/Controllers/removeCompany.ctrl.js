

/* remove company controller contains 1 function-'removeCompany' which receives the id number of the
 * company as a parameter and sends a request to service to delete the company matching that id.
 *  when this request is successful calls for the 'getAllCompanies' function again to receive the newly updated list of companies.
 * 
 * when this controller is first constructed a request to service to receive all companies is sent ,
 *  so as soon as the html loads, a list of all company is presented to user.
 *   
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
	var app=angular.module("myApp");
	app.controller("removeCompanyCtrl",removeCompanyCtrlCtr);

	
	function removeCompanyCtrlCtr(adminSrv,$ngConfirm,ngConfirmSrv){
		this.removed=false;
		this.notRemoved=false;
		var self=this;
		
		this.getAllCompanies=function(){
			var promise1=adminSrv.getAllCompanies();
			promise1.then(
					function(resp){
						self.companies=resp.data;},
					function(err){
							 if((err.status)==401){
								 ngConfirmSrv.needLoginAlert();
							 }	
						})
		}
		this.confirmDeletion=function(company){
			$ngConfirm({
				theme : 'dark',
				animation : 'rotateYR',
				closeAnimation : 'scale',
				animationSpeed : 500,
				boxWidth : '30%',
				useBootstrap : false,
			    title: "Confirmation",
			    content:"are you sure you want to remove company "+company.compName +"?",
			    typeAnimated: true,
			    buttons: {
			        Yes: {
			            text: 'Yes',
			            btnClass: 'btn-green',
			            action: function (){
			            	self.removeCompany(company.id);
			                }
			            },
			        Cancel: function () { 
			        	$ngConfirm('canceled');
			        	}}
			});
		}

	
		this.removeCompany=function(id){
			var promise=adminSrv.removeCompany(id);
			promise.then(
					function(resp){
						self.removed=true;
						self.notRemoved=false;
						self.getAllCompanies();
										
					},function(err){
						self.errorMessage=err.data;
						self.removed=false;
						self.notRemoved=true;
						 if((err.status)==401){
							 ngConfirmSrv.needLoginAlert();
						 }	
					})
		}
				this.getAllCompanies();
		
		
			
		
		
	}
	
})();