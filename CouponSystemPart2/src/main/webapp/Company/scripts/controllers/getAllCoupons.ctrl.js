
/* get all coupons controller contains 4 functions:
 * 
 * 1.getAllByType- requests all coupons matching the type from the relevant service function . 
 * if type is "all", the request is the same as getAll.
 * 2.getAllByPrice- requests all coupons matching the price(lower than or equal to) from the relevant service function 
 * 3.getAllByEndDate- requests all coupons matching the end date(earlier than or equal to)from the relevant service function 
 * 4.getAll- requests all coupons in the system from the relevant service function 
 * 
 * when this controller is first constructed a request to service to receive all coupons is sent ,
 *  so as soon as the html loads, a list of all coupons is presented to user.
 * 

 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
	
	var app=angular.module("myApp");
	app.controller("getAllCouponsCtrl" ,getAllCoupCtrlCtor);
	
	function getAllCoupCtrlCtor(CompSrv,ngConfirmSrv){
		this.coupons=[];
		this.type="All";
		this.price;
		this.endDate;
		var self=this;
		this.getAllByType=function(){
			if(this.type== undefined){
				return;
			}
			if(this.type!="All"){
			var promise2=CompSrv.getCouponsByType(this.type);
			promise2.then(
					function(resp){
						self.coupons=resp.data;
				
					},function(err){	 self.errorMessage=err.data;});
					
				
			
		
		}else{var promise=CompSrv.getAllCoupons();
		promise.then(
				function(resp){
					self.coupons=resp.data;
					
				},function(err){
					 self.errorMessage=err.data;
				});
			
		}
		}
		this.getAllByPrice=function(){
			if(this.price== undefined){
				return;
			}
			var promise3=CompSrv.getCouponsByPrice(this.price);
			promise3.then(
					function(resp){
						self.coupons=resp.data;
				
					},function(err){
						 self.errorMessage=err.data;
					});
				
			
		}
		this.getAllByEndDate=function(){
			if(this.endDate== undefined){
				return;
			};
			var promise4=CompSrv.getCouponsByEndDate(this.endDate);
			promise4.then(
					function(resp){
						self.coupons=resp.data;
				
					},function(err){
						 self.errorMessage=err.data;
					});
				
			
		}
		this.getAll=function(){
			var promise=CompSrv.getAllCoupons();
			promise.then(
						function(resp){
							self.coupons=resp.data;
							
						},function(err){
							 self.errorMessage=err.data;
								if((err.status)==401){
									ngConfirmSrv.needLoginAlert();
								 }	
							
						});
		}
		
		this.getAll();
	}
	
})();

	
