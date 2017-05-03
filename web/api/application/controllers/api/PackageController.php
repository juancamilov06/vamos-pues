<?php

defined('BASEPATH') OR exit('No direct script access allowed');

// This can be removed if you use __autoload() in config.php OR use Modular Extensions
/** @noinspection PhpIncludeInspection */
require APPPATH . '/libraries/REST_Controller.php';
require APPPATH . 'helpers/Response.php';

// use namespace
use Restserver\Libraries\REST_Controller;

/**
 * This is an example of a few basic user interaction methods you could use
 * all done with a hardcoded array
 *
 * @package         CodeIgniter
 * @subpackage      Rest Server
 * @category        Controller
 * @author          Phil Sturgeon, Chris Kacerguis
 * @license         MIT
 * @link            https://github.com/chriskacerguis/codeigniter-restserver
 */
class PackageController extends REST_Controller
{

    public function __construct()
    {
        parent::__construct();
        $this->load->model('PackageModel');
    }
    
    public function placeFind_get($id){
        $data = $this->PackageModel->getPlacePackages($id);
        $response = new stdClass();
        $response->packages = $data;
        echo Response::okResponse($response);
    }

    public function packageContentFind_get($placePackageId){
        $data = $this->PackageModel->getPackageContent($placePackageId);
        $response = new stdClass();
        $response->items = $data;
        echo Response::okResponse($response);
    }

}