/**contains the ng-confirm pop up for the unauthorized users which redirects users to the login back.
 * 
 *  This service is injected into each controller
 * 
 */

(function(){
	var app= angular.module("myApp");

	app.service("ngConfirmSrv", ngConfirmSrvCtor);
	
	function ngConfirmSrvCtor($ngConfirm){
		this.needLoginAlert=function(){
			$ngConfirm({
			    title: 'Encountered an error!',
			    content: 'you must log in to perform actions',
			    type: 'red',
			    typeAnimated: true,
			    buttons: {
			    	OKLogin: {
			            text: 'OK login',
			            btnClass: 'btn-red',
			            action: function(){
			            	window.location.href="http://localhost:8080/CouponSystemPart2/General/Html/login.html";
			            }
			        }
			    }
			});
		}
	}
	
	
})();