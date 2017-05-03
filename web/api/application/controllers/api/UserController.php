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
class UserController extends REST_Controller {

    public function __construct()
    {
        parent::__construct();
        $this->load->model('UserModel');
    }
    
    public function findCode_post(){
        $mail = $this->post('mail');
        if (!isset($mail) || $mail == null){
            echo Response::dataMissingResponse();
        } else {
            $result = $this->UserModel->getCode($mail);
            echo Response::okResponse($result);
        }
    }

    public function profile_post(){
        $mail = $this->post('mail');
        if (!isset($mail) || $mail == null){
            echo Response::dataMissingResponse();
        } else {
            $result = $this->UserModel->userProfileInfo($mail);
            echo Response::okResponse($result);
        }
    }
    
    public function updatePhone_post(){
        $phone = $this->post('phone');
        $mail = $this->post('mail');
        
        if ((!isset($phone) || $phone == null) || (!isset($mail) || $mail == null)){
            $response = Response::dataMissingResponse();
            echo $response;
            return;
        } 
        
        if ($this->UserModel->updatePhone($phone, $mail)){
            $response = new stdClass();
            $response->message = "Phone updated succesfully";
            echo Response::okResponse($response);
        } else {
            $response = new stdClass();
            $response->message = "Error updating phone";
            echo Response::okResponse($response);
        }
    } 

}