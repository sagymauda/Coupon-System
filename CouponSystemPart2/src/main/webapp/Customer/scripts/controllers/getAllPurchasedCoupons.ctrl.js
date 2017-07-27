
/* get all coupons controller contains 4 functions:
 * 
 * 1.getAllPurchasedCoupons- requests all coupons purchases by the currently logged in customer from the relevant service function   
 * 2.getAllPurchasedCouponsByType- gets all coupons matching the type and purchases by the currently logged in customer from the relevant service function .
 *3.getAllPurchasedCouponsByPrice- gets all coupons matching the price(lower than or equal to)and purchases by the currently logged in customer from the relevant service function 
 *
 * 
 * when this controller is first constructed , calls the relevant service function  to receive all purchased coupons 
 *  so as soon as the html loads, a list of all purchased coupons is presented to user.
 * 

 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
	var app=angular.module("myApp");
	app.controller("getAllPurchasedCouponsCtrl",getAllPurchasedCouponsCtrlCtor);
	
	function getAllPurchasedCouponsCtrlCtor(customerSrv,ngConfirmSrv){
		this.coupons=[];
		this.type="All";
		this.price;
		var self=this;
		this.getAllPurchasedCoupons=function(){
			var promise=customerSrv.getAllPurchasedCoupons();
			promise.then(
				function(resp){
					self.coupons=resp.data;
					
				},function(err){
					if((err.status)==401){
						ngConfirmSrv.needLoginAlert();
					}	
				});
		}
		this.getAllPurchasedCouponsByType=function(){
			if (this.type==undefined || this.type==""){
				return
			}
			var promise=customerSrv.getPurchasedByType(this.type);
			promise.then(
					function(resp){
						self.coupons=resp.data;
						
						
				},function(err){
					if((err.status)==401){
						ngConfirmSrv.needLoginAlert();
					}	
				});
		}
		
		this.getAllPurchasedCouponsByPrice=function(){
			if (this.price==undefined || this.price==""){
				return
			}
			var promise=customerSrv.getPurchasedByPrice(this.price);
			promise.then(
					function(resp){
						self.coupons=resp.data;
						
					},function(err){
					if((err.status)==401){
						ngConfirmSrv.needLoginAlert();
					}	
				});
		}
		

		
		this.getAllPurchasedCoupons();		
	}
	
})();