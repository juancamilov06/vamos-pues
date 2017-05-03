<?php

defined('BASEPATH') OR exit('No direct script access allowed');

// This can be removed if you use __autoload() in config.php OR use Modular Extensions
/** @noinspection PhpIncludeInspection */
require APPPATH . '/libraries/REST_Controller.php';
require APPPATH . 'helpers\Response.php';

// use namespace
use Restserver\Libraries\REST_Controller;


/**
 * Created by PhpStorm.
 * User: Manuela Duque M
 * Date: 19/04/2017
 * Time: 3:29 PM
 */

class MenuController extends REST_Controller {


    public function __construct()
    {
        parent::__construct();
        $this->load->model('MenuModel');
    }

    public function find_get($id){

        $data = $this->MenuModel->getDiscoMenu($id);
        if ($data != null){
            $response = Response::okResponse($data);
            echo $response;
        } else {
            echo 'error';
        }

    }

}