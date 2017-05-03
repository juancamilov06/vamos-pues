<?php

defined('BASEPATH') OR exit('No direct script access allowed');

// This can be removed if you use __autoload() in config.php OR use Modular Extensions
/** @noinspection PhpIncludeInspection */
require APPPATH . '/libraries/REST_Controller.php';
require APPPATH . 'helpers\Response.php';

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
class ReviewController extends REST_Controller {


    public function __construct()
    {
        parent::__construct();
        $this->load->model('ReviewModel');
    }
    public function create_post(){

        $review = $this->post('review');
        if (!isset($review) || $review == null){
            $response = Response::dataMissingResponse();
            echo $response;
            return;
        }
        
        $insert = $this->ReviewModel->createReview(json_decode($review));
        if ($insert){
            $response = Response::okInsertResponse();
            echo $response;
            return;
        } else {
            $response = Response::internalServerResponse();
            echo $response;
            return;
        }

    }

    public function find_get($placeId){
        $response = new stdClass();
        $data = $this->ReviewModel->findPlaceReviews($placeId);
        $response->reviews = $data;
        echo Response::okResponse($response);
    }

}
