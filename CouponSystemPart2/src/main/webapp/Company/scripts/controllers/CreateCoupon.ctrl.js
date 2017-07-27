
/* create coupon controller contains 3 attributes and a function.
 * Attributes: :1. success 2. failure 3.coupons Array
 * Function- createCoupon.
 * 
 * in the createCoupon function- the coupon object details are loaded onto controller 
 * this object is then sent to the relevant service function  to be added to the system.
 * if the webAPI response is successful- the controller then sends a request to receive the newly updated list of coupons from the service .
 * the html page will show these coupons to user using 'ng-repeat'.
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */
(function(){
	var app=angular.module("myApp");
	app.controller("createCouponCtrl",createCouponCtrlCtor);
	
	function createCouponCtrlCtor(CompSrv,ngConfirmSrv){
		this.success=false;
		this.failure=false;
		
		this.createCoupon=function(){
			if(this.coupon==undefined||this.coupon.title==undefined||this.coupon.start_date==undefined||this.coupon.end_date==undefined||
					this.coupon.amount==undefined||this.coupon.price==undefined||this.coupon.type==undefined){
				this.errorMessage="form not submitted, please fill in all required fields"
					return;
			}
			var self=this;
			var promise=CompSrv.createCoupon(this.coupon);
			promise.then(
					function(resp){
						self.success=true;
						self.failure=false;
						self.coupon={};
						var promise2=CompSrv.getAllCoupons();
						promise2.then(function(resp){self.coupons=resp.data;},function(err){});
						
					},function(err){
						
						self.success=false;
						self.failure=true;
						self.errorMessage=err.data;
						if((err.status)==401){
							ngConfirmSrv.needLoginAlert();
						 }	
					})
		}
		
	}
	
})();