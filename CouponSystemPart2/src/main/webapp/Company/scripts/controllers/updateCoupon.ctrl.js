/* update coupon controller contains 3 function-
 * 
 * 1.getAll- requests all coupons in the system from the service function  
 * 2.updateCoupon-receives an updated coupon as parameter and sends it to the relevant service function  
 *  when this request is successful calls for the 'getAll' function again to receive the newly updated list of coupons.
 * 3.setOrder- receives a field name by which to sort table.every time this function is called the Ascending variable
 *  changes its value to the opposite.
 *  
 * when this controller is first constructed a request to webAPI to receive all coupons is sent ,
 *  so as soon as the html loads, a list of all coupons is presented to user.
 *   
 * if the webAPI response is failure-  if the status is 401  the user is redirected to the login page in order to enter valid credentials
 * */

(function(){
	
	var app= angular.module("myApp");
	app.controller("updateCouponCtrl",updateCouponCtrlCtor);
	
	app.directive("formatDate", function() {
	    return {
	        require: 'ngModel',
	        link: function(scope, elem, attr, modelCtrl) {
	            modelCtrl.$formatters .push(function(modelValue) {
	                if (modelValue){
	                    return new Date(modelValue);
	                }
	                else {
	                    return null;
	                
	            }
            });
        }
    };
});
;
	
	
	function updateCouponCtrlCtor(CompSrv,ngConfirmSrv){
		var self=this;
		self.updateSuccess = false;
		self.updateFailure = false;
		
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
					
		this.updateCoupon=function(coupon){
			if(coupon.end_date===undefined||coupon.price===undefined){
				self.updateSuccess = false;
				self.updateFailure = true;
				self.errorMessage=" could not submit. fields cannot be blank"
		        return;
			}
		
			self.updateSuccess = false;
			self.updateFailure = false;
			coupon.editMode=false;
	        var promise1= CompSrv.updateCoupon(coupon);
			promise1.then(
					function(resp){
						self.updateSuccess = true;
						self.updateFailure = false;
						self.getAll();
				
			},function(err){
				self.errorMessage=err.data;
			});
			
		}
	
		this.setOrder = function(orderBy)
		{
			
			self.order=orderBy;
			self.Ascending=!self.Ascending;
		}
		this.getAll();
	}
})();