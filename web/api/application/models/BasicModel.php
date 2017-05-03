<?php
/**
 * Created by PhpStorm.
 * User: Manuela Duque M
 * Date: 19/04/2017
 * Time: 2:32 PM
 */

class BasicModel extends CI_Model{

    public function userExists($mail){
        $query = $this->db->select('mail, is_active')->from('user')->where('mail', $mail)->where('is_active', true)->get();
        return $query->row_array();
    }
    
    public function findUser($mail){
        $query = $this->db->where('mail', $mail)->get('user');
        return $query->row_array();
    }

    private function getCurrentDate(){
        $statement = "SELECT NOW() as date";
        $query = $this->db->query($statement);
        foreach ($query->result() as $row){
            return $row->date;
        }
    }

    public function createUser($user){

        if (isset($user->first_name) && isset($user->mail) && isset($user->phone)){
            try {
                
                $params = array(
                    'first_name' => $user->first_name,
                    'last_name' => $user->last_name,
                    'mail' => $user->mail,
                    'phone' => $user->phone,
                    'created' => $this->getCurrentDate(),
                    'modified' => $this->getCurrentDate(),
                    'is_active' => 1,
                    'balance' => 0
                );

                return $this->db->insert('user', $params);

            } catch (Exception $e){
                return false;
            }
        }

        return false;
    }
    
    public function findAll(){

        $response = new stdClass();

        $query = $this->db->get('place');
        $response->places = $query->result_array();

        $query = $this->db->get('music');
        $data = $query->result_array();

        $response->music = $data;

        $query = $this->db->get('zone');
        $data = $query->result_array();

        $response->zones = $data;

        $query = $this->db->get('item_type');
        $data = $query->result_array();

        $response->item_types = $data;

        return $response;

    }
    
}