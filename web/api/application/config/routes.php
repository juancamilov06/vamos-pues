<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/*
| -------------------------------------------------------------------------
| URI ROUTING
| -------------------------------------------------------------------------
| This file lets you re-map URI requests to specific controller functions.
|
| Typically there is a one-to-one relationship between a URL string
| and its corresponding controller class/method. The segments in a
| URL normally follow this pattern:
|
|	example.com/class/method/id/
|
| In some instances, however, you may want to remap this relationship
| so that a different class/function is called than the one
| corresponding to the URL.
|
| Please see the user guide for complete details:
|
|	https://codeigniter.com/user_guide/general/routing.html
|
| -------------------------------------------------------------------------
| RESERVED ROUTES
| -------------------------------------------------------------------------
|
| There are three reserved routes:
|
|	$route['default_controller'] = 'welcome';
|
| This route indicates which controller class should be loaded if the
| URI contains no data. In the above example, the "welcome" class
| would be loaded.
|
|	$route['404_override'] = 'errors/page_missing';
|
| This route will tell the Router which controller/method to use if those
| provided in the URL cannot be matched to a valid route.
|
|	$route['translate_uri_dashes'] = FALSE;
|
| This is not exactly a route, but allows you to automatically route
| controller and method names that contain dashes. '-' isn't a valid
| class or method name character, so it requires translation.
| When you set this option to TRUE, it will replace ALL dashes in the
| controller and method URI segments.
|
| Examples:	my-controller/index	-> my_controller/index
|		my-controller/my-method	-> my_controller/my_method
*/
$route['default_controller'] = 'welcome';
$route['404_override'] = 'api/BasicController/error';
$route['translate_uri_dashes'] = FALSE;

/*
| -------------------------------------------------------------------------
| REST API Routes
| -------------------------------------------------------------------------
*/
//Basic API Routes
$route['basic']['get'] = 'api/BasicController/find';
$route['auth/login']['post'] = 'api/BasicController/login';
$route['auth/check']['get'] = 'api/BasicController/check';
$route['auth/create']['post'] = 'api/BasicController/create';

//Place Menu API Routes
$route['place/(:num)/menu']['get'] = 'api/MenuController/find/$1';
$route['place/(:num)/events']['get'] = 'api/EventController/findFromPlace/$1';
$route['place/(:num)/reviews']['get'] = 'api/ReviewController/find/$1';

//Package API Routes
$route['place/(:num)/packages']['get'] = 'api/PackageController/placeFind/$1';
$route['package/(:num)']['get'] = 'api/PackageController/packageContentFind/$1';

//Event API Routes
$route['events/find']['get'] = 'api/EventController/find';
$route['events/(:num)/tickets']['get'] = 'api/EventController/tickets/$1';

//Reviews API Routes
$route['review/add']['post'] = 'api/ReviewController/create';

//User API Routes
$route['user/phone/update']['post'] = 'api/UserController/updatePhone';
$route['user/profile']['post'] = 'api/UserController/profile';
