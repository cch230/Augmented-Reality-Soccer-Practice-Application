<?php
 $db_name = 'users_database'; 		// DB Name
 $username = 'admin';		  		// User name
 $password = '1q2w3e4r'; 		 // MySQL password
 $servername = 'gg-database.cxe3tndsehwb.ap-northeast-2.rds.amazonaws.com';	// RDS Endpoint
 $port = '3306';
 
 $conn = mysqli_connect($servername, $username, $password, $db_name, $por);
 
?>
