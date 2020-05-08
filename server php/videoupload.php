<?php
 require "conn.php";

 $user_nickname = $_POST["input_id"];
 $video = $_POST["editTextUpload"];

 $isValidVideo = filter_var($video, FILTER_VALIDATE_Video);

 if ($conn) {
     
    if ($isValidVideo === false) {
        echo "업로드할 영상을 선택해주세요";
    } else {

	    $sql_upload = "INSERT INTO videoboard (user_nickname, video)VALUES($user_nickname', '$video')";

            if (mysqli_query($conn, $sql_upload)) {
                echo "성공적으로 업로드했습니다";
            } else {
                echo "업로드에 실패했습니다";
            }
        }
    }

 else {
     echo "연결 오류";
 }

?>
