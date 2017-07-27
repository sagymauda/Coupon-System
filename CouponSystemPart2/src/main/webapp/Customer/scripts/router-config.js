
(function(){
var app= angular.module("myApp");
app.config(['$locationProvider', function($locationProvider) {
      $locationProvider.hashPrefix('');
}]);

app.config(function($stateProvider,$urlRouterProvider){
	$stateProvider
	.state("purchaseCoupon",{url:"/purchaseCoupon",templateUrl:"templates/purchaseCoupon.html",controller:"purchaseCouponCtrl as purchase"})
	.state("getAllPurchasedCoupons",{url:"/getAllPurchasedCoupons",templateUrl:"templates/getAllPurchasedCoupons.html",controller:"getAllPurchasedCouponsCtrl as getP"})

	
	
	
	$urlRouterProvider.when('','/purchaseCoupon');
	$urlRouterProvider.otherwise('/404');
	
	
});
})();


