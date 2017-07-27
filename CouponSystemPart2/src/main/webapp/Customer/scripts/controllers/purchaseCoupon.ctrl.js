
/* purchase coupon controller contains 2 functions:
 * 
 * 1.getAll- requests all coupons from service. 
 * 2.purchaseCoupon- receives a coupon as a parameter and performs purchase. when successful calls for the 'getAll' function again.
 * 
 * when this controller is first constructed a request to service to receive all coupons is sent ,
 *  so as soon as the html loads, a list of all coupons is presented to user.
 * 

 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */





(function(){
	var app=angular.module("myApp");
	app.controller("purchaseCouponCtrl",purchaseCouponCtrlCtor)
	
	
	function purchaseCouponCtrlCtor(customerSrv,$ngConfirm,ngConfirmSrv,$scope){
		this.success=false;
		this.failure=false;
		var self=this;
	
		
		this.getAll=function(){
			var promise=customerSrv.getAll();
			promise.then(
						function(resp){
							self.coupons=resp.data;
							
						},function(err){
							self.failure=true;
							self.errorMessage=err.data;
							if((err.status)==401){
								ngConfirmSrv.needLoginAlert();
						
							}
								

								
						});
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
		    scope : $scope,
		    content:"are you sure you want to buy coupon "+coupon.title +"?",
		    typeAnimated: true,
		    buttons: {
		        Yes: {
		            text: 'Yes',
		            btnClass: 'btn-green',
		            action: function (){
		            	self.purchaseCoupon2(coupon);
		                }
		            },
		        Cancel: function () { 
		        	$ngConfirm('canceled');
		        	}
		    		}
		        
		    
		});
	}
	this. purchaseCoupon2=function(coupon){
		this.success=false;
		this.failure=false;
    	var promise2=customerSrv.purchaseCoupon(coupon);
		promise2.then(
				function(resp){
					self.success=true;
					self.failure=false;
					self.getAll();
				},function(err){
					self.success=false;
					self.failure=true;
					self.errorMessage=err.data;
					if((err.status)==401){
						ngConfirmSrv.needLoginAlert();
						

					 }else{
						 self.errorMessage=err.data;
					 }	
				});
		
	}
	this.getAll();
	
	}
	
})();