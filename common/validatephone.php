<?php
/*****************************************************************************
			Valid				InValid
input:			
1. Mobile
2. Device-id

output:
1. user-id.		Existed id			Null
2. status message.	some status message		Invalid status message.
*******************************************************************************/

//include database implementation class
include_once("UConnectDb.php");
if($_SERVER['REQUEST_MOTHOD'] == "POST")
{
//Get Data
	$mobile = isset($_POST['mobile'])?mysql_real_escape_string($_POST['mobile']):"";
	$device_id = isset($_POST['device_id'])?mysql_real_escape_string($_POST['device_id']):"";

	$ucdb = new UConnectDB("root","**********","uConnect_db_schema","localhost");
	if($ucdb->connect())
	{
		if($ucdb->selectDatabase())
		{
			$sql = "select t_user_id from t_user where t_user_mobile_no='$mobile' and t_user_device_id='$device_id'";
			$data = $ucdb->select($sql);
			$size = count($data);
			if($size == 0)
			{
				$json = array("user_id" => 0, "message" => "Success.");
			}
			foreach($data as $user)
			{
				$json = array("user_id" => $user, "message" => "Data retrieved successfully.");
			}
		}
		else
		{
			$json = array("user_id" => 0, "message" => "Not able to select database.");
		}
	}
	else
	{
		$json = array("user_id" => 0, "message" => "DB Connection failed.");
	}
}
else
{
	$json = array("user_id" => 0, "message" => "Request method not accepted."); 
}

echo $json
?>
