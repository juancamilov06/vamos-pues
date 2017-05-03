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
class EventController extends REST_Controller {


    public function __construct()
    {
        parent::__construct();
        $this->load->model('EventModel');
    }
    
    public function tickets_get($ticketId){
        $data = $this->EventModel->getTickets($ticketId);
        $response = Response::okResponse($data);
        echo $response;
    }

    public function find_get(){

        $data = $this->EventModel->findAll();
        if ($data != null){
            $response = Response::okResponse($data);
            echo $response;
        } else {
            echo 'error';
        }

    }
    
    public function findFromPlace_get($placeId){

        $data = $this->EventModel->findPlaceEvents($placeId);
        if ($data != null){
            $response = Response::okResponse($data);
            echo $response;
        } else {
            echo 'error';
        }
        
    }

}
