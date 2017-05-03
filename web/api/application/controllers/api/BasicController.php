<?php

defined('BASEPATH') OR exit('No direct script access allowed');

// This can be removed if you use __autoload() in config.php OR use Modular Extensions
/** @noinspection PhpIncludeInspection */
require APPPATH . '/libraries/REST_Controller.php';
require APPPATH . 'helpers/Response.php';
require APPPATH . 'helpers/JWT.php';

// use namespace
use Restserver\Libraries\REST_Controller;
use Firebase\JWT\JWT;
use Firebase\JWT\ExpiredException;

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
class BasicController extends REST_Controller {
    

    public function __construct()
    {
        parent::__construct();
        $this->load->model('BasicModel');
    }

    public function check_get(){
        $token = $this->get('token');
        if (!isset($token) || $token == null){
            $response = Response::dataMissingResponse();
            echo $response;
        } else {
            try{
                $user = JWT::decode($token,'key');
                $user->expired = false;
                echo Response::okResponse($user);
            } catch (ExpiredException $e){
                echo Response::tokenExpiredResponse();
            }
        }
    }
    
    public function create_post(){
        
        $user = json_decode($this->post('user'));
        if (!isset($user) || $user == null) {
            $response = Response::dataMissingResponse();
            echo $response;
        } else {
            $insert = $this->BasicModel->createUser($user);
            if ($insert) {

                $time = time();
                $expiration = $time + 5184000;

                $user = (object) $this->BasicModel->findUser($user->mail);
                $user->iat = $time;
                $user->exp = $expiration;
                $jwt = JWT::encode($user,'key');

                $data = new stdClass();
                $data->expiration = $expiration;
                $data->exists = true;
                $data->token = $jwt;
                $data->code = $user->id;

                $response = Response::okResponse($data);
                echo $response;
                return;
            } else {
                $response = Response::internalServerResponse();
                echo $response;
            }
        }
    }
    
    public function login_post(){

        $mail = $this->post('mail');

        if (!isset($mail) || $mail == null){
            $response = Response::dataMissingResponse();
            echo $response;
            return;
        }

        if(empty($this->checkFirstTime($mail))){

            $data = new stdClass();
            $data->exists = false;
            $data->message = "User doesn't exist on our database";
            $response = Response::okResponse($data);
            echo $response;
            return;

        } else {

            $time = time();
            $expiration = $time + 5184000;

            $user = (object) $this->BasicModel->findUser($mail);
            $user->iat = $time;
            $user->exp = $expiration;
            $jwt = JWT::encode($user,'key');

            $data = new stdClass();
            $data->expiration = $expiration;
            $data->exists = true;
            $data->token = $jwt;
            $data->code = $user->id;

            $response = Response::okResponse($data);
            echo $response;
        }
    }
    
    public function error_get(){
        echo Response::notFoundResponse();
    }

    public function find_get(){

        $data = $this->BasicModel->findAll();
        if ($data != null){
            $response = Response::okResponse($data);
            echo $response;
        } else {
            echo 'error';
        }

    }
    
    public function checkFirstTime($mail){
        return $this->BasicModel->userExists($mail);
    }

}
