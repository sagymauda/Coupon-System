

/* remove coupon controller contains 1 function-'removeCoupon' which receives the id number of the
 * coupon as a parameter and sends a request to service to delete the coupon matching that id. 
 * when this request is successful calls for the 'getAll' function again to receive the newly updated list of coupons.
 * 
 * when this controller is first constructed a request to service to receive all coupons is sent ,
 *  so as soon as the html loads, a list of all coupons is presented to user.
 *   
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
	
	var app= angular.module("myApp");
	app.controller("removeCouponCtrl",removeCouponCtrlCtor);
	
	
	
	function removeCouponCtrlCtor(CompSrv,$ngConfirm,ngConfirmSrv){
		var self=this;
		
		this.getAll=function(){
			var promise=CompSrv.getAllCoupons();
			promise.then(function(resp){
				self.coupons=resp.data;},
				function(err){ 
					self.errorMessage=err.data;
					if((err.status)==401){
						ngConfirmSrv.needLoginAlert();

				 }	});
		}
		this.confirm=function(coupon){
			
			$ngConfirm({
				theme : 'dark',
				animation : 'rotateYR',
				closeAnimation : 'scale',
				animationSpeed : 500,
				boxWidth : '30%',
				useBootstrap : false,
			    title: "Confirmation",
			    
			    content:"are you sure you want to Delete coupon "+coupon.title +"?",
			    typeAnimated: true,
			    buttons: {
			        Yes: {
			            text: 'Yes',
			            btnClass: 'btn-green',
			            action: function (){
			            	self.removeCoupon(coupon.coupon_id);
			                }
			            },
			        Cancel: function () { 
			        	$ngConfirm('canceled');
			        	}
			    		}
			        
			    
			});
		}
		this.removeCoupon=function(id){
				
				var promise=CompSrv.removeCoupon(id);
				promise.then(
						function(resp){
							self.getAll();
							
						},function(err){
							self.errorMessage=err.data;
						});
			}

		this.getAll();
	}
})();