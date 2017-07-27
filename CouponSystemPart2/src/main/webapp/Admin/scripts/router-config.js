
(function(){
var app= angular.module("myApp");
app.config(['$locationProvider', function($locationProvider) {
      $locationProvider.hashPrefix('');
}]);

app.config(function($stateProvider,$urlRouterProvider){
	$stateProvider
	.state("createCompany",{url:"/createCompany",templateUrl:"templates/createCompany.html", controller:"CreateCompanyCtrl as create1"})
	.state("RemoveCompany",{url:"/RemoveCompany",templateUrl:"templates/RemoveCompany.html",controller:"removeCompanyCtrl as remove1"})
	.state("UpdateCompany",{url:"/UpdateCompany",templateUrl:"templates/UpdateCompany.html",controller:"updateCompanyCtrl as update1"})
	.state("GetCompany",{url:"/GetCompany",templateUrl:"templates/GetCompany.html", controller:"getCompanyCtrl as get1"})
	.state("GetAllCompanies",{url:"/GetAllCompanies",templateUrl:"templates/GetAllCompanies.html",controller:"GetAllComp as allC"})
	
	.state("createCustomer",{url:"/createCustomer",templateUrl:"templates/createCustomer.html", controller:"createCustCtrl as create2"})
	.state("RemoveCustomer",{url:"/RemoveCustomer",templateUrl:"templates/RemoveCustomer.html",controller:"removeCustCtrl as remove2"})
	.state("UpdateCustomer",{url:"/UpdateCustomer",templateUrl:"templates/UpdateCustomer.html", controller:"updateCustomerCtrl as update2"})
	.state("GetCustomer",{url:"/GetCustomer",templateUrl:"templates/GetCustomer.html",controller:"getCustCtrl as get2"})
	.state("GetAllCustomers",{url:"/GetAllCustomers",templateUrl:"templates/GetAllCustomers.html",controller:"getAllcustomersCtrl as cust"});
	
	$urlRouterProvider.when('','/GetAllCompanies');
	$urlRouterProvider.otherwise('/404');
	
	
});
})();


