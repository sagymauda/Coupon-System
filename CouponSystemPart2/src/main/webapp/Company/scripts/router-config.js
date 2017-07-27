
(function(){
var app= angular.module("myApp");
app.config(['$locationProvider', function($locationProvider) {
      $locationProvider.hashPrefix('');
}]);

app.config(function($stateProvider,$urlRouterProvider){
	$stateProvider
	.state("createCoupon",{url:"/createCoupon",templateUrl:"templates/createCoupon.html" ,controller:"createCouponCtrl as create"})
	.state("removeCoupon",{url:"/removeCoupon",templateUrl:"templates/removeCoupon.html",controller:"removeCouponCtrl as remove"})
	.state("updateCoupon",{url:"/updateCoupon",templateUrl:"templates/updateCoupon.html",controller:"updateCouponCtrl as update"})
	.state("getCoupons",{url:"/getCoupons",templateUrl:"templates/getCoupons.html",controller:"getAllCouponsCtrl as get"})
	.state("getCompany",{url:"/getCompany",templateUrl:"templates/getCompany.html",controller:"getCompanyCtrl as getComp"})
	.state("401",{url:"/401",templateUrl:"../General/Html/login.html"})
	
	$urlRouterProvider.when('','/getCoupons');
	$urlRouterProvider.otherwise('/404');
	
});
})();
